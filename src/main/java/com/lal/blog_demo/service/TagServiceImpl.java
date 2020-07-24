package com.lal.blog_demo.service;

import com.lal.blog_demo.dao.Blog_TagsRepository;
import com.lal.blog_demo.dao.TagRepository;
import com.lal.blog_demo.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagRepository tagRepository;
    @Resource
    private Blog_TagsRepository blog_tagsRepository;

    @Transactional//添加到事务管理
    @Override
    public Tag saveTag(Tag tag) {

        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        //id查询tag是否存在，如果使用tagRepository.findById(id).get()数据不存在时将会报错，所以需要判断是否为空
        Optional<Tag> optionalTag=tagRepository.findById(id);
        if(optionalTag!=null && optionalTag.isPresent()){
            return optionalTag.get();
        }
        return null;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(converToList(ids));
    }

    @Override
    public Tag getMaxId() {
        return tagRepository.findMaxId();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort= Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable= PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    //将字符串转换为数组
    private List<Long> converToList(String ids){
        List<Long>list=new ArrayList<>();
        if(!"".equals(ids) && ids!=null){
            String[] idarray=ids.split(",");
            for(int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new com.lal.blog.NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new com.lal.blog.NotFoundException("不存在该类型");
        } else {
            blog_tagsRepository.deleteBlogTag(id);
            tagRepository.deleteById(id);
        }
    }
}
