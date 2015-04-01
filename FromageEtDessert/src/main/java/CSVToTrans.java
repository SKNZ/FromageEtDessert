import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sknz on 4/1/15.
 */
public class CSVToTrans {
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        final String inputFileName = args[0];
        final String outputFileName = args[1];
        final String outputDictionaryFileName = args[1] + ".dict";
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
                    System.out.println(item.equals(getKeyByValue(items, id)));
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
