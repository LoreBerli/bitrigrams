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

    @Override public boolean equals(Object other){
        if(!(other instanceof RecordHashMap))
            return false;

        RecordHashMap o = (RecordHashMap)other;
        if(o.size() != this.size())
            return false;

        for(Entry<List<String>, Integer> entry : this.entrySet()){
            if(!o.containsKey(entry.getKey()))
                return false;

            if(!o.get(entry.getKey()).equals(entry.getValue()))
                return false;
        }
        return true;

    }

}
