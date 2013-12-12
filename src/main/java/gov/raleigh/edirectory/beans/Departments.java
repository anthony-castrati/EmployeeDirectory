package gov.raleigh.edirectory.beans;

import gov.raleigh.edirectory.service.SearchService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@EnableScheduling
@Scope("singleton")
public class Departments {
	private static final Logger logger = LoggerFactory.getLogger(Departments.class);
	private ArrayList<String> departments;
	
	@Autowired 
	SearchService searchService;
	
	public Departments(){
		departments = new ArrayList<String>();
	}
	
	public ArrayList<String> getDepartments(){
		return departments;
	}

	private void setDepartments(ArrayList<String> departments) {
		this.departments = departments;
	}
	
	/**
	 * Scheduled job to refresh departments list.
	 */
	@Scheduled(fixedDelay=600000)
	private void updateDepartments(){
		logger.debug("Updating Departments");
		HashSet<String> results = searchService.getDepartments();
		
		if(results != null){
			Iterator<String> iter = results.iterator();
			ArrayList<String> temp = new ArrayList<String>();
			while(iter.hasNext()){
				temp.add(iter.next());
			}
			
			Collections.sort(temp, new Comparator<String>() {
				@Override
		        public int compare(String s1, String s2) {
		            return s1.compareToIgnoreCase(s2);
		        }
			});
			
			setDepartments(temp);
			
		}
		logger.debug("Departments Updated");
		
	}
}
