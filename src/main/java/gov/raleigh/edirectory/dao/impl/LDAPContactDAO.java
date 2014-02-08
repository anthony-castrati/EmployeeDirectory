package gov.raleigh.edirectory.dao.impl;

import gov.raleigh.edirectory.dao.ContactDAO;
import gov.raleigh.edirectory.dto.ContactDTO;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.AttributesMapperCallbackHandler;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.stereotype.Repository;

@Repository
public class LDAPContactDAO implements ContactDAO {
	private static Logger logger = Logger.getLogger(ContactDAO.class);
	@Value("${ldap.pageSize}")
	private int PAGE_SIZE;
	@Value("#{'${ldap.returnedAttrs}'.split(',')}")
	private List<String> returnedAttrs;
	@Value("${ldap.userquery}")
	private String userQuery;
	@Value("${ldap.departmentQuery}")
	private String departmentQuery;
	@Value("${imageFolder}")
	private String IMAGE_FOLDER;
	@Autowired
	private LdapTemplate ldapTemplate;
	
	public List getDepartments() {
		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(new AttributesMapper() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("department").get();
	        }
		});
		return query(departmentQuery,handler);
	}
	
	@Override
	@Cacheable(value="queries", key="T(java.util.Arrays).toString(#names).concat(#department)")
	public List getContacts(String[] names, String department){
		logger.debug("Searching...");
		logger.debug("Name: " + names);
		logger.debug("Department: " + department);
		AndFilter displayName = new AndFilter();
		for(int i = 0; i < names.length; i++){
			if(!"".equals(names[i]))
			displayName.and(new LikeFilter("displayName","*" + names[i] + "*"));
		}
		String finalQuery = MessageFormat.format(userQuery, displayName.encode(), department);
		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(new AttributesMapper() {
			public ContactDTO mapFromAttributes(Attributes attrs) throws NamingException {
				ContactDTO contact = new ContactDTO(attrs);
				Attribute employeeID = attrs.get("employeeID");
				if(employeeID != null){
					String path = IMAGE_FOLDER + File.separator + employeeID.get() + ".jpg";
					File f = new File(path);
					if(f.exists()){
						contact.setHasImage(true);
					}
				}
				return contact;
	        }
		});
		return query(finalQuery, handler);
	}
	
	private List query(String filter, CollectingNameClassPairCallbackHandler handler){
		SearchControls searchControls = new SearchControls();
	    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    searchControls.setReturningAttributes(returnedAttrs.toArray(new String[returnedAttrs.size()]));
	    PagedResultsCookie cookie = null;
	    PagedResultsDirContextProcessor requestControl;
	    do {
	    	requestControl = new PagedResultsDirContextProcessor(PAGE_SIZE, cookie);
	    	ldapTemplate.search("", filter, searchControls, handler, requestControl);
	    	cookie = requestControl.getCookie();
	    } while ( cookie.getCookie() != null );
	    
	    return handler.getList();
	}
}
