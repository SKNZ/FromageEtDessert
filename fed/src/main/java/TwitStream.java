import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitStream {
    public static void main(String[] args) throws TwitterException, IOException {
        final TwitToCSV twitToCSV = new TwitToCSV("trends_15h");

        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                twitToCSV.export(status);
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

            @Override
            public void onScrubGeo(long l, long l1) {}

            @Override
            public void onStallWarning(StallWarning stallWarning) {}
            public void onException(Exception ex) {ex.printStackTrace();}
        };

        TwitterStreamFactory twitterFactory = new TwitterStreamFactory(ConfigurationFactory.getInstance().getConfig());
        TwitterStream twitterStream = twitterFactory.getInstance();
        twitterStream.addListener(listener);

        FilterQuery filterQuery = new FilterQuery();

        Twitter twitter = new TwitterFactory(ConfigurationFactory.getInstance().getConfig()).getInstance();

        // France trend topics
        Trend[] dailyTrends = twitter.getPlaceTrends(23424819).getTrends();

        String [] keywords = new String[dailyTrends.length];

        int i = 0;

        for (Trend trend : dailyTrends) {
            keywords[i++] = trend.getName();
        }

        filterQuery.track(keywords);
        twitterStream.filter(filterQuery);

    }
}
