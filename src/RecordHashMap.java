import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by cioni on 12/12/16.
 */
public class RecordHashMap extends ConcurrentHashMap<List<String>,Integer> {
    private ReentrantReadWriteLock lock;
    protected Lock rLock;


    protected Lock wLock;
    public RecordHashMap(){
        super();
        this.lock = new ReentrantReadWriteLock();
        this.rLock= lock.readLock();
        this.wLock=lock.writeLock();
    }

    public void containsAndUpdate(List<String> gram){

        wLock.lock();
        try {
            if(super.containsKey(gram)){
                super.replace(gram,super.get(gram)+1);
            }else{add(gram);}

        } finally {
            wLock.unlock();
        }
    }


    public void add(List<String> ls){
        wLock.lock();
        try {
            super.put(ls,1);
        } finally {
            wLock.unlock();
        }
    }

}
