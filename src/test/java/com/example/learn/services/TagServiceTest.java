package com.example.learn.services;

import com.example.learn.daos.TagDAO;
import com.example.learn.models.Tag;
import com.example.learn.services.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TagServiceTest {

  @Mock
  TagDAO tagDAO;

  @InjectMocks
  TagService tagService = new TagServiceImpl();

  @Before
  private void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  private void setupEach() {
    when(tagDAO.create(any())).thenAnswer(
            invocation -> {
              Tag tagData = (Tag) invocation.getArgument(0);
              if (tagData.getName().equals("")) {
                return null;
              }
              return ServiceDataTest.dummyTag;
            }
    );

    when(tagDAO.findById(0)).thenReturn(null);
    when(tagDAO.findById(1)).thenReturn(ServiceDataTest.dummyTag);

    when(tagDAO.update(any())).thenReturn(ServiceDataTest.dummyTag);

    when(tagDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyTagList));

    when(tagDAO.delete(anyLong())).thenAnswer(
            invocation -> {
              long tagId = (long) invocation.getArgument(0);
              return tagId;
            }
    );
    when(tagDAO.searchTag(any())).thenAnswer(
            invocation -> {
              String searchNameString = (String) invocation.getArgument(0);
              List<Tag> tagListFound = new ArrayList<>();
              for (Tag tag: ServiceDataTest.dummyTagList) {
                if(tag.getName().contains(searchNameString)) tagListFound.add(tag);
              }
              return tagListFound;
            }
    );
  }

  @DisplayName("createNewTag success")
  @Test
  void createNewTagSuccess() {
  Tag result = tagService.createNewTag("test");
  assertEquals(ServiceDataTest.dummyTag, result );
  }

  @DisplayName("createNewTag fail")
  @Test
  void createNewTagFail() {
    Tag result = tagService.createNewTag("");
    assertEquals(null, result );
  }

  @DisplayName("getAllTags success")
  @Test
  void getAllTagsSuccess() {
    List<Tag> result = tagService.getAllTags();
    assertEquals(Arrays.asList(ServiceDataTest.dummyTagList), result );
  }

  @DisplayName("deleteTag success")
  @Test
  void deleteTagSuccess() {
    long result = tagService.deleteTag(1);
    assertEquals(1, result );
  }

  @DisplayName("deleteTag fail")
  @Test
  void deleteTagFail() {
    long result = tagService.deleteTag(0);
    assertEquals(0, result );
  }

  @DisplayName("searchTags success")
  @Test
  void searchTagSuccess() {
    List<Tag> result = tagService.searchTags("");
    assertEquals(Arrays.asList(ServiceDataTest.dummyTagList), result );
  }

  @DisplayName("searchTags fail")
  @Test
  void searchTagFail() {
    List<Tag> result = tagService.searchTags("wrong-input");
    assertEquals(0, result.size() );
  }

  @DisplayName("getTagById success")
  @Test
  void getTagByIdSuccess() {
    Tag result = tagService.getTagById(1);
    assertEquals(ServiceDataTest.dummyTag, result );
  }

  @DisplayName("getTagById fail")
  @Test
  void getTagByIdTagFail() {
    Tag result = tagService.getTagById(0);
    assertEquals(null, result );
  }

  @DisplayName("updateTagPost success")
  @Test
  void updateTagPostSuccess() {
    Tag result = tagService.updateTagPost(ServiceDataTest.dummyTag);
    assertEquals(ServiceDataTest.dummyTag, result );
  }

  @DisplayName("updateTag success")
  @Test
  void updateTagSuccess() {
    Tag result = tagService.updateTag(1, ServiceDataTest.dummyTag.getName());
    assertEquals(ServiceDataTest.dummyTag, result );
  }

  @DisplayName("updateTag fail")
  @Test
  void updateTagFail() {
    Tag result = tagService.updateTag(0,  ServiceDataTest.dummyTag.getName());
    assertEquals(null, result );
  }
}
