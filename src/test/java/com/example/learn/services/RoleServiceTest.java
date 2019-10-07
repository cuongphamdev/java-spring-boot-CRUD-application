package com.example.learn.services;

import com.example.learn.Utils;
import com.example.learn.daos.RoleDAO;
import com.example.learn.models.Role;
import com.example.learn.services.impl.RoleServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceTest {

  @Mock
  RoleDAO roleDAO;

  @InjectMocks
  RoleService roleService = new RoleServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  public void setupEach() {
    when(roleDAO.create(any())).thenReturn(ServiceDataTest.dummyRole);
    when(roleDAO.findById(anyLong())).thenAnswer(invocation -> {
      long roleId = invocation.getArgument(0);

      if (Utils.checkValidBetween(roleId, 0, ServiceDataTest.dummyRoleList.length)) {
        return ServiceDataTest.dummyRoleList[(int) roleId - 1];
      }
      return null;
    });
  }

  @Test
  @DisplayName("createRole success")
  public void createRoleSuccess () {
    Role result = roleService.createRole(ServiceDataTest.dummyRole);
    assertEquals(result, ServiceDataTest.dummyRole);
  }

  @Test
  @DisplayName("findById success")
  public void findByIdSuccess () {
    Role result = roleService.findById(1);
    assertEquals(ServiceDataTest.dummyRoleList[0], result);
  }

  @Test
  @DisplayName("findById fail")
  public void findByIdFail () {
    Role result = roleService.findById(ServiceDataTest.dummyRoleList.length);
    assertNull(result);
  }
}
