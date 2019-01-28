package ie.gmit.sw;

import java.io.File;
import java.util.Scanner;

/**
 * User chooses the file (has to enter absolute path) and subject directory,
 * if files are found/exist Processor processes the files: parses files into shingles, adds to queue
 */
public class Menu {
    public static final Scanner SCANNER = new Scanner(System.in);
    private String queryFileEntered;
    private String subjectDirectory;
    private File queryFile;

    public void show() {
        System.out.println("***  Document Comparison Service  ***");

        System.out.println("Enter Query File>\n ");
        boolean queryFileExists;
        do {
            queryFileEntered = SCANNER.nextLine();
            queryFile = new File(queryFileEntered);

            //System.out.println(queryFile + "" + queryFileEntered);
            queryFileExists = queryFile.exists();
            if (!queryFileExists) {
                System.out.println("Could not find " + queryFile + ", try again:");
            } else {
                System.out.println("Enter Subject Directory>\n");
            }
        } while (!queryFileExists);

        boolean subjectDirectoryExists;
        do {
            subjectDirectory = SCANNER.nextLine();
            File file = new File(subjectDirectory);
            subjectDirectoryExists = file.exists();
            if (!subjectDirectoryExists) {
                System.out.println("Could not find " + subjectDirectory + ", try again:");
            } else {
                //System.out.println("subjectDirectory found");
            }
        } while (!subjectDirectoryExists);

        Processor p = new Processor();
        p.process(queryFile, subjectDirectory);

    }
}//end of class
