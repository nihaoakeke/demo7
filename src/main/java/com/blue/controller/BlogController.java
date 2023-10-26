package com.blue.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.config.Log;
import com.blue.dao.BlogDao;
import com.blue.domain.*;
import com.blue.service.BloagService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BloagService bloagService;

    @Log(operation = "浏览某一篇博客")
    @GetMapping("/getById/{id}")
    public Result getViewById(@PathVariable Integer id) throws Exception {
        Blog blog = bloagService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (blog == null) {
            return new Result(10,blog,"kke");
        }
        return new Result(11,blog,"kek");
    }



    @Log(operation = "对某篇博客确认点赞")
    @GetMapping("/okLike/{id}")
    public Result OKLikeById(@PathVariable Integer id)
    {
        Blog blog = bloagService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (blog == null) {
            return new Result(Code.GET_ERR,blog,"点赞失败，请重试");
        }
        return new Result(Code.GET_OK,blog,"点赞成功");

    }

    @Log(operation = "对某篇博客取消点赞")
    @GetMapping("/flaseLike/{id}")
    public Result falseLikeById(@PathVariable Integer id)
    {
        Blog blog = bloagService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (blog == null) {
            return new Result(Code.GET_ERR,blog,"取消点赞失败，请重试");
        }
        return new Result(Code.GET_OK,blog,"取消点赞成功");
    }


    @Log(operation = "根据id查询谋篇博客详情")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Blog blog = blogDao.selectById(id);
        Integer code = blog != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blog != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blog,msg);
    }

    @Log(operation = "根据博客类型进行查询")
    @GetMapping("/type/{type}")
    public Result getBytype(@PathVariable String type) {
        List<Blog> blogList = bloagService.selectBlogBytypeDesc(type);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);
    }

    @Log(operation = "根据发表的用户进行查询博客详情")
    @GetMapping("/uid/{uid}")
    public Result getByuid(@PathVariable Integer uid) {
        List<Blog> blogList = bloagService.selectBlogByUid(uid);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);
    }

    @Log(operation = "根据分页页码查询博客详情内容")
    @GetMapping("/getpage/{type}/{current}/{num}")
    public String getPageBytype(@PathVariable String type,@PathVariable Integer current,@PathVariable Integer num)
    {
//        System.out.println(current);
//        System.out.println(num);
        IPage<Blog> page=new Page<>(current,num);
//2 执行分页查询

        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogType",type);
        blogDao.selectPage(page,qw);
        Map<String,String> map2 = new HashMap<>();
        System.out.println("当前页码值："+page.getCurrent());
        System.out.println("每页显示数："+page.getSize());
        System.out.println("一共多少页："+page.getPages());
        System.out.println("一共多少条数据："+page.getTotal());
        System.out.println("数据："+page.getRecords());

        List<Blog> blogList = page.getRecords();
        map2.put("data", blogList.toString());
        map2.put("pagenum", String.valueOf(page.getPages()));
        map2.put("total", String.valueOf(page.getTotal()));
      return new Gson().toJson(map2);
    }

//    @Log(operation = "根据分页页码查询博客详情内容")
//    @GetMapping("/getpage/{current}/{num}")
//    public String getPage(@PathVariable String type,@PathVariable Integer current,@PathVariable Integer num)
//    {
////        System.out.println(current);
////        System.out.println(num);
//        IPage<Blog> page=new Page<>(current,num);
////2 执行分页查询
//
//        QueryWrapper qw = new QueryWrapper();
//        qw.eq("blogType",type);
//        blogDao.selectPage(page,qw);
//        Map<String,String> map2 = new HashMap<>();
//        System.out.println("当前页码值："+page.getCurrent());
//        System.out.println("每页显示数："+page.getSize());
//        System.out.println("一共多少页："+page.getPages());
//        System.out.println("一共多少条数据："+page.getTotal());
//        System.out.println("数据："+page.getRecords());
//
//        List<Blog> blogList = page.getRecords();
//        map2.put("data", blogList.toString());
//        map2.put("pagenum", String.valueOf(page.getPages()));
//        map2.put("total", String.valueOf(page.getTotal()));
//        return new Gson().toJson(map2);
//    }


    @GetMapping("/getAllType")
    public String getAllType()
    {
        Set<String> blogSet = new HashSet<>();
        QueryWrapper qw = new QueryWrapper();
        qw.select("blogType");
        List<Blog> blogList = blogDao.selectList(qw);
        Iterator<Blog> iterator = blogList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            Blog blog = iterator.next();//获取集合下一个元素
            String name = blog.getBlogType();
            blogSet.add(name);
        }
        return new Gson().toJson(blogSet);
    }

    @GetMapping("/getAllBlog")
    public Result getAllBlog()
    {
      List<Blog> blogList = bloagService.list();
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
      return new Result(code,blogList,msg);
     }

    @Log(operation = "根据点赞量和用户id排序查询博客")
    @GetMapping("/like/uid/{uid}")
    public Result getlikeByuid(@PathVariable Integer uid)
    {
        List<Blog> blogList = bloagService.selectBlogByLike(uid);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);

    }

    @Log(operation="根据点赞量和类型进行排序查询博客")
    @GetMapping("/like/type/{type}")
    public Result getlikeBytype(@PathVariable String type)
    {
        List<Blog> blogList = bloagService.selectBlogByLike(type);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);
    }

    @Log(operation = "更新博客信息")
    @PutMapping
    public Result updateBlog(@RequestBody Blog blog)
    {
        Integer num = blogDao.updateById(blog);
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code,blog,msg);
    }

    @Log(operation = "删除某一篇博客")
    @DeleteMapping("/{id}")
    public Result deleteBlog(@PathVariable Integer id)
    {
        Integer num =blogDao.deleteById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据删除成功":"数据删除失败，请重试";
        return new Result(code,"",msg);
    }

    @Log(operation = "发表某一篇博客")
    @PostMapping
    public Result saveBlog(@RequestBody Blog blog){

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        blog.setBlogTime(date);
        Integer num = blogDao.insert(blog);
        Integer code = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,blog,msg);
    }

    @Log(operation = "根据浏览量进行查询")
    @GetMapping("/desc/{type}")
    public Result selectByViewBytype(@PathVariable String type){
        List<Blog> blogList = bloagService.selectBlogBytypeDesc(type);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);

    }

    @Log(operation = "根据用户id和浏览量进行排序")
    @GetMapping("/desc/{uid}")
    public Result selectByViewByuid(@PathVariable Integer uid){
        List<Blog> blogList = bloagService.selectBlogByUidDesc(uid);
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);

    }



}
