package gui;

import java.awt.*;
import javax.swing.*;

import logic.env.*;
import utils.Constants;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private RegionEnv environment;
	private RegionGUI region;
	private JTextArea output;
	private JScrollPane outScroll;
	private Box box;
	private JLabel label;

	public GUI(RegionEnv env) {
		super("Multi Agent System - FireFighting Simulation");
		environment = env;
		region = new RegionGUI(environment);
		initialise();
	}

	public void update() {

		region.update();

	}

	public void initialise() {

		Container c = getContentPane();

		label = new JLabel("Output");

		output = new JTextArea(6, 18);
		output.setEditable(false);

		outScroll = new JScrollPane();
		outScroll.setViewportView(output);
		outScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		box = new Box(BoxLayout.Y_AXIS);
		box.add(region);
		box.add(label);
		box.add(outScroll);

		c.add(box);
		setSize(Constants.WINDOW_SIZE +16+ Constants.RIGHT_INFO_BAR_SIZE, Constants.WINDOW_SIZE + 170);
		setVisible(true);
	}

	public void out(String out) {

		output.append(out + "\n");
		output.setCaretPosition(output.getDocument().getLength());

	}

}

