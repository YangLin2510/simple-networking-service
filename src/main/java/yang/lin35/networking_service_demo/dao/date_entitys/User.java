package yang.lin35.networking_service_demo.dao.date_entitys;

public class User {
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactList() {
		return contactList;
	}

	public void setContactList(String contactList) {
		this.contactList = contactList;
	}

	private String username;
	private String password;
	private String contactList;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
