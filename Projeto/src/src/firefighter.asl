tower(0, 0).

alive.

sumPos([], _, _, _):- true.
sumPos([[X1|Y1]| L], N, PBX, PBY):-  not being_extinguished(X1, Y1) & N <= (math.abs(PBX-X1) + math.abs(PBY-Y1)) & sumPos(L, N, PBX, PBY).   
sumPos([[X1|Y1]| L], N, PBX, PBY):- being_extinguished(X1, Y1) & sumPos(L, N, PBX, PBY).

isClosest(X, Y, PBX, PBY):- 	
	.findall([X1|Y1], fire(X1, Y1), L) & sumPos(L, math.abs(PBX-X) + math.abs(PBY-Y), PBX, PBY).
      
getClosestAux(_, _,[[X1|Y1]], X1, Y1):- true.                

getClosestAux(PBX, PBY, [[X1|Y1],[X2|Y2]|T], X, Y) :-
    (math.abs(PBX-X1) + math.abs(PBY-Y1)) <= (math.abs(PBX-X2) + math.abs(PBY-Y2)) &                            
    getClosestAux(PBX, PBY, [[X1|Y1]|T], X, Y).               

getClosestAux(PBX, PBY, [[X1|Y1],[X2|Y2]|T], X, Y) :-
     (math.abs(PBX-X1) + math.abs(PBY-Y1)) > (math.abs(PBX-X2) + math.abs(PBY-Y2)) &                                  
    getClosestAux(PBX, PBY, [[X2|Y2]|T], X, Y).   
   
getClosest(PBX, PBY, X, Y):- 
	.findall([X1|Y1], fire(X1, Y1), L) & .findall([X2|Y2], being_extinguished(X2, Y2), L2) 
	& differenceList(L, L2, [], L4) & getClosestAux(PBX, PBY, L4, X, Y).
      
differenceList([], _, L3, L3).   
differenceList([[X1|Y1]|T1], L2, L3, L4):- not .member([X1|Y1], L2) & differenceList(T1, L2, [[X1|Y1]|L3], L4). 
differenceList([[X1|Y1]|T1], L2, L3, L4):- differenceList(T1, L2,  L3, L4).
 
getPartners(Partners):- .findall(X, partner(X), Partners).

listSubsetOfList([], _):- true.
listSubsetOfList([[X1|Y1]|L1], L2):- .member([X1|Y1], L2) & listSubsetOfList(L1, L2).

allFiresBeingExtinguished:- .findall([X1|Y1], fire(X1, Y1), L1) & .findall([X2|Y2], being_extinguished(X2, Y2), L2)
						    & listSubsetOfList(L1, L2).
 
surrounded(X, Y):- fire(X + 1, Y) &  fire(X - 1, Y) & fire(X, Y + 1) & fire(X, Y - 1)
				 & fire(X + 1, Y + 1) & fire(X - 1, Y - 1) &  fire(X + 1, Y - 1) & fire(X - 1, Y + 1).
 
+dead
	: alive & .my_name(N)
	<- 	.drop_all_desires;
		.drop_all_intentions;
		-alive;
		!sendNotMarkedAnymore;
	   	!deadMessage(N)
	   	!updateindividual.

+new_burned(L) 
	: alive & getPartners(Partners)
	<- .send(Partners, achieve, new_burned_areas(L)).

+new_fires(L)
	: alive & getPartners(Partners)
	<- .send(Partners, achieve, new_fire_areas(L)).

+fire(X, Y)
	: alive & my_pos(X, Y) & not rescuing & not surrounded(X, Y)
	<- .drop_all_intentions;
		+running;
		!sendNotMarkedAnymore;
		move_away;
		-running;
		!extinguishClosestFire.

 +fire(X, Y) 
	: alive & my_pos(PBX, PBY) & not running & not being_extinguished(X, Y) & isClosest(X, Y, PBX, PBY) & not busy & not rescuing 
	  & getPartners(Partners) & .random(S)
	<- 	.wait(S*100);
		!canI(X, Y);
		.drop_all_intentions;
		!sendNotMarkedAnymore;
		!markPos(X, Y);
		!extinguishFire(X, Y).

+!updateindividual
	: carry(N) & my_pos(PBX, PBY)
	<- -carry(N);
		drop_individual(N, PBX, PBY).

+!updateindividual.		

+!deadMessage(N)
	: getPartners(Partners)
	<- .send(Partners, achieve, idied(N)).

+!markPos(X, Y)
	: getPartners(Partners)
	<- +intentionToExtinguish(X, Y);
		.send(Partners, achieve, marked(X, Y)).

@psf6[atomic]
+!idied(N)
	: alive & partner(N)
	<- partner_died(N).

+!idied(N).

@psf5[atomic]
+!new_fire_areas(L2)
	: alive & .findall([X1|Y1], fire(X1, Y1), L1) & differenceList(L2, L1, [], L4)
	<- add_new_fire_areas(L4).

+!new_fire_areas(_).

@psf4[atomic]
+!new_burned_areas(L2)
	: alive & .findall([X1|Y1], burned(X1, Y1), L1) & differenceList(L2, L1, [], L4)
	<- add_new_burned_areas(L4).

+!new_burned_areas(_).

+!canI(X, Y)
	: not being_extinguished(X, Y)
	<- true.

+!canI(X, Y)
	: allFiresBeingExtinguished
	<- true.
	
	
+!canI(X, Y)
	: true
	<- .drop_all_intentions;
		!extinguishClosestFire.
	

+!sendNotMarkedAnymore 
	: intentionToExtinguish(X, Y) & getPartners(Partners)
	<- -intentionToExtinguish(X, Y);
		.send(Partners, achieve, not_marked(X, Y)).	

+!sendNotMarkedAnymore.	
		
@psf[atomic]
+!marked(X, Y) 
	: alive & not being_extinguished(X, Y)
	<- +being_extinguished(X, Y).
	
+!marked(X, Y).

@psf2[atomic]
+!not_marked(X, Y) 
	: alive & being_extinguished(X, Y)
	<- -being_extinguished(X, Y).	

+!not_marked(X, Y).
	
+!extinguishFire(X, Y)
	: my_pos(X1, Y1) & math.abs(X - X1) < 2 & math.abs(Y - Y1) < 2 & getPartners(Partners)
	<- +busy 
		!untilItsDone(X, Y);
		-busy;
		!sendNotMarkedAnymore;
		!extinguishClosestFire.
		
+!extinguishFire(X, Y) 
	: fire(X, Y) & my_pos(PBX, PBY) & not (math.abs(PBX - X) < 2 & math.abs(PBY - Y) < 2)
	<- 	!stormInTheFront(X, Y);
		move_towards_fire(X, Y);
		!extinguishFire(X, Y).
		
+!extinguishFire(X, Y)
	: not fire(X, Y) & getPartners(Partners)
	<- .send(Partners, achieve, fire_extinguished(X, Y));
		!extinguishClosestFire.

+!untilItsDone(X, Y)
	: fire(X, Y)
	<-	put_water(X, Y);
	   	!untilItsDone(X, Y).
	   	
+!untilItsDone(X, Y)
	: getPartners(Partners)
	<- .send(Partners, achieve, fire_extinguished(X, Y)).

@psf3[atomic]
+!fire_extinguished(X, Y)
	: alive
	<- remove_fire_perception(X, Y).
	
+!fire_extinguished(_, _).

+!extinguishClosestFire
	: my_pos(PBX, PBY) & allFiresBeingExtinguished & .findall([X1|Y1], fire(X1, Y1), L) & getClosestAux(PBX, PBY, L, X, Y)
	<- !extinguishFire(X, Y).

+!extinguishClosestFire
	: my_pos(PBX, PBY) & fire(_, _) & getClosest(PBX, PBY, X, Y) & getPartners(Partners)
	<- +intentionToExtinguish(X, Y);
		.send(Partners, achieve, marked(X, Y));
		!extinguishFire(X, Y).

+!extinguishClosestFire
	: tower(X, Y)
	<- .drop_all_intentions;
		!goTo2(X, Y).
		
+!orderToRescueAt(PN, X, Y)
	: alive & tower(TX, TY) & .my_name(N)
	<- .drop_all_intentions;
		+rescuing;
		!goTo(X, Y);
		+carry(PN);
		carry(PN);
		!goTo(TX, TY);
		rescued(PN);
		-carry(PN);
		-rescuing;
		.send(tower, achieve, removeOnDuty(N, PN, X, Y));
		!extinguishClosestFire.

+!orderToRescueAt(PN, X, Y).
		
+!check(X, Y)
	: true
	<- !goTo(X, Y);
		!extinguishClosestFire.
	
+!goTo2(X, Y)
	: my_pos(X, Y) 
	<- true.
			
+!goTo2(X, Y)
	: fire(_,_)
	<- !extinguishClosestFire.
	
+!goTo2(X, Y)
	: true
	<- !stormInTheFront(X, Y);
		move_towards(X, Y);
		!goTo2(X, Y).
				
+!goTo(X, Y)
	: my_pos(X, Y) 
	<- true.
			
+!goTo(X, Y)
	: true
	<- !stormInTheFront(X, Y);
		move_towards(X, Y);
		!goTo(X, Y).


+!stormInTheFront(X, Y)
	: my_pos(PBX, PBY) & nextPoint(PBX, PBY, X, Y, NX, NY) & fire(NX, NY)
	<- !untilItsDone(NX, NY).
	
+!stormInTheFront(X, Y)
	: true
	<- true.
		
nextPoint(X1, Y1, X2, Y2, X3, Y3):-
    DX = math.abs(X2-X1) &
    DY =  math.abs(Y2-Y1) &
    updateNextPointSX(X1, X2, SX) &
    updateNextPointSY(Y1, Y2, SY) &
    ERR = DX - DY &
    E2 = 2 * ERR &
    AUX = 0 - DY &
    nextPointX(E2, AUX, X3, X1, SX) &
    nextPointY(E2, DX, Y3, Y1, SY).
 
updateNextPointSX(X1, X2, SX):-
	X1 < X2 &
	SX = 1.
	
updateNextPointSX(X1, X2, SX):-
	SX = -1.
	
updateNextPointSY(Y1, Y2, SY):-
	Y1 < Y2 &
	SY = 1.
	
updateNextPointSY(Y1, Y2, SY):-
	SY = -1.
		
 
nextPointX(E2, AUX, X3, X1, SX):-
    E2 > AUX &
    X3 = X1 + SX.
   
nextPointX(E2, AUX, X3, X1, SX):-
    X3 = X1.
   
nextPointY(E2, DX, Y3, Y1, SY):-
    E2 < DX &
    Y3 = Y1 + SY.
   
nextPointY(E2, DX, Y3, Y1, SY):-
    Y3 = Y1.
   
