package ie.gmit.sw;

import java.io.File;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * puts inquery file and all files from directory into queue (distribute files to threads)
 * puts words from files in a queue, distribute shingles from a queue to threads
 */

public class Processor {
    private int fileCount = 1;

    public void process(File queryFile, String dir) {
        BlockingQueue<Word> q = new ArrayBlockingQueue(5);
        File folder = new File(dir);
        new Thread(new FileParser(queryFile, q)).start();
        for (File file : folder.listFiles()) {
            fileCount++;
            new Thread(new FileParser(file, q)).start();
        }
        // System.out.println("num of files: " + queryFile);
        //test if the words are output in the console
        // q.forEach(System.out::println);

        new Thread(new ShingleTaker(q, fileCount, queryFile)).start();
    }
}
