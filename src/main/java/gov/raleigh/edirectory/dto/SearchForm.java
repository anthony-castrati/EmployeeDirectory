package gov.raleigh.edirectory.dto;

import javax.validation.constraints.Size;

public class SearchForm {
	@Size(min=2, message="Must provide a name. Minimum of 2 characters.")
	private String name;
	private String department;
	private String showall;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getShowall() {
		return showall;
	}
	public void setShowall(String showall) {
		this.showall = showall;
	}
	
}

