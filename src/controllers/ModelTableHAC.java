package controllers;

public class ModelTableHAC {

	String username, IDticket, phonenumber, issueticket, description, date;

	public ModelTableHAC(String username, String iDticket, String phonenumber, String issueticket, String description,
			String date) {
		super();
		this.username = username;
		IDticket = iDticket;
		this.phonenumber = phonenumber;
		this.issueticket = issueticket;
		this.description = description;
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIDticket() {
		return IDticket;
	}

	public void setIDticket(String iDticket) {
		IDticket = iDticket;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getIssueticket() {
		return issueticket;
	}

	public void setIssueticket(String issueticket) {
		this.issueticket = issueticket;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ModelTableHAC [username=" + username + ", IDticket=" + IDticket + ", phonenumber=" + phonenumber
				+ ", issueticket=" + issueticket + ", description=" + description + ", date=" + date + "]";
	}

	
}
