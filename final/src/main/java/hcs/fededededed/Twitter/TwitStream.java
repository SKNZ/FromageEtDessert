package hcs.fededededed.Twitter;

import twitter4j.*;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by m13003158 on 02/04/15.
 */
public class TwitStream {
    public static void capture(String hashtag, final int wantedTweets,
                               String fileName,
                               Consumer<Integer> progressCallback)
            throws IOException, TwitterException {
        final TwitToCSV twitToCSV = new TwitToCSV(fileName);
        Twitter
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
                twitToCSV.export(status);
                remainingTweets--;
                lastId = status.getId();
            }

            double tweetCount = (double)(wantedTweets - remainingTweets);
            progressCallback.accept((int) (tweetCount * 100/ wantedTweets));
            query.setMaxId(lastId - 1);
        }
    }
}
