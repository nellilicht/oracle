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
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteHandlerTest {
	FakeIO io = new FakeIO();
	VoteHandler voteHandler;
	Map testBallotMap;
	List<String> testTopCandidates;
	List<String> testCandidatesWithMinimalVotes;
	Map<String, List<Integer>>  reAssignedCandidatesMapWithBallots;
	Map<String, List<Integer>> testCandidatesAndBallotIDsMap;

	@Before
	public void setUp(){
		voteHandler  =  spy(new VoteHandler(io));
		testBallotMap = new HashMap<Integer, Ballot>(){{
			put(1, new Ballot(new char[]{'A', 'B'}));
			put(2, new Ballot(new char[]{'A', 'C'}));
			put(3, new Ballot(new char[]{'B', 'A'}));
			put(4, new Ballot(new char[]{'C', 'D', 'A'}));
			put(5, new Ballot(new char[]{'E'}));
		}};

		testTopCandidates = Arrays.asList("A", "B", "C", "E");
		testCandidatesWithMinimalVotes = Arrays.asList("B", "C", "E");
		testCandidatesAndBallotIDsMap = new HashMap<String, List<Integer>>() {{
			put("A", new ArrayList<>(Arrays.asList(1,2)));
			put("B", new ArrayList<>(Arrays.asList(3)));
			put("C", new ArrayList<>(Arrays.asList(4)));
			put("E", new ArrayList<>(Arrays.asList(5)));
		}};

		reAssignedCandidatesMapWithBallots = new HashMap<String, List<Integer>>() {{
			put("A", new ArrayList<>(Arrays.asList(1,2,3,4)));
		}};

		when(voteHandler.getBallotMap()).thenReturn(testBallotMap);

	}

	@Test
	public void candidateShouldBeElliminatedFromVoting(){
		testCandidatesWithMinimalVotes = new ArrayList<>(Arrays.asList("B", "C", "E"));

		Random rand = mock(Random.class);
		doReturn(rand).when(voteHandler).getRand();
		doReturn(2).when(rand).nextInt(anyInt());

		voteHandler.elliminateCandidate(testCandidatesAndBallotIDsMap, testCandidatesWithMinimalVotes);

		assertEquals(2, testCandidatesWithMinimalVotes.size());
		assertNull(testCandidatesAndBallotIDsMap.get("E"));
		assertNull(testBallotMap.get(5));
		verify(voteHandler, times(1)).setNonExhaustedBallots(anyInt());
	}


	@Test
	public void randomCandidateElliminationShouldBeCalledWhenAllVotesAreDividedEqually(){
		doReturn(1).when(voteHandler).getMinimalVotesCount(anyMap());
		doReturn(Arrays.asList("A", "B", "C", "E")).when(voteHandler).findCandidatesWithMinimalVotes(anyMap(), anyInt());
		doNothing().when(voteHandler).elliminateCandidate(anyMap(),anyList());

		voteHandler.handleBallotsWithMinimalVotes(testCandidatesAndBallotIDsMap);
		verify(voteHandler, times(1)).elliminateCandidate(anyMap(),anyList());
		verify(voteHandler, times(0)).reAssignBallots(anyMap(),anyList());
	}

	@Test
	public void randomCandidateElliminationShouldNotBeCalled(){
		doReturn(1).when(voteHandler).getMinimalVotesCount(anyMap());
		when(voteHandler.findCandidatesWithMinimalVotes(anyMap(), anyInt())).thenReturn(Arrays.asList("A"));
		voteHandler.handleBallotsWithMinimalVotes(reAssignedCandidatesMapWithBallots);
		verify(voteHandler, times(0)).elliminateCandidate(anyMap(),anyList());
	}

	@Test
	public void ballotsReAssigningShouldNotBeCalledWhenOnlyOneCandidateRemains(){
		voteHandler.handleBallotsWithMinimalVotes(reAssignedCandidatesMapWithBallots);
		verify(voteHandler, times(0)).reAssignBallots(anyMap(),anyList());
	}

	@Test
	public void ballotWithoutNextActiveVoteShouldBeRemoved(){
		doNothing().when(voteHandler).removeBallotFromVoting(anyMap(), anyString(), anyInt());
		doNothing().when(voteHandler).assignBallotToNextCandidate(anyMap(),anyString(),anyInt(), any(Ballot.class));

		voteHandler.reAssignBallots(testCandidatesAndBallotIDsMap, testCandidatesWithMinimalVotes);
		verify(voteHandler, times(1))
				.removeBallotFromVoting(testCandidatesAndBallotIDsMap, "E", 5);
	}

	@Test
	public void ballotsShouldBeReassignedToNextPriority(){
		doNothing().when(voteHandler).removeBallotFromVoting(anyMap(), anyString(), anyInt());
		doNothing().when(voteHandler).removeCandidateFromVoting(anyMap(),anyString());
		voteHandler.reAssignBallots(testCandidatesAndBallotIDsMap, testCandidatesWithMinimalVotes);
		assertEquals(new ArrayList<>(Arrays.asList(1,2,3,4)), testCandidatesAndBallotIDsMap.get("A"));
	}

	@Test
	public void calculateWinnerShouldReturnLastCandidateAsWinner(){
		int fakeNonExhaustedBallotsAmountToFindWinner = 1;
		when(voteHandler.getNonExhaustedBallots()).thenReturn(fakeNonExhaustedBallotsAmountToFindWinner);

		testCandidatesAndBallotIDsMap = new HashMap<String, List<Integer>>() {{
			put("C", new ArrayList<>(Arrays.asList(4)));
		}};

		assertEquals("C", voteHandler.calculateWinner(testCandidatesAndBallotIDsMap));
	}


	@Test(expected = IllegalArgumentException.class)
	public void calculateWinnerShouldThrowExceptionWhenNotEnoughBallots(){
		assertEquals("", voteHandler.calculateWinner(testCandidatesAndBallotIDsMap));
	}


	@Test
	public void calculateWinnerShouldReturnEmptyStringWhenCandidateIsNotFound(){
		int fakeNonExhaustedBallotsAmount = 100;
		when(voteHandler.getNonExhaustedBallots()).thenReturn(fakeNonExhaustedBallotsAmount);
		assertEquals("", voteHandler.calculateWinner(testCandidatesAndBallotIDsMap));
	}

	@Test
	public void calculateWinnerShouldReturnOneWinnerCandidateWhenCandidateIsFound(){
		int fakeNonExhaustedBallotsAmountToFindWinner = 1;
		when(voteHandler.getNonExhaustedBallots()).thenReturn(fakeNonExhaustedBallotsAmountToFindWinner);
		assertEquals("A", voteHandler.calculateWinner(testCandidatesAndBallotIDsMap));
	}


	@Test
	public void conductVotingRoundsShouldCallWinnerCalculationsOneTimesWhenWinnerFoundImmediately(){
		int fakeNonExhaustedBallotsAmountToFindWinner = 4;
		when(voteHandler.getNonExhaustedBallots()).thenReturn(fakeNonExhaustedBallotsAmountToFindWinner);

		voteHandler.conductVotingRounds(reAssignedCandidatesMapWithBallots);

		verify(voteHandler, times(1)).calculateWinner(anyMap());
	}

	@Test
	public void conductVotingRoundsShouldCallWinnerCalculationsMultipleTimesWhenWinnerNotFound(){
		Map<String, List<Integer>>  reAssignedCandidatesMapWithBallots = new HashMap<String, List<Integer>>() {{
			put("A", new ArrayList<>(Arrays.asList(1,2,3,4)));
		}};

		doReturn("").when(voteHandler).calculateWinner(testCandidatesAndBallotIDsMap);
		doReturn(reAssignedCandidatesMapWithBallots).when(voteHandler).handleBallotsWithMinimalVotes(anyMap());
		doReturn("A").when(voteHandler).calculateWinner(reAssignedCandidatesMapWithBallots);

		voteHandler.conductVotingRounds(testCandidatesAndBallotIDsMap);

		verify(voteHandler, atLeast(2)).calculateWinner(anyMap());
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
		assertEquals(testBallotMap.keySet().size(), voteHandler.getNonExhaustedBallots());
		assertEquals(2, testCandidatesAndBallotIDsMap.get("A").size());

		voteHandler.removeBallotFromVoting(testCandidatesAndBallotIDsMap,"A", 1);

		assertNull(testBallotMap.get(1));
		assertEquals(1, testCandidatesAndBallotIDsMap.get("A").size());
		assertEquals(testBallotMap.keySet().size(), voteHandler.getNonExhaustedBallots());
		assertEquals(Arrays.asList(2).toString(), testCandidatesAndBallotIDsMap.get("A").toString());
	}


	@Test
	public void minimalVotesCountsAndCandidatesShouldBeFound(){
		assertEquals(1, voteHandler.getMinimalVotesCount(testCandidatesAndBallotIDsMap));
		assertEquals(testCandidatesWithMinimalVotes, voteHandler.findCandidatesWithMinimalVotes(testCandidatesAndBallotIDsMap, 1));

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