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
        final List<AssociationRule> associationRules = new ArrayList<>();

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

        String out = "";
        BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));

        int total = FREQUENT_PATTERNS.get(0).getCount();
        System.out.println("TOTAL " + total);
        for (int i = 1; i < FREQUENT_PATTERNS.size(); ++i) {
            if (i % 1000 == 0) System.out.println(i + " / " + FREQUENT_PATTERNS.size());
            FrequentPattern x = FREQUENT_PATTERNS.get(i);
            List<Integer> xItems = x.getItems();
            double xFq = (double)x.getCount() / total;

            for (int j = 1; j < FREQUENT_PATTERNS.size(); ++j) {
                FrequentPattern z = FREQUENT_PATTERNS.get(j);
                List<Integer> zItems = z.getItems();
                double zFq = (double)z.getCount() / total;

                if (zFq < minFreq)
                    continue;

                if (zItems.size() <= xItems.size() || !zItems.containsAll(xItems))
                    continue;

                double conf = zFq / xFq;
                if (conf < minConf)
                    continue;

                List<Integer> yItems = new ArrayList<>();
                double yFq = 0.0d;

                for (Integer item : zItems)
                    yItems.add(item);

                for (Integer item : xItems)
                    yItems.remove(item);

                for (int k = 0; k < FREQUENT_PATTERNS.size(); ++k) {
                    FrequentPattern y = FREQUENT_PATTERNS.get(k);
                    if (y.getItems().equals(yItems)) {
                        yFq = (double)y.getCount() / total;
                        break;
                    }
                }

                double lift = conf / yFq;
//                System.out.println(zFq + " " + xFq + " " + yFq + " " + conf + " " + lift);
                if (lift >= minLift) {
                    String s = "";
                    for (int item : xItems)
                        s += item + " ";

                    s+= "-> ";

                    for (int item : yItems)
                        s += item + " ";

                    s += "| " + conf + " | " + lift;
//                    System.out.println(s);
                    out += s + '\n';
                    System.out.println("LEL");
                }
            }
        }

        outputFile.write(out);
        outputFile.close();
    }
}
