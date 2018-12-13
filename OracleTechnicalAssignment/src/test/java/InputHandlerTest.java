import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import util.FakeIO;

import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InputHandlerTest {
	private static final Map<String, String> testCandidatesList;

	FakeIO fakeIO;

	@Mock
	VoteHandler voteHandler;

	InputHandler inputHandler;


	static
	{
		testCandidatesList = new HashMap<>();
		testCandidatesList.put("A", "WINERY TOUR");
		testCandidatesList.put("B", "TEN PIN BOWLING");
		testCandidatesList.put("C", "MOVIE NIGHT");
		testCandidatesList.put("J", "MUSEUM VISIT");
	}

	private void setUpFakeInput(String[] input) {
		fakeIO = new FakeIO(input);
		inputHandler = Mockito.spy(new InputHandler(testCandidatesList, fakeIO));
		when(inputHandler.getVoteHandler()).thenReturn(voteHandler);
	}

	@Test
	public void inputWithWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{"A B", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());
		assertEquals(1, inputHandler.getBallotMap().keySet().size());
	}


	@Test
	public void inputWithOutWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{"AB", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void inputWithLeadingAndTrailingWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{" AB ", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void multipleVotesShouldBeRead(){
		setUpFakeInput(new String[]{"AB", "Y", "AC", "Y", "CA", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(3)).addVotesToBallot(any());
		assertEquals(3, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void specialCharactersShouldNotBeRead(){
		setUpFakeInput(new String[]{"AB*",  "Y", "??", "Y", "&&", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(0)).addVotesToBallot(any());
		assertEquals(0, inputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void notListedCandidatesShouldNotBeRead(){
		setUpFakeInput(new String[]{"ABX",  "Y", "AB*", "Y", "AB", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, inputHandler.getBallotMap().keySet().size());
		assertEquals("A", inputHandler.getBallotMap().get(1).getVoteByPriority(1));
		assertEquals("B", inputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void emptyStringShouldNotBeRead(){
		setUpFakeInput(new String[]{"           ",  "Y", "AB", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, inputHandler.getBallotMap().keySet().size());
		assertEquals("A", inputHandler.getBallotMap().get(1).getVoteByPriority(1));
		assertEquals("B", inputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void calculationOfVotesShouldBeInvoked(){
		setUpFakeInput(new String[]{"BA",  "Y", "AB", "Y", "AC", "tally"});
		inputHandler.readInput();

		verify(voteHandler, times(1)).countVotes(any());

	}
	@Test
	public void inputWithTooFewVotesShouldNotInitiateCounting(){
		setUpFakeInput(new String[]{"AB", "Y", "AC", "TALLY"});
		inputHandler.readInput();

		verify(inputHandler, times(2)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());

	}

	@Test
	public void inputWithUnrecognizedCommandShouldExit(){
		setUpFakeInput(new String[]{"AB", "WWW", "N"});
		inputHandler.readInput();

		verify(inputHandler, times(1)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());
	}

	@Test
	public void shouldReturnArrayWithUniqueLabels(){
		InputHandler iph = new InputHandler(testCandidatesList,fakeIO);
		char[] testArray = {'a', 'a', 'x', 'x', 'b'};
		assertArrayEquals(new char[] {'a', 'x', 'b'}, iph.getUniqueLabelsCharArray(testArray));
	}

	@Test
	public void shouldElliminateWgitespaceValuesFromInputArray(){
		InputHandler iph = new InputHandler(testCandidatesList,fakeIO);
		assertArrayEquals(new char[] {'A', 'X', 'B'}, iph.parseInputToUniqueLabels("aa xx bb"));
	}

	@Test
	public void shouldValidateUserInputAgainstExistingLabels(){
		InputHandler iph = new InputHandler(testCandidatesList,fakeIO);
		char[] invalidLabels = {'a', 'a', 'x', 'x', 'b'};
		assertFalse(iph.validateLabels(invalidLabels));
	}

	@Test
	public void shouldValidateUserInputAgainstExistingLabelsValid(){
		InputHandler iph = new InputHandler(testCandidatesList,fakeIO);
		char[] validLabels = {'a', 'a', 'b', 'c', 'c'};
		assertTrue(iph.validateLabels(validLabels));
	}



}
