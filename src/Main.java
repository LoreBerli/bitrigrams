import java.io.BufferedWriter;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Bamba on 09/12/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TextRetriever textRetriever = new TextRetriever("https://en.wikipedia.org/wiki/N-gram");

        File file = new File("file.html");
        BufferedWriter bW = new BufferedWriter(new FileWriter("file.html"));

        String line;
        int i = 0;
        while((line = textRetriever.readLine()) != null){
            System.out.println(i++);
            bW.append(line);
        }

    }

}
