package com.example.learn.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonUtils {
  public static String getSearchString (String searchString) {
    return "%" + searchString.toLowerCase() + "%";
  }
  public static Map<String, String> getMapErrors (Exception e, String message) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", message);
    errors.put("error", e.getMessage().toString());
    return errors;
  }
}
