package ie.gmit.sw;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Takes each shingle, adds to the queue, calculate cosine distance, creates map<K, V> where K  is the name of the file, V - calculation returned as a future
 * filecounter specifies if all the files are read (fileCount ==0), every time there is a poison(identifier of the end of file), filecounter is decremented
 */
public class ShingleTaker implements Runnable {
    private final File queryFile;
    private BlockingQueue<Word> q;
    private AtomicInteger fileCount;//An int value that may be updated atomically.
    private Map<File, FileDb> fileDbSet = new HashMap<>();
    private ExecutorService executorService = Executors.newWorkStealingPool();


    public ShingleTaker(BlockingQueue<Word> q, int fileCount, File queryFile) {
        this.q = q;
        this.fileCount = new AtomicInteger(fileCount);
        this.queryFile = queryFile;
    }

    @Override
    public void run() {
        Map<File, Future<Double>> futures = new HashMap<>();

        while (fileCount.get() > 0) {
            Word w = null;

            try {
                w = q.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (w instanceof Poison) {
                fileCount.decrementAndGet();
                futures.put(w.getBook(), calculateCosine(w));
                ;
            } else {
                String shingle = w.getShingle();
                // System.out.println("Shingle:" + shingle);

                if (fileDbSet.containsKey(w.getBook())) {
                    fileDbSet.get(w.getBook()).add(shingle);
                } else {
                    fileDbSet.put(w.getBook(), new FileDb(w.getBook()));//filename and all info about file
                    fileDbSet.get(w.getBook()).add(shingle);
                }
            }//else
        }//while

        showResults(futures);
    }//run()

    private Future<Double> calculateCosine(Word word) {//A Future represents the result of an asynchronous computation, get the result whenever it is ready, dont block, keep going
        Callable<Double> calculate = () -> fileDbSet.get(word.getBook()).calculateCosine(fileDbSet.get(queryFile));
        return executorService.submit(calculate);
    }

    private void showResults(Map<File, Future<Double>> futureList) {
        futureList.forEach(this::printIt);
    }

    private void printIt(File file, Future<Double> future) {
        System.out.printf("%s \t\t\t", queryFile.getName());
        System.out.printf("%s %n", file.getName());

        try {
            System.out.printf("Similarity \t\t\t\t %.2f %n", future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("---------------------------");
    }
}//ShingleTaker
