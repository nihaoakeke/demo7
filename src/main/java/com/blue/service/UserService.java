package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.*;
import com.blue.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
//public class UserService  extends ServiceImpl<UserMapper, User>{
public class UserService extends ServiceImpl<UserMapper, User>{

    @Autowired
    private BlogLikeDao blogLikeDao;

    @Autowired
    private FollowLinkDao followLinkDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private CommentLikeDao commentLikeDao;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CollectDao collectDao;


    /**
     *
     * @param email
     * @return
     */

    public User selectByeamil(String email)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_email",email);
        User user = userMapper.selectOne(qw);
        return user;
    }

    /**
     * 根据用户名称来查询用户名称
     * @param name
     * @return
     */

    public User selectByname(String name)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_name",name);
        User user = userMapper.selectOne(qw);
        return user;
    }

    /**
     * 根据用户的笔名查询用户
     * @param nickname
     * @return
     */

   public User selectBynickname(String nickname)
   {
       QueryWrapper qw = new QueryWrapper();
       qw.eq("user_nickname",nickname);
       User user = userMapper.selectOne(qw);
       return user;
   }

    /**
     * 根据用户笔名来模糊查询
     * @param nickname
     * @return
     */
   public List<User> selectLikenickname(String nickname)
   {
       QueryWrapper qw = new QueryWrapper();
       qw.like("user_nickname",nickname);
       List<User> userList = userMapper.selectList(qw);
       return userList;
   }

    /**
     * 根据用户名来模糊查询
     * @param name
     * @return
     */

    public List<User> selectLikename(String name)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.like("user_name",name);
        List<User> userList = userMapper.selectList(qw);
        return userList;
    }



    public List<Collect> selectCollectByuid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.like("user_id",uid);
        List<Collect>  collectList=collectDao.selectList(qw);
        return collectList;
    }



    public List<BlogLike> selectBlogLike(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("author_id",uid);
        List<BlogLike> blogLikes = blogLikeDao.selectList(qw);
        return blogLikes;
    }

    public List<CommentLike> selectCommentLike(Integer uid)
    {
        QueryWrapper qw =new QueryWrapper<>();
        qw.eq("author_id",uid);
        List<CommentLike> commentLikes =commentLikeDao.selectList(qw);
        return commentLikes;
    }


    public Boolean updateUserFlag(Integer uid,Integer flag)
    {
        Integer num = 0;
        if(flag==1){
            User user =userMapper.selectById(uid);
            user.setUFlag(flag);
            num =userMapper.updateById(user);
        }else if(flag==2){
            User user = userMapper.selectById(uid);
            user.setUFlag(flag);
            num =userMapper.updateById(user);
            QueryWrapper qw = new QueryWrapper<>();
            qw.eq("user_id",uid);
            List<Blog> blogList = blogDao.selectList(qw);
            for(Blog blog : blogList){
                blog.setBlogFlag(0);
                blogDao.updateById(blog);
            }
        }else if(flag==0)
        {
            User user = userMapper.selectById(uid);
            user.setUFlag(flag);
            num =userMapper.updateById(user);
            QueryWrapper qw = new QueryWrapper<>();
            qw.eq("user_id",uid);
            List<Blog> blogList = blogDao.selectList(qw);
            for(Blog blog : blogList){
                blog.setBlogFlag(0);
                blogDao.updateById(blog);
            }
        }

        return num!=0?true:false;

    }


    public Boolean checkFollow(Integer fromuser,Integer touser){
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("from_user",fromuser);
        qw.eq("to_user",touser);
        FollowLink followLink = followLinkDao.selectOne(qw);
        return followLink!=null?true:false;
    }


    public List<Blog> selectFollowBlog(Integer uid)
    {
        QueryWrapper qw =new QueryWrapper<>();
        qw.eq("from_user",uid);
      List<FollowLink> followLinkList = followLinkDao.selectList(qw);
      List<Blog> blogList =new ArrayList<>();
      for(FollowLink followLink:followLinkList){
          QueryWrapper qw2 =new QueryWrapper<>();
          qw2.eq("user_id",followLink.getToUser());
          List<Blog> blogList1 = blogDao.selectList(qw2);
          blogList.addAll(blogList1);
      }
      blogList.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
      return blogList;
    }

    public Boolean checkCollect(Integer uid,Integer blogId)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("user_id",uid);
        qw.eq("blog_id",blogId);
        Collect collect =collectDao.selectOne(qw);
        return collect!=null?true:false;
    }


}
