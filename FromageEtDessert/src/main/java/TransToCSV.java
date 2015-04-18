import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sknz on 4/1/15.
 */
public class TransToCSV {
    public static void main(String[] args) {
        final String inputFile = args[0];
        final String inputDictFile = args[1];
        final String outputFile = args[2];
        final HashMap<Integer, String> items = new HashMap<>();

        try (BufferedReader dictReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(
                                             inputDictFile)));
             BufferedReader transReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(
                                             inputFile)));
             CSVWriter csvWriter =
                     new CSVWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(
                                             outputFile)))) {

            String nextLine;
            Matcher p = Pattern.compile("(\\d+) (.+)").matcher("");
            while ((nextLine = dictReader.readLine()) != null) {
                p.reset(nextLine);
                p.find();
                items.put(Integer.parseInt(p.group(1)), p.group(2));
            }

            ArrayList<String> newLine = new ArrayList<>();
            Matcher p2 = Pattern.compile("(.*)\\((\\d+)\\)").matcher("");
            while ((nextLine = transReader.readLine()) != null) {
                p2.reset(nextLine);
                if (p2.find()) {
                    nextLine = p2.group(1);
                } else {
                    System.out.println("NO MATCH FOR " + nextLine);
                }

                if (nextLine.isEmpty())
                    continue;

                String[] split = nextLine.split(" ");

                for (String s : split) {
                    String e = items.get(Integer.parseInt(s));
                    newLine.add(e);
                }

                csvWriter.writeNext(newLine.toArray(new String[newLine.size()]));
                newLine.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
