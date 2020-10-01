package com.vrsoft.email.utils;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.vrsoft.email.EmailTransactionConfiguration;

public class EmailValidation extends HystrixCommand<Boolean> {

  private final String emailid;
  private final EmailTransactionConfiguration config;

  public EmailValidation(String emailid, EmailTransactionConfiguration config) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("EmailValidation"))
        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
        .withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
        .withExecutionTimeoutInMilliseconds(500)));
    this.emailid = emailid;
    this.config = config;
  }

  @Override
  protected Boolean run() throws Exception {
    return Utils.checkValidEmail(emailid, config);
  }

  @Override
  protected Boolean getFallback() {
    return true;
  }


}
