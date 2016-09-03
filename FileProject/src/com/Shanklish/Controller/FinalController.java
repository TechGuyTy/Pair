package com.Shanklish.Controller;

import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.catalina.connector.Request;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.internal.StatisticalLoggingSessionEventListener;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Controller
public class FinalController {

	// --------------------------------------HIBERNATE
	// CONFIGURATION----------------------------------

	// Driver & Factory Configuration
	private static SessionFactory factory;

	private static void setupFactory() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}

		catch (Exception e) {
			;// this is silliness!
		}

		Configuration configuration = new Configuration();

		// Pass hibernate configuration file
		configuration.configure("hibernate.cfg.xml");

		// pass in setup file for Product class
		configuration.addResource("users.hbm.xml");

		configuration.addResource("bookmarkedjob.hbm.xml");

		// Since version 4.x, service registry is being used
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		// Create session factory instance
		factory = configuration.buildSessionFactory(serviceRegistry);
	}

	// --------------------------------------RETRIEVES LIST OF Saved
	// Jobs----------------------------------

	public static List<job> getAllSavedJobs(String uID) {
		if (factory == null)
			setupFactory();

		// Get current session
		Session hibernateSession = factory.openSession();

		// Begin transaction
		hibernateSession.getTransaction().begin();

		// deprecated method & unsafe cast
		List<job> urls = hibernateSession
				.createQuery("from com.Shanklish.Controller.BookmarkedJob WHERE uID = '" + uID + "'").list();

		// Commit transaction
		hibernateSession.getTransaction().commit();

		hibernateSession.close();

		return urls; // Return arraylist of URLS
	}

	// --------------------------------------LOGIN CREDENTIAL
	// HANDLER----------------------------------

	// Pipes the email & password entered by the user
	@RequestMapping("/login")
	public String submitLogin(@ModelAttribute("command") User user, Model model) {
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		return "login";
	}

	// -------------------------------------------Add User
	// Methods-----------------

	@RequestMapping("/signup")
	public static ModelAndView showForm(@ModelAttribute("command") User user, Model model) {
		return (new ModelAndView("SignUp"));
	}

	@RequestMapping("/create")
	public static ModelAndView createUser(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpServletRequest request)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		User user = new User();
		user.setEmail(email);
		user.setPassword(generateStrongPasswordHash(password));

		addUser(user); // stores them into DB

		user.setId(getID(email));

		request.getSession().setAttribute("email", email);
		request.getSession().setAttribute("uID", getID(email));
		request.getSession().setMaxInactiveInterval(-1);

		return new ModelAndView("search", "message", "it worked");

	}

	public static ModelAndView addUser(User u) {
		if (factory == null)
			setupFactory();

		// Get current session
		Session hibernateSession = factory.openSession();

		// Begin transaction
		hibernateSession.getTransaction().begin();

		// save this specific record
		hibernateSession.save(u);

		// Commit transaction
		hibernateSession.getTransaction().commit();

		hibernateSession.close();

		return new ModelAndView("search", "message", "Account Created");
	}

	// --------------------------------RETRIEVES AND DISPLAYS ALL QUERIED JOBS
	// FROM VARIOUS APIS---------------------------------------

	@RequestMapping("/welcome")
	public ModelAndView helloWorld(Model model, @RequestParam("query") String keyword,
			@RequestParam("state") String location, HttpServletRequest request) throws ClientProtocolException,
			IOException, ParseException, NullPointerException, IndexOutOfBoundsException {

		ArrayList<job> jobList = diceJobSearch(keyword, location); // Array to
		// store
		// jobs
		// returned
		// from DICE

		ArrayList<job> indeedJobList = indeedJobSearch(keyword, location); // Array
																			// to
																			// store
																			// jobs
																			// returned
																			// from
																			// Indeed

		indeedJobList.addAll(jobList); // Combining the two arrays into one.

		List<job> bookmarks = getAllSavedJobs(request.getSession().getAttribute("uID").toString());

		model.addAttribute("bookmarkArray", bookmarks);

		model.addAttribute("array", indeedJobList); // Sends the array to welcome.jsp
												// using JSTL
		model.addAttribute("userID", request.getSession().getAttribute("uID").toString());

		// sends user to the welcome page with a header and list of requested
		// jobs
		return new ModelAndView("welcome", "message", "Jobs matching " + keyword + " in " + location);
	}

	// --------------------------------------DICE JOB
	// PARSER----------------------------------

	@RequestMapping("dice")
	public ArrayList<job> diceJobSearch(String pKeyword, String pLocation)
			throws ClientProtocolException, IOException, ParseException

	{
		String keyword = pKeyword.replaceAll("\\s", "+"); // removes whitespace
		String location = pLocation.replaceAll("\\s", "+");

		String url = "http://service.dice.com/api/rest/jobsearch/v1/simple.json?text=" + keyword + "&state=" + location
				+ ""; // search query with userInput injection

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request); // Executes HTTP GET
															// request

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); // Read
																											// content
																											// of
																											// JSON

		StringBuffer result = new StringBuffer();

		String line = "";

		while ((line = rd.readLine()) != null) // builds one long string
												// containing all content
												// retrieved
		{
			result.append(line);
		}
		System.out.println("@#$#@" + result);

		// ------------------------------------------^Retrieving List - vParsing
		// List--------------

		JSONParser parser = new JSONParser();

		Object object = null;

		object = parser.parse(result.toString()); // parses the content
		System.out.println("This is result:" + result);
		System.out.println("This is the result parsed to a string?:" + object);
		JSONObject jsonObject = (JSONObject) object;

		System.out.println("This is the jsonObject:" + jsonObject);
		JSONArray posts = (JSONArray) jsonObject.get("resultItemList"); // retrieves
																		// jsonObject
																		// which
																		// stores
																		// Job
																		// Data
		System.out.println("Theses are the posts:" + posts);

		System.out.println("Here are new posts:" + posts);

		ArrayList<job> diceJobArray = new ArrayList<job>();
		if (posts != null) {
			Iterator<JSONObject> iterator = posts.iterator();
			// Iterates through content

			job newjob = null;

			while (iterator.hasNext()) // At each loop, a new job is created and
										// the fields are assigned. All of which
										// are read from JSON retrieved.
			{
				newjob = new job();

				JSONObject job = iterator.next();

				newjob.setJobTitle((String) job.get("jobTitle"));
				newjob.setCompany((String) job.get("company"));
				newjob.setLocation((String) job.get("location"));
				newjob.setUrl((String) job.get("detailUrl"));
				newjob.setEngine("Dice");

				diceJobArray.add(newjob);

			}
		}
		return diceJobArray;
	}

	// --------------------------------------INDEED JOB
	// PARSER----------------------------------

	public ArrayList<job> indeedJobSearch(String pkeyword, String plocation)
			throws ClientProtocolException, IOException, ParseException {
		String keyword = pkeyword.replaceAll("\\s", "+");
		String location = plocation.replaceAll("\\s", "+");
		String url = "http://api.indeed.com/ads/apisearch?publisher=2945076701195809&q=" + keyword + "&l=" + location
				+ "&format=json&sort=&radius=&st=&jt=&start=&limit=50&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();

		String line = "";

		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		// ------------------------------------------

		JSONParser parser = new JSONParser();

		Object object = parser.parse(result.toString());

		JSONObject jsonObject = (JSONObject) object;

		JSONArray posts = (JSONArray) jsonObject.get("results");

		Iterator<JSONObject> iterator = posts.iterator();

		ArrayList<job> indeedJobArray = new ArrayList<job>();

		job newjob = null;

		while (iterator.hasNext()) {
			newjob = new job();

			JSONObject job = iterator.next();

			newjob.setJobTitle((String) job.get("jobtitle"));
			newjob.setCompany((String) job.get("company"));
			newjob.setLocation((String) job.get("formattedLocation"));
			newjob.setUrl((String) job.get("url"));
			newjob.setEngine("Indeed");

			indeedJobArray.add(newjob);

		}

		return indeedJobArray;
	}

	// --------------------------Retrieve uID------------

	public static Integer getID(String email) {

		if (factory == null)
			setupFactory();

		// Get current session
		Session hibernateSession = factory.openSession();

		// Begin transaction
		hibernateSession.getTransaction().begin();

		// deprecated method & unsafe cast
		List<Integer> list = hibernateSession
				.createQuery("select id from com.Shanklish.Controller.User where email = '" + email + "'").list();

		System.out.println(list);
		Integer id = list.get(0);

		// Commit transaction
		hibernateSession.getTransaction().commit();

		hibernateSession.close();

		return id;
	}

	// ------------------SAVE BOOKMARKED JOBS--------------------

	@RequestMapping("/bookmarkJob")
	public ModelAndView saveJob(@RequestParam("url") String url, @RequestParam("title") String title,
			HttpServletRequest request, Model model) // @requestParam
	{
		BookmarkedJob bJob = new BookmarkedJob();

		bJob.setUrl(url);
		bJob.setJobTitle(title);
		bJob.setuID((int) request.getSession().getAttribute("uID"));

		if (factory == null)
			setupFactory();

		// Get current session
		Session hibernateSession = factory.openSession();

		// Begin transaction
		hibernateSession.getTransaction().begin();

		// save this specific record
		hibernateSession.save(bJob);

		// Commit transaction
		hibernateSession.getTransaction().commit();

		hibernateSession.close();

		return new ModelAndView("search");

	}

	// --------------------------------------PASSWORD
	// ENCRYPTION----------------------------------

	private static String generateStrongPasswordHash(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000; // Generates a PBKDF2 Hash for passwords.
								// Iterated 1000 times over in addition to Salt.
		char[] chars = password.toCharArray();
		byte[] salt = getSalt();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException // Creates
																	// random
																	// string
																	// for
																	// SALTING
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException // Converts
																				// String
																				// to
																				// Hexadecimals
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	// --------------------------------------PASSWORD
	// VALIDATION----------------------------------

	@RequestMapping("/verifyPassword")
	public ModelAndView VerifyPassword(@RequestParam("password") String pWord, @RequestParam("email") String eMail,
			HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Verifies the password entered by user and the password stored in DB
		// are the same.
		request.getSession().setAttribute("email", eMail);
		request.getSession().setAttribute("uID", getID(eMail));
		request.getSession().setMaxInactiveInterval(-1);

		String wtv = getID(eMail).toString();
		System.out.println(wtv);

		String storedPass = getStoredPassword(eMail, pWord); // Retrieves
																// password
																// stored in DB

		boolean matched = validatePassword(pWord, storedPass); // Compares the
																// two passwords

		if (matched == true) // If true, sent to homepage. If false, page
								// reloads
		{

			ModelAndView mView = new ModelAndView("search", "message", "Welcome Back!");
			return mView;

		} else
			return new ModelAndView(new RedirectView("login.html"));

	}

	public String getStoredPassword(String userEmail, String iPass) {

		if (factory == null)
			setupFactory();

		// Get current session
		Session hibernateSession = factory.openSession();

		// Begin transaction
		hibernateSession.getTransaction().begin();

		// retrieves stored passwrd
		List<String> query = hibernateSession
				.createQuery("select password from com.Shanklish.Controller.User where email = '" + userEmail + "'")
				.list();

		String pass = query.get(0);

		// Commit transaction
		hibernateSession.getTransaction().commit();

		hibernateSession.close();

		return pass;
	}

	private static boolean validatePassword(String originalPassword, String storedPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String[] parts = storedPassword.split(":"); // Splits the hashed
													// password up into 3 parts
		int iterations = Integer.parseInt(parts[0]); // Iteration is parsed into
														// an Integer
		byte[] salt = fromHex(parts[1]); // Last two remaining parts are decoded
											// from Hexadecimal notation
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8); // Hashes
																												// the
																												// user
																												// inputted
																												// password
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length; // Compares, returns boolean
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException // Decodes
																				// from
																				// hex
	{
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	// --------------------------------------SORTING----------------------------------
	class LexicographicComparator implements Comparator<job> {
		@Override
		public int compare(job a, job b) {
			return a.getLocation().compareToIgnoreCase(b.getLocation());
		}
	}

}
