package com.taskboard.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.util.logging.*;
import java.util.ArrayList;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1;
	private static final String TITLE = "TaskBoard: Your Revolutionary Task Manager";

	private Logic _logic;

	private JPanel _displayArea;
	private JTextArea _feedbackArea;
	private JLabel _commandLabel;
	private JTextField _commandField;

	private static Logger logger = Logger.getLogger("UserInterface");

	public UserInterface() {
		JFrame frame = new JFrame("TaskBoard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
	}

	public Logic getLogic() {
		return _logic;
	}

	private void initComponents(Container pane) {
		pane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		_logic = new Logic();

		/*_displayArea = new JTextArea(20, 50);
		_displayArea.setEditable(false);
		constraints.weightx = 0.0;
		constraints.weighty = 0.5;
		constraints.insets = new Insets(7,7,7,7);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.gridx = 0;
		constraints.gridy = 0;
		pane.add(_displayArea, constraints);*/
		
		_displayArea = new JPanel();
		_displayArea.setBackground(Color.WHITE);
		_displayArea.setOpaque(true);
		_displayArea.setLayout(new GridBagLayout());
		_displayArea.setPreferredSize(new Dimension(800, 600));
		constraints.weightx = 0.0;
		constraints.weighty = 0.5;
		constraints.insets = new Insets(7,7,7,7);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.gridx = 0;
		constraints.gridy = 0;
		pane.add(_displayArea, constraints);		

		JScrollPane _displayScroll = new JScrollPane(_displayArea);
		_displayScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.add(_displayScroll, constraints);
		
		_feedbackArea = new JTextArea(1, 50);
		_feedbackArea.setEditable(false);
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.insets = new Insets(2,5,2,5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		pane.add(_feedbackArea, constraints);

		_commandLabel = new JLabel("Enter command below:");
		_commandLabel.setFont(new Font("Sans-Serif", Font.ITALIC, 12));
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		pane.add(_commandLabel, constraints);

		_commandField = new JTextField();
		_commandField.setEditable(true);
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 3;
		pane.add(_commandField, constraints);

		_commandField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub  
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					String userInput = _commandField.getText();

					if (userInput.toLowerCase().equals("exit")) {
						logger.log(Level.INFO, "System exit.");
						System.exit(0);
					} else {
						Response currentResponse = getLogic().processCommand(userInput);
						if (currentResponse.isSuccess()) {
							_displayArea.removeAll();
							
							ArrayList<Entry> entries = currentResponse.getEntries();
							if (currentResponse.getFeedback() != null) {
								String feedback = currentResponse.getFeedback().trim();
								_feedbackArea.setText(feedback);
							}

							if (entries != null) {
								for (int i = 0; i < entries.size(); i++) {
									Entry currentEntry = entries.get(i);
									constraints.weightx = 0.0;
									constraints.weighty = 0.5;
									constraints.insets = new Insets(2,1,2,1);
									constraints.gridx = 0;
									constraints.gridy = i;
									if (currentEntry.getDateParameter() != null) {
										JLabel deadlineLabel = new JLabel();
										deadlineLabel.setText(currentEntry.toString());
										deadlineLabel.setBackground(Color.RED);
										deadlineLabel.setOpaque(true);
										_displayArea.add(deadlineLabel, constraints);
									} else if (currentEntry.getStartDateParameter() != null) {
										JLabel eventLabel = new JLabel();
										eventLabel.setText(currentEntry.toString());
										eventLabel.setBackground(Color.GREEN);
										eventLabel.setOpaque(true);
										_displayArea.add(eventLabel, constraints);
									} else {
										JLabel floatLabel = new JLabel();
										floatLabel.setText(currentEntry.toString());
										floatLabel.setBackground(Color.CYAN);
										floatLabel.setOpaque(true);
										_displayArea.add(floatLabel, constraints);
									}
								}
								
								_displayArea.revalidate();
								_displayArea.repaint();
								
							}

							logger.log(Level.INFO, "Successfully updated display.");
						} else {
							String exception = currentResponse.getException().getMessage();
							_feedbackArea.setText(exception);
						}
					}

					_commandField.setText("");
				}
			}
		});

		//createLayout(new JComponent[]{_title, _displayScroll, _feedbackArea, _commandLabel, _commandField});
		_commandField.requestFocusInWindow();

		setTitle(TITLE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/*	private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

	    gl.setHorizontalGroup(gl.createSequentialGroup()
	    		.addGroup(gl.createParallelGroup()
	    			.addComponent(arg[0])
	    			.addComponent(arg[1])
	    			.addComponent(arg[2])
	    			.addComponent(arg[3])
	    			.addComponent(arg[4])
		    	)
	    );

	    gl.setVerticalGroup(gl.createSequentialGroup()
	    		.addComponent(arg[0])
	    		.addGap(10)
			    .addComponent(arg[1])
			    .addGap(10)
			    .addComponent(arg[2])
			    .addGap(10)
			    .addComponent(arg[3])
			    .addGap(10)
			    .addComponent(arg[4])
	    );

	    pack();
    }*/

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UserInterface _userInterface = new UserInterface();
			}
		});
	}
}