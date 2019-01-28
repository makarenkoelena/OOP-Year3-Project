package ie.gmit.sw;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * "Blueprint" class - how cosine distance needs to be calculated
 * all words of 1 file for each file
 */

public class FileDb {
    //black diamond???
    private ConcurrentMap<String, Long> db = new ConcurrentHashMap<>();
    private File fileName;

    public FileDb(File fileName) {
        this.fileName = fileName;
    }

    public void add(String shingle) {
        if (db.containsKey(shingle)) {
            long i = db.get(shingle);
            db.put(shingle, ++i);
        } else {
            db.put(shingle, 1L);
        }
    }

    public long dotProduct(FileDb subjectMapdb) {
        long dotProduct = 0;
        //Get vectors
        Map<String, Long> queryMap = this.db;//Long instead of Integer for big files -> output negative numbers
        Map<String, Long> subjectMap = subjectMapdb.db;//word, frequency

        //Get unique words from both sequences
        HashSet<String> intersection = new HashSet<>(queryMap.keySet());
        intersection.retainAll(subjectMap.keySet());
        //Calculate dot product
        for (String item : intersection) {
            dotProduct += queryMap.get(item) * subjectMap.get(item);
        }

        return dotProduct;
    }

    public double getMagnitude() {
        long magnitude = 0;
        for (String k : this.db.keySet()) {
            magnitude += Math.pow(this.db.get(k), 2);
        }
        return Math.sqrt(magnitude);
    }

    public double calculateCosine(FileDb subjectMapdb) {
        return dotProduct(subjectMapdb) / (this.getMagnitude() * subjectMapdb.getMagnitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDb fileDb = (FileDb) o;
        return Objects.equals(fileName, fileDb.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}

