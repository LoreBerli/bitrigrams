import java.util.ArrayList;

/**
 * Created by Bamba on 11/12/2016.
 */
public class MediatorTask implements Runnable{
    private int numReaders;
    private ArrayList<String> text;


    public MediatorTask(int n){
        numReaders = n;
    }


    @Override public void run(){
        //new Consumer(specifictext, gram, records)
        //new Consumer(commontext,phase, freq, gram, records)

        //read and parse wiki page
        //create k conumer thread

    }

}
