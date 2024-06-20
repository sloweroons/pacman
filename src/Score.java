import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Score {
    public static void recordScore(double score, ArrayList<Double> scores) throws IOException {
        FileWriter writer = new FileWriter("scores.txt");
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());
        if (scores.size() > 10) {
            for (int i = 10; i < scores.size(); ++i) {
                scores.remove(i);
            }
        }
        for (int i = 0; i < scores.size(); i++) {
            writer.write(i+1+"\t"+scores.get(i)+"\n");
        }
        writer.close();
    }
    public static ArrayList<Double> readScores() throws FileNotFoundException {
        ArrayList<Double> scores = new ArrayList<>();
        Scanner scanner = new Scanner(new File("scores.txt"));
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            String[] segment = data.split("\t");
            data = segment[1];
            scores.add(Double.parseDouble(data));
        }
        scanner.close();
        return scores;
    }
}
