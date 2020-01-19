package au.com.redbarn.animal.sentence.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import au.com.redbarn.animal.sentence.utils.SentenceUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarkovChain {

	/**
	 * The prefix length to use in the algorithm.
	 */
	private int prefixLen = 5;

	/**
	 * The suffix length to use in the algorithm.
	 */
	private int suffixLen = 1;

	/**
	 * A dictionary that holds the prefixes and corresponding suffixes that comprise the Markov chain.
	 * The key is the prefix.
	 * The value is a list of possible suffixes associated with that prefix.
	 */
	private Map<String, List<String>> dictionary = new HashMap<>();

	private Random random = new Random();

	@SuppressWarnings("unused")	// This is not for use. It's to disable the no argument constructor. So of course it's unused.
	private MarkovChain() {
		throw new IllegalStateException("The no argument constructor has been disabled. Please use MarkovChain(int prefixLen, int suffixLen).");
	}

	/**
	 * Constructor sets up MarkovChain, ready for generation.
	 * Accepts algorithm parameters.
	 * Accepts sample text.
	 * Trains the Markov chain.
	 *
	 * @param prefixLen The prefix length.
	 * @param suffixLen The suffix length.
	 * @param sampleText The sample text.
	 */
	public MarkovChain(int prefixLen, int suffixLen, String sampleText) {
		this.prefixLen = prefixLen;
		this.suffixLen = suffixLen;
		train(sampleText);
	}

	/**
	 * Trains the Markov chain (dictionary) with the sample text.
	 *
	 * @param sampleText The sample text to train the Markov chain with.
	 */
	private void train(String sampleText) {

		List<String> wordList = Arrays.asList(sampleText.split(" "));
		log.debug("Number of words: " + wordList.size());

		for (int i = 0; i < wordList.size() - this.prefixLen - this.suffixLen; i++) {

			var prefixBuilder = new StringBuilder();

			for (int j = i; j < i + this.prefixLen; j++) {
				prefixBuilder.append(wordList.get(j)).append(" ");
			}

			var suffixBuilder = new StringBuilder();

			for (int j = i + this.prefixLen; j < i + this.prefixLen + this.suffixLen; j++) {
				suffixBuilder.append(wordList.get(j)).append(" ");
			}

			var prefix = prefixBuilder.toString().trim();
			var suffix = suffixBuilder.toString().trim();
			addToDictionary(prefix, suffix);
		}
	}

	/**
	 * Adds a prefix - suffix pair to the dictionary.
	 *
	 * @param prefix The prefix to add to the dictionary.
	 * @param suffix The suffix to add to the dictionary.
	 */
	private void addToDictionary(String prefix, String suffix) {
		if (this.dictionary.containsKey(prefix)) {
			this.dictionary.get(prefix).add(suffix);
		}
		else {
			List<String> suffixes = new ArrayList<>();
			suffixes.add(suffix);
			this.dictionary.put(prefix, suffixes);
		}
	}

	/**
	 * Generates the desired number of sentences utilising the Markov chain.
	 *
	 * @param desiredNumberOfSentences The number of desired sentences.
	 * @return The desired sentences.
	 */
	public String generate(int desiredNumberOfSentences) {

		int numberOfSentences = 0;
		var prefix = getRandomPrefix();
		var text = new StringBuilder();
		text.append(prefix).append(" ");

		while (numberOfSentences < desiredNumberOfSentences + 2) {
			// Why desiredNumberOfSentences + 2?
			// The start and end of the generated text are usually fragments, which looks untidy. So I generate two extra sentences and discard them with
			// SentenceUtils.trim(), even if they are full sentences. That way I don't need to detect fragments and I always have the correct number of sentences.
			var suffix = getSuffix(prefix);
			numberOfSentences = SentenceUtils.countSentences(text.toString());
			text.append(suffix).append(" ");
			prefix =  getNewPrefix(text.toString());
		}

		return SentenceUtils.trim(text.toString());
	}

	/**
	 * Gets a random prefix from the dictionary.
	 * Used to start the generated text.
	 *
	 * @return The random prefix.
	 */
	private String getRandomPrefix() {
		List<String> prefixes = new ArrayList<>(this.dictionary.keySet());
		return prefixes.get(this.random.nextInt(prefixes.size()));
	}

	/**
	 * Gets a suffix associated with the given prefix.
	 * As per the algorithm, if there is more than one suffix associated with a prefix, I select one at random.
	 * 
	 * @param prefix The prefix.
	 * @return A suffix associated with the prefix.
	 */
	private String getSuffix(String prefix) {

		List<String> suffixes = this.dictionary.get(prefix.trim());

		if (suffixes.size() == 1) {
			return suffixes.get(0);
		}
		else {
			// get random suffix if there are more than one
			return suffixes.get(this.random.nextInt(suffixes.size()));
		}
	}

	/**
	 * Gets the new prefix from the text being generated.
	 * The new prefix consists of the last prefixLen words in the text.
	 *
	 * @param text The text being generated.
	 * @return The last prefixLen words in the text being generated.
	 */
	private String getNewPrefix(String text) {
		List<String> words = Arrays.asList(text.split(" "));
		List<String> prefixWords = words.subList(words.size() - this.prefixLen, words.size());
		return prefixWords.stream().collect(Collectors.joining(" "));
	}
}
