package myThesis;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jvntagger.MaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class ParsesQuestion {
	
	static VietTokenizer tokenizer = new VietTokenizer();
	static MaxentTagger tagger = new MaxentTagger("model\\maxent");
	public static String parseQuestion(String question) {
		// TODO Auto-generated method stub
		String result = null;
		result = question.replace(' ', '+');
		System.out.println(result);
		return result;
	}
	
	public static String ExtractQuery(String input) throws IOException {
		int QuestionType = IdentifyQuestion.ClassifyQuestion(input);
		String segmented = tokenizer.segment(input);
		System.out.println(segmented);
		String tagged = tagger.tagging(segmented);
		System.out.println(tagged);
		String query_extract = "";
		String regex_ = "\\S+/(N|Np|V|M|E|A|L|X)"; // Tag giữ lại
		Pattern pattern = Pattern.compile(regex_);
		Matcher matcher = pattern.matcher(tagged);
		while (matcher.find()) {
			query_extract += matcher.group() + " ";
			System.out.println(matcher.group() + " ");
		}

		// ----System.out.println(query_extract);
		query_extract = query_extract.replaceAll("/\\S+", "").replaceAll("_", " ");

		// -----System.out.println(query_extract);

		// textArea.append("Query after extract----->" + query_extract + '\n');
		return query_extract;

	}

	public static String QueryParseTitle(String text, String fieldName) {

		String result = " ";
		result += text.replace("\"", "").replaceAll("\\s$", "");
		result = result.replaceAll("\\s", " OR " + fieldName + ":").replaceAll("^ OR ", "");

		return result;

	}

	public static String PatternRepalace(String text) {
		// Xử lý một vài danh từ riêng đứng đầu câu bị gán nhãn là /N mà không
		// phải là /Np
		String segmented = tokenizer.segment("hehe " + text);
		// Tách lấy danh từ riêng
		String tagged = tagger.tagging(segmented);
		String noun_list = "";
		String regex_replace = "\\S+/Np|/M";
		Pattern pattern = Pattern.compile(regex_replace);
		Matcher matcher = pattern.matcher(tagged);

		while (matcher.find()) {
			noun_list += matcher.group() + "|";
		}
		// -----System.out.println("noun_list:" + noun_list);
		noun_list = noun_list.replace("_", " ").replaceAll("/Np|/M", "").replace("|", "").replaceAll("\\|$", "");
		if (noun_list == "") {
			noun_list = " ";
		}

		if (!(noun_list.toLowerCase().contains(" "))) {
			noun_list = " ";
		}
		// -----System.out.println("Black list ----->" + noun_list);
		String[] noun_list_array = noun_list.split("\\|");
		String result = text;
		// Đóng ngoặc kép cho danh từ riêng và thay thế danh từ riêng ban đầu
		for (String noun : noun_list_array) {
			if (noun != "") {
				result = result.replace(noun, "\"" + noun + "\"");
				// -----System.out.println("-----> " + noun);
			}

		}

		// -----System.out.println("-----> " + result);
		return result;

	}

}
