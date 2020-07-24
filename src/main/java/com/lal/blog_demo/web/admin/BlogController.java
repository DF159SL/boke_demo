package com.lal.blog_demo.web.admin;

import com.lal.blog_demo.po.Blog;
import com.lal.blog_demo.po.Tag;
import com.lal.blog_demo.po.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));

        //判断添加的标签是否存在新添加条目，以及无意义数字标签
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");//正则判断数字
        String str="";//定义处理后的tags字符串
        boolean flag=false;//判断字符串连接格式
        if(!"".equals(blog.getTagIds())&&blog.getTagIds()!=null){//判断该blog是否包含tags
            for(String i:blog.getTagIds().split(",")){//查询该blog添加的tags_id字符串分割循环处理
                //判断该次字符串是否是数字类型
                if(pattern.matcher(i).matches()){//是数字类型
                    Tag t=tagService.getTag(Long.valueOf(i));//查询该数字是否为已包含的tags_id
                    if(t==null) {//为空，表示为无意义数字标签直接返回到blog_list
                        attributes.addFlashAttribute("message", "请勿添加数字类型的标签");
                        return REDIRECT_LIST;
                    }else{//不为空，表示为已包含标签，重新连接到str
                        if(flag){
                            str+=","+i;
                        }else{
                            str+=i;
                        }
                        flag=true;
                    }
                }else{//不是数字类型
                    Tag newTag=new Tag();//新建tag进行添加
                    newTag.setId(tagService.getMaxId().getId()+1);//将其tag_id定义为已存在max(tag_id)+1
                    newTag.setName(i);//名称为该字符串
                    tagService.saveTag(newTag);
                    str+=(","+(tagService.getMaxId().getId()));//最后将重新查询到的max(tag_id)连接到str
                }
            }
            blog.setTags(tagService.listTag(str));//重新设定blog包含的标签
        }
        Blog b;
        if(blog.getId()==null){
            b=blogService.saveBlog(blog);//重新保存blog
        }else{
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return REDIRECT_LIST;
    }
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }
}
