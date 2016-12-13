import java.io.BufferedWriter;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Created by Bamba on 09/12/2016.
 */
public class ExampleMain {
//    public static void main(String[] args) throws IOException {
//        TextRetriever textRetriever = new TextRetriever("https://en.wikipedia.org/wiki/N-gram");
//
//        File file = new File("file.html");
//        BufferedWriter bW = new BufferedWriter(new FileWriter("file.html"));
//
//        String line;
//        int i = 0;
//        while((line = textRetriever.readLine()) != null){
//            System.out.println(i++);
//            bW.append(line);
//        }
//
//    }
    public  static  void main(String[] args){ //deprecato
        int PROD=3;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<String> test =loadFromFile("src/speeches.txt");
        RecordHashMap gramsSet = new RecordHashMap();
        Queue<Runnable> taskQue = new ConcurrentLinkedQueue<>();
        for(int j=0;j<PROD;j++){
            taskQue.add(new MediatorTask(3, "", Executors.newFixedThreadPool(12)));
            taskQue.add(new NgramTask(test,3,gramsSet));
        }
        for(Runnable t:taskQue){
            executor.execute(t);
        }
        executor.shutdown();
        while(!executor.isTerminated()){}
        gramsSet.forEach((K,V)->{System.out.println(K+"= "+V.toString());});

    }

    public static List<String> dummyList(){
        String prova = "a a b b a a";
        return Arrays.asList(prova.split(" "));
    }
    public static List<String> loadFromFile(String path){
        File f=new File(path);
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f.getAbsolutePath()));
            List<String> toBeSplitted = bf.lines().collect(Collectors.toList());
            List<String> out = new ArrayList<>();
            for (String l : toBeSplitted) {
                out.addAll(Arrays.asList(processString(l)));
            }
            return out;
        }
        catch (Exception e){
            System.out.println("File not found");
            return null;}
    }

    private static String[] processString(String s){
        String[] toClean={"\\.",",","“","\\?","—","\\–","\"",";"};
        for(int i =0;i<toClean.length;i++){
            s=s.replaceAll(toClean[i],"");
        }
        return s.toLowerCase().split(" ");
    }



}
