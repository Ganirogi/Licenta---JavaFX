package controllers;

public class ModelTableProfile {
	public ModelTableProfile(String username, String taxe, String role, String phonenumber, Double grosssalary,
			Double netsalary) {
		super();
		this.username = username;
		this.taxe = taxe;
		this.role = role;
		this.phonenumber = phonenumber;
		this.grosssalary = grosssalary;
		this.netsalary = netsalary;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTaxe() {
		return taxe;
	}
	public void setTaxe(String taxe) {
		this.taxe = taxe;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public Double getGrosssalary() {
		return grosssalary;
	}
	public void setGrosssalary(Double grosssalary) {
		this.grosssalary = grosssalary;
	}
	public Double getNetsalary() {
		return netsalary;
	}
	public void setNetsalary(Double netsalary) {
		this.netsalary = netsalary;
	}
	String username,taxe, role, phonenumber;
	Double grosssalary, netsalary;
}
