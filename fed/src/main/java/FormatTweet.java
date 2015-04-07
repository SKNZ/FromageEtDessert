import twitter4j.Status;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by m13003158 on 02/04/15.
 */
public class FormatTweet {
    private static String[] getUselessChar(){
        String[] uselessChar = {
                "\\,", "\"", "\\?", "\\!", "\\:", "\\-", "\\," , "\\'",
                "RT", "\\.", "..", "...", "le", "la", "les",
                "un", "une", "de", "des", "\\à", "\\a", "et", "ou",
                "par", "pour", "mes", "ses", "leur", "leurs", "son",
                "\\\n", "[a-z]"
        };

        return uselessChar;
    }

    public static String[] createTweet(Status status){
        String location;
        String currentTweet = status.getText().toLowerCase();

        // If we have the exact location or only the language
        if(status.getPlace() != null){
            location = status.getPlace().getCountry();
        } else {
            location = status.getLang();
        }

        // Clean the tweet for useless character
        for(String s : FormatTweet.getUselessChar()){
            // Delete the useless character and all useless spaces
                currentTweet = currentTweet.replaceAll("\\b" + s + "\\b", " ")
                                            .replaceAll("\\s+", " ");
        }

        // "Wed Apr 01 03:30:08 CEST 2015"
        // java.util.Date.getDay/getHour is deprecated so ...
        String day = status.getCreatedAt().toString().split(" ")[0];
        String hour = status.getCreatedAt().toString().split(" ")[3].split(":")[0];


        String[] toWrite = currentTweet.split(" ");

        StringBuilder builder = new StringBuilder();

        for(String str : toWrite){
            if(builder.length() > 0)
                builder.append(";");

            builder.append(str);
        }

        String tweet = status.getCreatedAt() + ";" + location + ";" + "@" + status.getUser().getScreenName() + ";" +
                builder.toString() + ";" + day + ";" + hour;

        return tweet.split(";");
    }

    private static String removeUrl(String str)
    {
        String urlPattern = "((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        int i = 0;

        while (m.find()) {
            str = str.replaceAll(m.group(i),"").trim();
            i++;
        }
        return str;
    }

}
