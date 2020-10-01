package com.vrsoft.email.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailStatus {
    private long id;
	private String status;
	
	public EmailStatus(long id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
	
    @JsonProperty
    public long getId() {
        return id;
    }
    
    @JsonProperty
    public String getStatus() {
      String[] split_Data = status.split(",");
      if(split_Data.length > 1){
          return split_Data[2];
      }
      return status;
    }
}
