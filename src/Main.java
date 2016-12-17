import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Bamba on 09/12/2016.
 */

public class Main {
    public final static boolean useCache = true;

    private  static int numOfTries = 50;

    private final static String filename = "wikifiles.txt";
    private final static int numOfthreads = 4;
    private final static int gram = 3;

    private static long timepar = 0;
    private static long timeseq = 0;


    public static void main(String[]args) throws Exception{

        List<Float> speedups = new ArrayList<Float>(numOfTries);
        for(int i = 0; i < numOfTries; i++){
            __main();
            speedups.add(i, new Float((float)timeseq/timepar));
        }

        System.out.println("Speedup medio su " + numOfTries + " tentativi: " + speedups.stream().reduce(0f, (a,b)-> a+b) / numOfTries);
    }


    public static void __main() throws Exception {
        RecordHashMap r1 = parallelMain();
        RecordHashMap r2 = sequentialMain();

        System.out.println("Sono " + (r2.reallyEquals(r1)? " uguali" : "diverse"));
        System.out.println("Speedup: " + (float)timeseq/timepar);
    }


    public static RecordHashMap parallelMain() throws Exception{
        LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
        lnr.skip(Long.MAX_VALUE);
        int lnnum = lnr.getLineNumber()+1;

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        ExecutorService exe = Executors.newFixedThreadPool(numOfthreads);
        MediatorTask.setGram(gram);
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
        }catch (Exception e){System.out.println("oh");}
        timepar = System.nanoTime() - pstime;


        System.out.println((float) timepar /1000000000);
        System.out.println(MediatorTask.recs.size());

        exe.shutdown();
        return MediatorTask.recs;
    }

    public static RecordHashMap sequentialMain() throws Exception{
        int pageNotRetrieved = 0;
        RecordHashMap result = new RecordHashMap();

        BufferedReader reader = new BufferedReader(new FileReader(new File("wikifiles.txt")));

        System.out.println("Start sequential");
        String line;

        long sstime = System.nanoTime();
        while(( line = reader.readLine())!= null ){
            try{
                List<String> text = useCache ?
                        TextRetriever.wikiTextCached(line):
                        TextRetriever.wikiTextByWord(line);

                //NgramTask ngramTask = new NgramTask(text, gram, result);
                NgramTask ngramTask = new NgramTask(text, 0, 1, gram, result);
                ngramTask.buildHistogram(text);
            }catch(Exception e){
                pageNotRetrieved++;
            }
        }
        timeseq = System.nanoTime() - sstime;

        System.out.println((float) timeseq /1000000000);
        System.out.println(result.size());
        System.out.println("Pages not retrieved " + pageNotRetrieved);
        return result;
    }
}


