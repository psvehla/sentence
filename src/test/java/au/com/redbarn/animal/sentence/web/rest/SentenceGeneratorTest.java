/**
 *
 */
package au.com.redbarn.animal.sentence.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import au.com.redbarn.animal.sentence.domain.SentenceGenerationRequest;
import au.com.redbarn.animal.sentence.utils.SentenceUtils;

/**
 * Tests for {@link SentenceGenerator}.
 *
 * @author peter
 *
 */
@SpringBootTest
public class SentenceGeneratorTest {

	@Test
	void generateAliceOzSentences() {
		final String TEST_FILENAME = "alice_oz.txt";
		final int NUMBER_OF_SENTENCES = 3;
		assertEquals(NUMBER_OF_SENTENCES, generateSentences(TEST_FILENAME, NUMBER_OF_SENTENCES));
	}

	@Test
	void generateNerdSentences() {
		final String TEST_FILENAME = "nerd.txt";
		final int NUMBER_OF_SENTENCES = 3;
		final int ERROR_MESSAGE_NUMBER_OF_SENTENCES = 2;
		assertEquals(ERROR_MESSAGE_NUMBER_OF_SENTENCES, generateSentences(TEST_FILENAME, NUMBER_OF_SENTENCES));
	}

	@Test
	void generatePeterRabbitSentences() {
		final String TEST_FILENAME = "PeterRabbit.txt";
		final int NUMBER_OF_SENTENCES = 3;
		assertEquals(NUMBER_OF_SENTENCES, generateSentences(TEST_FILENAME, NUMBER_OF_SENTENCES));
	}

	@Test
	void generatePiSentences() {
		final String TEST_FILENAME = "pi.txt";
		final int NUMBER_OF_SENTENCES = 3;
		final int ERROR_MESSAGE_NUMBER_OF_SENTENCES = 2;
		assertEquals(ERROR_MESSAGE_NUMBER_OF_SENTENCES, generateSentences(TEST_FILENAME, NUMBER_OF_SENTENCES));
	}

	private int generateSentences(String testFilename, int numberOfSentences) {

		var name = testFilename;
		var originalFileName = testFilename;
		var contentType = "text/plain";

		URL res = getClass().getClassLoader().getResource(testFilename);

		Path path = null;

		try {
			path = Paths.get(res.toURI());
		} catch (URISyntaxException e) {
			fail();
		}

		byte[] content = null;

		try {
		    content = Files.readAllBytes(path);
		}
		catch (final IOException e) {
			fail();
		}

		MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);

		var testReq = new SentenceGenerationRequest();
		testReq.setPrefixLen(1);
		testReq.setSuffixLen(2);
		testReq.setNumberOfSentences(numberOfSentences);
		testReq.setSelectedFile(file);

		SentenceGenerator sentenceGenerator = new SentenceGenerator();
		String response = sentenceGenerator.generateSentences(testReq, file);

		return SentenceUtils.countSentences(response);
	}

	@Test
	void generateSentencesNullFile() {

		var testReq = new SentenceGenerationRequest();
		testReq.setPrefixLen(1);
		testReq.setSuffixLen(2);
		testReq.setNumberOfSentences(3);
		testReq.setSelectedFile(null);

		SentenceGenerator sentenceGenerator = new SentenceGenerator();
		String response = sentenceGenerator.generateSentences(testReq, null);
		assertEquals("Please give me some text to work with.", response);
	}
}
