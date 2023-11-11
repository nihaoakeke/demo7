package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.BlogDao;
import com.blue.dao.BlogFileDao;
import com.blue.dao.BlogLikeDao;
import com.blue.dao.BlogTypeDao;
import com.blue.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BloagService extends ServiceImpl<BlogDao, Blog> {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogLikeDao blogLikeDao;

    @Autowired
    private BlogFileDao blogFileDao;

    @Autowired
    private BlogTypeDao blogTypeDao;


    public Boolean updateNumById(Blog blog) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(blog)>0;
    }

    /**
     * 博客类型进行查找
     * @param tid
     * @return
     */

    public List<Blog> selectBlogBytid(Integer tid)
    {

          QueryWrapper qw = new QueryWrapper();
          qw.eq("type_id",tid);
          List<BlogType> blogTypeList =blogTypeDao.selectList(qw);
          List<Blog> blogList = new ArrayList<>();
          for(BlogType blogType: blogTypeList){
              QueryWrapper qw1 = new QueryWrapper();
              qw1.eq("blog_id",blogType.getBlogId());
              List<Blog> blogList1 =blogDao.selectList(qw1);
              blogList.addAll(blogList1);
          }
          blogList.sort(Comparator.comparing(Blog::getBlogTime));
          return blogList;
    }

    /**
     * 根据用户id查找博客
     * @param uid
     * @return
     */

    public List<Blog> selectBlogByUid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",uid);
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }


    public List<Integer> selectTidBybid(Integer bid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blog_id",bid);
        List<BlogType> blogTypeList = blogTypeDao.selectList(qw);
        List<Integer> list =new ArrayList<>();
        for(BlogType blogType : blogTypeList){
            list.add(blogType.getTypeId());
        }
        return list;
    }

    public List<Integer> selectidBybid(Integer bid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blog_id",bid);
        List<BlogType> blogTypeList = blogTypeDao.selectList(qw);
        List<Integer> list =new ArrayList<>();
        for(BlogType blogType : blogTypeList){
            list.add(blogType.getId());
        }
        return list;
    }

    /**
     * 根据游览量来降序排列博客
     * @param uid
     * @return
     */

    public List<Blog> selectBlogByUidDesc(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",uid);
        qw.orderByDesc("blog_view");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }

    public List<Blog> selectLikeName(String name)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.like("blog_name",name);
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }



//    /**
//     * 按照点赞量进行排序
//     * @param type
//     * @return
//     */
//
//    public List<Blog> selectBlogByLike(String type)
//    {
//        QueryWrapper qw = new QueryWrapper();
//        qw.eq("blogType",type);
//        qw.orderByDesc("blogLike");
//        List<Blog> blogList = blogDao.selectList(qw);
//        return blogList;
//    }


    /**
     * 根据点赞量和用户来排序
     * @param uid
     * @return
     */
    public List<Blog> selectBlogByLike(int uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",uid);
        qw.orderByDesc("blog_like");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }


//    public  IPage<Blog> selectBypage(String type,int current,int num)
//    {
//        IPage<Blog> page=new Page<>(current,num);
////2 执行分页查询
//
//        QueryWrapper qw = new QueryWrapper();
////        qw.lt("age",18);
//        qw.eq("blogType",type);
//        blogDao.selectPage(page,qw);
////3 获取分页结果
////        System.out.println("当前页码值："+page.getCurrent());
////        System.out.println("每页显示数："+page.getSize());
////        System.out.println("一共多少页："+page.getPages());
////        System.out.println("一共多少条数据："+page.getTotal());
////        System.out.println("数据："+page.getRecords());
//            return page;
////        Map<String,String> map2 = new HashMap<>();
////        map2.put("current", "page.getCurrent()");
////        System.out.println(new Gson().toJson(map2));
//    }

    public BlogLike checkLike(Integer postid, Integer authorid, Integer bid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blog_id",bid);
        qw.eq("author_id",authorid);
        qw.eq("like_post_id",postid);
        return blogLikeDao.selectOne(qw);
    }



    public Blog checkBlogExist(Integer uid,Integer bid)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("blog_id",bid);
        qw.eq("user_id",uid);
        Blog blog = blogDao.selectOne(qw);
        return blog;
    }

    public Boolean checkBlogFile(Integer bid)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("blog_id",bid);
        List<BlogFile> blogFileList= blogFileDao.selectList(qw);
        return blogFileList.size()!=0?true:false;
    }

    public List<Blog> seleceUnPublish(){
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("blog_publish",0);
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }

    public List<Blog> selectPublish(){
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("blog_publish",1);
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }
}
