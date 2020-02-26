package au.com.redbarn.animal.sentence.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.redbarn.animal.sentence.errors.AlgorithmBrokenException;
import au.com.redbarn.animal.sentence.utils.SentenceUtils;

/**
 * Tests for {@link MarkovChain}.
 *
 * @author peter
 *
 */
@SpringBootTest
public class MarkovChainTest {

	@Test
	public void generateAliceOz() {

		final String TEST_FILENAME = "alice_oz.txt";
		final int NUMBER_OF_SENTENCES = 20;

		try {
			assertEquals(NUMBER_OF_SENTENCES, generate(TEST_FILENAME, NUMBER_OF_SENTENCES));
		} catch (AlgorithmBrokenException e) {
			fail();
		}
	}

	@Test
	public void generateNerd() {
		final String TEST_FILENAME = "nerd.txt";
		final int NUMBER_OF_SENTENCES = 20;
		assertThrows(AlgorithmBrokenException.class, () -> { generate(TEST_FILENAME, NUMBER_OF_SENTENCES); });
	}

	@Test
	public void generatePeterRabbit() {

		final String TEST_FILENAME = "PeterRabbit.txt";
		final int NUMBER_OF_SENTENCES = 20;

		try {
			assertEquals(NUMBER_OF_SENTENCES, generate(TEST_FILENAME, NUMBER_OF_SENTENCES));
		} catch (AlgorithmBrokenException e) {
			fail();
		}
	}

	@Test
	public void generatePi() {
		final String TEST_FILENAME = "pi.txt";
		final int NUMBER_OF_SENTENCES = 20;
		assertThrows(AlgorithmBrokenException.class, () -> { generate(TEST_FILENAME, NUMBER_OF_SENTENCES); });
	}

	private int generate(String testFilenam, int numberOfSentences) throws AlgorithmBrokenException {

		final int PREFIX_LEN = 10;
		final int SUFFIX_LEN = 2;

		URL res = getClass().getClassLoader().getResource(testFilenam);

		Path path = null;

		try {
			path = Paths.get(res.toURI());
		} catch (URISyntaxException e) {
			fail();
		}

		byte[] bytes = null;

		try {
			bytes = Files.readAllBytes(path);
		}
		catch (final IOException e) {
			fail();
		}

		String sampleText = new String(bytes);

		MarkovChain markovChain = new MarkovChain(PREFIX_LEN, SUFFIX_LEN, sampleText);
		var generatedText = markovChain.generate(numberOfSentences);

		return SentenceUtils.countSentences(generatedText);
	}
}
