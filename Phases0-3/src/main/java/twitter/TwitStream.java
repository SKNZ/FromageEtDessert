package twitter;

import twitter4j.*;

import java.io.IOException;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitStream {
    public static void main(String[] args) throws TwitterException, IOException {
       /* final TwitToCSV twitToCSV = new TwitToCSV("trendslol");

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
        twitterStream.filter(filterQuery);*/
        capture();

    }

    public static void capture() throws IOException {
        final TwitToCSV twitToCSV = new TwitToCSV("trendslol");
        TwitterStreamFactory twitterFactory = new TwitterStreamFactory(ConfigurationFactory.getInstance().getConfig());
        Twitter twitter = new TwitterFactory(ConfigurationFactory.getInstance().getConfig()).getInstance();


        int wantedTweets = 1000;
        long lastSearchID = Long.MAX_VALUE;
        long firstQueryID;
        int remainingTweets = wantedTweets;
        Query query = new Query("#UMP");

        try
        {

            int count = 0;
            while(remainingTweets > 0)
            {
                remainingTweets = wantedTweets - count;

                if(remainingTweets > 100)
                {
                    query.count(100);
                }
                else
                {
                    query.count(remainingTweets);
                }
                QueryResult result = twitter.search(query);

                int i = 0;
                for(Status status : result.getTweets()){
                if(i == 99){
                   firstQueryID = status.getId();
                    query.setMaxId(firstQueryID);
                }
                    twitToCSV.export(status);
                ++i;

                remainingTweets = wantedTweets - count;
            }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
