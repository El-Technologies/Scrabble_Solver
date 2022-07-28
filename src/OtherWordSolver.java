import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("rawtypes")
public class OtherWordSolver {
    private static Scanner reader;
    private static Map<Character, Integer> letterPoints;
    private static String letters, orders, choice;
    private static boolean validInput, greaterOrEqual;

    public static void main(String[] args) throws IOException {
        File words = new File("src/data/words in capital.txt");

        Scanner sc = new Scanner(System.in);
        reader = new Scanner(words);

        letterPoints = new HashMap<>();

        ScrabbleSolver.assignPointValuesToLetters(letterPoints);

        System.out.print("Enter a bunch of letters: ");
        letters = sc.nextLine().toUpperCase(Locale.ROOT);

        System.out.print("\nEnter orders separated by whitespaces (orders are the lengths of words you want)\n" +
                "press Enter if there's none: ");
        orders = sc.nextLine();

        greaterOrEqual = false;
        if (orders.length() == 1) {
            validInput = true;
            do {
                enterChoice(sc);
            } while (!validInput);

        }

        displayResult();

    }

    private static void enterChoice(Scanner sc) {
        System.out.println("\nEnter \"1\" if you want words with exactly " + orders + " letters or \"2\" if you want words with " + orders + " or more letters");

        choice = sc.nextLine();
        if (!choice.equals("1") & !choice.equals("2")) {
            System.out.println("Invalid input");
            validInput = false;
        } else {
            greaterOrEqual = choice.equals("2");
            validInput = true;
        }
    }

    private static void displayResult() {

        if (!orders.isEmpty()) {
            List<String> ordersInStringArray = new ArrayList<>(Arrays.asList(orders.split(" ")));

            int[] ordersInInt = new int[ordersInStringArray.size()];

            for (int i = 0; i < ordersInStringArray.size(); i++) {
                ordersInInt[i] = Integer.parseInt(ordersInStringArray.get(i));
            }

            if (letters.length() > 1) {
                Map<Character, Integer> countLetters = ScrabbleSolver.getCharacterCount(letters);
                List<List> LetterToPointPairList = new ArrayList<>();

                while (reader.hasNext()) {
                    String currentWord = reader.nextLine();
                    Map<Character, Integer> currentWordMap = ScrabbleSolver.getCharacterCount(currentWord);

                    boolean canMakeCurrentWord = true;
                    for (Character character : currentWordMap.keySet()) {
                        int currentWordCharCount = currentWordMap.get(character);
                        int lettersCharCount = countLetters.getOrDefault(character, 0);

                        if (currentWordCharCount > lettersCharCount) {
                            canMakeCurrentWord = false;
                            break;
                        }
                    }

                    if (canMakeCurrentWord) {
                        List<Object> LetterToPointPair = new ArrayList<>();
                        LetterToPointPair.add(currentWord);
                        LetterToPointPair.add(ScrabbleSolver.getMinimumPointForWord(currentWord, letterPoints));
                        LetterToPointPairList.add(LetterToPointPair);
                    }
                }
                System.out.println("\nWords:");

                for (List pair : LetterToPointPairList) {
                    for (int j : ordersInInt) {
                        if (greaterOrEqual) {
                            if (String.valueOf(pair.get(0)).length() >= j) System.out.println(pair.get(0));
                        } else {
                            if (String.valueOf(pair.get(0)).length() == j) System.out.println(pair.get(0));
                        }
                    }
                }

            } else System.out.println("Number of letters is too short");
        } else {
            if (letters.length() > 1) {
                Map<Character, Integer> countLetters = ScrabbleSolver.getCharacterCount(letters);
                List<List> LetterToPointPairList = new ArrayList<>();

                while (reader.hasNext()) {
                    String currentWord = reader.nextLine();
                    Map<Character, Integer> currentWordMap = ScrabbleSolver.getCharacterCount(currentWord);

                    boolean canMakeCurrentWord = true;
                    for (Character character : currentWordMap.keySet()) {
                        int currentWordCharCount = currentWordMap.get(character);
                        int lettersCharCount = countLetters.getOrDefault(character, 0);

                        if (currentWordCharCount > lettersCharCount) {
                            canMakeCurrentWord = false;
                            break;
                        }
                    }

                    if (canMakeCurrentWord) {
                        List<Object> LetterToPointPair = new ArrayList<>();
                        LetterToPointPair.add(currentWord);
                        LetterToPointPair.add(ScrabbleSolver.getMinimumPointForWord(currentWord, letterPoints));
                        LetterToPointPairList.add(LetterToPointPair);
                    }
                }
                System.out.println("\nWords:");

                for (List pair : LetterToPointPairList) {
                    System.out.println(pair.get(0));
                }

            } else System.out.println("Number of letters is too short");
        }


    }
}
