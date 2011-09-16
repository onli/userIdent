package userIdent;
import java.io.File;
import java.util.Vector;

import audio.SoundPreparer;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oRecoverableException;

class UserManager {
	private static UserManager instance = new UserManager();
	
	public static UserManager getInstance() {
		return instance;
	}
		
	private UserManager() {
		
	}
	
	/**
	 * Create and save a user
	 * @param name
	 * @return
	 */
	public User createUser(User user) {
		this.saveUser(user);
		return user;
	}
	
	/**
	 * Save a soundsample of a user
	 * @param user
	 * @param file
	 */
	public void setSound(User user, File file) {
		FileControl fc = new FileControl();
		SoundPreparer sp = new SoundPreparer();
		file = sp.removeSilence(file);
		fc.copy(file, Options.soundPath + user.getName() + ".wav");
	}
	
	private void saveUser(User user) {
		
		ObjectContainer userDB = Db4o.openFile(Options.DBpath+"users.db");
		try {
			userDB.store(user);
		} finally {
			userDB.close();
		}
	}
	
	/**
	 * Get all users
	 * @return
	 */
	public Vector<User> getUsers() {
		ObjectContainer userDB = Db4o.openFile(Options.DBpath+"users.db");
		Vector<User> users = new Vector<User>();
		try {
			ObjectSet<User> result = userDB.queryByExample(User.class);
			users.addAll(result);
		} finally {
			userDB.close();
		}
		return users;
	}
	/**
	 * Get a user with the current name
	 * @param name String username
	 * @return User or null if not existing
	 */
	public User getUser(String name) {
		ObjectContainer userDB = Db4o.openFile(Options.DBpath+"users.db");
		User user = new User(name);
		try {
			ObjectSet<User> result = userDB.queryByExample(user);
			return result.get(0);
		} catch (NullPointerException npe) {
			return null;
		} catch (Db4oRecoverableException db4oE) {
			return null;
		} finally {
			userDB.close();
		}
	}
	
	/**
	 * Remove a user from the db
	 * @param user User
	 */
	public void removeUser(User user) {
		ObjectContainer userDB = Db4o.openFile(Options.DBpath+"users.db");
		try {
			ObjectSet result = userDB.queryByExample(user);
			userDB.delete(result.get(0));
		} finally {
			userDB.close();
		}
	}
}

