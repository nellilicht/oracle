import domain.Ballot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import util.FakeIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteHandlerTest {
	FakeIO io = new FakeIO();
	Map testBallotMap;
	List<String> testTopCandidates;
	VoteHandler voteHandler  =  mock(VoteHandler.class);
	Map<String, List<Integer>> testCandidatesAndBallotIDsMap;

	@Before
	public void setUp(){
		voteHandler  =  spy(new VoteHandler(io));
		testBallotMap = new HashMap<Integer, Ballot>(){{
			put(1, new Ballot(new char[]{'A', 'B'}));
			put(2, new Ballot(new char[]{'A', 'C'}));
			put(3, new Ballot(new char[]{'B', 'A'}));
			put(4, new Ballot(new char[]{'C', 'D', 'A'}));
		}};

		testTopCandidates = Arrays.asList("A", "B", "C");
		testCandidatesAndBallotIDsMap = new HashMap<String, List<Integer>>() {{
			put("A", new ArrayList<>(Arrays.asList(1,2)));
			put("B", new ArrayList<>(Arrays.asList(3)));
			put("C", new ArrayList<>(Arrays.asList(4)));
		}};

		when(voteHandler.getBallotMap()).thenReturn(testBallotMap);

	}

	@Test
	public void candidateShouldBeRemovedFromVotingHavingNoBallotsLeft(){
		voteHandler.removeCandidateFromVoting(testCandidatesAndBallotIDsMap, "A");
		assertNull(testCandidatesAndBallotIDsMap.get("A"));
	}



	@Test
	public void ballotShouldBeAssignedToNextCandidateAndRemovedFromPrevious(){
		Ballot testBallot = new Ballot(new char[]{'A', 'B'});
		assertEquals(2, testCandidatesAndBallotIDsMap.get("A").size());
		voteHandler.assignBallotToNextCandidate(testCandidatesAndBallotIDsMap, "A", 1, testBallot);
		assertEquals(2, testBallot.getActiveVote());
		assertEquals("B", testBallot.getVoteByPriority(2));
		assertEquals(2, testCandidatesAndBallotIDsMap.get("B").size());
		assertEquals(1, testCandidatesAndBallotIDsMap.get("A").size());
	}


	@Test
	public void ballotShouldBeRemovedFromVotingWhenExhausted(){
		doNothing().when(voteHandler).conductVotingRounds(anyMap());
		voteHandler.countVotes(testBallotMap);
		assertEquals(4, voteHandler.getNonExhaustedBallots());
		assertEquals(2, testCandidatesAndBallotIDsMap.get("A").size());

		voteHandler.removeBallotFromVoting(testCandidatesAndBallotIDsMap,"A", 1);

		assertNull(testBallotMap.get(1));
		assertEquals(1, testCandidatesAndBallotIDsMap.get("A").size());
		assertEquals(3, voteHandler.getNonExhaustedBallots());
		assertEquals(Arrays.asList(2).toString(), testCandidatesAndBallotIDsMap.get("A").toString());
	}


	@Test
	public void minimalVotesCountsAndCandidatesShouldBeFound(){
		assertEquals(1, voteHandler.getMinimalVotesCount(testCandidatesAndBallotIDsMap));
		assertEquals(Arrays.asList("B", "C"), voteHandler.findCandidatesWithMinimalVotes(testCandidatesAndBallotIDsMap, 1));

	}

	@Test
	public void ballotsShouldBeMappedToCandidatesByTheirHighestPriorityVote() {
		voteHandler.mapBallotsToCandidates(testTopCandidates);
		assertEquals(testCandidatesAndBallotIDsMap,voteHandler.mapBallotsToCandidates(testTopCandidates));
	}


	@Test
	public void topCandidatesShouldBeListedCorrectly(){
		assertEquals(testTopCandidates, voteHandler.findAllTopCandidatesFromBallots());
	}



	@Test
	public void conductVotingRoundsShouldBeCalledWhenCountingVotesIsCalled(){
		when(voteHandler.findAllTopCandidatesFromBallots()).thenReturn(testTopCandidates);
		when(voteHandler.mapBallotsToCandidates(testTopCandidates)).thenReturn(testCandidatesAndBallotIDsMap);
		doNothing().when(voteHandler).conductVotingRounds(anyMap());

		voteHandler.countVotes(testBallotMap);
		assertTrue(voteHandler.nonExhaustedBallots > 0);
		verify(voteHandler, times(1)).conductVotingRounds(testCandidatesAndBallotIDsMap);

	}

}