package au.com.redbarn.animal.sentence.utils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A collection of utilities related to the processing and manipulation of sentences in text.
 *
 * @author peter
 *
 */
public class SentenceUtils {

	private SentenceUtils() {
		throw new IllegalStateException("This is a utility class containing only static methods. Do not instantiate.");
	}

	/**
	 * Counts the number of distinct sentences in a passage of text.
	 *
	 * @param text The text containing the sentences to count.
	 * @return The number of sentences.
	 */
	public static int countSentences(String text) {
		List<String> sentences = split(text);
		return sentences.size();
	}

	/**
	 * Trims sentence fragments from the start and end of a passage of text.
	 *
	 * @param text The passage of text.
	 * @return The trimmed passage of text.
	 */
	public static String trim(String text) {

		List<String> sentences = split(text);
		sentences.remove(sentences.size() - 1);
		sentences.remove(0);

		return sentences.stream().collect(Collectors.joining(" "));
	}

	/**
	 * Splits a passage of text into a list of sentences.
	 *
	 * @param text The passage of text.
	 * @return A list of sentences from the text.
	 */
	private static List<String> split(String text) {

		List<String> sentences = new ArrayList<>();

		BreakIterator boundary = BreakIterator.getSentenceInstance(new Locale("en_AU"));
		boundary.setText(text);
		int start = boundary.first();

		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			sentences.add(text.substring(start, end));
		}

		return sentences;
	}
}
