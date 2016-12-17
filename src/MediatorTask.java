
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    static RecordHashMap recs;
    static CountDownLatch cdl;//TODO

    static int gram = 2;

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
            if(i*l < Math.min((i+1)*l + MediatorTask.gram - 1,text.size())){
                //NgramTask ngramTask = new NgramTask(text.subList(i*l,Math.min((i+1)*l + MediatorTask.gram - 1,text.size())), MediatorTask.gram, recs);

                NgramTask ngramTask = new NgramTask(text, i, numReaders, MediatorTask.gram, recs);

                ngramTask.cdl = MediatorTask.cdl; //TODO
                exe.execute(ngramTask);
            }
            else{
                cdl.countDown();
            }

        }
        cdl.countDown(); //TODO
    }

    List<String> getText() {
        text = Main.useCache ?
                TextRetriever.wikiTextCached(url) :
                TextRetriever.wikiTextByWord(url);
        return text;
    }
    static public void setGram(int g){
        MediatorTask.gram = g;
    }
}
