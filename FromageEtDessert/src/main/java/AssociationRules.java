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

    public static void main(String[] args) {
        final String inputFileName = args[0];
        final double minConf = Double.parseDouble(args[1]);
        final String outputFileName = args[2];
        final List<FrequentPattern> frequentPatterns = new ArrayList<>();
        final List<AssociationRule> associationRules = new ArrayList<>();

        try {
            frequentPatterns.addAll(
                    Files.readAllLines(Paths.get(inputFileName))
                            .parallelStream()
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

        frequentPatterns.forEach(x -> {
            associationRules.addAll(
                    frequentPatterns
                            .stream()
                            .filter(z -> z.contains(x))
                            .map((z) -> new AssociationRule(x, z))
                            .filter(r -> r.getConf() >= minConf && r.getConf() <= 1)
                            .collect(Collectors.toList()));
        });

        try {
            BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));

            associationRules.forEach(associationRule -> {
                try {
                    outputFile.write(associationRule.textSerialize());
                    outputFile.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
