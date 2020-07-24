package com.lal.blog_demo.dao;

import com.lal.blog_demo.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByName(String name);

    @Query(value = "select t from Type t")
    List<Type> findTop(Pageable pageable);
}
