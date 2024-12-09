import java.io.*;
import java.util.*;

public class WordLinker {

    private static String DEFAULT_DICTIONARY_FILE = "wordList.txt";
    private static String DEFAULT_START_WORD = "a";

    private Set<String> allWords;
    private Set<String> candidateWords;
    private String start_word;

    public WordLinker() {
        allWords = wordSetFromFile(DEFAULT_DICTIONARY_FILE);
        setStartWord(DEFAULT_START_WORD);
    }

    /*
    reads a file organized as one word per line into a set
     */
    private Set<String> wordSetFromFile(String filename) {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            System.out.printf("WordLinker.setFromFile reading data file: '%s'\n", filename);
            String word;
            while ((word = br.readLine()) != null) {
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public int getNumWords() {
        return allWords.size();
    }

    public int getNumCandidates() {
        return candidateWords.size();
    }

    public boolean isWord(String w) {
        return allWords.contains(w);
    }

    public boolean isCandidate(String w) {
        return candidateWords.contains(w);
    }

    /*
    candidates are all words with same length as start_word
     */
    public void setStartWord(String start_word) {
        candidateWords = new HashSet<>();
        if (isWord(start_word)) {
            int target_word_length = start_word.length();
            for (String word : allWords) {
                if (word.length() == target_word_length) {
                    candidateWords.add(word);
                }
            }
        } else {
            System.out.printf("%s is not in dictionary\n", start_word);
        }
    }

    public List<String> getNeighbors(String w) {
        List<String> neighbors = new ArrayList<>();
        setStartWord(w);
        for (int ndxOfLtrToChange = 0; ndxOfLtrToChange < w.length(); ndxOfLtrToChange++) {
            String w_prefix = w.substring(0, ndxOfLtrToChange);
            String w_suffix = w.substring(ndxOfLtrToChange + 1, w.length());
            for (String candidate : candidateWords) {
                String o_prefix = candidate.substring(0, ndxOfLtrToChange);
                String o_suffix = candidate.substring(ndxOfLtrToChange + 1, candidate.length());
                if (!w.equals(candidate) && w_prefix.equals(o_prefix) && w_suffix.equals(o_suffix)) {
                    // System.out.printf("w_prefix: %s; w_suffix: %s\no_prefix: %s; o_suffix: %s\n", w_prefix, w_suffix, o_prefix, o_suffix);
                    neighbors.add(candidate);
                }
            }
        }
        return neighbors;
    }
}


