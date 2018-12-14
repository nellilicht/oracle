import util.SysIO;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {

	public static void main(String args[]) {
		ParseCandidates fp = new ParseCandidates();
		Map<String, String> candidates = null;
		try {
			candidates = fp.parseCandidates("candidates.txt");
		} catch (FileNotFoundException e) {
			System.out.println("Input file was not found!");
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(2);
		}
		InputHandler inputhandler = new InputHandler(candidates, new SysIO());
		inputhandler.readInput();

	}



}
