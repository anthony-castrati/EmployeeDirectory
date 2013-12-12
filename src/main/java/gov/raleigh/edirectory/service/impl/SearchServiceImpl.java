package gov.raleigh.edirectory.service.impl;

import gov.raleigh.edirectory.dao.ContactDAO;
import gov.raleigh.edirectory.service.SearchService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
	
	ContactDAO contactDao;
	
	@Autowired
	public void setContactDao(ContactDAO dao) {
		this.contactDao = dao;

	}

	@Override
	public HashSet<String> getDepartments() {
		HashSet departments = new HashSet();
		List depts = contactDao.getDepartments();
		Iterator<String> iter = depts.iterator();
		while(iter.hasNext()){
			departments.add((String) iter.next());
		}
		return departments;
	}

	@Override
	public List getContacts(String name, String department) {
		if ("All Departments".equals(department) )
			department = "*";
		String[] names = name.trim().replaceAll("[,.]","").split(" ");
		return contactDao.getContacts(names, department);
	}

}
