package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import logic.env.RegionEnv;
import utils.Constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;

public class InputGui extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField maxWindSpeedInput;
	private JTextField comburentFactorInput;
	private JTextField combustionConsuptionInput;
	private JTextField probWindStaysInput;
	private JTextField propagationFactorInput;
	private JTextField firefightersNumber;
	private JTextField updateTimeFirInput;
	private JTextField updateTimeEnvInput;
	private JTextField waterFluxInput;
	private JTextField peopleNumberInput;
	
	public InputGui(RegionEnv env) {
		super("Multi Agent System - Firefighters");
		
		getContentPane().setLayout(null);
		
		JButton btnStartSimulation = new JButton("Start Simulation");
		btnStartSimulation .addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try
			  {
				  Constants.MAX_WIND_SPEED = Integer.parseInt(maxWindSpeedInput.getText());
				  Constants.COMBURENT_FACTOR = Integer.parseInt(comburentFactorInput.getText());
				  Constants.COMBUSTION_CONSUMPTION = Integer.parseInt(combustionConsuptionInput.getText());
				  Constants.PROB_WIND_STAYS = Integer.parseInt(probWindStaysInput.getText());
				  Constants.PROPAGATION_FACTOR = Double.parseDouble(propagationFactorInput.getText());
				  Constants.FIREFIGHTERS_NUMBER = Integer.parseInt(firefightersNumber.getText());
				  Constants.UPDATE_TIME_FIREFIGHTERS = Integer.parseInt(updateTimeFirInput.getText());
				  Constants.UPDATE_TIME_ENV = Integer.parseInt(updateTimeEnvInput.getText());
				  Constants.WATER_FLUX = Integer.parseInt(waterFluxInput.getText());
				  Constants.PEOPLE_NUMBER = Integer.parseInt(peopleNumberInput.getText());
			
				  setVisible(false);
				  dispose();
				  env.startEnv();
			  }
			  catch (Exception ex)
			  {
				  System.out.println("Error found parsing the inputs! All the inputs must be a number.");
				  JOptionPane.showMessageDialog(null, "Error found parsing the inputs! All the inputs must be a number.");
			  }
		  }
		});
		
		btnStartSimulation.setBounds(150, 430, 200, 54);
		getContentPane().add(btnStartSimulation);
		
		JLabel lblMaxWindSpeed = new JLabel("Max Wind Speed");
		lblMaxWindSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMaxWindSpeed.setBounds(33, 20, 132, 16);
		getContentPane().add(lblMaxWindSpeed);
		
		JLabel lblComburentFactor = new JLabel("Comburent Factor");
		lblComburentFactor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblComburentFactor.setBounds(33, 60, 132, 16);
		getContentPane().add(lblComburentFactor);
		
		JLabel lblCombustionConsuption = new JLabel("Combustion Consuption");
		lblCombustionConsuption.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCombustionConsuption.setBounds(33, 100, 191, 16);
		getContentPane().add(lblCombustionConsuption);
		
		JLabel lblProbWindStays = new JLabel("Prob. Wind Stays The Same");
		lblProbWindStays.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblProbWindStays.setBounds(33, 140, 250, 16);
		getContentPane().add(lblProbWindStays);
		
		JLabel lblPropagationFactor = new JLabel("Propagation Factor");
		lblPropagationFactor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPropagationFactor.setBounds(33, 180, 161, 16);
		getContentPane().add(lblPropagationFactor);
		
		JLabel lblFirefightersNumber = new JLabel("Firefighters Number");
		lblFirefightersNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFirefightersNumber.setBounds(33, 220, 161, 16);
		getContentPane().add(lblFirefightersNumber);
		
		JLabel lblUpdateTimeFirefighters = new JLabel("Update Time Firefighters");
		lblUpdateTimeFirefighters.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUpdateTimeFirefighters.setBounds(33, 260, 178, 16);
		getContentPane().add(lblUpdateTimeFirefighters);
		
		JLabel lblUpdateTimeEnvironment = new JLabel("Update Time Environment");
		lblUpdateTimeEnvironment.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUpdateTimeEnvironment.setBounds(33, 300, 233, 16);
		getContentPane().add(lblUpdateTimeEnvironment);
		
		JLabel lblWaterFlux = new JLabel("Water Flux");
		lblWaterFlux.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblWaterFlux.setBounds(33, 340, 161, 16);
		getContentPane().add(lblWaterFlux);
		
		JLabel lblPeopleNumber = new JLabel("People Number");
		lblPeopleNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPeopleNumber.setBounds(33, 380, 161, 16);
		getContentPane().add(lblPeopleNumber);
		
		maxWindSpeedInput = new JTextField(Constants.MAX_WIND_SPEED + "");
		maxWindSpeedInput.setBounds(280, 20, 160, 22);
		getContentPane().add(maxWindSpeedInput);
		maxWindSpeedInput.setColumns(10);
		
		comburentFactorInput = new JTextField(Constants.COMBURENT_FACTOR + "");
		comburentFactorInput.setBounds(280, 60, 160, 22);
		getContentPane().add(comburentFactorInput);
		comburentFactorInput.setColumns(10);
		
		combustionConsuptionInput = new JTextField(Constants.COMBUSTION_CONSUMPTION + "");
		combustionConsuptionInput.setBounds(280, 100, 160, 22);
		getContentPane().add(combustionConsuptionInput);
		combustionConsuptionInput.setColumns(10);

		probWindStaysInput = new JTextField(Constants.PROB_WIND_STAYS + "");
		probWindStaysInput.setBounds(280, 140, 160, 22);
		getContentPane().add(probWindStaysInput);
		probWindStaysInput.setColumns(10);
		
		propagationFactorInput = new JTextField(Constants.PROPAGATION_FACTOR + "");
		propagationFactorInput.setBounds(280, 180, 160, 22);
		getContentPane().add(propagationFactorInput);
		propagationFactorInput.setColumns(10);
		
		firefightersNumber = new JTextField(Constants.FIREFIGHTERS_NUMBER + "");
		firefightersNumber.setBounds(280, 220, 160, 22);
		getContentPane().add(firefightersNumber);
		firefightersNumber.setColumns(10);
		
		updateTimeFirInput = new JTextField(Constants.UPDATE_TIME_FIREFIGHTERS + "");
		updateTimeFirInput.setBounds(280, 260, 160, 22);
		getContentPane().add(updateTimeFirInput);
		updateTimeFirInput.setColumns(10);
		
		updateTimeEnvInput = new JTextField(Constants.UPDATE_TIME_ENV + "");
		updateTimeEnvInput.setBounds(280, 300, 160, 22);
		getContentPane().add(updateTimeEnvInput);
		updateTimeEnvInput.setColumns(10);
		
		waterFluxInput = new JTextField(Constants.WATER_FLUX + "");
		waterFluxInput.setBounds(280, 340, 160, 22);
		getContentPane().add(waterFluxInput);
		waterFluxInput.setColumns(10);
		
		peopleNumberInput = new JTextField(Constants.PEOPLE_NUMBER + "");
		peopleNumberInput.setBounds(280, 380, 160, 22);
		getContentPane().add(peopleNumberInput);
		peopleNumberInput.setColumns(10);
		
		initialise();
	}
	
	public void initialise() {
		setSize(500, 550);
		setVisible(true);
	}
}
