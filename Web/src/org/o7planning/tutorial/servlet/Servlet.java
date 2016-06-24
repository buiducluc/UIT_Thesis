package org.o7planning.tutorial.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Bing bing = new Bing();
		String ques = request.getParameter("userName").trim();
		try {
			bing.SearchDocument(ques);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String userName = request.getParameter("userName").trim();
		System.out.println("number: " + bing.demo.ClassifyQuestion(ques));
		try {
			bing.SearchDocument(ques);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bing.demo.SearchSentences(ques, bing.Data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n------Lucene-----\n");
		for (int i = 0; i < bing.demo.result_Final.size(); i++) {
			System.out.println(i + "\t" + bing.demo.result_Final.get(i));
			bing.PatternMatch(bing.demo.result_Final.get(i), ques);
		}
		
		String result = null;
		//Cau hoi ve tong so ban thang
		if (bing.demo.ClassifyQuestion(ques) == 18) {
			bing.GetSumGoal(bing.result);
			for (int i = 0; i < bing.result_Final.size(); i++)

				result = result + bing.result_Final.get(i) + "\n";
		}
			
		else {
			for (int i = 0; i < bing.result.size(); i++)
				result = result + bing.result.get(i) + "\n";
		}

		response.setContentType("text/plain");
		response.getWriter().write(result);
	}

}