package util;

import java.util.ArrayList;
import java.util.List;

public final class FakeIO implements ConsoleIO {

	private int lineIndex = 0;
	private final String[] lines;

	public final List<String> output = new ArrayList<>();

	public FakeIO(String... lines) {
		this.lines = lines;
	}

	@Override
	public String getUserInput() {
		return lines[lineIndex++];
	}

	@Override
	public void printLine(String line) {
		output.add(line);
	}

	@Override
	public void close() {

	}

}


