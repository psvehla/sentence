/**
 *
 */
package au.com.redbarn.animal.sentence.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * Holds the POST data from a request to generate sentences.
 *
 * @author peter
 *
 */
@Data
public class SentenceGenerationRequest {
	MultipartFile selectedFile;
    int prefixLen;
    int suffixLen;
    int numberOfSentences;
}
