import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bamba on 09/12/2016.
 */
public class TextRetriever {
    private URL url;
    private BufferedReader bR;

    public TextRetriever (String strUrl){
        try{
            url = new URL(strUrl);
            bR = new BufferedReader(new InputStreamReader(url.openStream()));


        }catch(MalformedURLException mURLe){
            //blabla
        }catch(Exception e){
            //blabla
        }
    }

    public String readLine()  {
        try{
            return bR.readLine();
        }
        catch(Exception e){
            return null;
        }

    }

}
