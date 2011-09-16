package userIdent;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.setProperty("awt.useSystemAAFontSettings", "on");
		try {
			System.out.println(args[0]);
			if (args[0].equals("learn") || args[0].equals("--learn") || args[0].equals("-l")) {
				//Add user + sound for a better worldmodel
				System.out.println("Adding user");
				String name = args[1];
				System.out.println(name);
				System.out.println(args[2]);
				File soundFile = new File(args[2]);
				User user = UserManager.getInstance().getUser(name);
				if (user == null) {
					user = new User(name);
				}
				UserManager.getInstance().setSound(user, soundFile);
				Verifier verifier = new Verifier();
				verifier.prepareAudio(user);
				UserManager.getInstance().createUser(user);
				System.out.println("learned soundprofile from" + name);
				
			}
		} catch (ArrayIndexOutOfBoundsException aiiobe) {
			System.out.println("Starting gui");
			Gui.getInstance().show();
		}
		System.out.println("Doing nothing");
	}

}
