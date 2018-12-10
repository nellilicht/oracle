import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class VoteHandler {
		private Set<String> firstPriorityCandidates = new HashSet<>();

		//holds all ballots and their votes
		private Map<Integer, Ballot> ballotMap = new HashMap<>();
		int nonExhaustedBallots;


	public void countVotes(Map<Integer, Ballot> ballotMap) {
		this.ballotMap = ballotMap;
		nonExhaustedBallots = ballotMap.keySet().size();

		ballotMap.entrySet().stream()
				.forEach(entry -> {
					System.out.println("Vote : " + entry.getKey() + " Ballot : " + entry.getValue());
				});

		findAllTopCandidatesFromBallots(ballotMap, firstPriorityCandidates);

		System.out.println("firstPriorityCandidates: "+ firstPriorityCandidates);
		System.out.println("nonExhaustedBallots: "+nonExhaustedBallots);


		round1();
	}

	private void round1(){
		//filter all option and round priority 1  for them
		//eliminate exhausted ballots

		Map<String, List<Integer>> candidatesMapWithBallots = new HashMap();
		for(String candidate : firstPriorityCandidates){
			for( Integer entry : ballotMap.keySet()){
				 if (ballotMap.get(entry).getVoteByPriority(1).equals(candidate)){
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
		String winner = calculateWinner(candidatesMapWithBallots);

		while(winner.isEmpty()){
			System.out.println("no winner");
			Map<String, List<Integer>> reAssignedCandidatesMapWithBallots = reAssignBallots(candidatesMapWithBallots);
			winner = calculateWinner(reAssignedCandidatesMapWithBallots);
		}

	}

	private String calculateWinner(Map<String, List<Integer>> candidatesMapWithBallots) {
		//(number of non-exhausted ballots / 2) + 1
		String winnerCandidate = candidatesMapWithBallots.entrySet().stream()
				.filter(entry -> entry.getValue().size() > nonExhaustedBallots / 2 + 1).map(x -> x.getKey())
				.collect(Collectors.joining());
		if(!winnerCandidate.isEmpty()){
			System.out.println("Winner is candidate: " + winnerCandidate);
		}
		return winnerCandidate;
	}

	private Map<String, List<Integer>> reAssignBallots(Map<String, List<Integer>> candidatesMapWithBallotsList) {
		//find candidate with minimal ballots/votes
		//what should happen if multiple ballots candidates are removed

		int minimalVotes = candidatesMapWithBallotsList
				.entrySet().stream().min(comparingInt(e -> e.getValue().size())).get().getValue().size();

		//find all candidates with minimal votes
		List<String> candidatesWithMinimalVotes = candidatesMapWithBallotsList.entrySet().stream()
				.filter(entry -> entry.getValue().size() == minimalVotes).map(x -> x.getKey())
				.collect(Collectors.toList());

		System.out.println("Number least votes:" +minimalVotes);
		System.out.println("Candidate(s) with least votes:" +candidatesWithMinimalVotes);

		//get from ballots from candidate and re-assign them to next cancidate
		for(String candidate : candidatesWithMinimalVotes){

			List<Integer> tmp = new ArrayList<>();
			tmp.addAll(candidatesMapWithBallotsList.get(candidate));

			for(int ballotId : tmp){
				// need to know where to re-assign the ballot, the next priority (can be non existing candidate)
				Ballot ballot = ballotMap.get(ballotId);

				if(ballot.isExhausted()){
					ballotMap.keySet().remove(ballotId);
					nonExhaustedBallots = ballotMap.keySet().size();
					continue;
				} else {
					String nextCandidateForBallot = ballot.getNextPreferenceFromBallot();

				//remove ballot from map with id:ballotId
				candidatesMapWithBallotsList.get(candidate).removeIf(value -> value.equals(ballotId));

				candidatesMapWithBallotsList.get(nextCandidateForBallot).add(ballotId);

				if(candidatesMapWithBallotsList.get(candidate).isEmpty()){
					System.out.println("Removed Candidate: " + candidate);
					candidatesMapWithBallotsList.keySet().remove(candidate);
				}

				}
			}
		}

		return candidatesMapWithBallotsList;
	}

	//change initialization for firstprioritycandidates
	private void findAllTopCandidatesFromBallots(Map<Integer, Ballot> ballotMap, Set<String> firstPriorityCandidates) {
		ballotMap.values().stream().forEach(ballot -> firstPriorityCandidates.add(ballot.getCandidateWithFirstPriority()));
	}

	//return the label of the winner
}
