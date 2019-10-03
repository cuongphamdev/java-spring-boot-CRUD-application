# CRUD SPRING BOOT APPLICATION

## Introduce: 

This project is design about blog website. You can come to website to view the post and by login you can comment and post a post in the website, it will share to anyone.

## Install:

### Step 1: check postgreSQL, java and maven are install or not, if not install it
```
    java -version
    mvn -version
```

### Step 2: Clone the project

```
    git clone https://github.com/cuongphamdev/java-spring-boot-CRUD-application.git
```

### Step 3: checkout to develop branch
```
    git checkout develop
```

### Step 4: import packages need

```
    mvn clean install
```
### Step 5: Create database with name spring_mvc and import file main.sql

### Step 6: Config the database information in application.properties by your database config
Example:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_mvc
spring.datasource.username=postgres
spring.datasource.password=admin
```

### Step 7: Run file LearnApplication.java in the path `src/main/java/com/example/learn`

## Let's go and taste.