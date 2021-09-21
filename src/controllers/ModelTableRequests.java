package controllers;

public class ModelTableRequests {

	String ID, username, firstname, lastname, date, schedule, shortDescription, role, usernameAccept, approved;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

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

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsernameAccept() {
		return usernameAccept;
	}

	public void setUsernameAccept(String usernameAccept) {
		this.usernameAccept = usernameAccept;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public ModelTableRequests(String iD, String username, String firstname, String lastname, String date,
			String schedule, String shortDescription, String role, String usernameAccept, String approved) {
		super();
		ID = iD;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.date = date;
		this.schedule = schedule;
		this.shortDescription = shortDescription;
		this.role = role;
		this.usernameAccept = usernameAccept;
		this.approved = approved;
	}
}
