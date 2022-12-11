import java.io.*;

import java.util.Scanner;
import java.util.Vector;

public class WordVector {
    private Vector<String> wVector = new Vector<String>();
    private Scanner scanner = null;

    private final String BASICFILENAME = "words.txt";

    public WordVector() {
        try {
            scanner = new Scanner(new FileReader(BASICFILENAME));
        } catch (FileNotFoundException basicFileEx) {
            basicFileEx.printStackTrace();
        }

        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            wVector.add(word);
        }
    }
    
    // 랜덤 단어 반환
    public String getWord() {
        int idx = (int)(Math.random() * wVector.size());
        return wVector.get(idx);
    }

    /*
    * 단어 추가에 성공하면 1 반환
    * 이미 있는 단어인 경우 0 반환
    * 빈 단어를 입력하는 경우 -1 반환
    * 파일을 열 수 없는 경우는 -999 반환
    */
    public int addWord(String str) {
        if (wVector.contains(str)) return 0;
        else if (str.length() == 0) return -1;
        else {
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(BASICFILENAME, true));
                wVector.add(str);
                output.write(str);
                output.write("\r\n");
                output.flush();
                return 1;
            } catch (IOException e) { // 파일이 열리지 않는 경우
                return -999;
            }
        }
    }
}
