package com.stackroute.service;

import com.stackroute.domain.Blog;

import com.stackroute.repository.BlogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "blog")
@Service
public class BlogServiceImpl implements BlogService {
    private BlogRepository blogRepository;

    public BlogServiceImpl() {
    }

    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Caching(evict = {
            @CacheEvict(value="allblogcache", allEntries = true),
            @CacheEvict(value="blogcache", key = "#blog.blogId")
    })
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Cacheable(value="allblogcache")
    @Override
    public List<Blog> getAllBlogs() {
        System.out.println("******* "+ blogRepository.findAll().toString());
        return (List<Blog>) blogRepository.findAll();

    }

    @Cacheable(value="blogcache", key = "#blogId")
    @Override
    public Blog getBlogById(int blogId) {
        Blog retrievedBlog = null;
        retrievedBlog = blogRepository.findById(blogId).get();
//        System.out.println("******* "+retrievedBlog);
        return retrievedBlog;
    }
    @Caching(evict = {
            @CacheEvict(value="allblogcache", allEntries = true),
            @CacheEvict(value="blogcache", key = "#blogId")
    })
    @Override
    public Blog deleteBlogById(int blogId) {
        Blog blog = null;
        Optional optional = blogRepository.findById(blogId);
        if (optional.isPresent()) {
            blog = blogRepository.findById(blogId).get();
            blogRepository.deleteById(blogId);
        }
        return blog;
    }


    @CachePut(key = "#blog.blogId")
    @Override
    public Blog updateBlog(Blog blog) {
        Blog updatedBlog = null;
        Optional optional = blogRepository.findById(blog.getBlogId());
        if (optional.isPresent()) {
            Blog getBlog = blogRepository.findById(blog.getBlogId()).get();
            getBlog.setBlogContent(blog.getBlogContent());
            updatedBlog = saveBlog(getBlog);
        }
        return updatedBlog;

    }

}
