package userIdent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import audio.AudioResult;
import body.OSC;
import body.VideoProfile;

public class DemoGui {
	private static DemoGui instance = new DemoGui();
	
	public static DemoGui getInstance() {
		return instance;
	}
	
	private JFrame frame;
	private JPanel panel;
	private JList userList;
	private JScrollPane listScrollPane;
	private JRadioButton bothRadio;
	private JRadioButton bodyRadio;
	private JRadioButton voiceRadio;
	private JButton goButton = new JButton();
	private JButton addButton = new JButton();
	private JTextArea bodyLabel = new JTextArea("       Körperformerkennung      \n");
	private JTextArea voiceLabel = new JTextArea("       Sprechererkennung       \n");
	private JTextArea resultLabel = new JTextArea("       Ergebnis       \n");
	private JLabel pose = new JLabel();
	private DefaultListModel model = new DefaultListModel();

	
	private Vector<User> testUsers;
	
	private DemoGui() {
		frame = new JFrame();
		panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		testUsers = new Vector<User>();
		this.userList = new JList(model);
		int i = 0;
		for (User user: UserManager.getInstance().getUsers()) {
			if (user.getVideoProfile() != null) {
				testUsers.add(user);
				model.add(i, user.getName());
			}
		}
		
		
		listScrollPane = new JScrollPane(userList); 
		listScrollPane.setPreferredSize(new Dimension(200,480));
		listScrollPane.setMinimumSize(new Dimension(100,200));
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		
		c.gridheight = 3;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 2, 40);
		panel.add(listScrollPane, c);
		
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(10, 0, 2, 2);
		addButton.setIcon(new ImageIcon("add.png"));
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DemoGui.getInstance().onAddUser();
			} 
		});
		
		panel.add(addButton, c);
		
		pose.setIcon(new ImageIcon("pose.png"));
		pose.setOpaque(true);
		pose.setVisible(false);
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 2, 2);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		panel.add(pose, c);
		
		
		
		bothRadio = new JRadioButton("Both");
		bodyRadio = new JRadioButton("Body");
		voiceRadio = new JRadioButton("Voice");
		
		bothRadio.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(bodyRadio);
		group.add(voiceRadio);
		group.add(bothRadio);
		
		goButton.setIcon(new ImageIcon("arrow.png"));
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(bodyRadio);
		radioPanel.add(voiceRadio);
	    radioPanel.add(bothRadio);
	    panel.add(radioPanel, c);
	    

		
		c.gridx = 2;
		c.gridy = 1;
		panel.add(goButton, c);
		
		resultLabel.setMargin(new Insets(5, 5, 5, 5));
		resultLabel.setBorder(new LineBorder(Color.BLACK));
		resultLabel.setEditable(false);
		
		bodyLabel.setMargin(new Insets(5, 5, 5, 5));
		bodyLabel.setBorder(new LineBorder(Color.BLACK));
		bodyLabel.setEditable(false);
		
		voiceLabel.setMargin(new Insets(5, 5, 5, 5));
		voiceLabel.setBorder(new LineBorder(Color.BLACK));
		voiceLabel.setEditable(false);

		c.gridx = 3;
		c.gridy = 0;
		c.gridheight = 3;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 2, 20, 2);
		
		
		c.gridx = 3;
		c.gridy = 0;
		panel.add(bodyLabel, c);
		
		c.gridx = 4;
		c.gridy = 0;
		panel.add(voiceLabel, c);
		
		c.gridx = 5;
		c.gridy = 0;
		panel.add(resultLabel, c);
		
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (voiceRadio.isSelected()) {
					DemoGui.getInstance().onTestSound();
				} else if (bodyRadio.isSelected()) {
					DemoGui.getInstance().onTestBody();
				} else {
					DemoGui.getInstance().onTest();
				}
			} 
		});
		
		frame.add(panel);
		frame.setSize(1024, 768);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void show() {
		this.frame.setVisible(true);
		Options.demo = true;
	}
	
	public void onTest() {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						resetLabels();
						pose.setVisible(true);
						Verifier verifier = new Verifier();
						//body recognition
						Vector<User> users = verifier.whoIs();
						if (users == null) {
							return;
						}
						pose.setVisible(false);
						pose.setIcon(new ImageIcon("pose.png"));
						
						for (User user: users) {
							bodyLabel.setText(bodyLabel.getText() + "\n " +  user.getName());
						}
						//speaker recognition with possible speakers
						Dialogue dialog = new Dialogue();
						File file = dialog.soundDialog(frame, false);
						
						TreeMap<Double, User> results = verifier.whoIs(file, users);
						Iterator<Double> iter= results.navigableKeySet().iterator();
						String resultList = "";
						while(iter.hasNext()) {
							Double current = iter.next();
							resultList = results.get(current).getName() + "\t" + current + "\n" + resultList ;
						}
						voiceLabel.setText(voiceLabel.getText() +resultList);
						
						Vector<User> remainingUsers = (Vector<User>)testUsers.clone();
						for (User user: users) {
							for (int i=0; i < testUsers.size(); i++) {
								if (remainingUsers.get(i).getName().equals(user.getName())) {
									remainingUsers.remove(i);
									break;
								}
							}
						}
						TreeMap<Double, User> irrelevantResults = verifier.whoIs(file, remainingUsers);
						Iterator<Double> irrelevant = irrelevantResults.navigableKeySet().iterator();
						
						voiceLabel.setText(voiceLabel.getText() + "\n \n irrelevant: \n");
						
						resultList = "";
						while(irrelevant.hasNext()) {
							Double current = irrelevant.next();
							resultList = irrelevantResults.get(current).getName() + "\t" + current + "\n" + resultList ;
						}
						voiceLabel.setText(voiceLabel.getText() +resultList);
						
						resultLabel.setText(resultLabel.getText() + "\n " + results.get(results.lastKey()).getName() + "\t" +  results.lastKey());
						
					}
				}
		);	
		t.start();
	}
	
	public void onTestSound() {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						resetLabels();
						Verifier verifier = new Verifier();
						//speaker recognition with speakers
						Dialogue dialog = new Dialogue();
						File file = dialog.soundDialog(frame, false);
						TreeMap<Double, User> results = verifier.whoIs(file, testUsers);
						Iterator<Double> iter= results.navigableKeySet().iterator();
						while(iter.hasNext()) {
							Double current = iter.next();
							voiceLabel.setText(voiceLabel.getText() + "\n " +  results.get(current).getName() + "\t" + current);
						}
						resultLabel.setText(resultLabel.getText() + "\n " + results.get(results.lastKey()).getName() + "\t" +  results.lastKey());
					}
				}
		);	
		t.start();
	}
	
	public void poseSetActive() {
		pose.setIcon(new ImageIcon("pose-active.png"));
	}
	
	public void onTestBody() {
		Thread t = new Thread(
			new Runnable() {
				public void run() {
					pose.setVisible(true);
					resetLabels();
					
					Verifier verifier = new Verifier();
					//body recognition
					Vector<User> users = verifier.whoIs();
					String found = "";
					if (users == null) {
						found = "None"; 
					} 
					pose.setVisible(false);
					pose.setIcon(new ImageIcon("pose.png"));
					for (User user: users) {
						bodyLabel.setText(bodyLabel.getText() + "\n " + user.getName());
					}
				}
		    }
		);	
        t.start();
	}
	
	/**
	 * Eventhandler for adding users
	 */
	public void onAddUser() {
		Thread t = new Thread(
			new Runnable() {
				public void run() {
					String name = (String)JOptionPane.showInputDialog(DemoGui.getInstance().frame, "Username:");
					if (name == null) {
						return;
					}
					User user = UserManager.getInstance().getUser(name);
					if (user == null) {
						user = new User(name);
					}
					pose.setVisible(true);
					OSC osc = new OSC();
					VideoProfile videoProfile = osc.getProfile(); 
					if (videoProfile == null) {
						System.err.println("no bodyshape found!");
						return;
					}
					pose.setVisible(false);
					pose.setIcon(new ImageIcon("pose.png"));
					user.setVideoProfile(videoProfile);
					Logger log = new Logger();
					
					Dialogue dialog = new Dialogue();
					File soundFile = dialog.soundDialog(frame, true);
					
					UserManager.getInstance().setSound(user, soundFile);
					Verifier verifier = new Verifier();
					verifier.prepareAudio(user);
					UserManager.getInstance().createUser(user);
					int pos = DemoGui.getInstance().userList.getModel().getSize();
					DemoGui.getInstance().model.add(model.getSize(), user.getName());
					testUsers.add(user);
				}
			}
		);
		t.start();
	}
	
	private void resetLabels() {
		resultLabel.setText("       Ergebnis        \n ");
		bodyLabel.setText("       Körperformerkennung      \n");
		voiceLabel.setText("       Sprechererkennung        \n ");
		
	}
	
	
	
}
