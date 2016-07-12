package myThesis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReturnAnswer {
	@SuppressWarnings("static-access")
	public static List<String> PatternMatch(String sentence, String question) {
		String tempResult = "";
		String regex_final = "";
		List<String> answer = new ArrayList<String>();
		String regex_where_born = "(?<=Nơi sinh|sinh ở|sinh tại|tại)((\\s[\\p{Lu}][\\p{Ll}]+)\\,?)+";
		String regex_time_born = "(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+)";

		// String regex_when_die = "\\d+\\)|((-|–)\\s*\\D+\\d+\\))|mất ngày \\d+
		// tháng \\d+ năm \\d+|(mất năm) \\d+";
		String regex_whereSVD="(thành phố|nước|quốc gia)((\\s[\\p{Lu}][\\p{Ll}]+)\\,?)+";
		String regex_where = "(tại|ở)?((\\s[\\p{Lu}][\\p{Ll}]+)|(\\s(thành phố|nước|quốc gia))(\\s([\\p{Lu}][\\p{Ll}]+)(\\-)([\\p{Lu}][\\p{Ll}]+)|(\\s[\\p{Lu}][\\p{Ll}]+){1,}))";
		// String regex1 = "(?<=tổ chức)(\\s(tại|ở)?(\\s[\\p{Lu}][\\p{Ll}]+))+";
		String regex_tall = "(Chiều cao|chiều cao)(\\s\\d+(\\,|\\.)\\d+)|(Chiều cao: |chiều cao: )(\\s\\d+m\\d+)";
		String regex_height = "(Cân nặng|cân nặng)(\\s\\d+)";
		String regex_number = "(số áo|số|Số áo)(\\s\\d+)";
		String regex_people = "(([\\p{Lu}][\\p{Ll}]+\\s)+(trở thành|là|giành|giành được|đạt|xuất sắc nhận|đã giành|vừa giành|bên|đã chính thức|chính thức trở thành|chính thức đoạt|làm|sẽ là)"
				+ "(\\s(danh hiệu|giải thưởng|giải))?"
				+ "(\\s([Vv]ua phá lưới|[Tt]hủ môn|[Cc]hiếc giày [Vv]àng|[Gg]ăng tay [Vv]àng|QBV|[Cc]ầu thủ|HLV|[Hh]uấn luyện viên|[Qq]uả bóng vàng))"
				+"|((HLV|tiền đạo|tiền vệ|hậu vệ|thủ môn)(\\s[\\p{Lu}][\\p{Ll}]+)+))";
		String regex_time_start = "(?<=bắt đầu|Bắt đầu)(\\s)?(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+)";
		String regex_time_end = "((\\s)?(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+))";
		String regex_vitri="(Vị trí|vị trí)(\\s)([\\p{Lu}][\\p{Ll}]+\\s[\\p{Ll}]+)";
		String regex_SVD="(Sân vận động|sân vận động|SVĐ)((\\s)([\\p{Lu}][\\p{Ll}]+)){1,}";
		String regex_sobanthang="(Tổng cộng sự nghiệp)(\\s\\d+)+";
		String regex_team="((câu lạc bộ|Câu lạc bộ|CLB|khoác áo|tuyển|ĐT)(\\s?[\\p{Lu}][\\p{Ll}]+){1,})|(([\\p{Lu}][\\p{Ll}]+){1,}(\\s(đã|sắp))?(\\s(vô địch|chính thức|lên ngôi|giành danh hiệu))(\\s)(([\\p{Lu}][\\p{Ll}]+\\s){1,})?)";
		String regex_tiso="([Tt][ỉỷ]\\ssố)(\\:\\s|\\s)?(\\d+\\-\\d+)";
		//String regex_bietdanh="(biệt danh|Biệt danh)(\\slà)?(\\s)?(\\"|\\'|\\‘|\“)(\\D+|\\w+)(\\"|\\'|\\’|\\”)";
		String regex_sodo="(?<= sơ đồ|Sơ đồ chiến thuật)(\\s)?(\\d+\\-?)+";
		String regex_match="(\\s)(([\\p{Lu}][\\p{Ll}]+\\s){1,})(và)(\\s)(([\\p{Lu}][\\p{Ll}]+(\\s|\\.|\\,)?){1,})";
				
		switch (IdentifyQuestion.ClassifyQuestion(question)) {
		case QuestionResult.PEOPLE:
			regex_final = regex_people;
			break;
		case QuestionResult.TEAM:
			regex_final = regex_team;
			break;
		case QuestionResult.TIME_START:
			regex_final = regex_time_start;
			break;
		case QuestionResult.TIME_END:
			regex_final = regex_time_end;
			break;
		case QuestionResult.MATCH_RESULT:
			regex_final = regex_tiso;
			break;
		case QuestionResult.WHERE:
			regex_final = regex_where;
			break;
		case QuestionResult.COUNT:
			regex_final = "";
			break;
		case QuestionResult.NATION:
			regex_final = "";
			break;
		case QuestionResult.FOOT:
			regex_final = "";
			break;
		case QuestionResult.WEIGHT:
			regex_final = regex_height;
			break;
		case QuestionResult.HEIGHT:
			regex_final = regex_tall;
			break;
		case QuestionResult.POSITION:
			regex_final = regex_vitri;
			break;

		case QuestionResult.NUMBER:
			regex_final = regex_number;
			break;
		case QuestionResult.NICKNAME:
			regex_final = "";
			break;
		case QuestionResult.STADIUM:
			regex_final = regex_SVD;
			break;
		case QuestionResult.TIME_BORN:
			regex_final = regex_time_born;
			break;
		case QuestionResult.WHERE_BORN:
			regex_final = regex_where_born;
			break;
		case QuestionResult.SUM_GOAL:
			regex_final= regex_sobanthang;
			break;
		case QuestionResult.SUM_MATCH:
			regex_final= regex_sobanthang;
			break;
		case QuestionResult.SQUAD:
			regex_final= regex_sodo;
			break;
		case QuestionResult.WHERE_SVD:
			regex_final=regex_whereSVD;
			break;
		case QuestionResult.MATCH:
			regex_final=regex_match;
			break;
		default:
			break;

		}
		
	
		Pattern pattern = Pattern.compile(regex_final);
		Matcher matcher = pattern.matcher(sentence);
		while (matcher.find()) {
			tempResult = matcher.group();
//			// -----System.out.println(tempResult);
//			tempResult = tempResult.replaceAll("[^\\u00BF-\\u1FFF\\u2C00-\\uD7FF\\w\\s]", "").replaceAll("\\s+$", "");
			answer.add(tempResult);
		}
		return answer;
	}
}
