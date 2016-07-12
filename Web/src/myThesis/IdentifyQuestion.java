package myThesis;

public class IdentifyQuestion {
	public static int ClassifyQuestion(String question) {
		// Cau hoi ve nguoi
		if (question.matches("(Ai|ai|Người nào|người nào|Cầu thủ|cầu thủ|HLV|Huấn luyện viên|uấn luyện viên)(.*)") || question.matches("(.*) ai (.*)")) {
			System.out.println("Question--> people");
			return QuestionResult.PEOPLE;
		}

		// Cau hoi ve doi bong

		if (question.matches("(.*)(Đội bóng|Đội bóng nào|đội bóng|đội bóng nào|CLB nào|Câu lạc bộ nào|CLB)(.*)")
				|| question.matches("(.*)(đội bóng|đội bóng nào|CLB nào|Câu lạc bộ nào)(.*)")) {
			if (question.matches("(.*)biệt danh là gì(.*)") || question.matches("Biệt danh của(.*)")) {
				System.out.println("Question--> nickname");
				return QuestionResult.NICKNAME;
			} else {
				System.out.println("Question--> Team");
				return QuestionResult.TEAM;
			}
		}
		// Cau hoi ve thoi gian
		if (question.matches("(.*)(Khi nào|khi nào|lúc nào|thời gian nào|năm nào)(.*)")) {
			if (question.matches("(.*)sinh(.*)")) {
				System.out.println("Question-->Time(Born)");
				return QuestionResult.TIME_BORN;
			}
			if (question.matches("(.*)(khai mạc|bắt đầu|diễn ra)(.*)")) {
				System.out.println("Question-->Time(Start)");
				return QuestionResult.TIME_START;
			} else if (question.matches("(.*)(bế mạc|kết thúc)(.*)")) {
				System.out.println("Question-->Time(End)");
				return QuestionResult.TIME_END;
			}

		}
		// Cau hoi ve ti so
		if (question.matches("(Tỉ số|tỉ số|Kết quả|kết quả )(.*)")) {
			System.out.println("Question-->Result(Match)");
			return QuestionResult.MATCH_RESULT;
		}
		// Cau hoi ve dia diem
		if (question.matches("(.*)(ở đâu|chổ nào|thành phố nào|nước nào|sân vận động nào)(.*)")) {
			if (question.matches("(.*)(sinh ra|Sinh ra|lớn lên)(.*)")) {
				System.out.println("Question-->where_people");
				return QuestionResult.WHERE_BORN;

			}
			else if (question.matches("(.*)(tổ chức|diễn ra|xảy ra)(.*)")) {
				System.out.println("Question-->where");
				return QuestionResult.WHERE;
			}
		}

		// Cau hoi ve so luong
		if (question.matches("(.*)(bao nhiêu)(.*)")) {

			if (question.matches("(Cân nặng|cân nặng)(.*)") || question.matches("(.*)( cân nặng )(.*)")) {
				System.out.println("Question--> info");
				return QuestionResult.WEIGHT;
			} 
			else if (question.matches("( Chiều cao|chiều cao )(.*)") || question.matches("(.*)( chiều cao )(.*)")) {
				System.out.println("Question--> info");
				return QuestionResult.HEIGHT;
			}
			//so ao
			else if (question.matches("(.*)(mang áo|mặc áo|mang số áo|số áo)(.*)") || question.matches("(Số áo|số áo)(.*)")) {
				System.out.println("Question--> info");
				return QuestionResult.NUMBER;
			}
			//tong so ban thang
			else if (question.matches("(.*)(bàn thắng)(.*)") ) {
				System.out.println("Question--> info");
				return QuestionResult.SUM_GOAL;
			}
			//tong so tran thi dau
			else if (question.matches("(.*)(thi đấu)(.*)") ) {
				System.out.println("Question--> info");
				return QuestionResult.SUM_MATCH;
			}
			
			else if (question.matches("(Tỉ số|tỉ số|Kết quả|kết quả )(.*)")) {
				System.out.println("Question-->Result(Match)");
				return QuestionResult.MATCH_RESULT;
			}
			else {
				System.out.println("Question-->Count");
				return QuestionResult.COUNT;
			}

		}

		
		// thong tin cau thu
		if (question.matches("(Chân thuận)(.*)") || question.matches("(.*)(thuận chân)(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.FOOT;
		}
		if (question.matches("(.*)quốc tịch(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.NATION;
		}
		if (question.matches("(Cân nặng|cân nặng)(.*)") || question.matches("(.*)(cân nặng)(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.WEIGHT;
		}
		if (question.matches("(Chiều cao|chiều cao)(.*)") || question.matches("(.*)(chiều cao)(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.HEIGHT;
		}
		if (question.matches("(.*)vị trí(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.POSITION;
		}
		if (question.matches("(.*)(mang áo|mặc áo|mang số áo)(.*)") || question.matches("Số áo(.*)")) {
			System.out.println("Question--> info");
			return QuestionResult.NUMBER;
		}

		// thong tin doi bong
		if (question.matches("(.*)biệt danh là gì(.*)") || question.matches("Biệt danh của(.*)")) {
			System.out.println("Question--> nickname");
			return QuestionResult.NICKNAME;
		}

		if (question.matches("Sân vận động của(.*)(là gì|có tên là gì|tên gọi là gì)(.*)")) {
			System.out.println("Question--> SVD");
			return QuestionResult.STADIUM;
		}
		
		if (question.matches("(.*)(đội hình|sơ đồ|Đội hình|Sơ đồ)(.*)")) {
			System.out.println("Question--> so do");
			return QuestionResult.SQUAD;
		}

		return 0;

	}
}
