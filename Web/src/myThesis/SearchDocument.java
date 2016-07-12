package myThesis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jvntagger.MaxentTagger;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.hus.nlp.tokenizer.VietTokenizer;

public class SearchDocument {
	VietTokenizer tokenizer = new VietTokenizer();
	MaxentTagger tagger = new MaxentTagger("model\\maxent");
	
	public List<String> SearchWiki(String question) throws IOException {
		System.out.println("----Wiki-----");
		List<String> Name = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		String url = "https://vi.wikipedia.org/wiki/";

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
		if (Name.get(0).contains("/N")) {
			name = Name.get(0).substring(0, Name.get(0).length() - 2);
		}

		if (Name.get(0).contains("/Np")) {
			name = Name.get(0).substring(0, Name.get(0).length() - 3);
		}

		// String name = Name.get(0).substring(0, Name.get(0).length()-2);
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
			data.add(link1.text());

		}
//		Elements link2 = doc.select("p");
//		for (Element link1 : link2) {
//			// System.out.println("\ntext: " + link1.text());
//			System.out.println(link1.text());
//			data.add(link1.text());
//
//		}
		return data;

	}

	@SuppressWarnings("static-access")
	public List<String> SearchDocument2(String question) throws IOException, ParseException {
		List<String> data = new ArrayList<String>();
		if (IdentifyQuestion.ClassifyQuestion(question) == 17 || IdentifyQuestion.ClassifyQuestion(question) == 8
				|| IdentifyQuestion.ClassifyQuestion(question) == 9 || IdentifyQuestion.ClassifyQuestion(question) == 10
				|| IdentifyQuestion.ClassifyQuestion(question) == 11 || IdentifyQuestion.ClassifyQuestion(question) == 12
				|| IdentifyQuestion.ClassifyQuestion(question) == 13||IdentifyQuestion.ClassifyQuestion(question) == 16|| IdentifyQuestion.ClassifyQuestion(question) == 18) {

			data.addAll(SearchWiki(question));
		} else {
			data.addAll(SearchGoogle(question));
			data.addAll(SearchBing(question));
		}
		//data.addAll(SearchGoogle(question));
		return data;
	}

	public List<String> SearchGoogle(String question) throws IOException {
		String google = "http://www.google.com/search?q=";
		String charset = "UTF-8";
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";
		List<String> data = new ArrayList<String>();

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
				try {
					Document doc = Jsoup.connect(url).get();

					// Document doc1 = Jsoup.parse(link);
					Elements links1 = doc.select("p");

					/*
					 * text1 = doc1.body().text(); System.out.println(text1);
					 */

					for (Element link1 : links1) {
						// -----System.out.println(link1.text());
						data.add(link1.text());

					}
					System.out.println("Done!");
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("KO lay dc du lieu!");
				}

			}
			// text = doc.title();
			// text = doc.text();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

		// getTextJsoup(aResult.getString("Url") + "\r\n");
	}
	
	public List<String> SearchBing(String question) throws IOException, ParseException {
		String ques = ParsesQuestion.parseQuestion(question);
		final String accountKey = "WQGRwBuPuPF9VXsja/V40Fv9L/2GCOxLF0FZeAX+Ujk";
		final String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query='"
				+ ques + "'&$format=JSON";
		List<String> data = new ArrayList<String>();
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
			//Snippet = new ArrayList<String>();
			int limitLink = 20;
			for (int i = 0; i < limitLink; i++) {

				final JSONObject aResult = results.getJSONObject(i);

				// dataFromLink.add(aResult.getString("Description"));
				try {
					Document doc = Jsoup.connect(aResult.getString("Url")).get();
					Elements links = doc.select("p");

					for (Element link1 : links) {
						data.add(link1.text());
					}
				} catch (Exception e) {
					e.printStackTrace();
					limitLink += 1;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
		return data;

	}

	public List<String> SearchDocument1(String question) throws ParseException {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>();
//		if (IdentifyQuestion.ClassifyQuestion(question) == 17 || IdentifyQuestion.ClassifyQuestion(question) == 8
//				|| IdentifyQuestion.ClassifyQuestion(question) == 9 || IdentifyQuestion.ClassifyQuestion(question) == 10
//				|| IdentifyQuestion.ClassifyQuestion(question) == 11 || IdentifyQuestion.ClassifyQuestion(question) == 12
//				|| IdentifyQuestion.ClassifyQuestion(question) == 13||IdentifyQuestion.ClassifyQuestion(question) == 16|| IdentifyQuestion.ClassifyQuestion(question) == 18) {
//
//			try {
//				data.addAll(SearchWiki(question));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				data.addAll(SearchGoogle(question));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				data.addAll(SearchBing(question));
//			} catch (IOException | ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		try {
			data.addAll(SearchGoogle(question));
			data.addAll(SearchBing(question));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
