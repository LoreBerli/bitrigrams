import java.awt.*;
import java.io.LineNumberReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Bamba on 09/12/2016.
 */

public class Main {
    private final static String filename = "config.txt";
    private final static int numOfthreads = 16;


    public static void main(String[] args) throws Exception {
        RecordHashMap r1 = parallelMain();
        RecordHashMap r2 = sequentialMain();

        System.out.println("Sono " + (r2.equals(r1)? " uguali" : "diverse"));
    }


    public static RecordHashMap parallelMain() throws Exception{
        //get number of lines
        LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
        lnr.skip(Long.MAX_VALUE);
        int lnnum = lnr.getLineNumber()+1;

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        ExecutorService exe = Executors.newFixedThreadPool(numOfthreads);

        MediatorTask.cdl = new CountDownLatch(lnnum*(numOfthreads+1)); // # of lines * (k+1)

        System.out.println("Start parallel");
        String line;
        long pstime = System.nanoTime();
        while(( line = reader.readLine())!= null ){
            MediatorTask m = new MediatorTask(numOfthreads, line, exe);
            exe.execute(m);
        }
        try{
            MediatorTask.cdl.await();
        }catch (Exception e){}

        long pelap = System.nanoTime() - pstime;
        System.out.println((float) pelap /1000000000);
        System.out.println(MediatorTask.recs);

        exe.shutdown();
        return MediatorTask.recs;
    }

    public static RecordHashMap sequentialMain() throws Exception{
        int pageNotRetrieved = 0;
        RecordHashMap result = new RecordHashMap();

        BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));

        System.out.println("Start sequential");
        String line;
        long sstime = System.nanoTime();
        while(( line = reader.readLine())!= null ){
            try{
                List<String> text = TextRetriever.wikiTextByWord(line);
                NgramTask ngramTask = new NgramTask(text, 2, result);
                ngramTask.buildHistogram(text);
            }catch(Exception e){
                pageNotRetrieved++;
            }


        }
        long sselap = System.nanoTime() - sstime;

        System.out.println((float) sselap /1000000000);
        System.out.println("Pages not retrieved " + pageNotRetrieved);
        return result;
    }



}


