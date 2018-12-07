import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VoteHandler {
		Set<String> possibleCandidates = new HashSet<>();
		Map<Integer, Ballot> ballotMap;
		int nonExhaustedBallots;


	public void calculateVotes(Map<Integer, Ballot> ballotMap) {
		this.ballotMap = ballotMap;
		nonExhaustedBallots = ballotMap.keySet().size();

		ballotMap.entrySet().stream()
				.forEach(entry -> {
					System.out.println("Vote : " + entry.getKey() + " Ballot : " + entry.getValue());
				});
		              //rename
		findPossibleOptions(ballotMap, possibleCandidates);

		System.out.println("possibleCandidates: "+possibleCandidates);
		System.out.println("nonExhaustedBallots: "+nonExhaustedBallots);


		round1();
	}

	private void round1(){
		//filter all option and round priority 1  for them
		//eliminate exhausted ballots

		Map<String, List<Integer>> candidatesMapWithBallots = new HashMap();
		for(String candidate : possibleCandidates){
			for( Integer entry : ballotMap.keySet()){
				 if (ballotMap.get(entry).getVoteByPriority(1).equals(candidate)){
				 	if(candidatesMapWithBallots.get(candidate) == null){
						List<Integer> entryList = new ArrayList<>();
						entryList.add(entry);
				 		candidatesMapWithBallots.put(candidate, entryList);
					} else {
						candidatesMapWithBallots.get(candidate).add(entry);
					}
				//	System.out.println("Maps: " + candidatesMapWithBallots);
				 }

			}
		}

		//(number of non-exhausted ballots / 2) + 1
		String winner = candidatesMapWithBallots.entrySet().stream()
				.filter(entry -> entry.getValue().size() > nonExhaustedBallots / 2 + 1).map(x -> x.getKey())
				.collect(Collectors.joining());
		System.out.println("Winner: "+winner);

	}

	private void findPossibleOptions(Map<Integer, Ballot> ballotMap, Set<String> possibleOptions) {
		ballotMap.values().stream().forEach(ballot -> {ballot.getPrioritizedCandidatesMap().values().
				stream().forEach(option -> {possibleOptions.add(option);});});
	}

	//return the label of the winner
}
