package au.com.redbarn.animal.sentence.generator;

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

import au.com.redbarn.animal.sentence.utils.SentenceUtils;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MarkovChainTest {

	@Test
	public void generate() {

		final String TEST_FILENAME = "alice_oz.txt";
		final int PREFIX_LEN = 10;
		final int SUFFIX_LEN = 2;
		final int NUMBER_OF_SENTENCES = 20;

		URL res = getClass().getClassLoader().getResource(TEST_FILENAME);

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
		var generatedText = markovChain.generate(NUMBER_OF_SENTENCES);
		assertEquals(NUMBER_OF_SENTENCES, SentenceUtils.countSentences(generatedText));
	}
}
