package com.taskboard.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1;
	
	private Logic _logic;
	
	private JLabel _title;
	private JTextArea _displayArea;
	private JTextArea _feedbackArea;
	private JLabel _commandLabel;
	private JTextField _commandField;
	private JButton _submitButton;
	
	public UserInterface() {
		initComponents();
	}
	
	public Logic getLogic() {
		return _logic;
	}
	
	private void initComponents() {
		_logic = new Logic();
		
		_title = new JLabel("TaskBoard V0.1");
		_title.setFont(new Font("Serif", Font.BOLD, 28));
		
		_displayArea = new JTextArea(20, 50);
		_displayArea.setEditable(false);
		
		_feedbackArea = new JTextArea(1, 50);
		_feedbackArea.setEditable(false);
		
		_commandLabel = new JLabel("Enter command below:");
		
		_commandField = new JTextField(50);
		
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
                		System.exit(0);
                	} else {
                		Response currentResponse = getLogic().processCommand(userInput);
                		if (currentResponse.getIsSuccess()) {
                			String feedback = currentResponse.getFeedback().trim();
                			if (userInput.toLowerCase().equals("view")) {
                				_displayArea.setText(feedback);
                				_feedbackArea.setText(new String("Successfully displayed all entries."));
                			} else {
                				_displayArea.setText(new String("No entry to display"));
                				_feedbackArea.setText(feedback);
                			}
                		} else {
                			String exception = currentResponse.getException().getMessage();
                			_displayArea.setText("No entry to display");
                			_feedbackArea.setText(exception);
                		}
                	}
                	
                    _commandField.setText("");
                }
            }
        });
		
		_submitButton = new JButton("Submit");
		
		_submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	String userInput = _commandField.getText();
            	
            	if (userInput.toLowerCase().equals("exit")) {
            		System.exit(0);
            	} else {
            		Response currentResponse = getLogic().processCommand(userInput);
            		if (currentResponse.getIsSuccess()) {
            			String feedback = currentResponse.getFeedback().trim();
            			if (userInput.toLowerCase().equals("view")) {
            				_displayArea.setText(feedback);
            				_feedbackArea.setText(new String("Successfully displayed all entries."));
            			} else {
            				_displayArea.setText(new String("No entry to display"));
            				_feedbackArea.setText(feedback);
            			}
            		}
            	}
            	
                _commandField.setText("");
            }
        });
		
		createLayout(new JComponent[]{_title, _displayArea, _feedbackArea, _commandLabel, _commandField, _submitButton});
		_commandField.requestFocusInWindow();
		
		setTitle("TaskBoard: The Revolutionary Task Manager");
		//setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createLayout(JComponent... arg) {

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
	    			.addComponent(arg[5])
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
			    .addGap(10)
			    .addComponent(arg[5])
	    );
        
	    pack();
    }
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				UserInterface _userInterface = new UserInterface();
				_userInterface.setVisible(true);
			}
		});
	}
	
}