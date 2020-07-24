package com.lal.blog_demo.dao;

import com.lal.blog_demo.po.Blog_Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface Blog_TagsRepository extends JpaRepository<Blog_Tags,Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from t_blog_tags where tags_id=?1",nativeQuery = true)
    int deleteBlogTag(Long tags_id);
}
