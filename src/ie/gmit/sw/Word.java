package ie.gmit.sw;

import java.io.File;
import java.util.Objects;

/**
 * toString() - returns the string representation of the object instead of object hashcode
 * equals() - since String is immutable, checking the equality of string to another object should be done using equals() method rather than == operator.
 * hashCode() - return distinct integers for distinct objects; comparing hashcodes rather than shingles is faster
 */
public class Word {
    private File book;
    private String shingle;

    public Word(File book, String shingle) {
        this.book = book;
        this.shingle = shingle;
    }

    public File getBook() {
        return book;
    }

    public String getShingle() {
        return shingle;
    }

    @Override
    public String toString() {
        return "Word{" +
                "book='" + book + '\'' +
                ", shingle='" + shingle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(shingle, word.shingle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shingle);
    }
}
