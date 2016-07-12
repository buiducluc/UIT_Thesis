package myThesis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

@WebServlet
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		Bing bing = new Bing();
		List<String> dataLucene = new ArrayList<String>();
		List<String> answer = new ArrayList<String>();
		SearchDocument searDoc = new SearchDocument();
		String ques = request.getParameter("userName").trim();
		if(ques.matches("(.*)(World Cup sắp tới|World Cup lần tới|World Cup tiếp theo|World Cup tiếp theo)(.*)"))
		{
			String year = Utils.WC(Utils.getYear())+"";
			ques = ques.replace("sắp tới", year);
			ques =ques.replace("lần tới", year);
			ques =ques.replace("tiếp theo", year);
			ques =ques.replace("tiếp theo", year);
			System.out.println(ques);
		}
		if(ques.matches("(.*)(Euro sắp tới|Euro lần tới|Euro tiếp theo|Euro tiếp theo)(.*)"))
		{
			String year = Utils.Euro(Utils.getYear())+"";
			ques = ques.replace("sắp tới", year);
			ques =ques.replace("lần tới", year);
			ques =ques.replace("tiếp theo", year);
			ques =ques.replace("tiếp theo", year);
			System.out.println(ques);
		}
//		try {
//			bing.SearchDocument(ques);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// String userName = request.getParameter("userName").trim();
//		System.out.println("number: " + bing.demo.ClassifyQuestion(ques));
//		try {
//			bing.SearchDocument(ques);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		try {
//			bing.demo.SearchSentences(ques, bing.Data);
			dataLucene = Lucene.SearchSentences(ques, searDoc.SearchDocument2(ques));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < dataLucene.size(); i++) {
			System.out.println(i + "\t" + dataLucene.get(i));
			answer.addAll(ReturnAnswer.PatternMatch(dataLucene.get(i), ques));
		}
		
		String result = "Đáp án: \n";
		//Cau hoi ve tong so ban thang
		if(IdentifyQuestion.ClassifyQuestion(ques) == 18) {
			List<String> list = new ArrayList<String>();
			list = bing.GetSumGoal(answer);
			for (int i = 0; i <list.size(); i++)
				
				result = result + bing.result_Final.get(i) + "\n";
		}	
		
		else if (IdentifyQuestion.ClassifyQuestion(ques) == QuestionResult.SUM_MATCH) {
			List<String> list = new ArrayList<String>();
			list = bing.GetSumMatch(answer);
			for (int i = 0; i < list.size(); i++)
				
				result = result + bing.result_Final.get(i) + "\n";
		}
		
		
		else {
			for (int i = 0; i < answer.size(); i++)
				result = result + answer.get(i) + "\n";
		}

		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(result);
	}

}