package org.o7planning.tutorial.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import jvntagger.MaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class Lucene {
	String txt_doc = "";
	String[] txt_doc_check;
	String queryString = ""; // ExtractQuery
	VietTokenizer tokenizer = new VietTokenizer();
	MaxentTagger tagger = new MaxentTagger("model\\maxent");
	int QuestionType = 0;
	String black_list = "";
	List<String> result_Final = new ArrayList<String>();

	public void SearchSentences(String Question, ArrayList<String> Data) throws IOException, ParseException {
		// 0. Specify the analyzer for tokenizing text.
		// The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer();

		// 1. create the index
		Directory indexS = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(indexS, config);
		for (int i = 0; i < Data.size(); i++) {
			// ---System.out.println("Sentences: " + Data.get(i));
			String title = "";
			String arr[] = Data.get(i).split("\\.");
			title = arr[0];
			// System.out.println("Title: " + title);
			addDoc(w, Integer.toString(i + 1), title, Data.get(i));

		}
		w.close();

		// 2. query
		// extract
		queryString = ExtractQuery(Question);
		System.out.println("Extract reusult: " + queryString);

		String querystr = PatternRepalace(queryString);
		// System.out.println("Repalace result:" + querystr);

		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		Query q = new QueryParser("contents", analyzer).parse(QueryParseTitle(querystr, "title") + " OR " + querystr);

		System.out.println("Query: " + q);

		// 3. search
		int hitsPerPage = 30;
		IndexReader reader = DirectoryReader.open(indexS);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results

		System.out.println("Found " + hits.length + " hits.");
		String SentenceMerge = "";
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			SentenceMerge += d.get("contents") + ".";
			// System.out.println((i + 1) + ". " + d.get("id") + "\t" +
			// d.get("contents"));
			result_Final.add(d.get("contents"));
		}
		// System.out.println("heheheehehe " + SentenceMerge);

		// String[] result = PatternMacth(SentenceMerge, QuestionType);
		// textArea.append("Result----> \n");
		/*
		 * int i = 0; for (String value : result) { i++; System.out.println(i +
		 * ". " + value + '\n'); //textArea.append(i + ". " + value + '\n'); }
		 * 
		 * // reader can only be closed when there // is no need to access the
		 * documents any more. reader.close();
		 */
	}

	private static void addDoc(IndexWriter w, String id, String title, String contents) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("contents", contents, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("id", id, Field.Store.YES));
		w.addDocument(doc);
	}

	public String ExtractQuery(String input) throws IOException {
		QuestionType = ClassifyQuestion(input);
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

	public String QueryParseTitle(String text, String fieldName) {

		String result = " ";
		result += text.replace("\"", "").replaceAll("\\s$", "");
		result = result.replaceAll("\\s", " OR " + fieldName + ":").replaceAll("^ OR ", "");

		return result;

	}

	public String PatternRepalace(String text) {
		String segmented = tokenizer.segment("hehe " + text);
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
		black_list = noun_list;
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

	public static int ClassifyQuestion(String question) {
		// Cau hoi ve nguoi
		if (question.matches("(Ai|ai|Người nào|người nào|Cầu thủ|cầu thủ)(.*)") || question.matches("(.*) ai (.*)")) {
			System.out.println("Question--> people");
			return 1;
		}

		// Cau hoi ve doi bong

		if (question.matches("(.*)(Đội bóng|Đội bóng nào|đội bóng|đội bóng nào|CLB nào|Câu lạc bộ nào|CLB)(.*)")
				|| question.matches("(.*)(đội bóng|đội bóng nào|CLB nào|Câu lạc bộ nào)(.*)")) {
			if (question.matches("(.*)biệt danh là gì(.*)") || question.matches("Biệt danh của(.*)")) {
				System.out.println("Question--> nickname");
				return 14;
			} else {
				System.out.println("Question--> Team");
				return 2;
			}
		}
		// Cau hoi ve thoi gian
		if (question.matches("(.*)(Khi nào|khi nào|lúc nào|thời gian nào|năm nào)(.*)")) {
			if (question.matches("(.*)sinh(.*)")) {
				System.out.println("Question-->Time(Born)");
				return 16;
			}
			if (question.matches("(.*)(khai mạc|bắt đầu|diễn ra)(.*)")) {
				System.out.println("Question-->Time(Start)");
				return 3;
			} else if (question.matches("(.*)(bế mạc|kết thúc)(.*)")) {
				System.out.println("Question-->Time(End)");
				return 4;
			}

		}
		// Cau hoi ve ti so
		if (question.matches("(Tỉ số|tỉ số|Kết quả|kết quả )(.*)")) {
			System.out.println("Question-->Result(Match)");
			return 5;
		}
		// Cau hoi ve dia diem
		if (question.matches("(.*)(ở đâu|chổ nào|thành phố nào|nước nào|sân vận động nào)(.*)")) {
			if (question.matches("(.*)(sinh ra|Sinh ra|lớn lên)(.*)")) {
				System.out.println("Question-->where_people");
				return 17;

			}
			if (question.matches("(.*)(tổ chức|diễn ra|xảy ra)(.*)")) {
				System.out.println("Question-->where");
				return 6;
			}
		}

		// Cau hoi ve so luong
		if (question.matches("(.*)(bao nhiêu)(.*)")) {

			if (question.matches("(Cân nặng|cân nặng)(.*)") || question.matches("(.*)( cân nặng )(.*)")) {
				System.out.println("Question--> info");
				return 10;
			} else if (question.matches("( Chiều cao|chiều cao )(.*)") || question.matches("(.*)( chiều cao )(.*)")) {
				System.out.println("Question--> info");
				return 11;
			}
			else if (question.matches("(.*)(mang áo|mặc áo|mang số áo|số áo)(.*)") || question.matches("(Số áo|số áo)(.*)")) {
				System.out.println("Question--> info");
				return 13;
			}
			else if (question.matches("(.*)(bàn thắng)(.*)") ) {
				System.out.println("Question--> info");
				return 18;
			}
			if (question.matches("(Tỉ số|tỉ số|Kết quả|kết quả )(.*)")) {
				System.out.println("Question-->Result(Match)");
				return 5;
			}
			else {
				System.out.println("Question-->Count");
				return 7;
			}

		}

		// thong tin cau thu
		if (question.matches("(Chân thuận)(.*)") || question.matches("(.*)(thuận chân)(.*)")) {
			System.out.println("Question--> info");
			return 8;
		}
		if (question.matches("(.*)quốc tịch(.*)")) {
			System.out.println("Question--> info");
			return 9;
		}
		if (question.matches("(Cân nặng|cân nặng)(.*)") || question.matches("(.*)(cân nặng)(.*)")) {
			System.out.println("Question--> info");
			return 10;
		}
		if (question.matches("(Chiều cao|chiều cao)(.*)") || question.matches("(.*)(chiều cao)(.*)")) {
			System.out.println("Question--> info");
			return 11;
		}
		if (question.matches("(.*)vị trí(.*)")) {
			System.out.println("Question--> info");
			return 12;
		}
		if (question.matches("(.*)(mang áo|mặc áo|mang số áo)(.*)") || question.matches("Số áo(.*)")) {
			System.out.println("Question--> info");
			return 13;
		}
		if (question.matches("(.*)(tổng cộng bàn thắng)(.*)")) {
			System.out.println("Question--> info");
			return 18;
		}

		// thong tin doi bong
		if (question.matches("(.*)biệt danh là gì(.*)") || question.matches("Biệt danh của(.*)")) {
			System.out.println("Question--> nickname");
			return 14;
		}

		if (question.matches("Sân vận động của(.*)(là gì|có tên là gì|tên gọi là gì)(.*)")) {
			System.out.println("Question--> SVD");
			return 15;
		}

		return 0;

	}

}
