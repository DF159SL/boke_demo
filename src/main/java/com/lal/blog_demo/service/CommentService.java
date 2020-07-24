package com.lal.blog_demo.service;

import com.lal.blog_demo.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
