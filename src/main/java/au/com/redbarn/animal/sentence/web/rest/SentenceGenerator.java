/**
 *
 */
package au.com.redbarn.animal.sentence.web.rest;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import au.com.redbarn.animal.sentence.domain.SentenceGenerationRequest;
import au.com.redbarn.animal.sentence.generator.MarkovChain;
import lombok.extern.slf4j.Slf4j;

/**
 * Generates sentences based on provided text.
 *
 * A Markov chain is generated from the uploaded text, which is then used to generate sentences.
 *
 * @author peter
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@ResponseStatus(HttpStatus.OK)
@Slf4j
public class SentenceGenerator {

	@PostMapping("/generate")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateSentences(@ModelAttribute SentenceGenerationRequest req, @RequestPart(name = "file", required = false) MultipartFile file) {

		if (log.isDebugEnabled()) {
			log.debug("Call parameters:");
			log.debug("prefixLen: " + req.getPrefixLen());
			log.debug("suffixLen: " + req.getSuffixLen());
			log.debug("numberOfSentences: " + req.getNumberOfSentences());
		}

		if (file == null) {
			log.warn("File is null!");
		}

		if (file == null || file.isEmpty()) {
			return "Please give me some text to work with.";
		}

		try {
			byte[] bytes = file.getBytes();
			String sampleText = new String(bytes);

			if (log.isDebugEnabled()) {
				log.debug("sampleText string length: " + sampleText.length());
			}

			MarkovChain markovChain = new MarkovChain(req.getPrefixLen(), req.getSuffixLen(), sampleText);
			return markovChain.generate(req.getNumberOfSentences());
		} catch (IOException e) {
			log.error("Could not read input file.", e);
			return "I could not read the file you sent me, please try another. The file must be in plain text.";
		}
	}
}
