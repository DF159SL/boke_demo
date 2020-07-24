package com.lal.blog_demo.web;

import com.lal.blog_demo.po.Tag;
import com.lal.blog_demo.po.Type;
import com.lal.blog_demo.service.BlogService;
import com.lal.blog_demo.service.TagService;
import com.lal.blog_demo.service.TypeService;
import com.lal.blog_demo.vo.BlogQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TagShowController {
    @Resource
    private TagService tagService;
    @Resource
    private BlogService blogService;
    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size=8,sort={"updateTime"},direction= Sort.Direction.DESC)Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Tag> tags=tagService.listTagTop(10000);
        if(id==-1){
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}
