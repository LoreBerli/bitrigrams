import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Bamba on 09/12/2016.
 */
public class TextRetriever {
    static private String escChar = " ";
    static public ConcurrentHashMap<String,List<String>> stash;

    static public List<String> wikiTextByWord(String strUrl){
        try{
            Document doc = Jsoup.connect(strUrl).timeout(60000).get();
            return Arrays.asList(doc.getElementById("mw-content-text").text().toLowerCase().replaceAll("[^\\p{IsAlphabetic}]", " ").replaceAll(" +", " ").split(escChar));
        }catch (SocketTimeoutException stoe){
            System.out.println("Can't connect");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    static public List<String> fromStash(String url){
        return stash.get(url.replaceAll("[\\/\\.:]", "_"));
    }
    static public void buildFiles() {
        try{
            System.out.println("TextRetirever.buildFiles()");
        BufferedReader reader = new BufferedReader(new FileReader(new File("/home/cioni/git/bitrigrams/src/wikifiles.txt")));
        String line;
        ConcurrentHashMap<String,List<String>> stash= new ConcurrentHashMap<String,List<String>>(700,0.75f,16);
        while(( line = reader.readLine())!= null ) {
            String name = line.replaceAll("[\\/\\.:]", "_");
            stash.put(name,wikiTextCached(line));
        }
        TextRetriever.stash=stash;
        }
        catch (IOException ioe){System.out.println(ioe);}

    }


    static public List<String> wikiTextCached(String strUrl){
        String name = strUrl.replaceAll("[\\/\\.:]", "_");
        try{
            BufferedReader bfr = new BufferedReader(new FileReader(new File("src/cache/" + name + ".html")));

            String s = "";
            String line = "";
            while((line = bfr.readLine()) != null ){
                s+= line;
            }
            return Arrays.asList(s.split(escChar));
        }catch (IOException ioe){
            System.out.println(ioe);
        }
        return null;
    }











    public static void genCache(String strUrl) {
        String name = strUrl.replaceAll("[\\/\\.:]", "_");
        System.out.println("name " + name);
        File file = new File( "src/cache/" + name + ".html");
        try {
            file.createNewFile();
        }catch (IOException ioe){
            System.out.println(file);
            System.out.println(ioe);
        }


        BufferedWriter bfw = null;
        FileWriter fw = null;
        try{
            fw = new FileWriter(file);
            bfw = new BufferedWriter(fw);

            List<String> wikitext = wikiTextByWord(strUrl);
            System.out.println(wikitext);
            for(String s : wikitext){
                try {
                    bfw.write(s + " ");
                } catch (IOException e) {

                }
            }
        }catch (IOException ioe){

        }finally {
            if(bfw != null){
                try{
                    bfw.close();
                }catch (Exception e){}
            }
            if(fw != null){
                try{
                    fw.close();
                }catch (Exception e){}
            }
        }
    }

    public static void main(String[] args){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File("/home/cioni/git/bitrigrams/src/wikifiles.txt")));

            String line;
            while ((line=reader.readLine()) != null){
                genCache(line);
            }
        }catch (Exception e){}

    }

}
