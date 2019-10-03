package com.example.learn.utils;

public class CommonUtils {
  public static String getSearchString (String searchString) {
    return "%" + searchString.toLowerCase() + "%";
  }
}
