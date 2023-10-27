package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.BlogDao;
import com.blue.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloagService extends ServiceImpl<BlogDao, Blog> {

    @Autowired
    private BlogDao blogDao;

    public Boolean updateNumById(Blog blog) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(blog)>0;
    }

    /**
     * 搜索用户通过bid;
     * @param id
     * @return
     */
    public Blog seleBlogBybid(Integer id)
    {
        Blog blog = blogDao.selectById(id);
        return blog;
    }

    /**
     * 博客类型进行查找
     * @param type
     * @return
     */

    public List<Blog> selectBlogBytype(String type)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogType",type);
        List<Blog> list = blogDao.selectList(qw);
        return list;
    }

    /**
     * 根据用户id查找博客
     * @param uid
     * @return
     */

    public List<Blog> selectBlogByUid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uid);
        List<Blog> list = blogDao.selectList(qw);
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
        qw.eq("uId",1);
        qw.orderByDesc("blogView");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }

    /**
     * 根据博客类型与浏览量降序排列
     * @param type
     * @return
     */

    public List<Blog> selectBlogBytypeDesc(String type)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogType",type);
        qw.orderByDesc("blogView");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }

    /**
     * 按照点赞量进行排序
     * @param type
     * @return
     */

    public List<Blog> selectBlogByLike(String type)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogType",type);
        qw.orderByDesc("blogLike");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }


    /**
     * 根据点赞量和用户来排序
     * @param uid
     * @return
     */
    public List<Blog> selectBlogByLike(int uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uid);
        qw.orderByDesc("blogLike");
        List<Blog> blogList = blogDao.selectList(qw);
        return blogList;
    }


    public  IPage<Blog> selectBypage(String type,int current,int num)
    {
        IPage<Blog> page=new Page<>(current,num);
//2 执行分页查询

        QueryWrapper qw = new QueryWrapper();
//        qw.lt("age",18);
        qw.eq("blogType",type);
        blogDao.selectPage(page,qw);
//3 获取分页结果
//        System.out.println("当前页码值："+page.getCurrent());
//        System.out.println("每页显示数："+page.getSize());
//        System.out.println("一共多少页："+page.getPages());
//        System.out.println("一共多少条数据："+page.getTotal());
//        System.out.println("数据："+page.getRecords());
            return page;
//        Map<String,String> map2 = new HashMap<>();
//        map2.put("current", "page.getCurrent()");
//        System.out.println(new Gson().toJson(map2));
    }




}
