import java.util.*;
import java.io.*;

class Word {
    // stores probabilities for each word
    public static HashMap<List<String>, ArrayList<String>> words = new HashMap<List<String>, ArrayList<String>>();

    public static Scanner inp = new Scanner(System.in);
 
    public static void main(String args[]) {
        ArrayList<String> lines = new ArrayList<String>();
 
        // get lines of text from training file
        File f = new File("C:\\Users\\T410\\Desktop\\Word Chains\\training\\formatted.txt");
        Scanner scan = null;
  
        try {
            scan = new Scanner(f);
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        // number of words in group
        int n = 1;

        // train on all lines
        for (int i = 0; i < lines.size(); i++) {
            train(lines.get(i), n);
        }

        // generate text
        while (true) {
            if (inp.hasNextLine()) {
                String s = inp.nextLine();
            }
            markov(n, 200);
        }
   }
 
    // train on a given line of text; populate global hashmap words
    public static void train(String training, int n) {
    	// split line by words
    	List<String> wordArray = Arrays.asList(training.split(" "));

        // get collections of n number of words and their following word
        for (int i = 0; i < wordArray.size() - n; i++) {
 
            // get words 
            List<String> wordCollection = wordArray.subList(i, i + n);

            // if ngram has not already been logged
            if (words.get(wordCollection) == null) {
                // add to words
                words.put(wordCollection, new ArrayList<String>());
            }
 
            // map following word collection to following word
            words.get(wordCollection).add(wordArray.get(i + n));
        }
    }
 
    // generate text similar to training data, using an order of n
    public static void markov(int n, int amount) {
 
        // get random collection of words from existing to start off generation
        List<List<String>> keys = new ArrayList<List<String>>(words.keySet());

        // get random word collection to start
        List<String> random = keys.get((int) Math.floor(Math.random() * (keys.size())));

        // initialize result, arraylist of word strings
        ArrayList<String> result = new ArrayList<String>();

        // add random to result
        for (int i = 0; i < random.size(); i++) {
        	result.add(random.get(i));
        }

        // generate the given amount of words
        for (int i = 0; i < amount; i++) {

            // get the n last words in result
            List<String> currentWords = new ArrayList<String>();
            for (int word = result.size() - n; word < result.size(); word++) {
            	currentWords.add(result.get(word));
            }

            // if no existing word
            if (words.get(currentWords) == null) {
                // get similar word / words
            	currentWords = getSimilar(currentWords, words);
            }

            // get possible following words
            ArrayList<String> possible = words.get(currentWords);

            // choose random word, add to result
            result.add(possible.get((int) Math.floor(Math.random() * possible.size())));
        }
 
        System.out.println(String.join(" ", result));
    }

    // get a gram that exists in the probabilites map and is similar to the given gram
    private static List<String> getSimilar(List<String> s, HashMap<List<String>, ArrayList<String>> probabilities) {

        List<List<String>> keys = new ArrayList<List<String>>(probabilities.keySet());

        // best match
        List<String> best = keys.get(0);
        // number of word matches between s and best
        int bestMatches = 0;
  
  
        // for every gram
        for (int g = 0; g < keys.size(); g++) {
            // number of word matches between keys
            int matching = 0;
            // get next word list
            List<String> wordList = keys.get(g);
  
            // for every word in both lists
            for (int w = 0; w < s.size(); w++) {
                // if word matches, increment matching
                if (s.get(w).equals(wordList.get(w))) {
                    matching++;
                }
            }
  
            // get best
            if (matching > bestMatches) {
                best = wordList;
                bestMatches = matching;
            }
        }
  
        return best;
    }
}