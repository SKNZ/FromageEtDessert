package hcs.fededededed.Twitter;

import au.com.bytecode.opencsv.CSVWriter;
import twitter4j.Status;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitToCSV implements IExporter{
    private CSVWriter csvWriter;
    private int tweetsCount;

    public TwitToCSV(String filename) throws IOException {
        this.csvWriter = new CSVWriter(new FileWriter(filename));
        this.tweetsCount = 0;
    }

    public String[] format(Status status, String hashtag){
//        System.out.println("Number of tweets : " + ++tweetsCount);
        return FormatTweet.createTweet(status, hashtag);
    }

    public void export(Status status, String hashtag){
        csvWriter.writeNext(format(status, hashtag));
    }
}
