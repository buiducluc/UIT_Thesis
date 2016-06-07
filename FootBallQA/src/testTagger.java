import jvntagger.MaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class testTagger {
	static VietTokenizer tokenizer = new VietTokenizer();
	static MaxentTagger tagger = new MaxentTagger("model\\maxent");
	public static void main(String[] args){
		String input = "Nói tới Cristiano Ronaldo";
		String segmented = tokenizer.segment(input);
		System.out.println(segmented);
		String tagged = tagger.tagging(segmented);
		System.out.println(tagged);
	}
}
