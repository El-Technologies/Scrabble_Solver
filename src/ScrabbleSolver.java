import java.io.*;
import java.util.*;

@SuppressWarnings({"rawtypes", "InfiniteLoopStatement"})
public class ScrabbleSolver {
    public static void main(String[] args) throws FileNotFoundException {

        do {
            File words = new File("src/data/dictionary");
            Scanner reader = new Scanner(words), sc = new Scanner(System.in);
            Map<Character, Integer> letterPoints = new HashMap<>();

            assignPointValuesToLetters(letterPoints);
            System.out.print("Enter a bunch of letters: ");
            String letters = sc.nextLine().toUpperCase(Locale.ROOT);

            if (letters.length() > 1) printWords(reader, letters, letterPoints);
            else System.out.println("Number of letters is too short");

            System.out.println("\n\n");
        } while (true);

    }

    //this method contains things to be displayed onto the console
    public static void printWords(Scanner reader, String letters, Map<Character, Integer> letterPoints) {
        Map<Character, Integer> countLetters = getCharacterCount(letters);
        int numberOfWordsMade = 0;
        List<List> LetterToPointPairList = new ArrayList<>();

        //check the txt file of Scrabble words
        while (reader.hasNext()) {
            String currentWord = reader.nextLine();
            Map<Character, Integer> currentWordMap = getCharacterCount(currentWord);

            //check if the current word in the Scrabble dictionary can be formed with the set of letters inputted
            boolean canMakeCurrentWord = true;
            for (Character character : currentWordMap.keySet()) {
                int currentWordCharCount = currentWordMap.get(character);
                int lettersCharCount = countLetters.getOrDefault(character, 0);

                if (currentWordCharCount > lettersCharCount) {
                    canMakeCurrentWord = false;
                    break;
                }
            }

            /*if the word can be made with the letters inputted
            a pair consisting of the word and the score of the word is added to a list
            of all the words that can be made with those inputted letters*/
            if (canMakeCurrentWord) {
                List<Object> LetterToPointPair = new ArrayList<>();
                LetterToPointPair.add(currentWord);
                LetterToPointPair.add(getMinimumPointForWord(currentWord, letterPoints));

                LetterToPointPairList.add(LetterToPointPair);

                numberOfWordsMade++;
            }
        }

        //all the words that can be made are displayed...
        System.out.println("\nWords (" + numberOfWordsMade + " in number) :");

        for (List pair : LetterToPointPairList) System.out.println(pair.get(0) + " - " + pair.get(1));

        //the longest word(s) are displayed
        System.out.println("\nLongest Word(s):");
        for (List pair : getLongestWord(LetterToPointPairList)) {
            System.out.println(pair.get(0));
        }

        List<List> wordsWithMaxPoints = getWordsWithMaxPoint(LetterToPointPairList);

        /*if there are words with the maximum minimum points, they are also displayed
        * by maximum minimum points, I mean the word(s) with the highest score
        * without being placed on a DL, DW, TL or TW*/
        if (LetterToPointPairList.size() > wordsWithMaxPoints.size()) {
            if (wordsWithMaxPoints.size() > 1) {
                System.out.println("\nWords with the maximum minimum points (" + wordsWithMaxPoints.get(0).get(1) + " points):");
                for (List pair : wordsWithMaxPoints) System.out.println(pair.get(0));
            } else
                System.out.println("\nWord with the maximum minimum points (" + wordsWithMaxPoints.get(0).get(1) + " points): "
                        + wordsWithMaxPoints.get(0).get(0));
        }

        List<List> bingoWords = new ArrayList<>();
        List<List> longWords = new ArrayList<>();

        /*the list of words are checked to see if there are any bingo words or even longer words
        * if there are, they are stored*/
        boolean containsBingo = false, containsLongWord = false;
        for (List pair : LetterToPointPairList) {
            if ((String.valueOf(pair.get(0)).length() >= 7)) {
                if (String.valueOf(pair.get(0)).length() == 7) {
                    bingoWords.add(pair);
                    containsBingo = true;
                } else {
                    longWords.add(pair);
                    containsLongWord = true;
                }
            }
        }

        /*if there are bingo words, they are displayed
        * along with the bingo words that have the maximum minimum points*/
        if (containsBingo) {

            if (bingoWords.size() > 1) {
                System.out.println("\nBingo:");
                for (List pair : bingoWords) System.out.println(pair.get(0));

                List<List> bingoWordsWithMaxPoints = getWordsWithMaxPoint(bingoWords);

                System.out.print((bingoWordsWithMaxPoints.size() > 1) ? "\nBingo words with maximum minimum points:" :
                        "\nBingo word with maximum minimum points:");
                System.out.println("(" + bingoWordsWithMaxPoints.get(0).get(1) + ")");

                for (List pair : bingoWordsWithMaxPoints) {
                    System.out.println(pair.get(0));
                }
            } else {
                System.out.println("\nBingo word (" + bingoWords.get(0).get(1) + ") points:");
                System.out.println(bingoWords.get(0).get(0));
            }

        }

        //the same thing is done with the longer words
        if (containsLongWord) {

            if (longWords.size() > 1) {
                System.out.println("\nLonger words:");
                for (List pair : longWords) System.out.println(pair.get(0));

                List<List> longWordsWithMaxPoints = getWordsWithMaxPoint(longWords);

                System.out.print((longWordsWithMaxPoints.size() > 1) ? "\nLonger words with maximum minimum points:" :
                        "\nLonger word with maximum minimum points:");
                System.out.println("(" + longWordsWithMaxPoints.get(0).get(1) + ")");

                for (List pair : longWordsWithMaxPoints) {
                    System.out.println(pair.get(0));
                }
            } else {
                System.out.println("\nLonger word (" + longWords.get(0).get(1) + ") points:");
                System.out.println(longWords.get(0).get(0));
            }
        }
    }

    //this returns how many times a letter appears in the entered string
    public static Map<Character, Integer> getCharacterCount(String letters) {
        Map<Character, Integer> countLetters = new HashMap<>();

        for (int i = 0; i < letters.length(); i++) {
            char currentLetter = letters.charAt(i);
            int count = countLetters.getOrDefault(currentLetter, 0);
            countLetters.put(currentLetter, count + 1);
        }

        return countLetters;
    }

    //this maps the Scrabble values to each letter eg: Z = 10
    public static void assignPointValuesToLetters(Map<Character, Integer> letterPoints) throws FileNotFoundException {
        char[] lettersFromAToZ = new char[26];
        Scanner AtoZ = new Scanner(new File("src/data/AtoZ"));

        for (int i = 0; i < 26; i++) {
            lettersFromAToZ[i] = AtoZ.nextLine().charAt(0);

            switch (lettersFromAToZ[i]) {
                case 'A', 'E', 'I', 'O', 'U', 'L', 'N', 'S', 'T', 'R' -> letterPoints.put(lettersFromAToZ[i], 1);
                case 'D', 'G' -> letterPoints.put(lettersFromAToZ[i], 2);
                case 'B', 'C', 'M', 'P' -> letterPoints.put(lettersFromAToZ[i], 3);
                case 'F', 'H', 'V', 'W', 'Y' -> letterPoints.put(lettersFromAToZ[i], 4);
                case 'K' -> letterPoints.put(lettersFromAToZ[i], 5);
                case 'J', 'X' -> letterPoints.put(lettersFromAToZ[i], 8);
                case 'Q', 'Z' -> letterPoints.put(lettersFromAToZ[i], 10);
            }
        }
    }

    //this returns the minimum score of a word if there are no DL, DW, TL or TW
    public static int getMinimumPointForWord(String word, Map<Character, Integer> letterPoints) {
        int sum = 0;
        for (char c : word.toCharArray()) sum += letterPoints.get(c);

        return sum;
    }

    //this returns a list of words with the highest minimum score
    private static List<List> getWordsWithMaxPoint(List<List> words) {
        List<List> wordsWithMaxPoints = new ArrayList<>();
        int points, maxPoint = 0;

        for (List pair : words) {
            points = Integer.parseInt(String.valueOf(pair.get(1)));
            if (points > maxPoint) maxPoint = points;
        }

        for (List pair : words)
            if (Integer.parseInt(String.valueOf(pair.get(1))) == maxPoint) wordsWithMaxPoints.add(pair);

        return wordsWithMaxPoints;
    }

    //this returns the longest word(s) in a given list
    private static List<List> getLongestWord(List<List> words) {
        List<List> wordsWithLongestLength = new ArrayList<>();
        int length, longestLength = 0;

        for (List pair : words) {
            length = String.valueOf(pair.get(0)).length();
            if (length > longestLength) longestLength = length;
        }

        for (List pair : words)
            if (String.valueOf(pair.get(0)).length() == longestLength) wordsWithLongestLength.add(pair);

        return wordsWithLongestLength;
    }
}