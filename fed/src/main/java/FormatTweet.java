import twitter4j.Status;

import java.util.ArrayList;

/**
 * Created by m13003158 on 02/04/15.
 */
public class FormatTweet {
    private static String[] getUselessChar(){
        String[] uselessChar = {
                "", ",", "\"", "?", "!", ":", "-",
                "RT", ".", "..", "..."
        };

        return uselessChar;
    }

    public static String format(String tweet){
        for(String uselessChar : FormatTweet.getUselessChar()){
            tweet.replace(uselessChar, "");
        }

        return tweet;
    }

    public static String createTweet(Status status){
        String location;
        String tweet;
        String currentTweet = status.getText();

        if(status.getPlace() != null){
            location = status.getPlace().getCountry();
        } else {
            location = status.getLang();
        }

        for(String s : FormatTweet.getUselessChar()){
                currentTweet.replace(s, "");
        }

        // "Wed Apr 01 03:30:08 CEST 2015"
        // java.util.Date is deprecated so ...
        String day = status.getCreatedAt().toString().split(" ")[0];
        String hour = status.getCreatedAt().toString().split(" ")[3].split(":")[0];

        tweet = status.getCreatedAt() + " @" + status.getUser().getScreenName() + " " + currentTweet;

        return tweet;
    }
}
