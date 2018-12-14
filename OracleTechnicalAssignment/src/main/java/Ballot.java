import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Ballot {
	private Map<Integer, String> prioritizedCandidatesMap = new HashMap<>();
	private int activeVotesTotal;
	private int activeVote;

	public Ballot(char[] charArray) {
		activeVotesTotal = charArray.length;
		activeVote = 1;
		for(int i = 0; i< activeVotesTotal; i++){
			prioritizedCandidatesMap.put(i+1, String.valueOf(charArray[i]));
		}
	}

	@Override
	public String toString() {
		return prioritizedCandidatesMap.toString();
	}

	public String getCandidateWithPriority(int priority) {
		return prioritizedCandidatesMap.get(priority);
	}

	public String getVoteByPriority(Integer i){
		return prioritizedCandidatesMap.get(i);
	}

	//Assumes that ballot exhaustion check is done before
	public String getNextActiveCandidateFromBallot(Set<String> activeCandidates){
		String candidate = prioritizedCandidatesMap.get(++activeVote);

		boolean suitableCandidate=activeCandidates.contains(candidate.toUpperCase());

		while(!suitableCandidate){
			candidate = prioritizedCandidatesMap.get(++activeVote);
			if(activeCandidates.contains(candidate)){
				suitableCandidate = true;
			}
		}
		return candidate;
	}

	public boolean isExhausted(Set<String> activeCandidates){
		Map<Integer, String> possibleNextVotes = new HashMap<>(prioritizedCandidatesMap);

		for(int i = 0; i<activeVote; i++){
			possibleNextVotes.remove(activeVote);
		}

		return 	!(prioritizedCandidatesMap.keySet().size() >= activeVote + 1) ||
		!(possibleNextVotes.values().stream().distinct().filter(c -> activeCandidates.contains(c.toUpperCase())).count() > 0);

	}

	public Map<Integer, String> getPrioritizedCandidatesMap() {
		return prioritizedCandidatesMap;
	}

	public int getActiveVote() {
		return activeVote;
	}


}
