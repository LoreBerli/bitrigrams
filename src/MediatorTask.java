
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    static RecordHashMap recs;
    static CountDownLatch cdl;//TODO

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
        for(int i = 0, l = Math.floorDiv(text.size(),numReaders)+1; i < numReaders; i++){
            NgramTask ngramTask = new NgramTask(text.subList(i*l,Math.min((i+1)*l + 1,text.size())), 2, recs);
            ngramTask.cdl = MediatorTask.cdl; //TODO
            exe.execute(ngramTask);
        }
        cdl.countDown(); //TODO
    }

    List<String> getText(){
        text = TextRetriever.wikiTextByWord(url);

        return text;
    }
}
