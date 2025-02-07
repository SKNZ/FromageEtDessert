package hcs.fededededed;

import hcs.fededededed.Twitter.Twitter;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by sknz on 4/19/15.
 */
public class Mine {
    public static void main(String[] args) {
        final int id = Integer.parseInt(args[0]);
        try {
            final String basePath = "./";
            final String basePathId = basePath + id;

            try (Statement stmt = DB.conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM scenario WHERE id = " + id);
                rs.next();
                if (rs.getInt(1) != 1) {
                    throw new RuntimeException("Invalid id");
                }

                stmt.execute("UPDATE scenario SET doing = 1 WHERE id = " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            File file = new File(basePathId + "/");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new RuntimeException("Couldn't create directory");
                }
            }

            int argsPos = 2;
            int entryCount = 0;
            switch (args[1]) {
                case "twitter":
                    System.out.println("Twittering...");
                    PreparedStatement stmt = DB.conn.prepareStatement(
                            "UPDATE scenario " +
                            "SET state = CONCAT('Twittering... ', ?, '%') " +
                            "WHERE id = ?");

                    entryCount = Twitter.capture(args[argsPos++],
                                                 Integer.parseInt(args[argsPos++]),
                                                 basePathId + "/input.csv",
                                                 percent -> {
                                                     try {
                                                         stmt.setInt(1,
                                                                     percent);
                                                         stmt.setInt(2, id);
                                                         stmt.executeUpdate();
                                                     } catch (SQLException e) {
                                                         e.printStackTrace();
                                                     }
                                                     System.out.print(percent +
                                                                      "%...");
                                                 });
                    break;
                case "twitterStream":
                    System.out.println("Twittering...");
                    PreparedStatement stmt2 = DB.conn.prepareStatement(
                            "UPDATE scenario " +
                            "SET state = CONCAT('Twittering... ', ?, '%') " +
                            "WHERE id = ?");

                    entryCount = Twitter.captureStream(args[argsPos++],
                                                       Integer.parseInt(args[argsPos++]),
                                                       basePathId +
                                                       "/input.csv",
                                                       percent -> {
                                                           try {
                                                               stmt2.setInt(1,
                                                                           percent);
                                                               stmt2.setInt(2,
                                                                           id);
                                                               stmt2.executeUpdate();
                                                           } catch (SQLException e) {
                                                               e.printStackTrace();
                                                           }
                                                           System.out.print(
                                                                   percent +
                                                                   "%...");
                                                       });
                    break;
                case "csv":
                    break;
                default:
                    throw new RuntimeException("Invalid entry type");
            }

            System.out.println();
            System.out.println("CSV to trans...");

            CSVToTrans.convert(basePathId + "/input.csv",
                               basePathId + "/input.trans",
                               basePathId + "/input.trans.dict");

            switch (args[argsPos++]) {
                case "apriori-nedseb-c":
                    System.out.println("Apriori...");
                    Integer fq = (int)
                            (Double.parseDouble(args[argsPos++]) * entryCount);

                    ProcessBuilder aprioriBuilder =
                            new ProcessBuilder(basePath + "/apriori",
                                               basePathId + "/input.trans",
                                               fq.toString(), // minFreq
                                               basePathId + "/apriori.out");

                    aprioriBuilder.redirectError();
                    aprioriBuilder.redirectInput();

                    Process apriori = aprioriBuilder.start();

                    try (Statement stmt = DB.conn.createStatement()) {
                        stmt.execute("UPDATE scenario " +
                                     "SET state = 'Apriori...' " +
                                     "WHERE id = " + id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    apriori.waitFor();

                    double minConf = Double.parseDouble(args[argsPos++]);
                    double minLift = Double.parseDouble(args[argsPos++]);


                    try (Statement stmt = DB.conn.createStatement()) {
                        stmt.execute("UPDATE scenario " +
                                     "SET state = 'Rules...' " +
                                     "WHERE id = " + id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Associations rules...");
                    AprioriToDatabase.convert(id, basePathId + "/apriori.out",
                                              basePathId + "/input.trans.dict",
                                              minConf,
                                              minLift);
                    break;
                default:
                    throw new RuntimeException("Invalid process type");
            }


            try (Statement stmt = DB.conn.createStatement()) {
                stmt.execute("UPDATE scenario " +
                             "SET doing = 0 " +
                             "WHERE id = " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Donedededede");
        } catch (Exception e) {
            e.printStackTrace();

            try (Statement stmt = DB.conn.createStatement()) {
                stmt.execute("UPDATE scenario " +
                             "SET state = 'An unknown error happened' " +
                             "WHERE id = " + id);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
