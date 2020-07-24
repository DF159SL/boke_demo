package com.lal.blog_demo.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//赋予与数据库对应生成的能力
@Table(name = "t_blog_tags")//指定对应数据库中的表名
public class Blog_Tags {
    @Id//主键
    @GeneratedValue//生成策略，默认自动生成
    private Long blogs_id;
    private Long tags_id;

    public Blog_Tags() {
    }

    public Long getBlogs_id() {
        return blogs_id;
    }

    public void setBlogs_id(Long blogs_id) {
        this.blogs_id = blogs_id;
    }

    public Long getTags_id() {
        return tags_id;
    }

    public void setTags_id(Long tags_id) {
        this.tags_id = tags_id;
    }
}
