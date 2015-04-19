package hcs.fromageetdessert;

import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

@Path("/scenario")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    public class Scenario {
        private int id;

        public Scenario(int id) {
            this.id = id;
        }
    }

    @POST
    @Path("/new")
    public Scenario sayHello() {
        try (Statement st = DB.CONN.createStatement()) {
            ResultSet res = st.executeQuery("INSERT INTO scenario VALUES (DEFAULT, NULL, NULL) RETURNING id");
            return new Scenario(res.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }jj


    }
}