import au.com.bytecode.opencsv.CSVWriter;
import twitter4j.Status;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitToCSV {
    private String filename;
    private CSVWriter csvWriter;
    private int tweetsCount;

    public TwitToCSV(String filename) throws IOException {
        this.filename = filename;
        this.csvWriter = new CSVWriter(new FileWriter(filename + ".csv"));
        this.tweetsCount = 0;
    }

    public void saveTweet(Status status){
        String [] toWrite;
        String tweet;

        String currentTweet = status.getLang() + " @" + status.getUser().getScreenName() + " " + status.getText();

        toWrite = currentTweet.split(" ");

        StringBuilder builder = new StringBuilder();

        System.out.println("Number of tweets : " + ++tweetsCount);

        for(String str : toWrite){
            if(builder.length() > 0)
                builder.append(";");

            builder.append(str);
        }

        tweet = status.getCreatedAt() + ";" + builder.toString();
        toWrite = tweet.split(";");

        csvWriter.writeNext(toWrite);
    }
}
