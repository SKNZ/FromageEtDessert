import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;

/**
 * Created by sknz on 4/1/15.
 */
public class CSVToTrans {
    public static void main(String[] args) {
        final String inputFileName = args[0];
        final String outputFileName = args[1];
        final String outputDictionnaryFileName = args[1] + ".dict";
        final HashMap<String, Integer> items = new HashMap<>();
        int latestId = 0;

        try (BufferedWriter dictWriter =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(
                                             outputDictionnaryFileName)));
             BufferedWriter transWriter =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(
                                             outputFileName)))) {
            CSVReader reader = new CSVReader(new FileReader(inputFileName));

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (String item : nextLine) {
                    int id = 0;
                    if (items.containsKey(item)) {
                        id = items.get(item);
                    } else {
                        id = ++latestId;
                        items.put(item, id);
                    }
                    transWriter.write(String.format("%d ", id));
                }
                transWriter.newLine();
            }

            items.forEach((String item, Integer id) -> {
                try {
                    dictWriter.write(String.format("%d %s", id, item));
                    dictWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
