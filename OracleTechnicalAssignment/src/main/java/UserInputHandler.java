import domain.Ballot;
import util.ConsoleIO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserInputHandler {

	private ConsoleIO io;
	private Set<String> validCandidatesLabels;
	private Map<String, String> labelsAndCandidatesMap;
	private boolean runnable = true;

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
		while (isRunnable()) {
			displayCandidatesList(getLabelsAndCandidatesMap());

			io.printLine("Please insert the labels of candidates in the order of your preference!");

			userInput = io.getUserInput();

			char[] votes = parseInputToUniqueLabels(userInput);

			if(userInput.toUpperCase().equals("TALLY")){
				if(getBallotMap().keySet().size() <2){
					io.printLine("Not enough votes to calculate! Please enter more.");
					continue;
				}
				getVoteHandler().countVotes(getBallotMap());
				setRunnable(false);
			} else {


				if (!userInput.trim().matches("[a-zA-Z]+(\\s.*)?")) {
					io.printLine("Incorrect input!");

				} else if (!validateLabels(votes)) {
					io.printLine("Invalid vote! Atleast one of the inserted letters was not in the list.");

				} else {
					io.printLine("Thank you for your vote!");
					addVotesToBallot(votes);

				}

				runnable = enterAnotherVote(io);

			}
		}

		io.close();

	}

	protected boolean enterAnotherVote(ConsoleIO io) throws IllegalArgumentException{
		while (true) {
			io.printLine("Enter another vote? [Y/TALLY]");
			String addVote = io.getUserInput();


			if (addVote.toUpperCase().equals("TALLY")) {
				if (getBallotMap().keySet().size() > 2) {
					getVoteHandler().countVotes(getBallotMap());
				} else {
					io.printLine("Too few votes to calculate! Exiting program.");
				}
				return false;
			} else if (addVote.toUpperCase().equals("Y")) {
				return true;
			} else {
				io.printLine("Not recognized command!");
			}
		}
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
		for (char c : array) {
			if (_array.indexOf(c) == -1) {
				_array = _array + c;
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

	public boolean isRunnable() {
		return runnable;
	}


	void setRunnable(boolean runnable) {
		this.runnable = runnable;
	}


}
