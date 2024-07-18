package software.tnb.splunk.resource.local;

import software.tnb.common.deployment.Deployable;
import software.tnb.splunk.service.GraphQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(GraphQL.class)
public class LocalGraphQL extends GraphQL implements Deployable {

    private static final Logger LOG = LoggerFactory.getLogger(LocalGraphQL.class);

    private GraphQLContainer container;


    @Override
    public void deploy() {
        LOG.info("Starting GraphQL container");
        Map<String, String> envs = containerEnvironment();
        List<Integer> ports = new ArrayList<>();
        ports.add(8080);
        container = new GraphQLContainer(defaultImage(), ports, envs);
        container.start();
        LOG.info("GraphQL container started");
    }

    @Override
    public void undeploy() {
        if (container != null) {
            LOG.info("Stopping GraphQL container");
            container.stop();
        }
    }

    @Override
    public String host() {
        return container.getHost();
    }

    @Override
    public int port() {
        return container.getMappedPort(8080);
    }

    public Map<String, String> containerEnvironment() {
        return new HashMap<>(Map.of(
            "HASURA_GRAPHQL_DATABASE_URL", String.format("postgres://%s:%s@%s:%s/%s",
                postgres.account().username(),
                postgres.account().password(),
//                postgres.host(),
                "host.docker.internal",
                postgres.localPort(),
                postgres.account().database()),
            "HASURA_GRAPHQL_ENABLE_CONSOLE", "true"
        ));
    }
}
