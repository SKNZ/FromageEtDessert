package twitter;

import twitter4j.Status;

/**
 * Created by m13003158 on 02/04/15.
 */
public interface IExporter {

    public String[] format(Status status);

    public void export(Status status);
}
