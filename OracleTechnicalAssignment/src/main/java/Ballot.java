import java.util.HashMap;
import java.util.Map;

public class Ballot {
	private Map<Integer, String> prioritizedCandidatesMap = new HashMap<>();
	private int activeVotes;

	public Ballot(char[] charArray) {
		activeVotes = charArray.length;
		for(int i = 0; i<activeVotes; i++){
			prioritizedCandidatesMap.put(i+1, String.valueOf(charArray[i]));
		}
		System.out.println(prioritizedCandidatesMap);
	}

	@Override
	public String toString() {
		return prioritizedCandidatesMap.toString();
	}


	public Map<Integer, String> getPrioritizedCandidatesMap() {
		return prioritizedCandidatesMap;
	}

	public String getVoteByPriority(Integer i){
		return prioritizedCandidatesMap.get(i);
	}
}
