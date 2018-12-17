import domain.Ballot;
import util.ConsoleIO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserInputHandler {

	//user input = ballot and results, list or map
	private ConsoleIO io;
	private Set<String> validCandidatesLabels;
	private Map<String, String> labelsAndCandidatesMap;
	private boolean run = true;

	private Map<Integer, Ballot> ballotMap = new HashMap<>();
	private VoteHandler voteHandler;

	private String userInput;

	public UserInputHandler(Map<String, String> candidatesList, ConsoleIO io ){
		this.validCandidatesLabels = candidatesList.keySet();
		this.labelsAndCandidatesMap = candidatesList;
		this.io = io;
		voteHandler = new VoteHandler(io);
	}

	protected void readInput() throws IllegalArgumentException{
		while (run) {
			displayCandidatesList(getLabelsAndCandidatesMap());

			io.printLine("Type in the labels in the order of your preference!");

			userInput = io.getUserInput(); // Read first time

			char[] votes = parseInputToUniqueLabels(userInput);

			if(userInput.toUpperCase().equals("TALLY")){
				if(ballotMap.keySet().size() <2){
					io.printLine("Not enough votes to calculate!");
					continue;
				}
				getVoteHandler().countVotes(ballotMap);
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

				run = enterAnotherVote(io);

			}
		}

		io.close();

	}

	protected boolean enterAnotherVote(ConsoleIO io) {
		boolean addAnotherVote = true;

		while (addAnotherVote) {
			io.printLine("Enter another vote? [Y/TALLY]");
			String addVote = io.getUserInput();


			if (addVote.toUpperCase().equals("TALLY")) {
				if (getBallotMap().keySet().size() > 2) {
					getVoteHandler().countVotes(ballotMap);
				} else {
					io.printLine("Too few votes to calculate!");
				}
				return false;
			} else if (addVote.toUpperCase().equals("Y")) {
				return true;
			} else {
				io.printLine("Not recognized command!");
			}
		}
		return false;
	}


	private void displayCandidatesList(Map<String, String> candidatesMap) {
		for (String candidateKey: candidatesMap.keySet()) {
			io.printLine(candidateKey +". "+ candidatesMap.get(candidateKey));
		}
	}

	protected boolean validateLabels(char[] votes){
		boolean validation = true;
		for (char label : votes) {
			if (!validCandidatesLabels.contains(String.valueOf(label).toUpperCase())) {
				validation = false;
				break;
			}
		}

		return validation;
	}

	protected char[] parseInputToUniqueLabels(String votes){
		votes = votes.replaceAll("\\s","").toUpperCase();
		return getUniqueLabelsCharArray(votes.toCharArray());
	}

	protected void addVotesToBallot(char[] votes){
		int numberOfBallots = getBallotMap().keySet().size();
		getBallotMap().put(numberOfBallots+1, new Ballot(votes));

	}

	protected char[] getUniqueLabelsCharArray(char[] array) {
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

	public Map<String, String> getLabelsAndCandidatesMap() {
		return labelsAndCandidatesMap;
	}


	public VoteHandler getVoteHandler() {
		return voteHandler;
	}

}
