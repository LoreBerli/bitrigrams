import java.awt.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Bamba on 09/12/2016.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        //parallelMain();
        sequentialMain();
    }


    public static void parallelMain() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));

        ExecutorService exe = Executors.newFixedThreadPool(4);
        String line;
        while(( line = reader.readLine())!= null ){
            exe.execute(new MediatorTask(5, line));
        }
        //exe.shutdown();
    }

    public static void sequentialMain() throws Exception{
        int pageNotRetrieved = 0;

        BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));
        String line;

        System.out.println("Start...");
        long sstime = System.nanoTime();
        while(( line = reader.readLine())!= null ){
            try{
                List<String> text = TextRetriever.wikiTextByWord(line);
                ConsumerDummy consumerDummy = new ConsumerDummy(2);
                consumerDummy.consume(text);
            }catch(Exception e){
                pageNotRetrieved++;
            }


        }
        long sselap = System.nanoTime() - sstime;

        System.out.println((float) sselap /1000000000);
        System.out.println("Pages not retrieved " + pageNotRetrieved);
    }



}


