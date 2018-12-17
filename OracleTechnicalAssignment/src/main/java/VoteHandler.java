import domain.Ballot;
import util.ConsoleIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class VoteHandler {

	private static final int HIGHEST_VOTE = 1;
	//holds all ballotsId -s  and their ballots
	private Map<Integer, Ballot> ballotMap = new HashMap<>();

	int nonExhaustedBallots;
	private ConsoleIO io;

	private String winnerCandidate;

	public VoteHandler(ConsoleIO io){
		this.io = io;
	}

	public void countVotes(Map<Integer, Ballot> ballotMap){
		this.ballotMap = ballotMap;
		setNonExhaustedBallots(ballotMap.keySet().size());
		List topLevelCandidates = new ArrayList<>(findAllTopCandidatesFromBallots());
		Map candidatesMapWithBallots = mapBallotsToCandidates(topLevelCandidates);
		conductVotingRounds(candidatesMapWithBallots);

	}

	protected Map<String, List<Integer>> mapBallotsToCandidates(List<String> topLevelCandidates){
		Map<String, List<Integer>> candidatesMapWithBallots = new HashMap();
		for(String candidate : topLevelCandidates){
			for( Integer entry : getBallotMap().keySet()){
				 if (getBallotMap().get(entry).getVoteByPriority(HIGHEST_VOTE).equals(candidate)){
				 	if(candidatesMapWithBallots.get(candidate) == null){
						List<Integer> entryList = new ArrayList<>();
						entryList.add(entry);
				 		candidatesMapWithBallots.put(candidate, entryList);
					} else {
						candidatesMapWithBallots.get(candidate).add(entry);
					}
				 }

			}
		}

		return candidatesMapWithBallots;
	}

	protected void conductVotingRounds(Map<String, List<Integer>> candidatesWithBallotIDsMap){
		 winnerCandidate = calculateWinner(candidatesWithBallotIDsMap);

		while(winnerCandidate.isEmpty()){
			io.printLine("No winner, re-assigning ballots");
			io.printLine("Non-exhausted ballots :" + getNonExhaustedBallots());

			Map<String, List<Integer>> reAssignedCandidatesMapWithBallots = handleBallotsWithMinimalVotes(candidatesWithBallotIDsMap);
			winnerCandidate = calculateWinner(reAssignedCandidatesMapWithBallots);
		}
	}

	private String calculateWinner(Map<String, List<Integer>> candidatesMapWithBallots) {
		String winnerCandidate = candidatesMapWithBallots.entrySet().stream()
				.filter(entry -> entry.getValue().size() > getNonExhaustedBallots() / 2 + 1).map(x -> x.getKey())
				.collect(Collectors.joining());

		if(candidatesMapWithBallots.keySet().size() == 1){
			winnerCandidate = candidatesMapWithBallots.keySet().stream().findFirst().get();
		}

		if(!winnerCandidate.isEmpty()){
			io.printLine("Winner is candidate: " + winnerCandidate);
		}
		return winnerCandidate;
	}

	private Map<String, List<Integer>> handleBallotsWithMinimalVotes(Map<String, List<Integer>> candidatesWithBallotIDsMap) {
		//find candidate with minimal ballots/votes
		int minimalVotes = getMinimalVotesCount(candidatesWithBallotIDsMap);

		List<String> candidatesWithMinimalVotes = findCandidatesWithMinimalVotes(candidatesWithBallotIDsMap,
				minimalVotes);

		io.printLine("Amount of least votes:" +minimalVotes);
		io.printLine("Candidate(s) with least votes:" +candidatesWithMinimalVotes);

		if(candidatesWithBallotIDsMap.keySet().size() == candidatesWithMinimalVotes.size()){
			Random rand = new Random();
			int randomNumber = rand.nextInt(candidatesWithMinimalVotes.size());
			String candidateForElimination = candidatesWithMinimalVotes.get(randomNumber);

			io.printLine("Elliminating a random candidate and it's ballots: "+candidateForElimination);

			List<Integer> ballotsToBeRemoved = candidatesWithBallotIDsMap.get(candidateForElimination);

			for(Integer ballotId : ballotsToBeRemoved){
				getBallotMap().remove(ballotId);
				io.printLine("Elliminating ballot: " + ballotId);
			}

			candidatesWithBallotIDsMap.remove(candidateForElimination);
			candidatesWithMinimalVotes.remove(randomNumber);
			setNonExhaustedBallots(getBallotMap().keySet().size());
		}
		io.printLine("Non-exhausted ballots :" + getNonExhaustedBallots());
		//get from ballots from candidate and re-assign them to next candidate
		reAssignBallots(candidatesWithBallotIDsMap, candidatesWithMinimalVotes);

		return candidatesWithBallotIDsMap;
	}

	private void reAssignBallots(Map<String, List<Integer>> candidatesMapWithBallotsList,
			List<String> candidatesWithMinimalVotes) {

		io.printLine("******** Re-assigning ballots ********");
		for (String candidate : candidatesWithMinimalVotes) {

			List<Integer> ballotIds = new ArrayList<>(candidatesMapWithBallotsList.get(candidate));

			for (int ballotId : ballotIds) {
				//if last candidate remaining
				if(getBallotMap().keySet().size() == 1){
					continue;
				}
				// need to know where to re-assign the ballot, the next priority (can be non existing candidate)
				Ballot ballot = getBallotMap().get(ballotId);

				if (ballot.isExhausted(candidatesMapWithBallotsList.keySet())) {
					removeBallotFromVoting(candidatesMapWithBallotsList, candidate, ballotId);
				} else {
					assignBallotToNextCandidate(candidatesMapWithBallotsList, candidate, ballotId, ballot);
				}
				if (candidatesMapWithBallotsList.get(candidate).isEmpty()) {
					removeCandidateFromVoting(candidatesMapWithBallotsList, candidate);
				}

			}
		}
	}

	protected void removeCandidateFromVoting(Map<String, List<Integer>> candidateWithBallotIDsMap, String candidate) {
		io.printLine("Removed Candidate: " + candidate + ". It has no active votes!");
		candidateWithBallotIDsMap.keySet().remove(candidate);
	}

	protected void assignBallotToNextCandidate(Map<String, List<Integer>> candidatesMapWithBallotsList,
			String candidate, int ballotId, Ballot ballot) {
		String nextCandidateForBallot = ballot
				.getNextActiveCandidateFromBallot(candidatesMapWithBallotsList.keySet());

		candidatesMapWithBallotsList.get(candidate).removeIf(value -> value.equals(ballotId));
		io.printLine("Re-assigning ballot: "+ballotId+ " to Candidate: " + nextCandidateForBallot);

		candidatesMapWithBallotsList.get(nextCandidateForBallot).add(ballotId);
	}

	protected void removeBallotFromVoting(Map<String, List<Integer>> candidatesMapWithBallotIDsMap, String candidate,
			int ballotId) {
		io.printLine("Ballot exhausted:" + ballotId);
		getBallotMap().keySet().remove(ballotId);
		candidatesMapWithBallotIDsMap.get(candidate).removeIf(value -> value.equals(ballotId));
		setNonExhaustedBallots(getBallotMap().keySet().size());
	}

	protected List<String> findCandidatesWithMinimalVotes(Map<String, List<Integer>> candidatesMapWithBallotsList,
			int minimalVotes) {
		return candidatesMapWithBallotsList.entrySet().stream()
					.filter(entry -> entry.getValue().size() == minimalVotes).map(x -> x.getKey())
					.collect(Collectors.toList());
	}

	protected int getMinimalVotesCount(Map<String, List<Integer>> candidatesMapWithBallotsMap) {
		return candidatesMapWithBallotsMap

					.entrySet().stream().min(comparingInt(e -> e.getValue().size())).get().getValue().size();
	}

	protected List findAllTopCandidatesFromBallots() {
		Set topCandidates = new HashSet<>();
		getBallotMap().values().forEach(ballot -> topCandidates.add(ballot.getCandidateWithPriority(HIGHEST_VOTE)));
		return new ArrayList<>(topCandidates);
	}

	public Map<Integer, Ballot> getBallotMap() {
		return ballotMap;
	}


	public String getWinnerCandidate() {
		return winnerCandidate;
	}

	public int getNonExhaustedBallots() {
		return nonExhaustedBallots;
	}

	private void setNonExhaustedBallots(int nonExhaustedBallots) {
		this.nonExhaustedBallots = nonExhaustedBallots;
	}

}
