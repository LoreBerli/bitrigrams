import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Bamba on 12/12/2016.
 */
public class ConsumerDummy {
    private int gram;
    static private HashSet<Record> recs;
    public ConsumerDummy(int gram){
        if(recs == null){
            recs = new HashSet<Record>();
        }
        this.gram = gram;
    }

    public HashSet<Record> consume(List<String> text){
        for(int i = 0; i < text.size() - gram + 1; i++){
            ArrayList<String> g = new ArrayList<>(gram);
            for (int j = 0; j < gram; j++){
                g.add(j, text.get(i+j));
            }

            Record r = new Record(g);
            recs.add(r);
            for(Iterator<Record> itr = recs.iterator(); itr.hasNext(); ){
                Record rr = itr.next();
                if (rr.equals(r)){
                    rr.add();
                    System.out.println(rr);
                }
            }
        }
        return recs;
    }
}
