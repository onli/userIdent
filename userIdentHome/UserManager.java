class UserManager {
	private static UserManager instance = new UserManager();
	
	public static UserManager getInstance() {
		return instance;
	}
		
	private UserManager() {
		
	}
	
	public void createUser(String name) {
		User user = new User();
		user.setName("name");
		this.saveUser(user);
	}
	
	private saveUser(User user) {
		
	}
}

