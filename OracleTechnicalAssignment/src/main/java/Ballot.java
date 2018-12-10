import java.util.HashMap;
import java.util.Map;

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

		System.out.println(prioritizedCandidatesMap);
	}

	@Override
	public String toString() {
		return prioritizedCandidatesMap.toString();
	}


	public String getCandidateWithFirstPriority() {
		return prioritizedCandidatesMap.get(1);
	}

	public String getVoteByPriority(Integer i){
		return prioritizedCandidatesMap.get(i);
	}

	public String getNextPreferenceFromBallot(){
		return prioritizedCandidatesMap.get(++activeVote);
	}

	public boolean isExhausted(){
		return prioritizedCandidatesMap.keySet().size() < activeVote + 1;
	}



}
