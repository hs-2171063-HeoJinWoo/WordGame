import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class WordVector {
    private Vector<String> wVector = new Vector<String>();
    private Scanner scanner = null;

    public WordVector() {
        try {
            scanner = new Scanner(new FileReader("words.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            wVector.add(word);
        }
    }

    public String getWord() {
        int idx = (int)(Math.random() * wVector.size());
        return wVector.get(idx);
    }
}
