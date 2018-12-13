import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BallotTest {
	Ballot ballot;

	@Before
	public void setUp(){
		char[] votes = {'c', 'a', 'b'};
		ballot = new Ballot(votes);
	}

	@Test
	public void ballotShouldHavePrioritizedVotesAssigned(){
		Assert.assertEquals(3, ballot.getPrioritizedCandidatesMap().keySet().size());
		Assert.assertEquals( "c", ballot.getPrioritizedCandidatesMap().get(1));
		Assert.assertEquals( "a", ballot.getPrioritizedCandidatesMap().get(2));
		Assert.assertEquals( "b", ballot.getPrioritizedCandidatesMap().get(3));

		Map<Integer, String> votes = new HashMap<Integer, String>()
		{{
			put(1, "c");
			put(2, "a");
			put(3, "b");
		}};
		Assert.assertEquals(votes.toString(), ballot.toString());
	}


	@Test
	public void ballotShouldReturnCorrectCandidateByPriority(){
		Assert.assertEquals("c", ballot.getVoteByPriority(1));
		Assert.assertEquals("a", ballot.getVoteByPriority(2));
		Assert.assertEquals("b", ballot.getVoteByPriority(3));
	}

	@Test
	public void ballotShouldReturnNextCandidateByPriority(){
		Assert.assertEquals("c", ballot.getPrioritizedCandidatesMap().get(ballot.getActiveVote()));
		Assert.assertEquals("a", ballot.getNextPreferenceFromBallot());
		Assert.assertEquals("b", ballot.getNextPreferenceFromBallot());
	}

	@Test
	public void ballotShouldKeepActiveVoteStatus(){
		Assert.assertEquals("c", ballot.getPrioritizedCandidatesMap().get(ballot.getActiveVote()));
		Assert.assertEquals(1, ballot.getActiveVote());
		Assert.assertEquals("a", ballot.getNextPreferenceFromBallot());
		Assert.assertEquals(2, ballot.getActiveVote());
		Assert.assertEquals("b", ballot.getNextPreferenceFromBallot());
		Assert.assertEquals(3, ballot.getActiveVote());
	}

	@Test
	public void ballotShouldReturnExhaustedState(){
		Assert.assertEquals(false, ballot.isExhausted());
		ballot.getNextPreferenceFromBallot();
		ballot.getNextPreferenceFromBallot();
		Assert.assertEquals(true, ballot.isExhausted());

	}

	@Test
	public void ballotShouldReturnVoteWithFirstPriority(){
		Assert.assertEquals("c", ballot.getCandidateWithFirstPriority());
	}

}