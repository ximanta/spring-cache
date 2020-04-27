package com.stackroute.controller;

import com.stackroute.domain.Blog;
import com.stackroute.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/")
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);
    private BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("blog")
    public ResponseEntity<Blog> saveBlog(@RequestBody Blog blog) {
        Blog savedBlog = blogService.saveBlog(blog);
        return new ResponseEntity<>(savedBlog, HttpStatus.CREATED);
    }

    @GetMapping("blogs")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        logger.info(".... Fetching all Blogs");
        ResponseEntity responseEntity;
        List<Blog> retrievedBlogs = blogService.getAllBlogs();
        responseEntity = new ResponseEntity<List<Blog>>((List<Blog>) retrievedBlogs, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("blog/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable int id) {
        ResponseEntity responseEntity;
        Blog retrievedBlog = blogService.getBlogById(id);
        responseEntity = new ResponseEntity<Blog>(retrievedBlog, HttpStatus.FOUND);
        return responseEntity;
    }


    @DeleteMapping("blog/{blogId}")
    public ResponseEntity<Blog> deleteBlog(@PathVariable("blogId") int blogId) {
        ResponseEntity responseEntity;
        Blog deletedBlog = blogService.deleteBlogById(blogId);
        responseEntity = new ResponseEntity<Blog>(deletedBlog, HttpStatus.OK);

        return responseEntity;
    }

    @PutMapping("blog")
    public ResponseEntity<Blog> updateBlog(@RequestBody Blog blog) {
        logger.info(".... Updating Blog Content of id: " + blog.getBlogId());
        Blog updatedBlog = blogService.updateBlog(blog);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }


}