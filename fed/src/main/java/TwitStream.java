import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitStream {
    public static void main(String[] args) throws TwitterException, IOException {
        TwitToCSV twitToCSV = new TwitToCSV("trends");

        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                twitToCSV.saveTweet(status);
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

            @Override
            public void onScrubGeo(long l, long l1) {}

            @Override
            public void onStallWarning(StallWarning stallWarning) {}

            public void onException(Exception ex) {
                ex.printStackTrace();}
        };

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("OmxdH8dF8PagDSO5ePpZ8mZK6")
                .setOAuthConsumerSecret("wOGjv3ePR20G7MLPUcRAqKvimWbAYdSS0umNT64PXoKDIZlwj1")
                .setOAuthAccessToken("59915564-vn1q77VqbPe6RcXZ8oLzGXN6aTIP4gSiSJw5HEbpi")
                .setOAuthAccessTokenSecret("3WJMu2FvkgzPmj8wewzVnagWwsmQr2Re3ChWYPMxIpfrO");

        Configuration config = cb.build();

        TwitterStreamFactory twitterFactory = new TwitterStreamFactory(config);
        TwitterStream twitterStream = twitterFactory.getInstance();
        twitterStream.addListener(listener);

        FilterQuery filterQuery = new FilterQuery();

        Twitter twitter = new TwitterFactory(config).getInstance();
        Trend[] dailyTrends = twitter.getPlaceTrends(1).getTrends();

        String [] keywords = new String[dailyTrends.length];

        int i = 0;
        for (Trend trend : dailyTrends) {
            keywords[i++] = trend.getName();
        }

        filterQuery.track(keywords);
        twitterStream.filter(filterQuery);

    }
}
