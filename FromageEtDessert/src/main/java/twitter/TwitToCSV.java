package twitter;

import au.com.bytecode.opencsv.CSVWriter;
import twitter4j.Status;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitToCSV implements IExporter{
    private String filename;
    private CSVWriter csvWriter;
    private int tweetsCount;

    public TwitToCSV(String filename) throws IOException {
        this.filename = filename;
        this.csvWriter = new CSVWriter(new FileWriter(filename + ".csv"));
        this.tweetsCount = 0;
    }

    public String[] format(Status status){
        System.out.println("Number of tweets : " + ++tweetsCount);

        return FormatTweet.createTweet(status);
    }

    public void export(Status status){
        csvWriter.writeNext(format(status));
    }
}
