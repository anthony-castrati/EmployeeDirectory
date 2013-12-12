package gov.raleigh.edirectory.dao;

import java.util.List;

public interface ContactDAO {


	public List getDepartments();

	public List getContacts(String[] name, String department);
}