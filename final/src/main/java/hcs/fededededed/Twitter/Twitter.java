package hcs.fededededed.Twitter;

import twitter4j.*;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created by m13003158 on 02/04/15.
 */
public class Twitter {
    public static int capture(String hashtag, final int wantedTweets,
                              String fileName,
                              Consumer<Integer> progressCallback)
            throws IOException, TwitterException {
        final TwitToCSV twitToCSV = new TwitToCSV(fileName);
        twitter4j.Twitter
                twitter =
                new TwitterFactory(ConfigurationFactory.getInstance()
                                                       .getConfig()).getInstance();

        int remainingTweets = wantedTweets;
        long lastId = Long.MAX_VALUE;
        Query query = new Query(hashtag);

        while (remainingTweets > 0) {
            query.setCount(remainingTweets > 100 ? 100 : remainingTweets);

            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                twitToCSV.export(status, hashtag);
                remainingTweets--;
                lastId = status.getId();
            }

            double tweetCount = (double)(wantedTweets - remainingTweets);
            progressCallback.accept((int) (tweetCount * 100/ wantedTweets));
            query.setMaxId(lastId - 1);
        }

        return wantedTweets;
    }

    public static int captureStream(String hashtag, final int wantedTweets,
                              String fileName,
                              Consumer<Integer> progressCallback)
            throws IOException, TwitterException {
        final TwitToCSV twitToCSV = new TwitToCSV(fileName);

        Object l = new Object();

        TwitterStreamFactory twitterFactory = new TwitterStreamFactory(ConfigurationFactory.getInstance().getConfig());
        TwitterStream twitterStream = twitterFactory.getInstance();

        twitterStream.addListener(new StatusListener() {
            int i = 0;

            public void onStatus(Status status) {
                twitToCSV.export(status, hashtag);
                ++i;
                if (i % 100 == 0)
                    progressCallback.accept((int) ((double) i * 100 /
                                                   wantedTweets));

                if (i >= wantedTweets) {
                    twitterStream.cleanUp();
                    twitterStream.shutdown();
                    synchronized (l) {
                        l.notify();
                    }
                }
            }

            public void onDeletionNotice(
                    StatusDeletionNotice statusDeletionNotice) {
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });

        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(new String[]{hashtag});
        twitterStream.filter(filterQuery);

        try {
            synchronized (l) {
                l.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return wantedTweets;
    }
}
