package com.taskboard.main;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import java.util.logging.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1;
	private static final String TITLE = "TaskBoard: Your Revolutionary Task Manager";

	private Logic _logic;
	
	private JTextArea _feedbackArea;
	private JPanel _displayArea;
	private JLabel _commandLabel;
	private JTextField _commandField;
	private JLabel _title;

	private static Logger _logger = GlobalLogger.getInstance().getLogger();

	public UserInterface() {
		JFrame frame = new JFrame(TITLE);
		JLabel backgroundPane = new JLabel();
		try {
			backgroundPane.setIcon(new ImageIcon(ImageIO.read(new File("resources/images/space-wallpaper-download-800x640.jpg"))));
		} catch (IOException e) {
			// Handle IOException
		}
		backgroundPane.setLayout(new BorderLayout());
		frame.setContentPane(backgroundPane);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 650));

		initComponents(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
		_commandField.requestFocus();
	}

	public Logic getLogic() {
		return _logic;
	}

	private void initComponents(Container pane) {
		pane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		_logic = new Logic();
		
		ImageIcon titleImage = new ImageIcon("resources/images/TaskBoard-title2-v03.png");
		// another alternative for title's image:
		// ImageIcon titleImage = new ImageIcon("resources/images/TaskBoard-title1-v03.png");
		_title = new JLabel(titleImage);
		//_title.setText("TaskBoard V0.3");
		//_title.setFont(new Font("Sans-Serif", Font.BOLD, 25));
		//_title.setForeground(Color.WHITE);
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		pane.add(_title, constraints);
		
		_displayArea = new JPanel();
		_displayArea.setBackground(Color.WHITE);
		_displayArea.setOpaque(true);
		_displayArea.setLayout(new GridBagLayout());
		_displayArea.setAutoscrolls(true);
		constraints.weightx = 0.0;
		constraints.weighty = 0.5;
		constraints.insets = new Insets(7, 7, 7, 7);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		pane.add(_displayArea, constraints);		

		JScrollPane _displayScroll = new JScrollPane(_displayArea);
		_displayScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		_displayScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		_displayScroll.getViewport().setPreferredSize(new Dimension(640, 420));
		_displayScroll.getVerticalScrollBar().setUnitIncrement(16);
		pane.add(_displayScroll, constraints);
		
		_feedbackArea = new JTextArea(2, 50);
		_feedbackArea.setEditable(false);
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.insets = new Insets(2, 5, 2, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		pane.add(_feedbackArea, constraints);

		_commandLabel = new JLabel("Enter command below:");
		_commandLabel.setFont(new Font("Sans-Serif", Font.ITALIC, 12));
		_commandLabel.setForeground(Color.WHITE);
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		pane.add(_commandLabel, constraints);

		_commandField = new JTextField();
		_commandField.setEditable(true);
		constraints.weightx = 0.0;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 4;
		pane.add(_commandField, constraints);

		final ArrayList<Integer> pressed = new ArrayList<Integer>();
		
		_commandField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub  
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
					pressed.remove(new Integer(KeyEvent.VK_CONTROL));
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					executeInputCommand();
				} else if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
					pressed.add(new Integer(KeyEvent.VK_CONTROL));
				} else if (pressed.contains(new Integer(KeyEvent.VK_CONTROL))) {
					if (arg0.getKeyCode() == KeyEvent.VK_Z) {
						_commandField.setText("undo");
						executeInputCommand();
					} else if (arg0.getKeyCode() == KeyEvent.VK_Q) {
						_commandField.setText("exit");
						executeInputCommand();
					} else if (arg0.getKeyCode() == KeyEvent.VK_H) {
						_commandField.setText("help");
						executeInputCommand();
					}
				}
			}
		});

		setTitle(TITLE);
		//setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UserInterface();
			}
		});
	}
	
	public void executeInputCommand() {
		String userInput = _commandField.getText();

		if (userInput.toLowerCase().equals("exit")) {
			_logger.log(Level.INFO, "System exit.");
			System.exit(0);
		} else {
			Response currentResponse = getLogic().processCommand(userInput);
			if (currentResponse.isSuccess()) {
				_displayArea.removeAll();
				
				ArrayList<Entry> entries = currentResponse.getEntries();
				if (currentResponse.getFeedback() != null) {
					String feedback = currentResponse.getFeedback().trim();
					_feedbackArea.setText(feedback);
					
					_logger.log(Level.INFO, "Successfully updated feedback area.");
				}

				if (entries != null) {
					GridBagConstraints constraints = new GridBagConstraints();
					constraints.anchor = GridBagConstraints.PAGE_START;
					constraints.insets = new Insets(2, 2, 2, 2);
					constraints.fill = GridBagConstraints.NONE;
					
					String lastDate = "";
					
					int curGridY = 0;
					for (int i = 0; i < entries.size(); i++) {
						Entry currentEntry = entries.get(i);
						constraints.gridx = 0;
						constraints.gridy = curGridY;
						
						String pivotDate = "";
						if (currentEntry.getDateParameter() != null) {
							pivotDate = currentEntry.getDateParameter().getParameterValue();
						} else if (currentEntry.getStartDateParameter() != null) {
							pivotDate = currentEntry.getStartDateParameter().getParameterValue();
						} else {
							pivotDate = "Side Tasks";
						}
						
						if (!lastDate.equals(pivotDate)) {
							constraints.gridwidth = 2;
							JLabel dateLabel = new JLabel();
							dateLabel.setText(pivotDate);
							dateLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							dateLabel.setBackground(Color.WHITE);
							dateLabel.setOpaque(true);
							_displayArea.add(dateLabel, constraints);
							lastDate = pivotDate;
							constraints.gridx = 0;
							constraints.gridy = ++curGridY;
							constraints.gridwidth = 1;
						}
						
						if (i == entries.size() - 1) {
							constraints.weighty = 1;
						}
						
						JLabel indexLabel = new JLabel();
						if (currentEntry.getIndexParameter() != null) {
							indexLabel.setText(currentEntry.getIndexParameter().getParameterValue() + '.');
						}
						indexLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
						indexLabel.setOpaque(true);
						
						if (currentEntry.getDateParameter() != null) {
							indexLabel.setBackground(Color.PINK);
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JLabel deadlineLabel = new JLabel();
							deadlineLabel.setText(currentEntry.toUIString());
							deadlineLabel.setBackground(Color.PINK);
							deadlineLabel.setOpaque(true);
							deadlineLabel.setPreferredSize(new Dimension(480, 80));
							//deadlineLabel.setMinimumSize(new Dimension(640, 80));
							deadlineLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							deadlineLabel.setVerticalAlignment(JLabel.TOP);
							_displayArea.add(deadlineLabel, constraints);
						} else if (currentEntry.getStartDateParameter() != null) {
							indexLabel.setBackground(new Color (175, 255, 163));
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JLabel eventLabel = new JLabel();
							eventLabel.setText(currentEntry.toUIString());
							eventLabel.setBackground(new Color (175, 255, 163));
							eventLabel.setOpaque(true);
							eventLabel.setPreferredSize(new Dimension(480, 80));
							//eventLabel.setMinimumSize(new Dimension(640, 80));
							eventLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							eventLabel.setVerticalAlignment(JLabel.TOP);
							_displayArea.add(eventLabel, constraints);
						} else {
							indexLabel.setBackground(new Color (198, 255, 250));
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JLabel floatLabel = new JLabel();
							floatLabel.setText(currentEntry.toUIString());
							floatLabel.setBackground(new Color (198, 255, 250));
							floatLabel.setOpaque(true);
							floatLabel.setPreferredSize(new Dimension(480, 80));
							//floatLabel.setMinimumSize(new Dimension(640, 80));
							floatLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							floatLabel.setVerticalAlignment(JLabel.TOP);
							_displayArea.add(floatLabel, constraints);
						}
					}
					
					_displayArea.revalidate();
					_displayArea.repaint();
					
				}

				_logger.log(Level.INFO, "Successfully updated display.");
			} else {
				String exception = currentResponse.getException().getMessage();
				_feedbackArea.setText(exception);
				
				_logger.log(Level.INFO, "Successfully updated feedback area.");
			}
		}

		_commandField.setText("");
	}
	
}