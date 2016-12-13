import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by cioni on 12/12/16.
 */
public class RecordHashMap{
    private ConcurrentHashMap<Record, Record> map;


    public RecordHashMap(){
        map = new ConcurrentHashMap<Record, Record>();
    }

    public void containsAndUpdate(List<String> gram){
        Record r = new Record(gram);
        map.putIfAbsent(r, r);
        map.get(r).oneMore();
    }


    public int size(){
        return map.size();
    }
    public boolean reallyEquals(RecordHashMap other){
        if(!this.equals(other))
            return false;

        for(Map.Entry<Record, Record> e : this.map.entrySet()){
            if(!(other.map.get(e.getKey()).count() == e.getValue().count())){
                return false;
            }
        }
        return true;
    }



    @Override public boolean equals(Object other){
        return other != null && other instanceof RecordHashMap && (((RecordHashMap) other).map).equals(this.map);
    }
    @Override public String toString(){
        //return this.map.values().stream().map(Record::toString).reduce("", (s1, s2)->s1 + " | " + s2);
        String str = "";
        for (Map.Entry<Record, Record> e : map.entrySet()){
            str+= e.getValue().toString() + " | ";
        }
        return str;
    }

}
