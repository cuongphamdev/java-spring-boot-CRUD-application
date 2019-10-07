package com.example.learn;

import com.example.learn.models.Post;
import com.example.learn.models.User;

import java.util.Comparator;

public class Utils {
  public static boolean checkValidBetween(Long value, long min, long max) {
    return value > min && value < max;
  }
  public static Comparator<User> compareUserByNameASC = (User u1, User u2) ->
          u1.getName().compareTo( u2.getName() );
  public static Comparator<User> compareUserByNameDESC = (User u1, User u2) ->
          u2.getName().compareTo( u1.getName() );
  public static Comparator<Post> comparePostByTitleDESC = (Post p1, Post p2) ->
          p2.getTitle().compareTo( p1.getTitle() );
  public static Comparator<Post> comparePostByTitleASC = (Post p1, Post p2) ->
          p1.getTitle().compareTo( p2.getTitle() );
}
