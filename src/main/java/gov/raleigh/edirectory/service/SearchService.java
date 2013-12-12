package gov.raleigh.edirectory.service;

import java.util.HashSet;
import java.util.List;

public interface SearchService {
	public HashSet<String> getDepartments();
	public List getContacts(String name, String department);
}
