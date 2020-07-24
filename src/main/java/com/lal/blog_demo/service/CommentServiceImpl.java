package com.lal.blog_demo.service;

import com.alibaba.fastjson.JSON;
import com.lal.blog_demo.dao.CommentRepository;
import com.lal.blog_demo.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService{
    @Resource
    private CommentRepository commentRepository;
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort= Sort.by("createTime");
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);//各评论的顶级评论
        return eachComment(comments);//返回处理过的评论集合
    }

    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId=comment.getParentComment().getId();
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {//循环将顶级评论节点复制到新的list中
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);//commentsView等同于传入参数comments,只是顶级评论集合
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {//comments就是顶级评论集合

        for (Comment comment : comments) {//遍历传递的顶级评论节点集合
            List<Comment> replys1 = comment.getReplyComments();//获取各顶级评论节点的子评论
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //System.out.println("tempReplys:"+JSON.toJSONString(tempReplys));
            //清除临时存放区
            tempReplys = new ArrayList<>();
            //重置flag
            flag=true;
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    //三级以上评论flag控制不重复添加父评论
    private boolean flag=true;
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {//comment即顶级评论的第一个子节点
        if(flag){//判断是否为三级以上评论
            tempReplys.add(comment);//顶级子节点添加到临时存放集合
        }
        if (comment.getReplyComments().size()>0) {//如果该子节点存在下级节点
            List<Comment> replys = comment.getReplyComments();//获取子节点的下级节点
            for (Comment reply : replys) {//同样遍历下级节点
                tempReplys.add(reply);//添加到顶级评论的全部下级节点临时存放集合中
                if (reply.getReplyComments().size()>0) {//再次判断是否存在下级节点
                    flag=false;//第一次遇见三级评论时将flag设为false，不再重复添加该评论
                    recursively(reply);//调用自身
                }
            }
        }
    }
}
