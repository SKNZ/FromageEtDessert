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

    public static void main(String[] args) {
        final String inputFileName = args[0];
        final double minConf = Double.parseDouble(args[1]);
        final String outputFileName = args[2];
        final double minLift = Double.parseDouble(args[3]);
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

        FREQUENT_PATTERNS.forEach(x -> {
            associationRules.addAll(
                    FREQUENT_PATTERNS
                            .stream()
                            .filter(z -> z.contains(x))
                            .map(z -> new AssociationRule(x, z))
                            .filter(r -> r.getConf() >= minConf && r.getConf() <= 1)
                            .filter(r -> r.getLift() >= minLift)
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
