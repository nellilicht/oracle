import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import util.FakeIO;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InputHandlerTest {
	private static final Map<String, String> testCandidatesList;
	InputHandler inputHandler;

	static
	{
		testCandidatesList = new HashMap<>();
		testCandidatesList.put("A", "WINERY TOUR");
		testCandidatesList.put("B", "TEN PIN BOWLING");
		testCandidatesList.put("C", "MOVIE NIGHT");
		testCandidatesList.put("J", "MUSEUM VISIT");
	}
	@Before
	public void setUp(){
	inputHandler = mock(InputHandler.class);
	}

	@Test
	public void inputWithWhitespaceShouldBeRead(){
		FakeIO fakeIO = new FakeIO("A B", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		Assert.assertEquals(1, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void inputWithOutWhitespaceShouldBeRead(){
		FakeIO fakeIO = new FakeIO("AB", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		Assert.assertEquals(1, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void inputWithLeadingAndTrailingWhitespaceShouldBeRead(){
		FakeIO fakeIO = new FakeIO(" AB ", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		Assert.assertEquals(1, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void multipleVotesShouldBeRead(){
		FakeIO fakeIO = new FakeIO("AB", "Y", "AC", "Y", "CA", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(3)).addVotesToBallot(any());
		Assert.assertEquals(3, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void specialCharactersShouldNotBeRead(){
		FakeIO fakeIO = new FakeIO("AB*",  "Y", "??", "Y", "&&", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(0)).addVotesToBallot(any());
		Assert.assertEquals(0, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void notListedCandidatesShouldNotBeRead(){
		FakeIO fakeIO = new FakeIO("ABX",  "Y", "AB*", "Y", "AB", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		Assert.assertEquals(1, inputHandler.getBallotMap().keySet().size());
		Assert.assertEquals("A", inputHandler.getBallotMap().get(1).getVoteByPriority(1));
		Assert.assertEquals("B", inputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void emptyStringShouldNotBeRead(){
		FakeIO fakeIO = new FakeIO("           ",  "Y", "AB", "N");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		Assert.assertEquals(1, inputHandler.getBallotMap().keySet().size());
		Assert.assertEquals("A", inputHandler.getBallotMap().get(1).getVoteByPriority(1));
		Assert.assertEquals("B", inputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void calculationOfVotesShouldBeInvoked(){
		FakeIO fakeIO = new FakeIO("BA",  "Y", "AB", "N","tally");
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		inputHandler.readInput();

	}

}
