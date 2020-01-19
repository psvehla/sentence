package au.com.redbarn.animal.sentence.utils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

public class SentenceUtils {

	private SentenceUtils() { }

	/**
	 * Counts the number of distinct sentences in a passage of text.
	 *
	 * @param text The text to count the sentences in.
	 * @return The number of sentences.
	 */
	public static int countSentences(String text) {

		List<String> sentences = new ArrayList<>();

		BreakIterator boundary = BreakIterator.getSentenceInstance(new Locale("en_AU"));
		boundary.setText(text);
		int start = boundary.first();

		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			sentences.add(text.substring(start, end));
	     }

		return sentences.size();
	}
}
