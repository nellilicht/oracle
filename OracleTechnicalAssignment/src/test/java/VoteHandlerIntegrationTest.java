import domain.Ballot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import util.FakeIO;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class VoteHandlerIntegrationTest {
	FakeIO fakeIO = new FakeIO();
	Map testBallotMap;
	VoteHandler voteHandler;

	@Before
	public void setUp(){
		voteHandler = spy(new VoteHandler(fakeIO));
	}


	@Test
	public void voteCalculationShouldProvideCorrectWinner(){

		testBallotMap = new HashMap<Integer, Ballot>(){{
			put(1, new Ballot(new char[]{'A', 'B'}));
			put(2, new Ballot(new char[]{'B', 'A'}));
			put(3, new Ballot(new char[]{'B', 'A'}));
			put(4, new Ballot(new char[]{'C'}));
		}};
		voteHandler.countVotes(testBallotMap);
		assertEquals("Winner is candidate: B", fakeIO.output.get(fakeIO.output.size()-1));

	}

	@Test
	public void voteCalculationShouldReturnRandomWinnerForEqualVotesCandidates(){
		Map testBallotMap = new HashMap<Integer, Ballot>(){{
			put(1, new Ballot(new char[]{'A'}));
			put(2, new Ballot(new char[]{'B'}));
			put(3, new Ballot(new char[]{'C'}));
			put(4, new Ballot(new char[]{'D'}));
			put(5, new Ballot(new char[]{'E'}));
		}};

		voteHandler.countVotes(testBallotMap);
		assertNotEquals( "", voteHandler.getWinnerCandidate());
	}

	@Test
	public void voteCalculationShouldAssignBallotsToExistingCandidates(){
		testBallotMap = new HashMap<Integer, Ballot>(){{
			put(1, new Ballot(new char[]{'A','D','C'}));
			put(2, new Ballot(new char[]{'A','D','C'}));
			put(3, new Ballot(new char[]{'B','C'}));
			put(4, new Ballot(new char[]{'C'}));
			put(5, new Ballot(new char[]{'C'}));

		}};

		voteHandler.countVotes(testBallotMap);
		assertEquals("Winner is candidate: C", fakeIO.output.get(fakeIO.output.size()-1));


	}

}