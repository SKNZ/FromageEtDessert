package hcs.fededededed.Twitter;

import twitter4j.Status;

/**
 * Created by m13003158 on 02/04/15.
 */
public class FormatTweet {
    private static String[] getUselessString() {
        String[] uselessString = {
                "le",
                "la",
                "les",
                "un",
                "une",
                "de",
                "des",
                "\\à",
                "\\a",
                "et",
                "ou",
                "que",
                "je",
                "il",
                "elle",
                "and",
                "your",
                "you",
                "her",
                "his",
                "them",
                "they",
                "or",
                "but",
                "where",
                "au",
                "avec",
                "what",
                "when",
                "why",
                "because",
                "for",
                "do",
                "did",
                "done",
                "if",
                "it",
                "she",
                "he",
                "the",
                "pas",
                "par",
                "pour",
                "mes",
                "ses",
                "leur",
                "leurs",
                "son",
                "a",
                "rt",
                "sur",
                "mais",
                "ça",
                "votre",
                "vous",
                "en",
                "du",
                "on"
        };

        return uselessString;
    }

    private static String[] getUselessChar() {
        String[] uselessChar = {
                "?", "!", "-", ",", "'", "\"", "\\\n", ":"
        };

        return uselessChar;
    }

    public static String[] createTweet(Status status, String hashtag) {
        String location;
        String currentTweet = status.getText().toLowerCase();

        // If we have the exact location or only the language
        if (status.getPlace() != null) {
            location = status.getPlace().getCountry();
        } else {
            location = status.getLang();
        }

        // Clean the tweet for useless character
        for (String s : FormatTweet.getUselessString()) {
            // Delete the useless character and all useless spaces
            currentTweet = currentTweet.replaceAll("\\b" + s + "\\b", " ")
                                       .replaceAll("\\s+", " ");
        }

        for (String s : FormatTweet.getUselessChar()) {
            currentTweet = currentTweet.replace(s, "");
        }

        // "Wed Apr 01 03:30:08 CEST 2015"
        // java.util.Date.getDay/getHour is deprecated so ...
        String day = status.getCreatedAt().toString().split(" ")[0];
        String
                hour =
                status.getCreatedAt().toString().split(" ")[3].split(":")[0];


        String[] toWrite = currentTweet.split(" ");


        StringBuilder builder = new StringBuilder();

        for (String str : toWrite) {
            if (!str.isEmpty()) {
                if (builder.length() > 0) {
                    builder.append(";");
                }

                builder.append(str);
            }
        }

        String tweet =
                "@"
                + status.getUser().getScreenName()
                + ";"
                + builder.toString();

        return tweet.split(";");
    }
}
