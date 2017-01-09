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
    //public final static boolean useCache = true;
    public final static String mode="ram"; // ram,disk,online
    private  static int numOfTries = 10;

    private final static String filename = "wikifiles.txt";
    private static int numOfthreads;
    private static int gram;
    private static final int[] threads={2,4,6,8};
    private static final int[] grams={2,3};
    private static long timepar = 0;
    private static long timeseq = 0;


    public static void main(String[]args) throws Exception{
        TextRetriever.buildFiles();
        System.out.println(Runtime.getRuntime().availableProcessors());
        for(int g:grams) {
            for (int t : threads) {
                List<Float> speedups = new ArrayList<Float>(numOfTries);
                for (int i = 0; i < numOfTries; i++) {
                    numOfthreads = t;
                    gram=g;
                    MediatorTask.recs = null;
                    __main();
                    speedups.add(i, new Float((float) timeseq / timepar));
                }
                System.out.println(numOfthreads + "," + gram + "," + speedups.stream().reduce(0f, (a, b) -> a + b) / numOfTries);
            }
            //System.out.println("_________________________");
        }
        //System.out.println(MediatorTask.recs.);

    }


    public static void __main() throws Exception {

        RecordHashMap r1 = parallelMain();
        RecordHashMap r2 = sequentialMain();

    }


    public static RecordHashMap parallelMain() throws Exception{
        LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
        lnr.skip(Long.MAX_VALUE);
        int lnnum = lnr.getLineNumber()+1;

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        ExecutorService exe = Executors.newFixedThreadPool(numOfthreads);
        MediatorTask.setGram(gram);
        MediatorTask.cdl = new CountDownLatch(lnnum*(numOfthreads+1)); // # of lines * (k+1)
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

        exe.shutdown();
        return MediatorTask.recs;
    }

    public static RecordHashMap sequentialMain() throws Exception{
        int pageNotRetrieved = 0;
        RecordHashMap result = new RecordHashMap();

        BufferedReader reader = new BufferedReader(new FileReader(new File("wikifiles.txt")));

        String line;

        long sstime = System.nanoTime();

        while(( line = reader.readLine())!= null ){
            try{
                String u = line.replaceAll("[\\/\\.:]", "_");
                List<String> text= new ArrayList<>();

                switch (mode){
                    case "ram":
                        text = TextRetriever.fromStash(u);
                        break;
                    case "disk":
                        text = TextRetriever.wikiTextCached(line);
                        break;
                    case "online":
                        text = TextRetriever.wikiTextByWord(line);
                        break;
                }
                //NgramTask ngramTask = new NgramTask(text, gram, result);
                NgramTask ngramTask = new NgramTask(text, 0, 1, gram, result);
                ngramTask.buildHistogram(text);
            }catch(Exception e){
                pageNotRetrieved++;
            }
        }
        timeseq = System.nanoTime() - sstime;

       // System.out.println((float) timeseq /1000000000);
        //System.out.println(result.size());
        //System.out.println("Pages not retrieved " + pageNotRetrieved);
        return result;
    }
}


