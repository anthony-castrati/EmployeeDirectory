package gov.raleigh.edirectory;

import gov.raleigh.edirectory.beans.Departments;
import gov.raleigh.edirectory.dto.ContactDTO;
import gov.raleigh.edirectory.dto.SearchForm;
import gov.raleigh.edirectory.service.SearchService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Value( "${initialresults}" )
	private int INITIAL_RESULTS;
	@Value( "${imageFolder}" )
	private String IMAGE_FOLDER;
	@Value("${imageCacheTime}")
	private int CACHE_DURATION_IN_SECOND;
	final long   CACHE_DURATION_IN_MS = CACHE_DURATION_IN_SECOND  * 1000;
	@Autowired
	private Departments departments;
	@Autowired
	SearchService searchService;
	public HomeController(){
		
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		SearchForm bean = new SearchForm();
        model.addAttribute("searchForm", bean);
        
		//model.addAttribute("departments", d.getDepartments());
		
		return "home";
	}
	
	/**
	 * Uses form inputs to search LDAP based on given input
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String search(HttpSession session, SearchForm searchForm, BindingResult bindResult, @RequestParam("showall") boolean showall, @RequestParam("name") String name, @RequestParam("department") String dept, Model model) {
		
		List<ContactDTO> results = searchService.getContacts(name, dept);
		
		session.setAttribute("results", results);
		if (showall)
			session.setAttribute("max",results.size());
		else
			session.setAttribute("max",19);
		
		Collections.sort(results, new Comparator<ContactDTO>() {
			@Override
	        public int compare(ContactDTO s1, ContactDTO s2) {
	            return s1.getName().compareToIgnoreCase(s2.getName());
	        }
		});
		session.setAttribute("count", results.size());
		return "home";
	}
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String searchGet( Locale locale, Model model) {
		return home(locale, model);
	}

	@RequestMapping(value = "images/{uniqueid}", method = RequestMethod.GET)
	public void getJson(@PathVariable String uniqueid, HttpServletResponse response, WebRequest request) throws IOException {
		File f = new File(IMAGE_FOLDER + File.separator + uniqueid + ".jpg");
		
		if(f.exists()){
			if (request.checkNotModified(f.lastModified())) {
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
		     }
			response.setContentType("image/jpeg");
			long now = System.currentTimeMillis();
			//res being the HttpServletResponse of the request
			response.addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
			response.setDateHeader("Last-Modified", f.lastModified());
			response.setDateHeader("Expires", now + CACHE_DURATION_IN_MS);
			response.addHeader("ETag", f.getName());
			InputStream is;
			try {
				is = new FileInputStream(f);
				byte[] buf = new byte[512];
			    int bytesRead = is.read(buf);
			    ServletOutputStream out = response.getOutputStream();
			    while (bytesRead != -1) {
			        out.write(buf, 0, bytesRead);
			        bytesRead = is.read(buf);
			      }
			} catch (FileNotFoundException e) {
				logger.warn("The requested image was not found - " + uniqueid + ".jpg");
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("Error writing image to response - " + uniqueid + ".jpg");
				e.printStackTrace();
			}
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
