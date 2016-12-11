
import java.util.*;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    private int numReaders;
    private List<String> text;


    public MediatorTask(int n){
        numReaders = n;
    }


    @Override public void run(){
        text = TextRetriever.wikiTextByWord("https://it.wikipedia.org/wiki/Esplorazioni_geografiche");
        System.out.println(text);
        //new Consumer(specifictext, gram, records)
        //new Consumer(commontext,phase, freq, gram, records)

        //read and parse wiki page
        //create k conumer thread

    }

}
