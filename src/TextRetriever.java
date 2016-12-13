import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Bamba on 09/12/2016.
 */
public class TextRetriever {
    static public List<String> wikiTextByWord(String strUrl){
        try{
            Document doc = Jsoup.connect(strUrl).get();
            return Arrays.asList(doc.getElementById("mw-content-text").text().replaceAll("[^\\p{IsAlphabetic}]"," ").replaceAll(" +"," ").split(" "));
        }catch (IOException ioe){
            System.out.println("Can't connect");
            ioe.printStackTrace();
        }
        return null;
    }
}
