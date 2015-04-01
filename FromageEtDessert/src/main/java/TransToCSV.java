import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by sknz on 4/1/15.
 */
public class TransToCSV {
    public static void main(String[] args) {
        final String inputFile = args[0];
        final String inputDictFile = args[0] + ".dict";
        final String outputFile = args[1];
        final HashMap<Integer, String> items = new HashMap<>();

        try (Scanner dictReader =
                     new Scanner(
                             new InputStreamReader(
                                     new FileInputStream(
                                             inputDictFile)));
             Scanner transReader =
                     new Scanner(
                             new InputStreamReader(
                                     new FileInputStream(
                                             inputFile)));
             CSVWriter csvWriter =
                     new CSVWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(
                                             outputFile)))) {

            while (dictReader.hasNextLine()) {
                items.put(dictReader.nextInt(), dictReader.nextLine());
            }

            Vector<String> newLine = new Vector<>();
            transReader.useDelimiter(System.getProperty("line.separator"));
            while (transReader.hasNextLine()) {
                while (transReader.hasNextInt()) {
                    newLine.add(items.get(transReader.nextInt()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
