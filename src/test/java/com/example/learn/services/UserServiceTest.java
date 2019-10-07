package com.example.learn.services;

import com.example.learn.Utils;
import com.example.learn.daos.UserDAO;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.impl.UserServiceImpl;
import com.example.learn.utils.CommonUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

  @Mock
  private UserDAO userDAO;

  @Mock
  private EntityManager entityManager;

  @Mock
  private CriteriaBuilder builder;

  @Mock
  private CriteriaQuery<User> criteria;

  @Spy
  private HttpSession session;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  private void setupEach() {

    when(request.getSession(true)).thenReturn(session);
    when(userDAO.findById(1)).thenReturn(ServiceDataTest.dummyUser);
    when(userDAO.findById(2)).thenReturn(null);
    when(userDAO.create(any())).thenAnswer(
            invocation -> {
              User arguments = (User) invocation.getArgument(0);
              if (!arguments.getEmail().equals(ServiceDataTest.dummyUser.getEmail())) {
                return ServiceDataTest.dummyUser;
              }
              return null;
            }
    );
    when(userDAO.findByEmailAndPassword(anyString(), anyString())).thenAnswer(
            invocation -> {
              String email = invocation.getArgument(0);
              String password = invocation.getArgument(1);
              for (User user : ServiceDataTest.dummyUserList) {
                if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                  return user;
                }
              }
              return null;
            }
    );

    when(userDAO.searchUserByNameOrEmail(anyString())).thenAnswer(invocation -> {
      String searchQuery = invocation.getArgument(0);
      List<User> userList = new ArrayList<>();
      for (User user : ServiceDataTest.dummyUserList) {
        if (user.getEmail().contains(searchQuery)) {
          userList.add(user);
        }
      }
      return userList;
    }
    );

    when(userDAO.update(any())).thenAnswer(invocation -> {
              User user = invocation.getArgument(0);
              if (user.getEmail().equals(ServiceDataTest.dummyUser.getEmail())) {
                return ServiceDataTest.dummyUser;
              }
              return null;
            }
    );

    when(userDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyUserList));
     when(userDAO.searchByPaginationAndOrderByName(anyString(), anyString(), anyInt())).thenAnswer(invocation -> {
       String query = invocation.getArgument(0);
       String order = invocation.getArgument(1);
       int page = invocation.getArgument(2);
       List<User> userList = new ArrayList<>();
       if (page > 1) {
         return null;
       }
       for (User eachUser: ServiceDataTest.dummyUserList) {
         if (eachUser.getName().contains(query)) {
           userList.add(eachUser);
         }
       }

       if (order.equals("a2z")) {
         Collections.sort(userList, Utils.compareUserByNameASC);
       } else {
         Collections.sort(userList, Utils.compareUserByNameDESC);
       }
       Search<User> response = new Search<>(userList, userList.size(), 1, query, 1);
       return response;
     });

    when(userDAO.delete(anyLong())).thenAnswer(invocation -> {
      long userId = invocation.getArgument(0);
      if (Utils.checkValidBetween(userId, 0, ServiceDataTest.dummyUserList.length)) return userId;
      return (long) 0;
    });

    when(userDAO.searchUserEqualEmail(anyString())).thenAnswer(invocation -> {
      String query = invocation.getArgument(0);
      for (User eachUser: ServiceDataTest.dummyUserList) {
        if (eachUser.getEmail().equals(query)) return eachUser;
      }
      return null;
    });
  }

  @DisplayName("Find user by valid id and return valid user data")
  @Test
  void findUserByIdTest() {
    User result = userService.findUserById(1);
    assertSame(ServiceDataTest.dummyUser, result);
    verify(userDAO).findById(1);
  }

  @DisplayName("Find user by invalid id and return null")
  @Test
  void findUserByInvalidIdTest() {
    User result = userService.findUserById(2);
    assertSame(null, result);
    verify(userDAO).findById(2);
  }

  @DisplayName("Create new User by valid data")
  @Test
  void createNewUserWithValidDataTest() {
    User result = userService.createNewUser(ServiceDataTest.dummyUserList[1].getName(), ServiceDataTest.dummyUserList[1].getEmail(), ServiceDataTest.dummyUserList[1].getPassword(), 1);
    assertEquals(ServiceDataTest.dummyUser, result);
    verify(userDAO).create(argThat((aUser) -> aUser.getEmail().equals(ServiceDataTest.dummyUserList[1].getEmail())));
  }

  @DisplayName("Create new User by invalid data - existed email")
  @Test
  void createNewUserWithInvalidDataTest() {
    User result = userService.createNewUser("Cuong", "cuongpham.dev@gmail.com", "123456", 1);
    assertEquals(null, result);
    verify(userDAO).create(argThat((aUser) -> aUser.getEmail().equals(ServiceDataTest.dummyUser.getEmail())));
  }

  @DisplayName("loginByEmailAndPassword return valid user data")
  @Test
  void loginWithValidEmailAndPassword () {
    User result = userService.loginByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail(), "123456");
    assertEquals(ServiceDataTest.dummyUserList[1], result);
    verify(userDAO).findByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail(), "e10adc3949ba59abbe56e057f20f883e");
  }

  @DisplayName("loginByEmailAndPassword return invalid user data")
  @Test
  void loginWithInvalidEmailAndPassword () {
    User result = userService.loginByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail() + "wrong", ServiceDataTest.dummyUserList[1].getPassword());
    assertEquals(null, result);
    verify(userDAO).findByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail() + "wrong", CommonUtils.getHashPassword(ServiceDataTest.dummyUserList[1].getPassword()));
  }

  @DisplayName("searchUserByNameOrEmail valid search query")
  @Test
  void searchUserByNameOrEmailValidQuery () {
    String queryString = "test";
    List<User> result = userService.searchUserByNameOrEmail(queryString);
    assertNotEquals(null, result);
    verify(userDAO).searchUserByNameOrEmail(queryString);
  }

  @DisplayName("searchUserByNameOrEmail valid search query")
  @Test
  void searchUserByNameOrEmailInvalidQuery () {
    String searchString = "wrong data";
    List<User> result = userService.searchUserByNameOrEmail(searchString);
    assertEquals(0, result.size());
    verify(userDAO).searchUserByNameOrEmail(searchString);
  }

  @DisplayName("update success")
  @Test
  void update () {
    User result = userService.updateUser(ServiceDataTest.dummyUser);
    assertEquals(ServiceDataTest.dummyUser, result);
  }

  @DisplayName("update fail")
  @Test
  void updateFail () {
    User user = new User(ServiceDataTest.dummyUser.getName(), "wrongemail@dot.com", ServiceDataTest.dummyUser.getPassword());
    User result = userService.updateUser(user);
    assertEquals(null, result);
  }

  @DisplayName("findAllUser success")
  @Test
  void findAll () {
    List<User> result = userService.findAllUser();
    assertEquals(Arrays.asList(ServiceDataTest.dummyUserList), result);
  }

  @DisplayName("checkAuthentication if user has loggedin")
  @Test
  public void checkAuthenticationSuccessReturnTrue () {
    when(session.getAttribute("loginSession")).thenReturn(ServiceDataTest.dummyUser);
    boolean result = userService.checkAuthentication(request);
    assertTrue(result);
  }

  @DisplayName("checkAuthentication if user hasn't loggedin")
  @Test
  public void checkAuthenticationSuccessReturnFalse () {
    when(session.getAttribute("loginSession")).thenReturn(null);
    boolean result = userService.checkAuthentication(request);
    assertFalse(result);
  }

  @DisplayName("setAuthentication success")
  @Test
  public void setAuthenticationSuccess () {
    userService.setAuthenticate(request, ServiceDataTest.dummyUser);
    verify(session).setAttribute("loginSession", ServiceDataTest.dummyUser);
  }

  @DisplayName("removeAuthenticate success")
  @Test
  public void removeAuthenticateSuccess () {
    userService.removeAuthenticate(request);
    verify(session).removeAttribute("loginSession");
  }

  @DisplayName("getCurrentUser after login success")
  @Test
  public void getCurrentUserSuccess () {
    when(session.getAttribute("loginSession")).thenReturn(ServiceDataTest.dummyUser);
    User result = userService.getCurrentUser(request);
    verify(session).getAttribute("loginSession");
    assertEquals(ServiceDataTest.dummyUser, result);
  }

  @DisplayName("getCurrentUser if user hasn't login")
  @Test
  public void getCurrentUserSuccessReturnNull () {
    User result = userService.getCurrentUser(request);
    verify(session).getAttribute("loginSession");
    assertNull(result);
  }

  @DisplayName("getCurrentUserId and return 1")
  @Test
  public void getCurrentUserIdIfUserLogin () {
    User loginUser = new User (ServiceDataTest.dummyUser.getName(), ServiceDataTest.dummyUser.getEmail(), ServiceDataTest.dummyUser.getPassword());
    loginUser.setId(1);
    when (session.getAttribute("loginSession")).thenReturn(loginUser);
    long result = userService.getCurrentUserId(request);
    assertEquals(1, result);
  }

  @DisplayName("getCurrentUserId not login and return 0")
  @Test
  public void getCurrentUserIdIfUserNotLogin () {
    when (session.getAttribute("loginSession")).thenReturn(null);
    long result = userService.getCurrentUserId(request);
    assertEquals(0, result);
  }

  @DisplayName("Delete user success then return id")
  @Test
  public void deleteUserSuccess() {
    long result = userService.deleteUser(1);
    assertEquals(1, result);
  }

  @DisplayName("Delete user success then return id")
  @Test
  public void deleteUserFail() {
    long result = userService.deleteUser(ServiceDataTest.dummyUserList.length);
    assertEquals((long) 0, result);
  }

  @DisplayName("findUserByEmail user success then return user")
  @Test
  public void findUserByEmailSuccess() {
    User result = userService.findUserByEmail(ServiceDataTest.dummyUserList[1].getEmail());
    assertEquals(ServiceDataTest.dummyUserList[1], result);
  }

  @DisplayName("findUserByEmail user fail then return null")
  @Test
  public void findUserByEmailFail() {
    User result = userService.findUserByEmail("wrongemail@gmail.com");
    assertNull(result);
  }

  @DisplayName("searchUserInOrderAndPagination success with valid data")
  @Test
  public void searchUserInOrderAndPaginationSuccess() {
    Search<User> result = userService.searchUserInOrderAndPagination("Cuong", "a2z", 1);
    assertEquals(3, result.getListItems().size());
  }

  @DisplayName("searchUserInOrderAndPagination fail with invalid page")
  @Test
  public void searchUserInOrderAndPaginationFail() {
    Search<User> result = userService.searchUserInOrderAndPagination("test", "a2z", 1);
    assertEquals(0, result.getListItems().size());
  }
}
