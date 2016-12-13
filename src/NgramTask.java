import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cioni on 11/12/16.
 */
public class NgramTask implements Runnable{
    private List<String> text=new ArrayList<>();
    private int gram;
    private RecordHashMap hist;

    CountDownLatch cdl; //TODO

    public NgramTask(List<String> text, int gram, RecordHashMap map){
        this.text=text;
        this.gram=gram;
        this.hist=map;
    }

    public void buildHistogram(List<String> words){
        for(int i=0;i<words.size()-gram+1;i++){
            List<String> s=words.subList(i,i+gram);
            hist.containsAndUpdate(s);
            }
        }

    @Override
    public void run(){
        buildHistogram(text);
        cdl.countDown(); //TODO
    }

}
