package com.lal.blog_demo.dao;

import com.lal.blog_demo.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    @Transactional
    @Query(value = "select * from t_tag order by id desc limit 0,1",nativeQuery = true)
    Tag findMaxId();

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
