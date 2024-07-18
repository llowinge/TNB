package software.tnb.splunk.service;

import software.tnb.common.account.NoAccount;
import software.tnb.common.client.NoClient;
import software.tnb.common.service.Service;
import software.tnb.common.service.ServiceFactory;
import software.tnb.common.validation.NoValidation;
import software.tnb.db.common.account.SQLAccount;
import software.tnb.db.common.validation.SQLValidation;

import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class GraphQL extends Service<NoAccount, NoClient, NoValidation> {

    protected final software.tnb.db.postgres.service.PostgreSQL postgres = ServiceFactory.create(software.tnb.db.postgres.service.PostgreSQL.class);

    public void deploy() {
    }

    public void undeploy() {
    }

    public abstract String host();

    public abstract int port();

    public void openResources() {
    }

    public void closeResources() {
    }

    public String defaultImage() {
        return "hasura/graphql-engine:latest";
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        postgres.beforeAll(context);
        deploy();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        postgres.afterAll(context);
        undeploy();
    }

    public SQLValidation sqlValidation() {
        return postgres.validation();
    }

    public SQLAccount postgresAccount() {
        return postgres.account();
    }
}
