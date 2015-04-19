package hcs.fededededed.Twitter;

import twitter4j.Status;

/**
 * Created by m13003158 on 02/04/15.
 */
public interface IExporter {

    public String[] format(Status status, String hashtag);

    public void export(Status status, String hashtag);
}
