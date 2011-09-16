package userIdent;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import audio.AudioResult;
import audio.Mistral;
import audio.SoundPreparer;
import body.OSC;
import body.VideoProfile;

/**
 * Central control of all verification
 * @author Malte
 */
public class Verifier {
	
	
	/**
	 * As there is no param given, try to determine a possible usergroup using the bodyrecognition
	 * @return
	 */
	public Vector<User> whoIs() {
		OSC osc = new OSC();
		VideoProfile videoProfile = null;		

		while ((videoProfile = osc.getProfile()) == null) {
			try {
				wait(500);
			} 
			catch (InterruptedException e) {}
			catch (IllegalMonitorStateException imse) {return null;} 
		}
		Logger log = new Logger();
		log.log(videoProfile.print());
		       
		Vector<User> users = new Vector<User>();
		
		for(User user: UserManager.getInstance().getUsers()) {
			try {
				if (user.getVideoProfile().fits(videoProfile)) {
					users.add(user);
					log.log(user.getName());
				}
			} catch (NullPointerException npe) {
				//ignore those users without body-profile, only added for worldmodel
			}
		}
		return users;
	}
	
	
	
	/**
	 * Verify based on audio only
	 * @param audio
	 */
	public User whoIs(File sound) {
		Mistral mist = new Mistral();
		SoundPreparer sp = new SoundPreparer();
		sound = sp.removeSilence(sound);
		TreeMap<Double, User> results = new TreeMap<Double, User>();
		for(User user: UserManager.getInstance().getUsers()) {
			results.put(mist.testUser(user, sound), user);
		}
		Logger logger = new Logger();
		Iterator<Double> iter= results.navigableKeySet().iterator();
		while(iter.hasNext()) {
			Double current = iter.next();
			logger.log(results.get(current).getName()+":");
			logger.log(current.toString());
		}
		return results.get(results.lastKey());
	}
	
	/**
	 * Verify based on audio, considering only the given users 
	 * @param audio
	 * @param users
	 */
	public TreeMap<Double, User> whoIs(File sound, Vector<User> users) {
		Mistral mist = new Mistral();
		SoundPreparer sp = new SoundPreparer();
		sound = sp.removeSilence(sound);
		TreeMap<Double, User> results = new TreeMap<Double, User>();
		for(User user: users) {
			results.put(mist.testUser(user, sound), user);
		}

		Logger logger = new Logger();
		Iterator<Double> iter= results.navigableKeySet().iterator();
		
		while(iter.hasNext()) {
			Double current = iter.next();
			logger.log(results.get(current).getName()+":");
			logger.log(current.toString());
		}
		return results;
	}
	
	/**
	 * train the audio-profile of a user
	 * @param user
	 */
	public void prepareAudio(User user) {
		Mistral mist = new Mistral();
		mist.extractFeatures(user);
		mist.normalizeEnergy(user);
		mist.normalizeFeatures(user);
		mist.trainUser(user);		
	}
	
	public void prepareWorld(Vector<User> users) {
		Mistral mist = new Mistral();
		mist.trainWorld(users);
	}
	
}
