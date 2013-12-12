package gov.raleigh.edirectory.dto;

import java.io.Serializable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8969445700029523947L;
	private String department = "";
	private String division  = "";
	private String name  = "";
	private String office  = "";
	private String street  = "";
	private String city = "";
	private String state = "";
	private String zip = "";
	private String title = "";
	private String email = "";
	private String username = "";
	private String phone = "";
	private String mobile = "";
	private String employeeID = "";
	private static final Logger logger = LoggerFactory.getLogger(ContactDTO.class);
	public ContactDTO(){
		
	}

	public ContactDTO(Attributes attribs) {
		// TODO Auto-generated constructor stub
		try {
			Attribute city = attribs.get("l");
			Attribute street = attribs.get("streetAddress");
			Attribute state = attribs.get("st");
			Attribute zip = attribs.get("postalCode");
			Attribute name = attribs.get("displayName");
			Attribute username = attribs.get("sAMAccountName");
			Attribute department = attribs.get("department");
			Attribute division = attribs.get("division");
			Attribute title = attribs.get("title");
			Attribute email = attribs.get("mail");
			Attribute office = attribs.get("physicalDeliveryOfficeName");
			Attribute phone = attribs.get("telephoneNumber");
			Attribute mobile = attribs.get("mobile");
			Attribute employeeID = attribs.get("employeeID");
			
			this.city = city != null ? (String) city.get() : "";
			this.street = street != null ? (String) street.get() : "";
			this.state = state != null ? (String) state.get() : "";
			this.zip = zip != null ? (String) zip.get() : "";
			this.name = name != null ? (String) name.get() : "";
			this.username = username != null ? (String) username.get() : "";
			this.department = department != null ? (String) department.get() : "";
			this.division = division != null ? (String) division.get() : "";
			this.title = title != null ? (String) title.get() : "";
			this.email = email != null ? (String) email.get() : "";
			this.office = office != null ? (String) office.get() : "";
			this.phone = phone != null ? (String) phone.get() : "";
			this.mobile = mobile != null ? (String) mobile.get() : "";
			this.setEmployeeID(employeeID != null ? (String) employeeID.get() : "");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to create person");
			e.printStackTrace();
		}
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getMobileTel(){
		return mobile.replaceAll("[- )(.]", "");
	}
	
	public String getPhoneTel(){
		return phone.replaceAll("[- )(.]", "");
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	
}
