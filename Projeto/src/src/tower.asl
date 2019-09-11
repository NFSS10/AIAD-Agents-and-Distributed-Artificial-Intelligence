teamDead:- .findall(FF, ffPos(FF, _, _), FFS) & .length(FFS, S) & S = 0.

getFF(FFS):- .findall(FF2, ffPos(FF2, _, _), FFS).

getFreeFFs(L3):- 
	.findall(FF1, onDuty(FF1, _, _, _), L1) & getFF(L2) &
	 .difference(L2, L1, L3).

assignFFtoRescue(_, _,[FF], FF):- true.                

assignFFtoRescue(RX, RY, [FF1,FF2|T], FF) :- ffPos(FF1, X1, Y1) & ffPos(FF2, X2, F2) &
    (math.abs(RX-X1) + math.abs(RY-Y1)) <= (math.abs(RX-X2) + math.abs(RY-Y2)) &                            
    assignFFtoRescue(RX, RY, [FF1|T], FF).               

assignFFtoRescue(RX, RY, [FF1,FF2|T], FF) :- ffPos(FF1, X1, Y1) & ffPos(FF2, X2, F2) &
     (math.abs(RX-X1) + math.abs(RY-Y1)) > (math.abs(RX-X2) + math.abs(RY-Y2)) &                                  
    assignFFtoRescue(RX, RY, [FF2|T], FF).   
   
      
+startAt(X, Y)
	: getFF(FFS)
	<- .send(FFS, achieve, check(X, Y)).
   
+ffdied(N, PBX, PBY)
	: onDuty(N, N2, X, Y)
	<- -onDuty(N, N2, X, Y);
		!helpAt(N2, PBX, PBY).
		
@psf2[atomic]
+!removeOnDuty(FF, N,  X, Y)
	: onDuty(FF, N, X, Y)
	<- -onDuty(FF, N, X, Y).

@psf[atomic]
+!helpAt(N, X, Y)
	: getFreeFFs(L) & not .empty(L) & assignFFtoRescue(X, Y, L, FF)
	<- +onDuty(FF, N, X, Y); 
		.send(FF, achieve, orderToRescueAt(N, X, Y)).

+!helpAt(N, X, Y)
	: not teamDead
	<- .wait(getFreeFFs(L) & not .empty(L));
		!helpAt(N, X, Y).
		
+!helpAt(N, X, Y).