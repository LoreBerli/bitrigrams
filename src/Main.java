import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Bamba on 09/12/2016.
 */
public class Main {
    private static CountDownLatch cdl;
    private static final int nTasks = 8;
    private static final int iter = 500000;

    static class Task implements Runnable{
        private Record[] recs;
        private int i;

        Task(Record[] r, int id){
            recs = r;
            i = id;
        }

        @Override
        public void run() {
            for (int j = i*(recs.length/nTasks); j < (i+1)*(recs.length/nTasks); j++){
                recs[j].add();
                recs[j].count();
            }
            cdl.countDown();

        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(4);
        exe.execute(new MediatorTask(5));
        Thread.sleep(1000);
        exe.shutdown();

    }

    static float test2() throws InterruptedException {
        cdl = new CountDownLatch(nTasks);
        Record[] recs = new Record[iter];

        for (int i = 0; i< iter; i++){
            ArrayList<String> as = new ArrayList<String>();
            as.add(Integer.toString(i));
            Record r = new Record(as);
            recs[i] = r;
        }

        System.out.println("init seq test");

        float sssum = 0;

        long sstime = System.nanoTime();
        for (int i = 0; i < recs.length; i++){
            recs[i].add();
            sssum += recs[i].count();
        }
        long selap = System.nanoTime() - sstime;

        System.out.println("Seq ex time - " + (float)selap / 1000000000);
        //System.out.println();



        ExecutorService exe = Executors.newFixedThreadPool(nTasks );
        Task[] ts = new Task[nTasks ];
        for(int i = 0; i < nTasks ; i++){
            ts[i] = new Main.Task(recs, i);
        }
        long pstime = System.nanoTime();
        for(int i = 0; i < nTasks ; i++){
            exe.execute(ts[i]);
        }
        try{
            cdl.await();
        }catch (Exception e ){}

        long pelap = System.nanoTime() -pstime;
        System.out.println("Par ex time - " + (float) pelap / 1000000000);


        for(Record r : recs){
            if(r.count()!= 2){
                return -1000000;
            }
        }

        exe.shutdown();
        return (float)selap/pelap;
    }


}
