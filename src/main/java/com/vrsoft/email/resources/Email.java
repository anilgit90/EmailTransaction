package com.vrsoft.email.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.vrsoft.email.EmailTransactionConfiguration;
import com.vrsoft.email.api.EmailStatus;
import com.vrsoft.email.utils.Utils;
import com.vrsoft.hysterix.EmailValidation;

@Path("/sendEmail")
public class Email {

  private final AtomicLong counter;
  private final EmailTransactionConfiguration config;

  public Email(final EmailTransactionConfiguration config) {
    this.counter = new AtomicLong();
    this.config = config;
  }

  @GET
  @Timed
  @ExceptionMetered
  @Produces(MediaType.APPLICATION_JSON)
  public EmailStatus sendEmail(@QueryParam("emailid") final String c_Email,
      @QueryParam("message") final String c_Msg, @QueryParam("subject") final String c_Subject,
      @QueryParam("file_name") @DefaultValue("") final String file_name,
      @QueryParam("send_type") @DefaultValue("smtp") final String send_type,
      @QueryParam("email_validation") @DefaultValue("n") final String email_validation,
      @QueryParam("test_email") @DefaultValue("0") final String test_email) {

    String emailId = Utils.validateEmail(c_Email);
    String checkParams = Utils.checkParam(c_Email, c_Subject, c_Msg);

    if (emailId.length() == 0) {
      return new EmailStatus(counter.incrementAndGet(), "Invalid Email Id");
    }

    if (checkParams.length() > 0) {
      return new EmailStatus(counter.incrementAndGet(), "Invalid parameters");
    }

    if(email_validation != null){
      if (("y").equals(email_validation.trim()) && !new EmailValidation(c_Email, config).execute()) {          
          return new EmailStatus(counter.incrementAndGet(), "Incorrect Email Id");        
      }
      if ("API".equalsIgnoreCase(send_type.trim())) {
        if (file_name.trim().length() > 0) {
          return new EmailStatus(counter.incrementAndGet(),
              Utils.sendEmail_MailGun(c_Email, c_Subject, c_Msg, file_name,test_email, config).toString());
        } else {
          return new EmailStatus(counter.incrementAndGet(),
              Utils.sendEmail_MailGun(c_Email, c_Subject, c_Msg,test_email, config).toString());
        }
      } else if (file_name.trim().length() > 0) {
        return new EmailStatus(counter.incrementAndGet(),
            Utils.sendEmail_SMTP(c_Email, c_Subject, c_Msg, file_name, "", config));
      } else {
        return new EmailStatus(counter.incrementAndGet(),
            Utils.sendEmail_SMTP(c_Email, c_Subject, c_Msg, "", "", config));
      }


    } else {
      return new EmailStatus(counter.incrementAndGet(), "Incorrect Email Id");
    }
  }
}

