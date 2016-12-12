import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Bamba on 11/12/2016.
 */
public class Record {
    private List<String> id;
    private int count;
    private ReentrantReadWriteLock lock;

    Record(ArrayList<String> id){
        this.id = id;
        count = 0;
        lock = new ReentrantReadWriteLock();
    }

    @Override public int hashCode(){
        return id.hashCode();
    }

    @Override public boolean equals(Object other) {
        return other instanceof Record && ((Record) other).id.equals(this.id);
    }

    @Override public String toString(){
        String str = "";
        for (String s : id){
            str += s + "-";
        }
        return str + count;

    }
    public void add(){
        lock.writeLock().lock();
        try {
            count++;
        }
        finally {
            lock.writeLock().unlock();
        }
    }
    public int count(){
        lock.readLock().lock();
        try {
            return count;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    List<String> getId(){
        return id;
    }

}
