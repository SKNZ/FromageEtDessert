import au.com.bytecode.opencsv.CSVWriter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by thomasmunoz on 31/03/15.
 */
public class main {
    public static void main(String[] args) throws TwitterException, IOException, InterruptedException {

        String queryTerm = "le";

        ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("OmxdH8dF8PagDSO5ePpZ8mZK6")
                        .setOAuthConsumerSecret("wOGjv3ePR20G7MLPUcRAqKvimWbAYdSS0umNT64PXoKDIZlwj1")
                        .setOAuthAccessToken("59915564-vn1q77VqbPe6RcXZ8oLzGXN6aTIP4gSiSJw5HEbpi")
                        .setOAuthAccessTokenSecret("3WJMu2FvkgzPmj8wewzVnagWwsmQr2Re3ChWYPMxIpfrO");

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());

        Twitter twitter = twitterFactory.getInstance();
        CSVWriter csvWriter = new CSVWriter(new FileWriter(queryTerm + ".csv"));


        Query query = new Query("" + queryTerm);

        // Number of tweets per page
        query.setCount(100);

        QueryResult result = twitter.search(query);

        String currentTweet;
        String [] toWrite;
        String tweet;
        int tweetCount = 0;

        do {
            for (Status status : result.getTweets()) {

                currentTweet = status.getLang() + " @" + status.getUser().getScreenName() + " " + status.getText();

                toWrite = currentTweet.split(" ");

                StringBuilder builder = new StringBuilder();

                System.out.println("Number of tweets : " + ++tweetCount);

                for(String str : toWrite){
                    if(builder.length() > 0)
                        builder.append(";");

                    builder.append(str);
                }

                tweet = status.getCreatedAt() + ";" + builder.toString();
                toWrite = tweet.split(";");

                csvWriter.writeNext(toWrite);
            }

            // Next page of the query
            query = result.nextQuery();
            if(query != null){
                result = twitter.search(query);
            }
            Thread.sleep(10000);

        } while(query != null);

        csvWriter.close();
    }
}
