package ie.gmit.sw;

import java.io.File;

/**
 * This class helps to identify the end of each file.
 * "Poison pill" is the last item in the queue
 */
public class Poison extends Word {
    public Poison(File book) {
        super(book, "");
    }
}
