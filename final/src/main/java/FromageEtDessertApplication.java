import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by sknz on 4/18/15.
 */
public class FromageEtDessertApplication extends Application<FromageEtDessertConfiguration> {
    public static void main(String[] args) throws Exception {
        new FromageEtDessertApplication().run(args);
    }

    @Override
    public String getName() {
        return "fromageetdessert";
    }

    @Override
    public void initialize(Bootstrap<FromageEtDessertConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(FromageEtDessertConfiguration fromageEtDessertConfiguration, Environment environment) throws Exception {

    }
}
