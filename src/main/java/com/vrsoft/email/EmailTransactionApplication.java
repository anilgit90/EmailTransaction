package com.vrsoft.email;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.vrsoft.email.health.EmailAPICheck;
import com.vrsoft.email.resources.Email;

public class EmailTransactionApplication extends Application<EmailTransactionConfiguration> {

    public static void main(final String[] args) throws Exception {
        new EmailTransactionApplication().run(args);
    }

    @Override
    public String getName() {
        return "EmailTransaction";
    }

    @Override
    public void initialize(final Bootstrap<EmailTransactionConfiguration> bootstrap) {
      
    }

    @Override
    public void run(final EmailTransactionConfiguration configuration,
                    final Environment environment) {
        final Email email = new Email(configuration);
        environment.healthChecks().register("MailGun-status", new EmailAPICheck(configuration));
        environment.jersey().register(email);
    }

}
