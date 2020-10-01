package com.vrsoft.email;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class EmailTransactionConfiguration extends Configuration {
  @NotEmpty
  private String smtp_host;

  @NotEmpty
  private String smtp_socketFactoryport;

  @NotEmpty
  private String smtp_socketFactoryclass;

  @NotEmpty
  private String smtp_authentication;

  @NotEmpty
  private String smtp_port;

  @NotEmpty
  private String start_tls;

  @NotEmpty
  private String from_email;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  @NotEmpty
  private String attachment_path;

  @NotEmpty
  private String send_type;

  @NotEmpty
  private String api_url;
  
  @NotEmpty
  private String api_from;

  @NotEmpty
  private String api_key;

  @NotEmpty
  private String api_val_key;

  @NotEmpty
  private String api_domain;

  @NotEmpty
  private String api_check_url;

  @JsonProperty
  public String getApi_from() {
    return api_from;
  }

  @JsonProperty
  public void setApi_from(String api_from) {
    this.api_from = api_from;
  }

  @JsonProperty
  public String getApi_domain() {
    return api_domain;
  }

  @JsonProperty
  public void setApi_domain(String api_domain) {
    this.api_domain = api_domain;
  }

  @JsonProperty
  public String getSmtp_host() {
    return smtp_host;
  }

  @JsonProperty
  public void setSmtp_host(String smtp_host) {
    this.smtp_host = smtp_host;
  }

  @JsonProperty
  public String getSmtp_socketFactoryport() {
    return smtp_socketFactoryport;
  }

  @JsonProperty
  public void setSmtp_socketFactoryport(String smtp_socketFactoryport) {
    this.smtp_socketFactoryport = smtp_socketFactoryport;
  }

  @JsonProperty
  public String getSmtp_socketFactoryclass() {
    return smtp_socketFactoryclass;
  }

  @JsonProperty
  public void setSmtp_socketFactoryclass(String smtp_socketFactoryclass) {
    this.smtp_socketFactoryclass = smtp_socketFactoryclass;
  }

  @JsonProperty
  public String getSmtp_authentication() {
    return smtp_authentication;
  }

  @JsonProperty
  public void setSmtp_authentication(String smtp_authentication) {
    this.smtp_authentication = smtp_authentication;
  }

  @JsonProperty
  public String getSmtp_port() {
    return smtp_port;
  }

  @JsonProperty
  public void setSmtp_port(String smtp_port) {
    this.smtp_port = smtp_port;
  }

  @JsonProperty
  public String getStart_tls() {
    return start_tls;
  }

  @JsonProperty
  public void setStart_tls(String start_tls) {
    this.start_tls = start_tls;
  }

  @JsonProperty
  public String getFrom_email() {
    return from_email;
  }

  @JsonProperty
  public void setFrom_email(String from_email) {
    this.from_email = from_email;
  }

  @JsonProperty
  public String getUsername() {
    return username;
  }

  @JsonProperty
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonProperty
  public String getAttachment_path() {
    return attachment_path;
  }

  @JsonProperty
  public void setAttachment_path(String attachment_path) {
    this.attachment_path = attachment_path;
  }

  @JsonProperty
  public String getSend_type() {
    return send_type;
  }

  @JsonProperty
  public void setSend_type(String send_type) {
    this.send_type = send_type;
  }

  @JsonProperty
  public String getApi_url() {
    return api_url;
  }

  @JsonProperty
  public void setApi_url(String api_url) {
    this.api_url = api_url;
  }

  @JsonProperty
  public String getApi_key() {
    return api_key;
  }

  @JsonProperty
  public void setApi_key(String api_key) {
    this.api_key = api_key;
  }

  @JsonProperty
  public String getApi_val_key() {
    return api_val_key;
  }

  @JsonProperty
  public void setApi_val_key(String api_val_key) {
    this.api_val_key = api_val_key;
  }

  @JsonProperty
  public String getApi_check_url() {
    return api_check_url;
  }

  @JsonProperty
  public void setApi_check_url(String api_check_url) {
    this.api_check_url = api_check_url;
  }

}
