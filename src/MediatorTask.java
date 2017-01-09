
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    static RecordHashMap recs;
    static CountDownLatch cdl;//TODO

    private static int gram;

    private int numReaders;
    private String url;
    private List<String> text;
    private ExecutorService exe;

    public MediatorTask(int n, String url, ExecutorService exe){
         if(recs == null)
            recs = new RecordHashMap();

        this.exe = exe;
        numReaders = n;
        this.url = url;
    }

    @Override public void run(){
        getText();

        for(int i = 0; i < numReaders; i++){

                NgramTask ngramTask = new NgramTask(text, i, numReaders, MediatorTask.gram, recs);

                ngramTask.cdl = MediatorTask.cdl; //TODO
                exe.execute(ngramTask);
            }

        cdl.countDown(); //TODO
    }

    List<String> getText() {

        String u = url.replaceAll("[\\/\\.:]", "_");
        switch (Main.mode){
            case "ram":
                text = TextRetriever.fromStash(u);
                break;
            case "disk":
                text = TextRetriever.wikiTextCached(url);
                break;
            case "online":
                text = TextRetriever.wikiTextByWord(url);
                break;
        }

        return text;
    }
    static public void setGram(int g){
        MediatorTask.gram = g;
    }
}
