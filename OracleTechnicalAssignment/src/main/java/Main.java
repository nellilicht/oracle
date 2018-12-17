import util.SysIO;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {

	public static void main(String args[]) {
		SysIO io = new SysIO();
		ParseCandidates fp = new ParseCandidates();
		Map<String, String> candidates;

		try {
			candidates = fp.parseCandidates("candidates.txt");
			InputHandler inputhandler = new InputHandler(candidates, io);
			inputhandler.readInput();
		} catch (FileNotFoundException e) {
			io.printLine("Input file was not found!");
			System.exit(1);
		} catch (IllegalArgumentException e) {
			io.printLine(e.getMessage());
			System.exit(2);
		}


	}



}
