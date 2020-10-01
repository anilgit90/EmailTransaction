package com.vrsoft.email.health;

import com.codahale.metrics.health.HealthCheck;
import com.vrsoft.email.EmailTransactionConfiguration;
import com.vrsoft.email.utils.Utils;

public class EmailAPICheck extends HealthCheck {
	
	private final EmailTransactionConfiguration config;
	
	public EmailAPICheck(EmailTransactionConfiguration config) {
		this.config = config;
	}
	
	@Override
	protected Result check() throws Exception {
			
		if(Utils.getMailStatus(config)){
			return Result.healthy();
		}
		
		return Result.unhealthy("Problems in Mailgun API");
	}

}
