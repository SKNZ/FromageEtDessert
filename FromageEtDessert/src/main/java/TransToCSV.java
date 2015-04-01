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
            Pattern p = Pattern.compile("(\\d+) (.+)");
            while ((nextLine = dictReader.readLine()) != null) {
                Matcher m = p.matcher(nextLine);
                m.find();
                items.put(Integer.parseInt(m.group(1)), m.group(2));
            }

            ArrayList<String> newLine = new ArrayList<>();
            Pattern p2 = Pattern.compile("(.*)\\((\\d+)\\)");
            while ((nextLine = transReader.readLine()) != null) {
                Matcher m = p2.matcher(nextLine);
                if (m.find()) {
                    nextLine = m.group(1);
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
