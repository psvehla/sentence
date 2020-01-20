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
	void generateSentences() {

		final String TEST_FILENAME = "alice_oz.txt";
		final int NUMBER_OF_SENTENCES = 3;

		var name = TEST_FILENAME;
		var originalFileName = TEST_FILENAME;
		var contentType = "text/plain";

		URL res = getClass().getClassLoader().getResource(TEST_FILENAME);

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
		testReq.setNumberOfSentences(NUMBER_OF_SENTENCES);
		testReq.setSelectedFile(file);

		SentenceGenerator sentenceGenerator = new SentenceGenerator();
		String response = sentenceGenerator.generateSentences(testReq, file);
		assertEquals(NUMBER_OF_SENTENCES, SentenceUtils.countSentences(response));
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
