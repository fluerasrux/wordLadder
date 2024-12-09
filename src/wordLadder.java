//import java.io.*;
import java.util.*;

public class wordLadder {

    private WordLinker wordLinker;
    private Scanner scanner;

    public wordLadder() {
        wordLinker = new WordLinker();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        wordLadder app = new wordLadder();
        app.run();
    }

    public void run() {
        System.out.println("Welcome to the Word Ladder app!");
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. See neighbors of a word");
            System.out.println("2. Construct a random walk");
            System.out.println("3. Find shortest path between two words");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showNeighbors();
                    break;
                case 2:
                    performRandomWalk();
                    break;
                case 3:
                    findShortestPath();
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Todo1
    private void showNeighbors() {
        System.out.println("Enter a word:");
        String word = scanner.nextLine();
        if (wordLinker.isWord(word)) {
            List<String> neighbors = wordLinker.getNeighbors(word);
            if (neighbors.isEmpty()) {
                System.out.println("No neighbors found.");
            } else {
                System.out.println("Neighbors of '" + word + "': " + String.join(", ", neighbors));
            }
        } else {
            System.out.println("Word not found in dictionary.");
        }
    }

    // Todo2
    private void performRandomWalk() {
        System.out.println("Enter the start word:");
        String startWord = scanner.nextLine();
        System.out.println("Enter the target length for the walk:");
        int length = scanner.nextInt();
        scanner.nextLine();  // consume newline

        if (wordLinker.isWord(startWord)) {
            List<String> walk = randomWalk(startWord, length);
            System.out.println("Random walk: " + String.join(" --> ", walk));
        } else {
            System.out.println("Start word not found in dictionary.");
        }
    }

    private List<String> randomWalk(String startWord, int targetLength) {
        List<String> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        String currentWord = startWord;

        path.add(currentWord);
        visited.add(currentWord);

        Random rand = new Random();

        for (int i = 1; i < targetLength; i++) {
            List<String> neighbors = wordLinker.getNeighbors(currentWord);
            neighbors.removeAll(visited);

            if (neighbors.isEmpty()) {
                System.out.println("No more unvisited neighbors, stopping early.");
                break;
            }

            currentWord = neighbors.get(rand.nextInt(neighbors.size()));
            path.add(currentWord);
            visited.add(currentWord);
        }

        return path;
    }



    // Todo3
    private void findShortestPath() {
        System.out.println("Enter the start word:");
        String startWord = scanner.nextLine();
        System.out.println("Enter the end word:");
        String endWord = scanner.nextLine();

        if (wordLinker.isWord(startWord) && wordLinker.isWord(endWord)) {
            List<String> path = bfs(startWord, endWord);
            if (path != null) {
                System.out.println("Shortest path: " + String.join(" --> ", path));
            } else {
                System.out.println("No path found between '" + startWord + "' and '" + endWord + "'.");
            }
        } else {
            System.out.println("One or both words are not in the dictionary.");
        }
    }


    public List<String> bfs(String startWord, String endWord) {
        if (!wordLinker.isWord(startWord) || !wordLinker.isWord(endWord)) {
            System.out.println("One or both words are not in the dictionary.");
            return null;
        }
        // BFS
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        List<String> startPath = new ArrayList<>();
        startPath.add(startWord);
        queue.add(startPath);
        visited.add(startWord);

        while (!queue.isEmpty()) {
            List<String> currentPath = queue.poll();
            String currentWord = currentPath.get(currentPath.size() - 1);

            if (currentWord.equals(endWord)) {
                return currentPath;
            }
            List<String> neighbors = wordLinker.getNeighbors(currentWord);

            neighbors.sort((a, b) -> compareByLetterProximity(a, b, endWord));

            // Explore each neighbor
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        System.out.println("No path found.");
        return null;
    }

    private int compareByLetterProximity(String word1, String word2, String targetWord) {
        int matchingLettersWord1 = countMatchingLetters(word1, targetWord);
        int matchingLettersWord2 = countMatchingLetters(word2, targetWord);

        return Integer.compare(matchingLettersWord2, matchingLettersWord1);
    }

    private int countMatchingLetters(String word, String targetWord) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == targetWord.charAt(i)) {
                count++;
            }
        }
        return count;
    }

}
    