package controllers;

public class ModelTable {

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	String username, firstname, lastname, date, registrationType;

	public ModelTable(String username, String firstname, String lastname, String date, String registrationType) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.date = date;
		this.registrationType = registrationType;
	}
	
	@Override
	public String toString() {
		return username + " " + firstname + " " + lastname + " " + date + " " + registrationType;
	}
	
}
