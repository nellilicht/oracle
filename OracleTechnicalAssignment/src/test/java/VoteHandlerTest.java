import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import util.FakeIO;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class VoteHandlerTest {
	FakeIO fakeIO = new FakeIO();









	@Test
	public void voteCalculationShouldProvideCorrectWinner(){
		VoteHandler voteHandler = new VoteHandler(fakeIO);

		Map testBallotMap = new HashMap<Integer, Ballot>(){{
			put(Integer.valueOf(1), new Ballot(new char[]{'A', 'B'}));
			put(Integer.valueOf(2), new Ballot(new char[]{'B', 'A'}));
			put(Integer.valueOf(3), new Ballot(new char[]{'B', 'A'}));
			put(Integer.valueOf(4), new Ballot(new char[]{'C'}));
		}};
		voteHandler.countVotes(testBallotMap);
		Assert.assertEquals("Winner is candidate: B", fakeIO.output.get(fakeIO.output.size()-1));

	}

	@Test
	public void voteCalculationShouldReturnWinnerForEqualVotesCandidates(){
		VoteHandler voteHandler = new VoteHandler(fakeIO);

		Map testBallotMap = new HashMap<Integer, Ballot>(){{
			put(Integer.valueOf(1), new Ballot(new char[]{'A'}));
			put(Integer.valueOf(2), new Ballot(new char[]{'B'}));
			put(Integer.valueOf(3), new Ballot(new char[]{'C'}));
			put(Integer.valueOf(4), new Ballot(new char[]{'D'}));
			put(Integer.valueOf(5), new Ballot(new char[]{'E'}));
		}};

		voteHandler.countVotes(testBallotMap);

	}

	@Test
	public void voteCalculationShouldAssignBallotsToExistingCandidates(){
		VoteHandler voteHandler = new VoteHandler(fakeIO);

		Map testBallotMap = new HashMap<Integer, Ballot>(){{
			put(Integer.valueOf(1), new Ballot(new char[]{'A','D','C'}));
			put(Integer.valueOf(2), new Ballot(new char[]{'A','D','C'}));
			put(Integer.valueOf(3), new Ballot(new char[]{'B','C'}));
			put(Integer.valueOf(4), new Ballot(new char[]{'C'}));
			put(Integer.valueOf(5), new Ballot(new char[]{'C'}));

		}};

		voteHandler.countVotes(testBallotMap);
		Assert.assertEquals("Winner is candidate: C", fakeIO.output.get(fakeIO.output.size()-1));


	}

}