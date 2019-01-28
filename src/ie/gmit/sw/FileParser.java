package ie.gmit.sw;

import java.io.*;
import java.util.concurrent.BlockingQueue;

/**
 * Each thread reads a separate file line by line, strips out punctuation, divides separate words
 * takes each word and puts it into a queue of words
 */

public class FileParser implements Runnable {
    private File file;
    private BlockingQueue<Word> q;
    private int fileCount;
    public FileParser() {};

    public FileParser(File file, BlockingQueue<Word> q) {
        this.file = file;
        this.q = q;
    }

    public FileParser(BlockingQueue<Word> q, int fileCount) {
        this.q = q;
        this.fileCount = fileCount;
    }

    public BlockingQueue<Word> getQ() {
        return q;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String strLine;

        try {
            while ((strLine = br.readLine()) != null) {
                //get rid of punctuation, make all letters lowercase, split by space
                String[] words = strLine.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");//(?<=\\G.{"+3+"})
                for (String s : words) {
                    q.put(new Word(file, s));
                }//for
            }//while
            //poison tells the queue where the file ends; just need the name of the file
            q.put(new Poison(file));

            //Close the input stream
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
