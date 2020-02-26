/**
 *
 */
package au.com.redbarn.animal.sentence.errors;

/**
 * Thrown when the Markov algorithm somehow gets itself into an illegal state.
 *
 * @author peter
 *
 */
public class AlgorithmBrokenException extends Exception {

	private static final long serialVersionUID = -2107709992881966953L;

	public AlgorithmBrokenException(String message) {
		super(message);
	}
}
