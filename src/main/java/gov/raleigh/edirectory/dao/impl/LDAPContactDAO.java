package gov.raleigh.edirectory.dao.impl;

import gov.raleigh.edirectory.dao.ContactDAO;
import gov.raleigh.edirectory.dto.ContactDTO;

import java.util.HashSet;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.AttributesMapperCallbackHandler;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

@Repository
public class LDAPContactDAO implements ContactDAO {
	
	@Value("${ldap.pageSize}")
	private int PAGE_SIZE;
	@Value("#{'${ldap.returnedAttrs}'.split(',')}")
	private List<String> returnedAttrs;
	@Value("${ldap.userquery}")
	private String userQuery;
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Override
	public List getDepartments() {
		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(new AttributesMapper() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("department").get();
	        }
		});
		
		return query(userQuery,handler);
	}
	
	@Override
	public List getContacts(String[] names, String department){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectClass","user"));
		filter.and(new LikeFilter("department",department));
		filter.and(new LikeFilter("employeeID","*"));
		filter.and(new LikeFilter("displayName","*"));
		filter.and(new EqualsFilter("objectCategory","person"));
		filter.and(new NotFilter(new EqualsFilter("userAccountControl:1.2.840.113556.1.4.803:","2")));
		OrFilter name = new OrFilter();
		name.or(new LikeFilter("sAMAccountName",names[0].equals("") ? "*" : names[0]));
		AndFilter displayName = new AndFilter();
		for(int i = 0; i < names.length; i++){
			if(!"".equals(names[i]))
			displayName.and(new LikeFilter("displayName","*" + names[i] + "*"));
		}
		name.or(displayName);
		filter.and(name);
		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(new AttributesMapper() {
			public ContactDTO mapFromAttributes(Attributes attrs) throws NamingException {
				ContactDTO contact = new ContactDTO(attrs);
				return contact;
	        }
		});
		return query(filter.encode(), handler);
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
