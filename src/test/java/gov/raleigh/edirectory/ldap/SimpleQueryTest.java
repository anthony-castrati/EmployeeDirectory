package gov.raleigh.edirectory.ldap;

import static org.junit.Assert.assertTrue;
import gov.raleigh.edirectory.dao.impl.LDAPContactDAO;
import gov.raleigh.edirectory.dto.ContactDTO;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
public class SimpleQueryTest {
	@Autowired
	LDAPContactDAO dao;
/**
	@Test
	public void testSimpleQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResults() {
		fail("Not yet implemented");
	}
**/
	@Test
	public void testGetDepartments(){
		List departments = dao.getDepartments();
		assertTrue(departments.size()>1000);
	}
	@Test
	public void testGetContacts(){
		String[] names = {"Anthony","Castrati"};
		String department = "*";
		
		List<ContactDTO> contacts = dao.getContacts(names, department);
		assertTrue(contacts.size() == 1);
	}
}
