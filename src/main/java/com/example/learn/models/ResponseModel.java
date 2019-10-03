package com.example.learn.models;

import java.util.Map;

public class ResponseModel<T> {

  private Map<String, T> data;
  private Map<String, String> errors;

  public ResponseModel() {
  }

  public Map<String, T> getData() {
    return data;
  }

  public void setData(Map<String, T> data) {
    this.data = data;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }
}
