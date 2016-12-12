
import java.util.*;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    private int numReaders;
    private String url;
    private List<String> text;


    public MediatorTask(int n, String url){
        numReaders = n;
        this.url = url;
    }

    @Override public void run(){
        getText();
        ConsumerDummy c = new ConsumerDummy(3);
        HashSet<Record> recs = c.consume(text);
        System.out.println(recs);


        //new Consumer(specifictext, gram, records)
        //new Consumer(commontext,phase, freq, gram, records)

        //read and parse wiki page
        //create k conumer thread
    }

    List<String> getText(){
        text = TextRetriever.wikiTextByWord(url);
        System.out.println(text);
        return text;
    }
}
