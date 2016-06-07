
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import jvntagger.MaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

import java.util.ArrayList;

public class Bing {
	String link;
	String query;
	String blackList;
	ArrayList<String> Snippet;
	ArrayList<String> Data = new ArrayList<String>();;
	Lucene demo = new Lucene();
	String regex_final = "";

	List<String> result = new ArrayList<String>();
	
	VietTokenizer tokenizer = new VietTokenizer();
	MaxentTagger tagger = new MaxentTagger("model\\maxent");

	// use bing to search
	public String parseQuestion(String str) {
		String result = null;
		result = str.replace(' ', '+');
		System.out.println(result);
		return result;
	}

	public void PatternMatch(String sentence, String question) {
		String tempResult = "";

		String regex_when_born = "\\(\\d+|(\\(\\D+\\d+\\s*(-|–))|\\d+-\\d+-\\d+:|sinh ngày \\d+ tháng \\d+ năm \\d+|(sinh năm) \\d+";
		String regex_born = "(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+)";

		String regex_when_die = "\\d+\\)|((-|–)\\s*\\D+\\d+\\))|mất ngày \\d+ tháng \\d+ năm \\d+|(mất năm) \\d+";
		String regex = "([\\p{Lu}][\\p{Ll}]+\\s?){1,}.*(?=Quả bóng vàng)";
		String regex_where = "(tại|ở)?(\\s(sân| sân vận động|thành phố|nước|quốc gia))(\\s[\\p{Lu}][\\p{Ll}+]+){1,}";
		String regex1 = "(?<=tổ chức)(\\s(tại|ở)?(\\s[\\p{Lu}][\\p{Ll}]+))+";
		String regex_tall = "(Chiều cao|chiều cao)(\\s\\d+(\\,|\\.)\\d+)|(Chiều cao: |chiều cao: )(\\s\\d+m\\d+)";
		String regex_height = "(Cân nặng|cân nặng)(\\s\\d+)";
		String regex_number = "(số áo|số|Số áo)(\\s\\d+)";
		String regex_people = "(\\s?[\\p{Lu}][\\p{Ll}]+){1,}(.*)(?=(thủ môn xuất sắc nhất 2014|Vua bóng đá|biệt danh))";
		
		

		/*switch (demo.ClassifyQuestion(question)) {
		case 1:
			regex_final = "";
			break;
		case 2:
			regex_final = "";
			break;
		case 3:
			regex_final = "";
			break;
		case 4:
			regex_final = "";
			break;
		case 5:
			regex_final = "";
			break;
		case 6:
			regex_final = "";
			break;
		case 7: 
			regex_final = "";
			break;
		case 8:
			regex_final = "";
			break;
		case 9:
			regex_final = "";
			break;
		case 10:
			regex_final = regex_height;
			break;
		case 11:
			regex_final = regex_tall;
			break;
		case 12:
			regex_final = "";
			break;
			
		case 13:
			regex_final = regex_number;
			break;
		case 14:
			regex_final = "";
			break;
		case 15:
			regex_final = "";
			break;
		case 16:
			regex_final = regex_born;
			break;
		default:
			break;

		}*/

		

		Pattern pattern = Pattern.compile(regex_people);
		Matcher matcher = pattern.matcher(sentence);
		while (matcher.find()) {
			tempResult = matcher.group();
			//-----System.out.println(tempResult);
			tempResult = tempResult.replaceAll("[^\\u00BF-\\u1FFF\\u2C00-\\uD7FF\\w\\s]", "").replaceAll("\\s+$", "");
			result.add(tempResult);
		}
	}
	
	public void SearchWiki(String question) throws IOException{
		System.out.println("----Wiki-----");
		List<String> Name = new ArrayList<String>();
		String url="https://vi.wikipedia.org/wiki/";
		
		
		String segmented = tokenizer.segment(question);
		System.out.println(segmented);
		String tagged = tagger.tagging(segmented);
		System.out.println(tagged);
		String query_extract = "";
		String regex_ = "\\S+/(Np|N)"; // Tag giữ lại
		Pattern pattern = Pattern.compile(regex_);
		Matcher matcher = pattern.matcher(tagged);
		while (matcher.find()) {
			query_extract += matcher.group() + " ";
			System.out.println(matcher.group() + " ");
			Name.add(matcher.group());
			
		}
		String name = "";
		if(Name.get(0).contains("/N")){
			name = Name.get(0).substring(0, Name.get(0).length()-2);
		}
		
			if(Name.get(0).contains("/Np")){
				name = Name.get(0).substring(0, Name.get(0).length()-3);
			}

		
		//String name = Name.get(0).substring(0, Name.get(0).length()-2);
		System.out.println(name);
		url = url + name;
		
		System.out.println("---URL----: " + url);
		
		Document doc = Jsoup.connect(url).get();
		// Document doc1 = Jsoup.parse(link);
		Elements links1 = doc.select("table");

		/*
		 * text1 = doc1.body().text(); System.out.println(text1);
		 */

		for (Element link1 : links1) {
			// System.out.println("\ntext: " + link1.text());
			System.out.println(link1.text());
			Data.add(link1.text());

		}
		Elements link2 = doc.select("p");
		for (Element link1 : link2) {
			// System.out.println("\ntext: " + link1.text());
			System.out.println(link1.text());
			Data.add(link1.text());

		}
		
		
	}
	
	public void SearchDocument(String question) throws IOException, ParseException{
		if(demo.ClassifyQuestion(question)==16||demo.ClassifyQuestion(question)==9||demo.ClassifyQuestion(question)==10||
											demo.ClassifyQuestion(question)==11||demo.ClassifyQuestion(question)==12||demo.ClassifyQuestion(question)==13){
		
			SearchWiki(question);
		}
		else{
			SearchGoogle(question);
			SearchBing(question);
		}
	}

	public void SearchGoogle(String question) throws IOException {
		String google = "http://www.google.com/search?q=";
		String charset = "UTF-8";
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";

		Elements links = Jsoup.connect(google + URLEncoder.encode(question, charset)).userAgent(userAgent).get()
				.select(".g>.r>a");
		

		try {
			for (Element link : links) {
				String title = link.text();
				String url = link.absUrl("href"); // Google returns URLs in
													// format
													// "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
				url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

				if (!url.startsWith("http")) {
					continue; // Ads/news/etc.
				}
				Document doc = Jsoup.connect(url).get();
				// Document doc1 = Jsoup.parse(link);
				Elements links1 = doc.select("p");

				/*
				 * text1 = doc1.body().text(); System.out.println(text1);
				 */

				for (Element link1 : links1) {
					//-----System.out.println(link1.text());
					Data.add(link1.text());

				}
			}
			// text = doc.title();
			// text = doc.text();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// getTextJsoup(aResult.getString("Url") + "\r\n");
	}

	public ArrayList<String> trueData(ArrayList<String> data) {
		ArrayList<String> trueData = new ArrayList<String>();
		for (int i = 0; i < data.size() - 3; i = i + 3) {
			trueData.add(data.get(i) + data.get(i + 1) + data.get(i + 2));
		}
		return trueData;
	}

	public void SearchBing(String question) throws IOException, ParseException {
		final String accountKey = "WQGRwBuPuPF9VXsja/V40Fv9L/2GCOxLF0FZeAX+Ujk";
		final String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query='"
				+ parseQuestion(question) + "'&$format=JSON";

		final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());

		final URL url = new URL(bingUrlPattern);
		final URLConnection connection = url.openConnection();
		connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			final StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			final JSONObject json = new JSONObject(response.toString());
			final JSONObject d = json.getJSONObject("d");
			final JSONArray results = d.getJSONArray("results");
			Snippet = new ArrayList<String>();

			for (int i = 0; i < 20; i++) {

				final JSONObject aResult = results.getJSONObject(i);
				// -----System.out.println(i + "----");
				//Data.add(aResult.getString("Description"));
				try {
					Document doc = Jsoup.connect(aResult.getString("Url")).get();
					// Document doc1 = Jsoup.parse(link);
					Elements links = doc.select("p");

					/*
					 * text1 = doc1.body().text(); System.out.println(text1);
					 */

					for (Element link1 : links) {
						// -----System.out.println("\ntext: " + link1.text());
						Data.add(link1.text());

					}
					// text = doc.title();
					// text = doc.text();
					/*
					 * demo.SearchSentences(question, Data); for(int j=0;
					 * j<demo.result_Final.size(); j++){
					 * PatternMatch(demo.result_Final.get(j)); }
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}

				// getTextJsoup(aResult.getString("Url") + "\r\n");

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}

	}


	/*
	 * public static void main(String[] args) throws Exception { // String str =
	 * "Ronaldo sinh nam bao nhieu"; Scanner in = new Scanner(System.in); String
	 * str = in.nextLine(); in.close();
	 * 
	 * Bing test = new Bing(); test.GoogleData(str); test.searchDocument(str);
	 * for (int i = 0; i < test.trueData(test.Data).size(); i++) {
	 * System.out.println(i + "\t" + test.trueData(test.Data).get(i)); } }
	 */
	public static void main(String[] arg) throws Exception {
		Scanner in = new Scanner(System.in);
		String question = in.nextLine();
		in.close();
		Bing bing = new Bing();
		//bing.SearchGoogle(question);
		//bing.SearchBing(question);
		System.out.println("number: " + bing.demo.ClassifyQuestion(question));
		bing.SearchDocument(question);
		
		/*
		 * for (int i = 0; i < bing.Data.size(); i++) { System.out.println(i +
		 * "\t" + bing.Data.get(i));
		 * 
		 * }
		 */
		// demo.SearchSentences(question, Data);
		bing.demo.SearchSentences(question, bing.Data);
		System.out.println("\n------Lucene-----\n");
		for (int i = 0; i < bing.demo.result_Final.size(); i++) {
			
			System.out.println(i + "\t" + bing.demo.result_Final.get(i));
			bing.PatternMatch(bing.demo.result_Final.get(i), question);
			

		}
		for(int i=0; i<bing.result.size(); i++){
			System.out.println(bing.result.get(i));
		}
	}

}
