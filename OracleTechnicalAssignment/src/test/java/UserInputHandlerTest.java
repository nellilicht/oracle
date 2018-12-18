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
public class UserInputHandlerTest {
	private static final Map<String, String> testCandidatesList;

	FakeIO fakeIO;

	@Mock
	VoteHandler voteHandler;

	UserInputHandler userInputHandler;


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
		userInputHandler = Mockito.spy(new UserInputHandler(testCandidatesList, fakeIO));
		when(userInputHandler.getVoteHandler()).thenReturn(voteHandler);
	}

	@Test
	public void inputWithWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{"A B", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());
		assertEquals(1, userInputHandler.getBallotMap().keySet().size());
	}


	@Test
	public void inputWithOutWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{"AB", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, userInputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void inputWithLeadingAndTrailingWhitespaceShouldBeRead(){
		setUpFakeInput(new String[]{" AB ", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, userInputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void multipleVotesShouldBeRead(){
		setUpFakeInput(new String[]{"AB", "Y", "AC", "Y", "CA", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(3)).addVotesToBallot(any());
		assertEquals(3, userInputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void specialCharactersShouldNotBeRead(){
		setUpFakeInput(new String[]{"AB*",  "Y", "??", "Y", "&&", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(0)).addVotesToBallot(any());
		assertEquals(0, userInputHandler.getBallotMap().keySet().size());

	}

	@Test
	public void notListedCandidatesShouldNotBeRead(){
		setUpFakeInput(new String[]{"ABX",  "Y", "AB*", "Y", "AB", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, userInputHandler.getBallotMap().keySet().size());
		assertEquals("A", userInputHandler.getBallotMap().get(1).getVoteByPriority(1));
		assertEquals("B", userInputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void emptyStringShouldNotBeRead(){
		setUpFakeInput(new String[]{"           ",  "Y", "AB", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		assertEquals(1, userInputHandler.getBallotMap().keySet().size());
		assertEquals("A", userInputHandler.getBallotMap().get(1).getVoteByPriority(1));
		assertEquals("B", userInputHandler.getBallotMap().get(1).getVoteByPriority(2));

	}

	@Test
	public void calculationOfVotesShouldBeInvoked(){
		setUpFakeInput(new String[]{"BA",  "Y", "AB", "Y", "AC", "tally"});
		userInputHandler.readInput();

		verify(voteHandler, times(1)).countVotes(any());

	}
	@Test
	public void inputWithTooFewVotesShouldNotInitiateCounting(){
		setUpFakeInput(new String[]{"AB", "Y", "AC", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(2)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());

	}

	@Test
	public void inputWithUnrecognizedCommandShouldExit(){
		setUpFakeInput(new String[]{"AB", "WWW", "TALLY"});
		userInputHandler.readInput();

		verify(userInputHandler, times(1)).addVotesToBallot(any());
		verify(voteHandler, times(0)).countVotes(any());
	}

	@Test
	public void shouldReturnArrayWithUniqueLabels(){
		UserInputHandler iph = new UserInputHandler(testCandidatesList,fakeIO);
		char[] testArray = {'a', 'a', 'x', 'x', 'b'};
		assertArrayEquals(new char[] {'a', 'x', 'b'}, iph.getUniqueLabelsCharArray(testArray));
	}

	@Test
	public void shouldEliminateWhitespaceValuesFromInputArray(){
		UserInputHandler iph = new UserInputHandler(testCandidatesList,fakeIO);
		assertArrayEquals(new char[] {'A', 'X', 'B'}, iph.parseInputToUniqueLabels("aa xx bb"));
	}

	@Test
	public void shouldValidateUserInputAgainstExistingLabels(){
		UserInputHandler iph = new UserInputHandler(testCandidatesList,fakeIO);
		char[] invalidLabels = {'a', 'a', 'x', 'x', 'b'};
		assertFalse(iph.validateLabels(invalidLabels));
	}

	@Test
	public void shouldValidateUserInputAgainstExistingLabelsValid(){
		UserInputHandler iph = new UserInputHandler(testCandidatesList,fakeIO);
		char[] validLabels = {'a', 'a', 'b', 'c', 'j'};
		assertTrue(iph.validateLabels(validLabels));
	}



}
