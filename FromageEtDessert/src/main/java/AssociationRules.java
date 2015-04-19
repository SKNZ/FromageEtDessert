import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by sknz on 4/2/15.
 */
public class AssociationRules {
    public static final List<FrequentPattern> FREQUENT_PATTERNS = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        final String inputFileName = args[0];
        final double minConf = Double.parseDouble(args[1]);
        final String outputFileName = args[2];
        final double minLift = Double.parseDouble(args[3]);
        final double minFreq = Double.parseDouble(args[4]);

        try {
            FREQUENT_PATTERNS.addAll(
                    Files.readAllLines(Paths.get(inputFileName))
                            .stream()
                            .map((s) -> {
                                try {
                                    return FrequentPattern.fromString(s);
                                } catch (FedEx fedEx) {
                                    fedEx.printStackTrace();
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));

        double total = FREQUENT_PATTERNS.get(0).getCount();
        System.out.println("TOTAL " + total);
        for (int i = 1; i < FREQUENT_PATTERNS.size(); ++i) {
            if (i % 1000 == 0) System.out.println(i + " / " + FREQUENT_PATTERNS.size());
            FrequentPattern y = FREQUENT_PATTERNS.get(i);
            List<Integer> yItems = y.getItems();
            double yFq = (double)y.getCount() / total;

            for (int j = 0; j < i; ++j) {
                FrequentPattern x = FREQUENT_PATTERNS.get(j);
                List<Integer> xItems = x.getItems();
                double xFq = (double)x.getCount() / total;

                if (!yItems.containsAll(xItems) || xItems.isEmpty())
                    continue;

                double conf = yFq / xFq;
                if (conf < minConf || conf > 1)
                    continue;

                List<Integer> yMinusXItems = new ArrayList<>(yItems);
                yMinusXItems.removeAll(xItems);
                double yMinusXFq = yFq;

                if (yMinusXItems.isEmpty())
                    continue;

                double lift = conf / yMinusXFq;
                if (lift >= minLift) {
                    String s = "";
                    for (int item : xItems)
                        s += item + " ";

                    s+= "-> ";

                    for (int item : yMinusXItems)
                        s += item + " ";

                    s += "| " + conf + " | " + lift;

                    outputFile.write(s);
                    outputFile.newLine();
                }
            }
        }
        outputFile.close();
    }
}
