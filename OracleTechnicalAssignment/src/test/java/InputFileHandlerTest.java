import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InputFileHandlerTest {
	InputFileHandler parseCandidates;

	@Before
	public void setUp()
	{
		File file = new File(this.getClass().getResource("test-candidates.txt").getFile());
		System.out.println(file.getAbsolutePath());
		parseCandidates = new InputFileHandler();
	}

	@Test
	public void correctInputFileShouldBeParsed() throws FileNotFoundException {
		List<String> test = parseCandidates.readCandidatesFromFile("test-candidates.txt");
		assertEquals(Arrays.asList("Winery tour", "Ten pin bowling", "Movie night", "Museum visit"), test);
	}

	@Test(expected = FileNotFoundException.class)
	public void invalidFilePathShouldThrowErrorWhenReading() throws FileNotFoundException {
		parseCandidates.readCandidatesFromFile("xfiles");
	}

	@Test(expected = FileNotFoundException.class)
	public void invalidFilePathShouldThrowErrorWhenParsing() throws FileNotFoundException {
		parseCandidates.parseInputFile("xfiles");
	}


	@Test
	public void candidatesShouldBeLabelled() throws FileNotFoundException {
		Map<String, String> test = parseCandidates.parseInputFile("test-candidates.txt");
		Map<String, String> parsedCandidates = new HashMap<String, String>(){{
			put("A", "WINERY TOUR");
			put("B", "TEN PIN BOWLING");
			put("C", "MOVIE NIGHT");
			put("D", "MUSEUM VISIT");
		}};
		assertEquals(parsedCandidates, test);

	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyCandidatesShouldThrowError(){
		List<String> testCandidatesList = Arrays
				.asList("a", "b", "c", "a", "b", "c","a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a",
						"b", "c", "a", "b", "c" );
		parseCandidates.labelCandidates(testCandidatesList);
	}

	@Test
	public void maximumNumberCandidatesShouldbeParsed(){
		List<String> testCandidatesList = Arrays
				.asList("a", "b", "c", "a", "b", "c","a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a",
						"b", "c", "a");
		assertEquals(25, parseCandidates.labelCandidates(testCandidatesList).keySet().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void toofewCandidatesShouldThrowError(){
		List<String> testCandidatesList = Arrays
				.asList("a", "b");
		parseCandidates.labelCandidates(testCandidatesList);
	}

	@Test
	public void minNumberCandidatesShouldbeParsed(){
		List<String> testCandidatesList = Arrays
				.asList("a", "b", "c");
		assertEquals(3, parseCandidates.labelCandidates(testCandidatesList).keySet().size());
	}

}