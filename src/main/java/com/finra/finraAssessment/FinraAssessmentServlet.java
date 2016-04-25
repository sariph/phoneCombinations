package com.finra.finraAssessment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sariph Shrestha
 * @since  04-24-2016
 * @version 1.0
 */
public class FinraAssessmentServlet extends HttpServlet {
	private int paginationSize = 10;
	private String[] lettersFromPhoneNumber = { "0", "1", "2abc", "3def", "4ghi", "5jkl", "6mno", "7pqrs", "8uvw", "9xyz" };
	private int ZERO_INDEX = 0;
	private String EMPTY_STRING = "";

	private final String PAGE_NUMBER = "pageNumber";
	private final String PHONE_NUMBER = "phoneNumber";

	@Override
	public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		httpServletResponse.setContentType("text/html");

		//If user is not found then please log in
		if(user==null){
			httpServletResponse.getWriter().println("Please Login to get the Combinations");
			httpServletResponse.getWriter().println(
					"<a href='" + userService.createLoginURL(httpServletRequest.getRequestURI()) + "'>Login</a>");
		//If user is found then proceed further
		} else if (user != null) {

			//If phone number is not present, it is the first time page is getting entry
			//This is the entry point
			if (httpServletRequest.getParameter(PHONE_NUMBER) == null) {
				httpServletResponse.getWriter()
            .println("<h1>Welcome to Finra Assessment!</h1> Hello, " + httpServletRequest.getUserPrincipal().getName());
        		httpServletResponse.getWriter().println("<p>Hey, " +
                                     user.getNickname() +
                                     "!  You can <a href=\"" +
                                     userService.createLogoutURL(httpServletRequest.getRequestURI()) +
                                     "\">sign out</a>.</p>");

				httpServletResponse.getWriter()
						.println("<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>"
								+ "<script src=\"finraApplication.js\"></script>");
				httpServletResponse.getWriter()
						.println("<div id=\"combination\"/>");
				httpServletResponse.getWriter()
						.println("<form name=\"finraForm\" " + "onsubmit=\"return validatePhoneNumber()\" "
								+ "method=\"get\"> Enter Phone Number: " + "<input type=\"text\" name=\"phoneNumber\">"
								+ "<input type=\"submit\" value=\"Submit\"></form></div>");
			} else {
				String pageNumberString = httpServletRequest.getParameter(PAGE_NUMBER);
				int pageNumber = (pageNumberString != null) ? Integer.parseInt(pageNumberString) : 0;
				String phoneNumber = httpServletRequest.getParameter(PHONE_NUMBER);
				//List<String> allLetterCombinations = letterCombinations(phoneNumber);
				List<String> allLetterCombinations = letterCombinationsUpdated(phoneNumber,0);

				httpServletResponse.getWriter()
						.println("<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>"
								+ "<script src=\"finraApplication.js\"></script>");
				httpServletResponse.getWriter()
						.println("<div><span>Total Number of Combinations are: " + allLetterCombinations.size()+"</span>");
				
				StringBuilder stringHtmlBuilder = new StringBuilder();
				//Begin Table Body
				stringHtmlBuilder.append("<table id=\"data\"><tr><td>Phone Numbers Combinations:</td></tr><tbody>");
				for (int index = pageNumber; index < allLetterCombinations.size() && index < pageNumber + paginationSize; index++) {
					stringHtmlBuilder.append("<tr><td>" + allLetterCombinations.get(index) + "</td></tr>");
				}

				//End Table Body
				stringHtmlBuilder.append("</tbody></table></div>");
				httpServletResponse.getWriter().println(stringHtmlBuilder);

				httpServletResponse.getWriter().println("<input type=\"hidden\" value=\""+phoneNumber+"\" id=\"phoneNumber\">");
				httpServletResponse.getWriter().println("<input type=\"hidden\" value=\""+pageNumber+"\" id=\"pageNumber\">");
				httpServletResponse.getWriter().println("<input type=\"hidden\" value=\""+paginationSize+"\" id=\"paginationSize\">");
				httpServletResponse.getWriter().println("<input type=\"hidden\" value=\""+allLetterCombinations.size()+"\" id=\"totalSize\">");

				httpServletResponse.getWriter().println("<input id=\"previousPageButton\" type=\"button\" value=\"Previous Page\" />");
				httpServletResponse.getWriter().println("<input id=\"nextPageButton\" type=\"button\" value=\"Next Page\" />");
			}
		} 
	}

	public List<String> letterCombinationsUpdated(String phoneNumber, int step) {
		if (phoneNumber.length() == step)
			return new ArrayList<String>();
		int phoneDigit = Integer.parseInt(phoneNumber.charAt(step) + "");
		List<String> allCombinations = new ArrayList<String>();
		for (int j = 0; j < lettersFromPhoneNumber[phoneDigit].length(); j++) {
			String phoneNumberString = phoneNumber.substring(0, step) + lettersFromPhoneNumber[phoneDigit].charAt(j) + phoneNumber.substring(step + 1);
			List<String> allCombinationsSubList = letterCombinationsUpdated(phoneNumberString, step + 1);
			if (!allCombinationsSubList.contains(phoneNumberString))
				allCombinationsSubList.add(phoneNumberString);
			allCombinations.addAll(allCombinationsSubList);
		}
		return allCombinations;
	}

	public List<String> letterCombinations(String phoneNumber) {
		if (phoneNumber.length() == 1) {
			List<String> allCombinations = new ArrayList<String>();
			int phoneNumberDigit = Integer.parseInt(String.valueOf(phoneNumber.charAt(ZERO_INDEX)));
			allCombinations = new ArrayList<String>(Arrays.asList(lettersFromPhoneNumber[phoneNumberDigit].split(EMPTY_STRING)));
			return allCombinations;
		} else {
			char firstCharacter = phoneNumber.charAt(ZERO_INDEX);
			List<String> letterCombinations = letterCombinations(phoneNumber.substring((1)));
			List<String> allLetterCombinations = new ArrayList<String>();
			int phoneNumberDigit = Integer.parseInt(String.valueOf(firstCharacter));
			List<String> lettersForPhoneDigitList = new ArrayList<String>(
					Arrays.asList(lettersFromPhoneNumber[phoneNumberDigit].split(EMPTY_STRING)));
			for (String lettersForPhoneDigit : lettersForPhoneDigitList) {
				for (String combination : letterCombinations) {
					allLetterCombinations.add(lettersForPhoneDigit + combination);
				}
			}
			return allLetterCombinations;
		}
	}
}
