//@@author A0129526B
package com.taskboard.main.userinterface;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.imageio.ImageIO;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.Logic;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class UserInterface extends JFrame {
	
	private static final String DEFAULT_TITLE = "TaskBoard: Your Revolutionary Task Manager";
	private static final String TITLE_IMAGE_FILE_PATH = "resources/images/TaskBoard-title.png";
	private static final String HIGH_PRIORITY_FILE_PATH = "resources/images/HighPriority.jpg";
	private static final String MEDIUM_PRIORITY_FILE_PATH = "resources/images/MediumPriority.jpg";
	private static final String LOW_PRIORITY_FILE_PATH = "resources/images/LowPriority.jpg";
	private static final String NORMAL_PRIORITY_FILE_PATH = "resources/images/NormalPriority.jpg";
	private static final String PAST_ENTRY_FILE_PATH = "resources/images/Past.png";
	private static final String REMINDER_FILE_PATH = "resources/images/Soon.png";
	private static final String DEFAULT_BACKGROUND_FILE_PATH = "resources/images/default.jpg";
	private static final int DEFAULT_REMINDER_HOUR = 1;
	
	private static final String MESSAGE_PROMPT_COMMAND = "Enter command below:";
	private static final String MESSAGE_NO_FEEDBACK = "No feedback to display.";
	
	private static final float DISPLAY_AREA_RELATIVE_TRANSPARENCY = 0.8f;
	private static final int LABEL_RELATIVE_TRANSPARENCY = 255;
	
	private static final String UP_BUTTON_CODE = "UP";
	private static final String DOWN_BUTTON_CODE = "DOWN";
	private static final String PAGE_UP_BUTTON_CODE = "PAGE_UP";
	private static final String PAGE_DOWN_BUTTON_CODE = "PAGE_DOWN";
	private static final String POSITIVE_SCROLL_CODE = "positiveUnitIncrement";
	private static final String NEGATIVE_SCROLL_CODE = "negativeUnitIncrement";
	
	private static final String EXIT_COMMAND_STRING = "exit";
	
	private static final String START_OF_DAY_TIME = "00:00";
	private static final String END_OF_DAY_TIME = "23:59";
	private static final String STRING_NONE = "N/A";
	
	private static final long serialVersionUID = 1;
	
	private static UserInterface _instance;
	private Logic _logic;
	private JFrame _frame;
	private String _title;
	private JLabel _backgroundPane;
	private JPanel _displayArea;
	private JScrollPane _displayScroll;
	private JScrollBar _verticalDisplayScroll;
	private JTextPane _feedbackArea;
	private JScrollPane _feedbackScroll;
	private JScrollBar _verticalFeedbackScroll;
	private JPanel _commandArea;
	private JLabel _commandLabel;
	private JTextField _commandField;
	private JLabel _titleLabel;
	private String _backgroundPath;
	private ImageIcon _backgroundImageIcon;
	private int _reminderHour;

	private static Logger _logger = GlobalLogger.getInstance().getLogger();
	
	private UserInterface() {
		_title = DEFAULT_TITLE;
		_frame = new JFrame(_title);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setPreferredSize(new Dimension(800, 720));
		_frame.addComponentListener(new WindowResizeListener());
		_frame.setVisible(true);
		
		_backgroundPane = new JLabel();
		
		try {
			setBackgroundPath(DEFAULT_BACKGROUND_FILE_PATH);
			setReminderHour(DEFAULT_REMINDER_HOUR);
		} catch (IOException e) {
			JTextPane feedbackArea = UserInterface.getInstance().getFeedbackArea();
			if (feedbackArea == null) {
				UserInterface.getInstance().setFeedbackArea(new JTextPane());
			}
			feedbackArea.setText("Unexpected error during background initialization.");
		}
		
		_backgroundPane.setLayout(new BorderLayout());
		_frame.setContentPane(_backgroundPane);
		
		initComponents(_frame.getContentPane());
		_frame.pack();
		
		_commandField.requestFocus();
	}

	public static UserInterface getInstance() {
		if (_instance == null) {
			_instance = new UserInterface();
		}
		return _instance;
	}
	
	public Logic getLogic() {
		return _logic;
	}
	
	public JTextField getCommandField() {
		return _commandField;
	}
	
	public String getBackgroundPath() {
		return _backgroundPath;
	}
	
	public String getDefaultBackgroundFilePath() {
		return DEFAULT_BACKGROUND_FILE_PATH;
	}
	
	public int getReminderHour() {
		return _reminderHour;
	}
	
	public int getDefaultReminderHour() {
		return DEFAULT_REMINDER_HOUR;
	}
	
	public JTextPane getFeedbackArea() {
		return _feedbackArea;
	}
	
	public void setTitle(String newTitle) {
		_title = newTitle;
		updateTitle();
	}
	
	public void updateTitle() {
		_frame.setTitle(_title);
	}
	
	public void setBackgroundPath(String newBackgroundFilePath) throws IOException {
		_backgroundPath = newBackgroundFilePath;
		updateBackgroundImageIcon();
	}
	
	public void setReminderHour(int newReminderHour) {
		_reminderHour = newReminderHour;
	}
	
	public void updateBackgroundImageIcon() throws IOException {
		try {
			ImageIcon sourceIcon = new ImageIcon(ImageIO.read(new File(_backgroundPath)));
			_backgroundImageIcon = sourceIcon;
			updateBackground();
		} catch (IOException e) {
			try {
				URL sourceIconURL = new URL(_backgroundPath);
				final HttpURLConnection connection = (HttpURLConnection) sourceIconURL.openConnection();
				connection.setRequestProperty(
				    "User-Agent",
				    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
				ImageIcon sourceIcon = new ImageIcon(ImageIO.read(connection.getInputStream()));
				_backgroundImageIcon = sourceIcon;
				updateBackground();
			} catch (MalformedURLException eMal) {
				JTextPane feedbackArea = UserInterface.getInstance().getFeedbackArea();
				if (feedbackArea == null) {
					UserInterface.getInstance().setFeedbackArea(new JTextPane());
				}
				feedbackArea.setText("Unexpected error during background initialization.");
			} catch (IOException eURL) {
				throw eURL;
			}
		}
	}
	
	public void updateBackground() throws IOException {
		Rectangle frameRect = _frame.getBounds();
		Image resizedImage = _backgroundImageIcon.getImage().getScaledInstance(frameRect.width, frameRect.height,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);
		_backgroundPane.setIcon(resizedIcon);
	}
	
	public void setFeedbackArea(JTextPane newFeedbackArea) {
		_feedbackArea = newFeedbackArea;
	}
	
	private void initComponents(Container pane) {
		pane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		_logic = new Logic();
		
		ImageIcon titleImage = new ImageIcon(TITLE_IMAGE_FILE_PATH);
		_titleLabel = new JLabel(titleImage);
		constraints = setConstraints(2, 0.0, 0.1, 0, 0, 0, 0, 0, 0);
		pane.add(_titleLabel, constraints);
		
		_displayArea = new TransparentPanel(DISPLAY_AREA_RELATIVE_TRANSPARENCY);
		_displayArea.setBackground(Color.WHITE);
		_displayArea.setLayout(new GridBagLayout());
		_displayArea.setAutoscrolls(true);
		constraints = setConstraints(2, 0.0, 0.5, 0, 1, 0, 0, 0, 0);
		pane.add(_displayArea, constraints);

		_displayScroll = new TransparentScrollPane(_displayArea, DISPLAY_AREA_RELATIVE_TRANSPARENCY);
		_displayScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		_displayScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		_displayScroll.getViewport().setPreferredSize(new Dimension(640, 400));
		_displayScroll.getVerticalScrollBar().setUnitIncrement(40);
		_displayScroll.setBackground(Color.WHITE);
		pane.add(_displayScroll, constraints);
		
		// Enable up and down buttons for scrolling.
		_verticalDisplayScroll = _displayScroll.getVerticalScrollBar();
		InputMap displayScrollIM = _verticalDisplayScroll.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		displayScrollIM.put(KeyStroke.getKeyStroke(DOWN_BUTTON_CODE), POSITIVE_SCROLL_CODE);
		displayScrollIM.put(KeyStroke.getKeyStroke(UP_BUTTON_CODE), NEGATIVE_SCROLL_CODE);
		
		_feedbackArea = new JTextPane();
		_feedbackArea.setEditable(false);
		_feedbackArea.setAutoscrolls(false);
		_feedbackArea.setContentType("text/html");
		constraints = setConstraints(2, 0.0, 0.1, 0, 2, 0, 0, 0, 0);
		pane.add(_feedbackArea, constraints);
		
		_feedbackScroll = new JScrollPane(_feedbackArea);
		_feedbackScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		_feedbackScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		_feedbackScroll.getViewport().setPreferredSize(new Dimension(640, 100));
		_feedbackScroll.getVerticalScrollBar().setUnitIncrement(20);
		pane.add(_feedbackScroll, constraints);
		
		// Enable up and down buttons for scrolling.
		_verticalFeedbackScroll = _feedbackScroll.getVerticalScrollBar();
		InputMap feedbackScrollIM = _verticalFeedbackScroll.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		feedbackScrollIM.put(KeyStroke.getKeyStroke(PAGE_DOWN_BUTTON_CODE), POSITIVE_SCROLL_CODE);
		feedbackScrollIM.put(KeyStroke.getKeyStroke(PAGE_UP_BUTTON_CODE), NEGATIVE_SCROLL_CODE);
		
		_commandArea = new TransparentPanel(DISPLAY_AREA_RELATIVE_TRANSPARENCY);
		_commandArea.setLayout(new GridBagLayout());
		_commandArea.setPreferredSize(new Dimension(640, 50));
		constraints = setConstraints(2, 0.0, 0.2, 0, 3, 0, 0, 0, 0);
		pane.add(_commandArea, constraints);
		
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		_commandLabel = new JLabel(MESSAGE_PROMPT_COMMAND);
		_commandLabel.setHorizontalAlignment(JLabel.CENTER);
		_commandLabel.setFont(new Font("Sans-Serif", Font.BOLD, 16));
		_commandLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		_commandLabel.setForeground(Color.WHITE);
		_commandLabel.setBackground(new Color(0, 0, 0, 216));
		constraints = setConstraints(2, 0.1, 0.1, 0, 0, 0, 0, 0, 0);
		_commandArea.add(_commandLabel, constraints);

		_commandField = new JTextField();
		_commandField.setEditable(true);
		_commandField.addKeyListener(new ShortcutListener());
		constraints = setConstraints(2, 0.1, 0.1, 0, 1, 0, 0, 0, 0);
		_commandArea.add(_commandField, constraints);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UserInterface.getInstance();
			}
		});
	}
	
	public void executeInputCommand() {
		String userInput = _commandField.getText();

		if (userInput.toLowerCase().equals(EXIT_COMMAND_STRING)) {
			_logger.log(Level.INFO, "System exit.");
			System.exit(0);
		} else {
			String feedbackFontColor = "";
			
			Response currentResponse = getLogic().processCommand(userInput);
			if (currentResponse.isSuccess()) {
				ArrayList<Entry> entries = currentResponse.getEntries();
				
				if (entries != null) {
					_displayArea.removeAll();
					
					GridBagConstraints constraints = new GridBagConstraints();
					constraints.anchor = GridBagConstraints.PAGE_START;
					constraints.insets = new Insets(2, 2, 2, 2);
					constraints.fill = GridBagConstraints.NONE;
					
					String lastDate = "";
					
					int curGridY = 0;
					int lastHelpX = 1;
					for (int i = 0; i < entries.size(); i++) {
						Entry currentEntry = entries.get(i);
						constraints.gridx = 0;
						constraints.gridy = curGridY;
						
						String pivotDate = "";
						if (currentEntry.getDateParameter() != null) {
							pivotDate = currentEntry.getDateParameter().getParameterValue();
							pivotDate = toDisplayDateFormat(pivotDate);
						} else if (currentEntry.getStartDateParameter() != null) {
							pivotDate = currentEntry.getStartDateParameter().getParameterValue();
							pivotDate = toDisplayDateFormat(pivotDate);
						} else if (currentEntry.getNameParameter() != null) {
							pivotDate = "Side Tasks";
						} else {
							pivotDate = "";
						}
						
						if (!lastDate.equals(pivotDate)) {
							constraints.gridwidth = 2;
							JLabel dateLabel = new JLabel();
							dateLabel.setText(pivotDate);
							dateLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
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
							indexLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
						}
						indexLabel.setVerticalAlignment(JLabel.TOP);
						indexLabel.setPreferredSize(new Dimension(30, 30));
						indexLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
						indexLabel.setOpaque(true);
						
						if (currentEntry.getDateParameter() != null) {
							indexLabel.setBackground(new Color(255, 192, 203, LABEL_RELATIVE_TRANSPARENCY));
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JTextPane deadlineLabel = new JTextPane();
							deadlineLabel.setEditable(false);
							
							JLabel deadlineIcon = new JLabel();
							deadlineIcon.setBounds(368, 0, 112, 27);
							assignPriorityIcon(currentEntry, deadlineIcon);
							deadlineLabel.add(deadlineIcon);
							
							TransparentTextArea deadlineText = new TransparentTextArea(1.0f);
							String dateString = currentEntry.getDateParameter().getParameterValue();
							Parameter timeParameter = currentEntry.getTimeParameter();
							String timeString;
							if (timeParameter != null) {
								timeString = timeParameter.getParameterValue();
							} else {
								timeString = STRING_NONE;
							}
							if (isPastDateTime(dateString, timeString)) {
								JLabel pastIcon = new JLabel();
								pastIcon.setBounds(320, 0, 48, 27);
								pastIcon.setIcon(new ImageIcon(PAST_ENTRY_FILE_PATH));
								deadlineLabel.add(pastIcon);
							} else if (isInReminderPeriod(dateString, timeString)) {
								BlinkingLabel reminderIcon = new BlinkingLabel();
								reminderIcon.setBounds(320, 0, 48, 27);
								reminderIcon.setIcon(new ImageIcon(REMINDER_FILE_PATH));
								deadlineLabel.add(reminderIcon);
							}
							
							deadlineText = setTextArea(currentEntry, "deadline");
							deadlineLabel.add(deadlineText);
							
							if (currentEntry.getCategoryParameter() != null) {
								JTextArea categoryText = new JTextArea();
								categoryText = createCategoryText(currentEntry, "deadline");
								deadlineLabel.add(categoryText);
							}
							
							deadlineLabel.setBackground(new Color(255, 192, 203, LABEL_RELATIVE_TRANSPARENCY));
							deadlineLabel.setOpaque(true);
							deadlineLabel.setPreferredSize(new Dimension(480, 64));
							deadlineLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							
							_displayArea.add(deadlineLabel, constraints);
						} else if (currentEntry.getStartDateParameter() != null) {
							indexLabel.setBackground(new Color (175, 255, 163, LABEL_RELATIVE_TRANSPARENCY));
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JTextPane eventLabel = new JTextPane();
							eventLabel.setEditable(false);
							
							JLabel eventIcon = new JLabel();
							eventIcon.setBounds(368, 0, 112, 27);
							assignPriorityIcon(currentEntry, eventIcon);
							eventLabel.add(eventIcon);
							
							TransparentTextArea eventText = new TransparentTextArea(1.0f);
							String startDateString = currentEntry.getStartDateParameter().getParameterValue();
							String endDateString = currentEntry.getEndDateParameter().getParameterValue();
							Parameter startTimeParameter = currentEntry.getStartTimeParameter();
							Parameter endTimeParameter = currentEntry.getEndTimeParameter();
							String startTimeString;
							String endTimeString;
							if (startTimeParameter != null) {
								startTimeString = startTimeParameter.getParameterValue();
							} else {
								startTimeString = STRING_NONE;
							}
							if (endTimeParameter != null) {
								endTimeString = endTimeParameter.getParameterValue();
							} else {
								endTimeString = END_OF_DAY_TIME;
							}
							if (isPastDateTime(endDateString, endTimeString)) {
								JLabel pastIcon = new JLabel();
								pastIcon.setBounds(320, 0, 48, 27);
								pastIcon.setIcon(new ImageIcon(PAST_ENTRY_FILE_PATH));
								eventLabel.add(pastIcon);
							} else if (isInReminderPeriod(startDateString, startTimeString)) {
								BlinkingLabel reminderIcon = new BlinkingLabel();
								reminderIcon.setBounds(320, 0, 48, 27);
								reminderIcon.setIcon(new ImageIcon(REMINDER_FILE_PATH));
								eventLabel.add(reminderIcon);
							}
							
							eventText = setTextArea(currentEntry, "event");
							eventLabel.add(eventText);
							
							if (currentEntry.getCategoryParameter() != null) {
								JTextArea categoryText = new JTextArea();
								categoryText = createCategoryText(currentEntry, "event");
								eventLabel.add(categoryText);
							}
							
							eventLabel.setBackground(new Color (175, 255, 163, LABEL_RELATIVE_TRANSPARENCY));
							eventLabel.setOpaque(true);
							eventLabel.setPreferredSize(new Dimension(480, 64));
							eventLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							
							_displayArea.add(eventLabel, constraints);
						} else if (currentEntry.getNameParameter() != null) {
							indexLabel.setBackground(new Color (198, 255, 250, LABEL_RELATIVE_TRANSPARENCY));
							_displayArea.add(indexLabel, constraints);
							
							constraints.gridx = 1;
							constraints.gridy = curGridY++;
							JTextPane floatLabel = new JTextPane();
							floatLabel.setEditable(false);
							
							JLabel floatIcon = new JLabel();
							floatIcon.setBounds(368, 0, 112, 27);
							assignPriorityIcon(currentEntry, floatIcon);
							floatLabel.add(floatIcon);
							
							TransparentTextArea floatText = new TransparentTextArea(1.0f);
							floatText = setTextArea(currentEntry, "float");
							floatLabel.add(floatText);
							
							if (currentEntry.getCategoryParameter() != null) {
								JTextArea categoryText = new JTextArea();
								categoryText = createCategoryText(currentEntry, "float");
								floatLabel.add(categoryText);
							}
							
							floatLabel.setBackground(new Color (198, 255, 250, LABEL_RELATIVE_TRANSPARENCY));
							floatLabel.setOpaque(true);
							floatLabel.setPreferredSize(new Dimension(480, 64));
							floatLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							
							_displayArea.add(floatLabel, constraints);
						} else {
							if (lastHelpX == 1) {
								constraints.gridx = 0;
								constraints.gridy = curGridY;
								lastHelpX = 0;
							} else {
								constraints.gridx = 1;
								constraints.gridy = curGridY++;
								lastHelpX = 1;
							}
							constraints.gridwidth = 1;
							JTextPane helpLabel = new JTextPane();
							helpLabel.setContentType("text/html");
							helpLabel.setEditable(false);
							DefaultCaret helpCaret = (DefaultCaret) helpLabel.getCaret();
							helpCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
							helpLabel.setText(currentEntry.toUIString());
							helpLabel.setBounds(0, 0, 310, 32);
							helpLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
							helpLabel.setBackground(new Color(255, 255, 153, LABEL_RELATIVE_TRANSPARENCY));
							helpLabel.setOpaque(true);
							helpLabel.setPreferredSize(new Dimension(310, 32));
							_displayArea.add(helpLabel, constraints);
						}
					}
					
					_displayArea.revalidate();
					_displayArea.repaint();
					_displayScroll.revalidate();
					_displayScroll.repaint();
				}
				
				_logger.log(Level.INFO, "Successfully updated display area.");
				
				feedbackFontColor = "#009900";
			} else {
				feedbackFontColor = "#CC0000";
			}
			
			if (currentResponse.getFeedback() != null) {
				String feedback = currentResponse.getFeedback().trim();
				_feedbackArea.setText("<font color='" + feedbackFontColor + "'>" + feedback + "</font>");
				_feedbackArea.setCaretPosition(0);
				
				_logger.log(Level.INFO, "Successfully updated feedback area.");
			} else {
				_feedbackArea.setText("<font color='" + feedbackFontColor + "'>" + MESSAGE_NO_FEEDBACK + "</font>");
				_feedbackArea.setCaretPosition(0);
				
				_logger.log(Level.INFO, "Successfully updated feedback area.");
			}
		}

		_commandField.setText("");
	}
	
	private static void assignPriorityIcon(Entry currentEntry, JLabel currentLabel) {
		Parameter priority = currentEntry.getPriorityParameter();
		if (priority != null) {
			switch (priority.getParameterValue()) {
				case "high":
					currentLabel.setIcon(new ImageIcon(HIGH_PRIORITY_FILE_PATH));
					break;
				case "medium":
					currentLabel.setIcon(new ImageIcon(MEDIUM_PRIORITY_FILE_PATH));
					break;
				case "low":
					currentLabel.setIcon(new ImageIcon(LOW_PRIORITY_FILE_PATH));
					break;
			}
		} else {
			currentLabel.setIcon(new ImageIcon(NORMAL_PRIORITY_FILE_PATH));
		}
	}
	
	private static String getDeadlineTaskDisplayString(Entry currentEntry) {
		String displayString = "";
		
		String entryName = currentEntry.getNameParameter().getParameterValue();
		displayString += entryName + "\n";
		
		Parameter dateParameter = currentEntry.getDateParameter();
		String dateString = dateParameter.getParameterValue();
		dateString = toDisplayDateFormat(dateString);
		
		Parameter timeParameter = currentEntry.getTimeParameter();
		if (timeParameter != null) {
			String timeString = timeParameter.getParameterValue();
			displayString += "On " + dateString + " " + timeString;
		} else {
			displayString += "On " + dateString;
		}
		
		return displayString;
	}
	
	public static String getEventDisplayString(Entry currentEntry) {
		String displayString = "";
		
		String entryName = currentEntry.getNameParameter().getParameterValue();
		displayString += entryName + "\n";
		
		Parameter startDateParameter = currentEntry.getStartDateParameter();
		String startDateString = startDateParameter.getParameterValue();
		startDateString = toDisplayDateFormat(startDateString);
			
		Parameter startTimeParameter = currentEntry.getStartTimeParameter();
		if (startTimeParameter != null) {
			String startTimeString = startTimeParameter.getParameterValue();
			displayString += "From " + startDateString + " " + startTimeString + "\n";
		} else {
			displayString += "From " + startDateString + "\n";
		}
		
		Parameter endDateParameter = currentEntry.getEndDateParameter();
		String endDateString = endDateParameter.getParameterValue();
		endDateString = toDisplayDateFormat(endDateString);
			
		Parameter endTimeParameter = currentEntry.getEndTimeParameter();
		if (endTimeParameter != null) {
			String endTimeString = endTimeParameter.getParameterValue();
			displayString += "To " + endDateString + " " + endTimeString;
		} else {
			displayString += "To " + endDateString;
		}
		
		return displayString;
	}
	
	private static String getFloatingTaskDisplayString(Entry currentEntry) {
		String displayString = "";
		
		String entryName = currentEntry.getNameParameter().getParameterValue();
		displayString += entryName;
		
		return displayString;
	}
	
	private static String toDisplayDateFormat(String dateString) {
		SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
		SimpleDateFormat storageDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			return displayDateFormat.format(storageDateFormat.parse(dateString));
		} catch (ParseException e) {
			_logger.log(Level.SEVERE, "Fatal error: failed formatting date string from storage");
			assert false: "Fatal error: failed formatting date string from storage.";
			return null;
		}
	}
	
	private static boolean isPastDateTime(String dateString, String timeString) {
		if (timeString.equals(STRING_NONE)) {
			timeString = END_OF_DAY_TIME;
		}
		String dateTimeString = dateString + " " + timeString;
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			Date dateTime = dateTimeFormat.parse(dateTimeString);
			if (dateTime.compareTo(new Date()) < 0) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			_logger.log(Level.SEVERE, "Fatal error: failed formatting date string from storage");
			assert false: "Fatal error: failed formatting date string from storage.";
			return false;
		}
	}
	
	private boolean isInReminderPeriod(String dateString, String timeString) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date currentDateTime = new Date();
		Date startReminderDateTime = new Date();
		Date endReminderDateTime = new Date();
		if (timeString.equals(STRING_NONE)) {
			String startDateTimeString = dateString + " " + START_OF_DAY_TIME;
			String endDateTimeString = dateString + " " + END_OF_DAY_TIME;
			try {
				endReminderDateTime = dateTimeFormat.parse(endDateTimeString);
				startReminderDateTime = dateTimeFormat.parse(startDateTimeString);
				Calendar cal = Calendar.getInstance();
				cal.setTime(startReminderDateTime);
				cal.add(Calendar.HOUR_OF_DAY, -_reminderHour);
				startReminderDateTime = cal.getTime();
			} catch (ParseException e) {
				_logger.log(Level.SEVERE, "Fatal error: failed formatting date string from storage");
				assert false: "Fatal error: failed formatting date string from storage.";
				return false;
			}
		} else {
			String endDateTimeString = dateString + " " + timeString;
			try {
				endReminderDateTime = dateTimeFormat.parse(endDateTimeString);
				Calendar cal = Calendar.getInstance();
				cal.setTime(endReminderDateTime);
				cal.add(Calendar.HOUR_OF_DAY, -_reminderHour);
				startReminderDateTime = cal.getTime();
			} catch (ParseException e) {
				_logger.log(Level.SEVERE, "Fatal error: failed formatting date string from storage");
				assert false: "Fatal error: failed formatting date string from storage.";
				return false;
			}
		}
		boolean isAfterStartReminder = (startReminderDateTime.compareTo(currentDateTime) <= 0);
		boolean isBeforeEndReminder = (currentDateTime.compareTo(endReminderDateTime) <= 0);
		if (isAfterStartReminder && isBeforeEndReminder) {
			return true;
		} else {
			return false;
		}
	}
	
	private static GridBagConstraints setConstraints(int fill, double weightx, double weighty, int gridx, int gridy,
									   int insetsTop, int insetsLeft, int insetsBottom, int insetsRight){
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = fill;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
	
		return constraints;
	}
	
	private static JTextArea createCategoryText (Entry currentEntry, String typeToken) {
		JTextArea categoryText = new JTextArea();
		
		DefaultCaret categoryCaret = (DefaultCaret) categoryText.getCaret();
		categoryCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		categoryText.setText(currentEntry.getCategoryParameter().getParameterValue());
		categoryText.setFont(new Font("Sans-Serif", Font.BOLD, 14));
		categoryText.setLineWrap(true);
		categoryText.setBorder(new EmptyBorder(5, 5, 5, 5));
		categoryText.setBounds(320, 27, 160, 37);
		categoryText.setPreferredSize(new Dimension(160, 37));
		
		// Determining the color of the label according to the type of tasks
		if (typeToken.equalsIgnoreCase("deadline")) {
			categoryText.setBackground(new Color(255, 162, 173, LABEL_RELATIVE_TRANSPARENCY));
		} else if (typeToken.equalsIgnoreCase("event")) {
			categoryText.setBackground(new Color(135, 255, 123, LABEL_RELATIVE_TRANSPARENCY));
		} else if (typeToken.equalsIgnoreCase("float")) {
			categoryText.setBackground(new Color(168, 255, 250, LABEL_RELATIVE_TRANSPARENCY));
		}
		
		categoryText.setOpaque(true);
		
		return categoryText;
	}
	
	private static TransparentTextArea setTextArea (Entry currentEntry, String typeToken) {
		TransparentTextArea text = new TransparentTextArea(1.0f);
		DefaultCaret textCaret = (DefaultCaret) text.getCaret();
		textCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		if (typeToken.equals("deadline")) {
			text.setText(getDeadlineTaskDisplayString(currentEntry));
		} else if (typeToken.equals("event")) {
			text.setText(getEventDisplayString(currentEntry));
		} else if (typeToken.equals("float")) {
			text.setText(getFloatingTaskDisplayString(currentEntry));
		}
		
		text.setFont(new Font("Sans-Serif", Font.BOLD, 14));
		text.setLineWrap(true);
		text.setBorder(new EmptyBorder(5, 5, 5, 5));
		text.setBounds(0, 0, 320, 64);
		text.setPreferredSize(new Dimension(320, 64));
		
		return text;
	}
	
}