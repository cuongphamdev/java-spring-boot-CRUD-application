package com.example.learn.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {
  public static String getSearchString(String searchString) {
    return "%" + searchString.toLowerCase() + "%";
  }

  public static Map<String, String> getMapErrors(Exception e, String message) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", message);
    errors.put("error", e.getMessage().toString());
    return errors;
  }

  public static String getHashPassword (String password) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(password.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    byte[] digest = messageDigest.digest();
    String myHashPassword = DatatypeConverter
            .printHexBinary(digest).toLowerCase();
    return myHashPassword;
  }
}
