import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by Bamba on 13/12/2016.
 */


public class Record {
    private int count;
    private List<String> id;
    private ReentrantLock lock;


    public Record(List<String> id){
        this.id = id;
        count = 0;
        lock = new ReentrantLock();
    }

    public void oneMore(){
        lock.lock();
        try{
            count++;
        }finally {
            lock.unlock();
        }
    }
    public int count(){
        return count;
    }
    public List<String> id(){return id;}


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
