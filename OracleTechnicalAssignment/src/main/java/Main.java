import util.SysIO;
import java.util.Map;

public class Main {

	public static void main(String args[]) {
		ParseCandidates fp = new ParseCandidates();
		Map<String, String> candidates = fp.parseCandidates("candidates.txt");
		InputHandler inputhandler = new InputHandler(candidates, new SysIO());
		inputhandler.readInput();

	}



}
