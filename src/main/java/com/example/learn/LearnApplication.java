package com.example.learn;

import com.example.learn.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearnApplication implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private TagService tagService;

  @Autowired
  private RoleService roleService;

  public static void main(String[] args) {
    SpringApplication.run(LearnApplication.class, args);
  }

  @Override
  public void run(String... args) {
//    Random random = new Random();
//    //import role
//    roleService.createRole(new Role("user"));
//    roleService.createRole(new Role("admin"));
//
//    //import user
//    userService.createNewUser("Cuong", "cuongpham@gmail.com", "123456", 2);
//    userService.createNewUser("Lai", "laipham@gmail.com", "123456", 1);
//    userService.createNewUser("Xuyen", "xuyennguyen@gmail.com", "123456", 1);
//    userService.createNewUser("Linh", "linhnguyen@gmail.com", "123456", 1);
//    userService.createNewUser("Tuan", "nhinguyen@gmail.com", "123456", 1);
//    userService.createNewUser("Nhan", "nhannguyen@gmail.com", "123456", 1);
//    userService.createNewUser("Kieu", "kieupham@gmail.com", "123456", 1);
//    userService.createNewUser("Thi", "thinguyen@gmail.com", "123456", 1);
//    userService.createNewUser("Quy", "quyle@gmail.com", "123456", 1);
//    userService.createNewUser("Tu", "tule@gmail.com", "123456", 1);
//
//    //import post
//    postService.createNewPost("Be a good Java developer", "Be a good Java developer", 1);
//    postService.createNewPost("Wordpress introduce", "One of the best thing about WordPress is that you can customise almost anything. In the admin area you can see a list of all the posts you have added in WordPress. Within this table it shows the basic information for each of the posts, the title, the author, the category, tags, comments and the date the post was published.", 1);
//    for (int k = 3; k <= 120; k ++) {
//      try {
//        postService.createNewPost("Test title " + k, "Test content " + k, random.nextInt(10) + 1);
//      }catch (Exception e) {
//        System.out.println("Error: " + e);
//      }
//    }
//
//    //import tag
//    tagService.createNewTag("Java");
//    tagService.createNewTag("PHP");
//    tagService.createNewTag("JavaScript");
//    tagService.createNewTag("HTML");
//    tagService.createNewTag("CSS");
//    tagService.createNewTag("SASS");
//    tagService.createNewTag("GoLang");
//    tagService.createNewTag("Python");
//
//    // import tag_post
//    for (int i = 0; i < 250; i++) {
//      try {
//        Tag tag = tagService.getTagById(random.nextInt(8) + 1);
//        Set<Post> listPost = tag.getPosts();
//        Set<Post> newPosts = listPost.size() > 0 ? listPost: new HashSet<Post>();
//        newPosts.add(postService.findPostById(random.nextInt(50) + 1));
//        tag.setPosts(newPosts);
//        tagService.updateTagPost(tag);
//      } catch (Exception e) {
//        System.out.println("Error: " + e);
//      }
//    }
//
//    //import Comment
//    for (int j = 0; j < 300; j ++) {
//      try {
//        commentService.createComment("test content comment parent " + j, random.nextInt(120) + 1, random.nextInt(10) + 1, 0);
//      }catch (Exception e) {
//        System.out.println("Error: " + e);
//      }
//    }

  }
}
