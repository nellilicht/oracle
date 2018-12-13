import util.ConsoleIO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InputHandler {

	//user input = ballot and results, list or map
	private ConsoleIO io;
	private Set<String> validCandidatesLabels;
	private Map<String, String> candidatesMap;
	private boolean run = true;

	private Map<Integer, Ballot> ballotMap = new HashMap<>();
	private VoteHandler voteHandler = new VoteHandler();

	private String userInput;

	public InputHandler(Map<String, String> candidatesList, ConsoleIO io ){
		this.validCandidatesLabels = candidatesList.keySet();
		this.candidatesMap = candidatesList;
		this.io = io;
	}

	protected void readInput(){
		while (run) {
			displayCandidatesList(getCandidatesMap());

			io.printLine("Type in the labels in the order of your preference!");

			userInput = io.getUserInput(); // Read first time

			char[] votes = parseInputToUniqueLabels(userInput);

			if(userInput.equals("tally")){
				if(votes.length <2){
					io.printLine("Not enough votes to calculate!");
					continue;
				}
				voteHandler.countVotes(ballotMap);
				run = false;
			} else {


				if (!userInput.trim().matches("[a-zA-Z]+(\\s.*)?")) {
					io.printLine("Incorrect input!");

				} else if (!validateLabels(votes)) {
					io.printLine("Incorrect input! Atleast one of the inserted letters was not in the list");

				} else {
					io.printLine("Thank you for your vote!");
					addVotesToBallot(votes);

				}

				io.printLine("Enter another vote? [Y/N]");
				String addVote = io.getUserInput();

				if (addVote.toUpperCase().equals("N")||addVote.toUpperCase().equals("TALLY")) {
					voteHandler.countVotes(ballotMap);
					run = false;
				} else if(addVote.toUpperCase().equals("Y")) {
					continue;
				} else {
					io.printLine("Not recognized command. Exiting program!");
					System.exit(1);
				}
			}
		}

		io.close();

	}


	private void displayCandidatesList(Map<String, String> candidatesMap) {
		for (String candidateKey: candidatesMap.keySet()) {
			io.printLine(candidateKey +". "+ candidatesMap.get(candidateKey));
		}
	}

	protected boolean validateLabels(char[] votes){
		boolean validation = true;
		for (char c : votes) {
			if (!validCandidatesLabels.contains(String.valueOf(c))) {
				validation = false;
				break;
			}
		}

		return validation;
	}

	//char array keeps the order of elements
	protected char[] parseInputToUniqueLabels(String votes){
		votes = votes.replaceAll("\\s","").toUpperCase();
		return getUniqueLabelsCharArray(votes.toCharArray());
	}

	protected void addVotesToBallot(char[] votes){
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

	public Map<Integer, Ballot> getBallotMap() {
		return ballotMap;
	}

	public Map<String, String> getCandidatesMap() {
		return candidatesMap;
	}
}
