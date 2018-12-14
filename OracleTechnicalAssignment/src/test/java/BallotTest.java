import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class BallotTest {
	Ballot ballot;
	Map<Integer, String> votesMap;
	Map candidatesWithBallotsMaps;

	@Before
	public void setUp(){
		char[] votes = {'c', 'a', 'b'};
		ballot = new Ballot(votes);

		votesMap = new HashMap<Integer, String>()
		{{
			put(1, "c");
			put(2, "a");
			put(3, "b");
		}};

		candidatesWithBallotsMaps = new HashMap<String, List<Integer>>(){{
			put("A", Arrays.asList(1));
			put("B", Arrays.asList(2));
			put("C", Arrays.asList(3));
		}};
	}

	@Test
	public void ballotShouldHavePrioritizedVotesAssigned(){
		Assert.assertEquals(3, ballot.getPrioritizedCandidatesMap().keySet().size());
		Assert.assertEquals( "c", ballot.getPrioritizedCandidatesMap().get(1));
		Assert.assertEquals( "a", ballot.getPrioritizedCandidatesMap().get(2));
		Assert.assertEquals( "b", ballot.getPrioritizedCandidatesMap().get(3));

		Assert.assertEquals(votesMap.toString(), ballot.toString());
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
		Assert.assertEquals("a", ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet()));
		Assert.assertEquals("b", ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet()));
	}

	@Test
	public void ballotShouldKeepActiveVoteStatus(){
		Assert.assertEquals("c", ballot.getPrioritizedCandidatesMap().get(ballot.getActiveVote()));
		Assert.assertEquals(1, ballot.getActiveVote());
		Assert.assertEquals("a", ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet()));
		Assert.assertEquals(2, ballot.getActiveVote());
		Assert.assertEquals("b", ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet()));
		Assert.assertEquals(3, ballot.getActiveVote());
	}

	@Test
	public void ballotShouldReturnExhaustedStateWhenHavingRunOutOfVotes(){
		Assert.assertEquals(false, ballot.isExhausted(new HashSet<>(Arrays.asList("A","B", "C"))));
		ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet());
		ballot.getNextActiveCandidateFromBallot(candidatesWithBallotsMaps.keySet());
		Assert.assertEquals(true, ballot.isExhausted(candidatesWithBallotsMaps.keySet()));

	}

	@Test
	public void ballotShouldReturnExhaustedStateIfItNotInActiveCandidatesList(){
		Assert.assertEquals(true,
				ballot.isExhausted(new HashSet<>(Arrays.asList("J","K"))));

	}

	@Test
	public void ballotShouldReturnVoteWithFirstPriority(){
		Assert.assertEquals("c", ballot.getCandidateWithPriority(1));
	}

}