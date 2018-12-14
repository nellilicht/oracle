import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParseCandidates {
	private static final int MAXIMUM_NUMBER_OF_CANDIDATES = 25;
	private static final int MINIMUM_NUMBER_OF_CANDIDATES = 2;
	private static final String ALPHABETH = "abcdefghijklmopqrstuvwxyz'";

	public Map<String, String> parseCandidates(String fileName) throws FileNotFoundException, IllegalArgumentException {
		List<String> candidates = readCandidatesFromFile(fileName);
		return labelCandidates(candidates);
	}

	protected List<String> readCandidatesFromFile(String fileName) throws FileNotFoundException {
		List<String> list = new ArrayList<>();

		URL fileToRead = ParseCandidates.class.getResource(fileName);

		if(fileToRead == null){ throw new FileNotFoundException();
		}

		try (Stream<String> stream = Files.lines(Paths.get(fileToRead.toURI()))) {

			list =  stream
					.map(s -> s.trim())
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return list;
	}

	protected Map<String, String> labelCandidates(List<String> candidatesList) throws IllegalArgumentException{
		Map<String, String> labelledCandidatesMap = new HashMap<>();

		if (candidatesList.size()>MAXIMUM_NUMBER_OF_CANDIDATES){
			throw new IllegalArgumentException("Too many candidates listed, please review the input file (max 25)!");
		} else if (candidatesList.size() <= MINIMUM_NUMBER_OF_CANDIDATES){
			throw new IllegalArgumentException("Too few candidates listed, please review the input file (min 2)!");
		}

		char[] charArray = ALPHABETH.toCharArray();

		for(int i = 0; i < candidatesList.size() ; i++){
			labelledCandidatesMap.put(String.valueOf(charArray[i]).toUpperCase(), candidatesList.get(i).toUpperCase());
		}

		return labelledCandidatesMap;
	}



}
