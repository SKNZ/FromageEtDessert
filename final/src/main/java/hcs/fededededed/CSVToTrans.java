package hcs.fededededed;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.HashMap;

/**
 * Created by sknz on 4/19/15.
 */
public class CSVToTrans {
    public static void convert(String inputFileName, String outputFileName, String outputDictionaryFileName) {
        final HashMap<String, Integer> items = new HashMap<>();
        int latestId = 0;

        try (BufferedWriter dictWriter =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(
                                             outputDictionaryFileName)));
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
                        latestId = latestId + 1;
                        id = latestId;
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
