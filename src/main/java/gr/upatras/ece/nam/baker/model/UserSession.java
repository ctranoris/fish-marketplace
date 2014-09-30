package gr.upatras.ece.nam.baker.model;

public class UserSession {


	private String username = null;
	private String password = null;
	
	
	
	public UserSession() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
