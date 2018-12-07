import java.util.Map;

public class Main {


	//read file, input, parse
	//display options, read input, save results

	//max candidates 26 / alphabeth letters


	public static void main(String args[]) {
		ParseCandidates fp = new ParseCandidates();
		Map<String, String> candidates = fp.parseCandidates("/Users/nelli/projects/OracleTechnicalAssignment/src/main/resources/candidates.txt");
		InputHandler inputhandler = new InputHandler(candidates);

	}



}
