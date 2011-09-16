class User {
	private String name;
	private AudioProfile audioProfile;
	private VideoProfile videoProfile;
	
	public void setAudioProfile(AudioProfile audioProfile) {
		this.audioProfile = audioProfile;
	} 
	
	public AudioProfile getAudioProfile() {
		return this.audioProfile;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
