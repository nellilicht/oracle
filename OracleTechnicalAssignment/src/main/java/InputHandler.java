import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class InputHandler {

	private boolean run = true;

	//user input = ballot and results, list or map
	private Scanner scanner = new Scanner(System.in);
	private Map<String, String> candidatesMap;
	private Set<String> validCandidatesLabels;

	private Map<Integer, Ballot> ballotMap = new HashMap<>();
	private VoteHandler voteHandler = new VoteHandler();

	private String userInput;

	public InputHandler(Map<String, String> candidatesList){
		this.candidatesMap = candidatesList;
		this.validCandidatesLabels = candidatesList.keySet();
		readInput();

	}

	private void readInput(){
		while (run) {

			for (String candidateKey: candidatesMap.keySet()) {
				System.out.println(candidateKey +". "+ candidatesMap.get(candidateKey));
			}
			System.out.println("Type in the letters in the order of your preference!");
			userInput = scanner.nextLine(); // Read first time

			if(userInput.equals("tally")){
				voteHandler.countVotes(ballotMap);
				run = false;
			} else {

				char[] votes = parseVotes(userInput);

				if (!userInput.trim().matches("[a-zA-Z]+(\\s.*)?")) {
					System.out.println("Incorrect input!");

				} else if (!validateLabels(votes)) {
					System.out.println("Incorrect input! Atleast one of the inserted letters was not in the list");

				} else {
					System.out.println("Thank you for your vote!");
					addVotesToBallot(votes);

				}

				//			System.out.println("Do you wish to enter another vote? [Y/N]");
				//			String addVote = scanner.nextLine();
				//
				//			if (addVote.toUpperCase().equals("N")){
				//				run = false;
				//			} else if (addVote.toUpperCase().equals("Y")){
				//				clearConsole();
				//			}
			}
		}

		scanner.close();

	}

	private boolean validateLabels(char[] votes){
		boolean validation = true;
		for (char c : votes) {
			if (!validCandidatesLabels.contains(String.valueOf(c))) {
				validation = false;
				break;
			}
		}

		return validation;
	}

	private void clearConsole(){
		for (int i = 0; i < 10; ++i) System.out.println();
	}

	//char array keeps the order of elements
	private char[] parseVotes(String votes){
		votes = votes.replaceAll("\\s","").toUpperCase();
		return getUniqueLabelsCharArray(votes.toCharArray());
	}

	private void addVotesToBallot(char[] votes){
		int numberOfBallots = ballotMap.keySet().size();
		ballotMap.put(numberOfBallots+1, new Ballot(votes));

	}

	private char[] getUniqueLabelsCharArray(char[] array) {
		String _array = "";
		for(int i = 0; i < array.length; i++) {
			if(_array.indexOf(array[i]) == -1){
				_array = _array+array[i];
			}
		}
		return _array.toCharArray();
	}


}
