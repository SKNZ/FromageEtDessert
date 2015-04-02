import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * Created by sknz on 4/2/15.
 */
public class AssociationRules {

    public static void main(String[] args) {
        final String inputFileName = args[0];
        final List<FrequentPattern> frequentPatterns = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(
                                             inputFileName)))) {

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                frequentPatterns.add(Arrays.asList(nextLine.split(" "))
                                             .parallelStream()
                                             .map(Integer::parseInt)
                                             .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
