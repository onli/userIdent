package userIdent;

import audio.AudioProfile;
import body.*;

public class User {
	private String name;
	private AudioProfile audioProfile;
	private VideoProfile videoProfile;
	
	public User(String name) {
		this.name = name;
	}
	

	public void setAudioProfile(AudioProfile audioProfile) {
		this.audioProfile = audioProfile;
	} 
	
	public void setVideoProfile(VideoProfile videoProfile) {
		this.videoProfile = videoProfile;
	}
	
	
	public AudioProfile getAudioProfile() {
		return this.audioProfile;
	}
	
	public VideoProfile getVideoProfile() {
		return this.videoProfile;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
