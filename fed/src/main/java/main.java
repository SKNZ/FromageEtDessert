import au.com.bytecode.opencsv.CSVWriter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by thomasmunoz on 31/03/15.
 */
public class main {
    public static void main(String[] args) throws TwitterException, IOException, InterruptedException {

        ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("OmxdH8dF8PagDSO5ePpZ8mZK6")
                        .setOAuthConsumerSecret("wOGjv3ePR20G7MLPUcRAqKvimWbAYdSS0umNT64PXoKDIZlwj1")
                        .setOAuthAccessToken("59915564-vn1q77VqbPe6RcXZ8oLzGXN6aTIP4gSiSJw5HEbpi")
                        .setOAuthAccessTokenSecret("3WJMu2FvkgzPmj8wewzVnagWwsmQr2Re3ChWYPMxIpfrO");

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());

        Twitter twitter = twitterFactory.getInstance();
        CSVWriter csvWriter = new CSVWriter(new FileWriter("PatronIncognito.csv"));

        int tweetCount = 0;

        for(int i = 0; i < 100; ++i){
            Query query = new Query("#PatronIncognito");
            query.setCount(100);

            QueryResult result = twitter.search(query);

            String currentTweet;
            String [] toWrite;
            String tweet;

            for (Status status : result.getTweets()) {

                currentTweet = status.getLang() + " @" + status.getUser().getScreenName() + " " + status.getText();

                toWrite = currentTweet.split("\\s+");

                Arrays.sort(toWrite);

                StringBuilder builder = new StringBuilder();

                System.out.println("Number of tweets : " + tweetCount++);

                for(String str : toWrite){
                    if(builder.length() > 0)
                        builder.append(";");

                    builder.append(str);
                }

                tweet = status.getCreatedAt() + ";" + builder.toString();
                toWrite = tweet.split(";");

                csvWriter.writeNext(toWrite);
            }

            Thread.sleep(1500);
        }

        csvWriter.close();
    }
}
