package gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import logic.agents.FireFighter;
import logic.agents.Individual;
import logic.env.*;
import utils.Constants;
import utils.Pair;

public class RegionGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private RegionCell[][] region;
	private RegionEnv environment;
	public ArrayList<FireFighter> firefighters;
	private ArrayList<Individual> people;
	private ArrayList<Pair<Integer, Integer>> fire_being_extinguished;

	public RegionGUI(RegionEnv env) {
		this.environment = env;
		this.firefighters = new ArrayList<>(Constants.FIREFIGHTERS_NUMBER);
		this.people = new ArrayList<>(Constants.PEOPLE_NUMBER);
		this.fire_being_extinguished = new ArrayList<Pair<Integer, Integer>>();
		update();
	}

	public void paintComponent(Graphics g) {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = null;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());


		for (int row = 0; row < Constants.GRID_SIZE; row++) {
			for (int col = 0; col < Constants.GRID_SIZE; col++) {

				image = toolkit.getImage("..\\resources\\ground.png");

				if (region[col][row].getType().equals("Vegetation")) {

					Vegetation vegetation = (Vegetation) region[col][row];

					switch (vegetation.getDensity()) {
						case 1:
							image = toolkit.getImage("..\\resources\\grass_level1.png");
							break;
						case 2:
							image = toolkit.getImage("..\\resources\\grass_level2.png");
							break;
						case 3:
							image = toolkit.getImage("..\\resources\\grass_level3.png");
							break;
						default:
							break;
					}
				} else if (region[col][row].getType().equals("Fire")) {
					VegetationOnFire fire = (VegetationOnFire) region[col][row];

					switch (fire.getDensity()) {
						case 1:
							image = toolkit.getImage("..\\resources\\fire_grass_level1.gif");
							break;
						case 2:
							image = toolkit.getImage("..\\resources\\fire_grass_level2.gif");
							break;
						case 3:
							image = toolkit.getImage("..\\resources\\fire_grass_level3.gif");
							break;
						default:
							break;
					}
				} else if (region[col][row].getType().equals("BurnedArea")) {
					image = toolkit.getImage("..\\resources\\burned_area.png");
				}

				// Print image
				g.drawImage(image, col * Constants.CELL_WIDTH, row * Constants.CELL_HEIGHT, this);
			}
		}


		for (int i = 0; i < this.firefighters.size(); i++) {
			if (!this.firefighters.get(i).getPosition().equals(Constants.TOWER_POS)) {
				if (!this.firefighters.get(i).isDead()) {
					image = toolkit.getImage("..\\resources\\firefighter.png");
					g.drawImage(image, this.firefighters.get(i).getPosition().getX() * Constants.CELL_WIDTH, this.firefighters.get(i).getPosition().getY() * Constants.CELL_HEIGHT, this);
					drawHealthBar(g, this.firefighters.get(i).getPosition().getX() * Constants.CELL_WIDTH, this.firefighters.get(i).getPosition().getY() * Constants.CELL_HEIGHT, this.firefighters.get(i).getHp());
				} else {
					image = toolkit.getImage("..\\resources\\rip.png");
					g.drawImage(image, this.firefighters.get(i).getPosition().getX() * Constants.CELL_WIDTH, this.firefighters.get(i).getPosition().getY() * Constants.CELL_HEIGHT, this);
				}

			}
		}

		image = toolkit.getImage("..\\resources\\individual.png");
		for (int i = 0; i < this.people.size(); i++)
		{
			if (!this.people.get(i).beingHelpedOut())
			{
				g.drawImage(image, this.people.get(i).getPosition().getX() * Constants.CELL_WIDTH, this.people.get(i).getPosition().getY() * Constants.CELL_HEIGHT, this);


				if (this.people.get(i).alreadyAskedForHelp())
				{
					g.drawImage(toolkit.getImage("..\\resources\\danger.png"),
							this.people.get(i).getPosition().getX() * Constants.CELL_WIDTH + 10,
							this.people.get(i).getPosition().getY() * Constants.CELL_HEIGHT - 5,
							14, 14, this);
				}
			}
		}


		image = toolkit.getImage("..\\resources\\quartel.png");
		g.drawImage(image, Constants.TOWER_POS.getX() * Constants.CELL_WIDTH, Constants.TOWER_POS.getY() * Constants.CELL_HEIGHT, this);



		image = toolkit.getImage("..\\resources\\water_drop.png");
		for (int i = 0; i < this.fire_being_extinguished.size(); i++)
		{
			g.drawImage(image,
					this.fire_being_extinguished.get(i).getX() * Constants.CELL_WIDTH + 5,
					this.fire_being_extinguished.get(i).getY() * Constants.CELL_HEIGHT,
					10, 10, this);
		}


		//Info lado direito stuff
		// 1 - N , 2 - NE, 3 - E, 4 -  SE, 5 - S, 6 - SO, 7 - O, 8 - NO
		switch (environment.wind.getWindDirection())
		{
			case 1:
				image = toolkit.getImage("..\\resources\\wind\\N.png");
				break;
			case 2:
				image = toolkit.getImage("..\\resources\\wind\\NE.png");
				break;
			case 3:
				image = toolkit.getImage("..\\resources\\wind\\E.png");
				break;
			case 4:
				image = toolkit.getImage("..\\resources\\wind\\SE.png");
				break;
			case 5:
				image = toolkit.getImage("..\\resources\\wind\\S.png");
				break;
			case 6:
				image = toolkit.getImage("..\\resources\\wind\\SO.png");
				break;
			case 7:
				image = toolkit.getImage("..\\resources\\wind\\O.png");
				break;
			case 8:
				image = toolkit.getImage("..\\resources\\wind\\NO.png");
				break;
			default:
				break;
		}
		g.drawImage(image, Constants.WIND_DIR_POS.getX()*Constants.CELL_WIDTH, Constants.WIND_DIR_POS.getY()*Constants.CELL_HEIGHT, this);
		g.setColor(Color.BLACK);
		g.drawString("Vel: " + Integer.toString(environment.wind.getWindSpeed()),Constants.WIND_DIR_POS.getX()*Constants.CELL_WIDTH + 5, Constants.WIND_DIR_POS.getY()*Constants.CELL_HEIGHT + 3*Constants.CELL_HEIGHT);



		image = toolkit.getImage("..\\resources\\firefighter.png");
		g.drawImage(image, Constants.FIREFIGHTER_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.FIREFIGHTER_INFO_POS.getY()*Constants.CELL_HEIGHT, this);
		g.drawString("Alive: " + Integer.toString(environment.getAliveFirefightersNumber()), Constants.FIREFIGHTER_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.FIREFIGHTER_INFO_POS.getY()*Constants.CELL_HEIGHT + (int)(1.5f*Constants.CELL_HEIGHT));
		g.drawString("Dead: " + Integer.toString(environment.getDeadFirefightersNumber()), Constants.FIREFIGHTER_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.FIREFIGHTER_INFO_POS.getY()*Constants.CELL_HEIGHT + (int)(2.2f*Constants.CELL_HEIGHT));



		image = toolkit.getImage("..\\resources\\individual.png");
		g.drawImage(image, Constants.PEOPLE_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.PEOPLE_INFO_POS.getY()*Constants.CELL_HEIGHT, this);
		g.drawString("Asked for Help: " + Integer.toString(environment.getPeopleAskedHelpNumber()), Constants.PEOPLE_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.PEOPLE_INFO_POS.getY()*Constants.CELL_HEIGHT + (int)(1.5f*Constants.CELL_HEIGHT));
		g.drawString("Rescued: " + Integer.toString(environment.getPeopleRescuedNumber()), Constants.PEOPLE_INFO_POS.getX()*Constants.CELL_WIDTH, Constants.PEOPLE_INFO_POS.getY()*Constants.CELL_HEIGHT + (int)(2.2f*Constants.CELL_HEIGHT));


	}

	public void update() {

		region = environment.getRegion();
		this.firefighters = this.environment.getFirefighters();
		this.people = this.environment.getPeople();
		this.fire_being_extinguished = this.environment.getAllFireBeingExtinguished();


		repaint();
	}

	public Dimension getPreferredSize() {
		return new Dimension(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE);
	}

	public static void main(String[] args) {

	}


	public void drawHealthBar(Graphics g, int x, int y, int hp)
	{
		int barhp = (int)((hp * Constants.CELL_WIDTH) / Constants.HP);

		g.setColor(Color.RED);
		g.fillRect(x, y, 20, 3);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, barhp, 3);
	}



}
