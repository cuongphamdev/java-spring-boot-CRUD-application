package com.example.learn;

public class Utils {
  public static boolean checkValidBetween(Long value, long min, long max) {
    return value > min && value < max;
  }
}
