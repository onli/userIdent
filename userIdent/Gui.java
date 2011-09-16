package userIdent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import audio.AudioResult;
import audio.Mistral;
import body.OSC;
import body.VideoProfile;


public class Gui {
	private JFrame frame;
	private JPanel panel;
	private JList userList;
	private JButton addUserButton;
	private JButton removeUserButton;
	private JButton trainWorldButton;
	private JButton trainUserButton;
	private JButton testButton;
	private JButton testSoundButton;
	private JButton testBodyButton;
	private GroupLayout layout;
	private JTextArea console;
	private JScrollPane scrollPane;
	private JScrollPane listScrollPane;
	
	private static Gui instance = new Gui();
	
	public static Gui getInstance() {
		return instance;
	}
	
	private Gui() {
		frame = new JFrame();
		panel = new JPanel();
		
		addUserButton = new JButton("Add");
		removeUserButton = new JButton("Remove");
		trainUserButton = new JButton("Train");
		trainWorldButton = new JButton("Train World");
		testButton = new JButton("Test All");
		testSoundButton = new JButton("Speaker only");
		testBodyButton = new JButton("Bodyshape");
		console = new JTextArea(5, 20);
		
		console.setEditable(false);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		scrollPane = new JScrollPane(console); 
		scrollPane.setPreferredSize(new Dimension(400,280));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onAddUser();
			} 
		});
		removeUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onRemove();
			} 
		});
		trainWorldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onTrainWorld();
			} 
		});
		trainUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onTrain();
			} 
		});
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onTest();
			} 
		});
		testSoundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onTestSound();
			} 
		});
		testBodyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gui.getInstance().onTestBody();
			} 
		});
		
		
		this.layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.repaintUserList();
		
		frame.add(panel);
		frame.setSize(640, 620);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void show() {
		this.frame.setVisible(true);
	}
	/**
	 * repaints the userlist with the actual users in the system
	 */
	public void repaintUserList() {
		Vector<String> userNames = new Vector<String>();
		for (User user: UserManager.getInstance().getUsers()) {
			userNames.add(user.getName());
		}
		try {
			this.panel.removeAll();
		} catch (NullPointerException npe) {
			
		}
		this.userList = new JList(userNames);
		
		MouseListener mouseListener = new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	if (e.getButton() == e.BUTTON3) {
			    	int index = userList.locationToIndex(e.getPoint());
			    	userList.setSelectedIndex(index);
			    	userList.getSelectedValue();
		        	JPopupMenu popup = new JPopupMenu();
		            JMenuItem menuItem = new JMenuItem("Info");
		            menuItem.addActionListener(new ActionListener() {
		    			public void actionPerformed(ActionEvent action) {
		    				Gui.getInstance().showInfo((String)userList.getSelectedValue());
		    				((JMenuItem)action.getSource()).getParent().setVisible(false);
		    			} 
		    		});
		            popup.add(menuItem);
		            popup.setLocation(e.getXOnScreen(), e.getYOnScreen());
		            popup.setVisible(true);
		         }
		    }
		};
		userList.addMouseListener(mouseListener);
		listScrollPane = new JScrollPane(userList); 
		listScrollPane.setPreferredSize(new Dimension(200,480));
		listScrollPane.setMinimumSize(new Dimension(100,200));
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setElements();
	}
	
	private void setElements() {
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(listScrollPane)
				   			.addComponent(trainWorldButton)
				   		)
				      	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				      		.addComponent(addUserButton)
				      		.addComponent(removeUserButton)
				      		.addComponent(trainUserButton)
				      	)
				      	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				      		.addGroup(layout.createSequentialGroup()
				      			.addComponent(testBodyButton)
				      			.addComponent(testSoundButton)
			      				.addComponent(testButton)
				      		)
				      		.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				      	)
				      	
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(listScrollPane)
							.addGroup(layout.createSequentialGroup()
									.addComponent(addUserButton)     
									.addComponent(removeUserButton)
									.addComponent(trainUserButton)
							)
					)
					
					.addComponent(trainWorldButton) 
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(testBodyButton)
							.addComponent(testSoundButton)
							.addComponent(testButton)	  
					)
					.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)   
		);
	}
	
	/**
	 * Eventhandler for removing users
	 */
	public void onRemove() {
		Object[] selected = Gui.getInstance().userList.getSelectedValues();
		for(Object username: selected) {
			User user = UserManager.getInstance().getUser((String)username); 
			UserManager.getInstance().removeUser(user);
		}
		this.repaintUserList();
	}
	
	/**
	 * Eventhandler for adding users
	 */
	public void onAddUser() {
		Thread t = new Thread(
			new Runnable() {
				public void run() {
					String name = (String)JOptionPane.showInputDialog(Gui.getInstance().frame, "Username:");
					if (name == null) {
						return;
					}
					User user = UserManager.getInstance().getUser(name);
					if (user == null) {
						user = new User(name);
					}
					
					OSC osc = new OSC();
					VideoProfile videoProfile = osc.getProfile(); 
					if (videoProfile == null) {
						System.err.println("no bodyshape found!");
						return;
					}
					user.setVideoProfile(videoProfile);
					Logger log = new Logger();
					
					Dialogue dialog = new Dialogue();
					File soundFile = dialog.soundDialog(frame, true);
					
					UserManager.getInstance().setSound(user, soundFile);
					Verifier verifier = new Verifier();
					verifier.prepareAudio(user);
					UserManager.getInstance().createUser(user);
					Gui.getInstance().repaintUserList();
				}
			}
		);
		t.start();
	}
	
	public void onTrainWorld() {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						Verifier verifier = new Verifier();
						Object[] selected = Gui.getInstance().userList.getSelectedValues();
						Vector<User> users = new Vector<User>();
						if (selected.length < 2) {
							users = UserManager.getInstance().getUsers();
						} else {
							for(Object username: selected) {
								users.add(UserManager.getInstance().getUser((String)username));
							}
						}
						verifier.prepareWorld(users);
					}
				}
		);
		t.start();
	}
	
	public void onTrain() {
		Thread t = new Thread(
			new Runnable() {
				public void run() {
					Verifier verifier = new Verifier();
					Object[] selected = Gui.getInstance().userList.getSelectedValues();
					Vector<User> users = new Vector<User>();
					for(Object username: selected) {
						users.add(UserManager.getInstance().getUser((String)username));
					}			
					for (User user: users) {
						verifier.prepareAudio(user);
					}
				}
			}
		);
		t.start();
	}
	
	public void onTest() {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						Verifier verifier = new Verifier();
						//body recognition
						Vector<User> users = verifier.whoIs();
						if (users == null) {
							return;
						}
						//speaker recognition with possible speakers
						Dialogue dialog = new Dialogue();
						File file = dialog.soundDialog(frame, false);
						
						TreeMap<Double, User> results = verifier.whoIs(file, users);
						Iterator<Double> iter= results.navigableKeySet().iterator();
						JOptionPane.showMessageDialog(frame, results.get(results.lastKey()).getName() + "\t" + results.lastKey());
					}
				}
		);	
		t.start();
	}
	
	public void onTestSound() {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						Verifier verifier = new Verifier();
						//speaker recognition with speakers
						Dialogue dialog = new Dialogue();
						File file = dialog.soundDialog(frame, false);
						User user = verifier.whoIs(file);
						JOptionPane.showMessageDialog(frame, user.getName());
					}
				}
		);	
		t.start();
	}
	
	public void onTestBody() {
		Thread t = new Thread(
			new Runnable() {
				public void run() {
					Verifier verifier = new Verifier();
					//body recognition
					Vector<User> users = verifier.whoIs();
					String found = "";
					if (users == null) {
						found = "None"; 
					} 
					for (User user: users) {
						found += user.getName() + "\n";
					}
					
			        JOptionPane.showMessageDialog(frame, found);
				}
		    }
		);	
        t.start();
	}
	
	public void showInfo(String name) {
		User user = UserManager.getInstance().getUser(name);
		try {
			this.showText(user.getVideoProfile().print());
		} catch (NullPointerException npe) {
			
		}
	}
	
	public void showText(String text) {
		this.console.append(text);
	}
}
