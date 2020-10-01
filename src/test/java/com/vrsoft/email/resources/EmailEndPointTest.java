package com.vrsoft.email.resources;


import com.vrsoft.email.EmailTransactionApplication;
import com.vrsoft.email.EmailTransactionConfiguration;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.assertj.core.api.Assertions.assertThat;


public class EmailEndPointTest {

  @Rule
  public final DropwizardAppRule<EmailTransactionConfiguration> RULE =
      new DropwizardAppRule<EmailTransactionConfiguration>(EmailTransactionApplication.class,
          ResourceHelpers.resourceFilePath("email-config.yml"));

  public void runSMTPServerTest() {
    Client client = new JerseyClientBuilder().build();
    String result =
        client.target(String.format("http://localhost:%d/sendEmail", RULE.getLocalPort()))
            .queryParam("emailid", "email-id").queryParam("message", "Testing")
            .queryParam("subject", "Test Email").queryParam("file_name", "").request()
            .get(String.class);


    JSONObject res = new JSONObject(result);
    assertThat(res.get("id")).isEqualTo(1);
    assertThat(res.get("status")).isEqualTo("Email Sent");

  }

  public void runMailGunAPIServerTest() {
    Client client = new JerseyClientBuilder().build();
    String result =
        client.target(String.format("http://localhost:%d/sendEmail", RULE.getLocalPort()))
            .queryParam("emailid", "email-id").queryParam("message", "Testing")
            .queryParam("subject", "Test Email").queryParam("file_name", "")
            .queryParam("send_type", "api").request().get(String.class);

    JSONObject res = new JSONObject(result);
    assertThat(res.get("id")).isEqualTo(1);
    assertThat(res.get("status")).isEqualTo(" status=200");

  }

}
