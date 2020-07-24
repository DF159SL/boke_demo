package com.lal.blog_demo.web;

import com.lal.blog_demo.po.Type;
import com.lal.blog_demo.service.BlogService;
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
public class TypeShowController {
    @Resource
    private TypeService typeService;
    @Resource
    private BlogService blogService;
    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size=8,sort={"updateTime"},direction= Sort.Direction.DESC)Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Type> types=typeService.listTypeTop(10000);
        if(id==-1){
            id=types.get(0).getId();
        }
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
