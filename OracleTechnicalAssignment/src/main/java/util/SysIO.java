package util;

import java.util.Scanner;

public final class SysIO implements ConsoleIO {

	private final Scanner scanner = new Scanner(System.in);

	@Override
	public String getUserInput() {
		return scanner.nextLine();
	}

	@Override
	public void printLine(String line) {
		System.out.println(line);
	}

	@Override
	public void close() { scanner.close(); }
}



