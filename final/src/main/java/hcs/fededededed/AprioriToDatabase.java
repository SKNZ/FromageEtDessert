package hcs.fededededed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sknz on 4/19/15.
 */
public class AprioriToDatabase {
    static class FrequentPattern {
        private int count;
        private List<Integer> items = new ArrayList<>();
    }

    public static void convert(final int id, final String inputFile,
                               String dictFile,
                               double minConf,
                               double minLift) throws IOException,
                                                      SQLException {
        final HashMap<Integer, String> dictionary = new HashMap<>();

        BufferedReader dictReader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(dictFile)));

        String nextLine;
        Matcher p = Pattern.compile("(\\d+) (.*)").matcher("");
        while ((nextLine = dictReader.readLine()) != null) {
            p.reset(nextLine);
            p.find();
            dictionary.put(Integer.parseInt(p.group(1)), p.group(2));
        }

        final List<FrequentPattern> frequentPatterns = new ArrayList<>();

        Pattern regexPattern = Pattern.compile("(.*)\\(([0-9]+)\\)");

        try {
            frequentPatterns.addAll(
                    Files.readAllLines(Paths.get(inputFile))
                         .stream()
                         .map(s -> {
                             FrequentPattern pattern = new FrequentPattern();
                             Matcher regex = regexPattern.matcher(s);

                             regex.reset(s);
                             if (!regex.find()) {
                                 throw new RuntimeException("Bad file format");
                             }

                             String freq = regex.group(2).trim();
                             pattern.count = Integer.parseInt(freq);

                             if (regex.group(1) != null &&
                                 !regex.group(1).isEmpty()) {
                                 pattern.items.addAll(
                                         Arrays.asList(regex.group(1)
                                                            .split("\\s+"))
                                               .stream()
                                               .map(String::trim)
                                               .map(Integer::parseInt)
                                               .collect(Collectors.toList()));
                             }

                             return pattern;
                         })
                         .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt =
                DB.conn.prepareStatement(
                        "INSERT INTO rules (scenario, x, y, conf, lift) VALUES (?, ?, ?, ?, ?)");

        double total = frequentPatterns.get(0).count;

        PreparedStatement statusStmt = DB.conn.prepareStatement(
                "UPDATE scenario " +
                "SET state = CONCAT('Rules... ', ?, '%') " +
                "WHERE id = ?");

        int latestPercent = 1;
        for (int i = 1; i < frequentPatterns.size(); ++i) {
            int percent = (int)((double)i * 100 / frequentPatterns.size());
            if (percent % 3 == 0 && percent == latestPercent) {
                try {
                    latestPercent = percent;
                    statusStmt.setInt(1, percent);
                    statusStmt.setInt(2, id);
                    statusStmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.print(percent + "%...");
            }

            FrequentPattern y = frequentPatterns.get(i);
            List<Integer> yItems = y.items;
            double yFq = (double) y.count / total;

            for (int j = 0; j < i; ++j) {
                FrequentPattern x = frequentPatterns.get(j);
                List<Integer> xItems = x.items;
                double xFq = (double) x.count / total;

                if (!yItems.containsAll(xItems) || xItems.isEmpty()) {
                    continue;
                }

                double conf = yFq / xFq;
                if (conf < minConf || conf > 1) {
                    continue;
                }

                List<Integer> yMinusXItems = new ArrayList<>(yItems);
                yMinusXItems.removeAll(xItems);
                double yMinusXFq = yFq;

                if (yMinusXItems.isEmpty()) {
                    continue;
                }

                double lift = conf / yMinusXFq;
                if (lift >= minLift) {
                    String xString = "";
                    for (int item : xItems) {
                        xString += dictionary.get(item) + " ";
                    }

                    String yMinusXString = "";
                    for (int item : yMinusXItems) {
                        yMinusXString += dictionary.get(item) + " ";
                    }

                    stmt.setInt(1, id);
                    stmt.setString(2, xString);
                    stmt.setString(3, yMinusXString);
                    stmt.setDouble(4, conf);
                    stmt.setDouble(5, lift);
                    stmt.execute();
                }
            }
        }
    }
}
