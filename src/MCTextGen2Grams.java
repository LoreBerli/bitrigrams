import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bamba on 13/12/2016.
 */
public class MCTextGen2Grams {
    public static class KeysToTheMap{
        private KeysToTheMap(){}
    }

    private static KeysToTheMap keys = new KeysToTheMap();
    private Map<String, Map<String, Double>> jumpProb;


    public MCTextGen2Grams(RecordHashMap recs){
        ConcurrentHashMap<Record, Record> map = recs.mapAccess(keys);

        jumpProb = new HashMap<>();
        for(Map.Entry<Record, Record> e : map.entrySet()){
            if(!jumpProb.containsKey(e.getKey().id().get(0))){
                jumpProb.put(e.getKey().id().get(0), new HashMap<>());
            }
            jumpProb.get(e.getKey().id().get(0)).put(e.getKey().id().get(1), new Double(e.getValue().count()));
        }

        jumpProb.forEach((s1,m)->{
            double sum = m.values().stream().reduce((double)0, (a,b)->a+b);
            m.forEach((s2, d)-> d = d/sum);
        });
    }

}
