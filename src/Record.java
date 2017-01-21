import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by Bamba on 13/12/2016.
 */


public class Record {
    private int count;
    private List<String> id;
    //private ReentrantLock lock;
    private ReadWriteLock lock;

    public Record(List<String> id){
        this.id = id;
        count = 0;
        //lock = new ReentrantLock();
        lock = new ReentrantReadWriteLock();
    }

    public void oneMore(){
        lock.writeLock().lock();
        try{
            count++;
        }finally {
            lock.writeLock().unlock();
        }
    }
    public int count(){
        lock.readLock().lock();
        try{
            return count;
        } finally {
            lock.readLock().unlock();
        }

    }
    public List<String> id(){
        lock.readLock().lock();
        try{
            return id;
        }
        finally {
            lock.readLock().unlock();
        }
    }


    @Override public int hashCode(){
        return id.hashCode();
    }
    @Override public boolean equals(Object other){
        return other instanceof Record && ((Record)other).id.equals(this.id);
    }
    @Override public String toString(){
        //return id.stream().reduce("", (s1, s2)->s1 + "-" + s2) + "["+count + "]";
        String str = "";
        for(String s : id){
            str+= s+"-";
        }
        return str+"[ " + count + " ]";
    }


}
