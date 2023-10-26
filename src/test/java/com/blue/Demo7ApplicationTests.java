package com.blue;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.dao.*;
import com.blue.domain.*;
import com.blue.service.LinkService;
import com.blue.utils.SenstiveUtils;
import com.google.gson.Gson;
import com.vdurmont.emoji.EmojiParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@SpringBootTest
class Demo7ApplicationTests {

    @Autowired
    BlogDao blogDao;
    @Autowired
    private BookDao bookDao;

    @Autowired UserDao userDao;
    
    @Autowired
    CommentDao commentDao;
    


    @Test
    public void testGetById(){
//        Book book = bookDao.selectById(2);
//        System.out.println(book);

//        User user =userDao.selectById(2);
//        System.out.println(user);
//        Book book = bookDao.getById(2);
//        System.out.println(book);
//        Blog blog = blogDao.sele(2);
//        System.out.println(blog);
//        List

    }

    @Test
    public void testGetAll(){
//        QueryWrapper qw = new QueryWrapper();
////        qw.lt("age",18);
//        List<Book> userList = bookDao.selectList(qw);
//        System.out.println(userList);
        Comment comment = null;
        comment.setCId(1);
        comment.setCContent("dasdasd");
        commentDao.updateById(comment);
    }


    @Test
    public void testSelectPage(){
//1 创建IPage分页对象,设置分页参数,1为当前页码，3为每页显示的记录数
        IPage<Blog> page=new Page<>(2,2);
//2 执行分页查询

        QueryWrapper qw = new QueryWrapper();
//        qw.lt("age",18);
        qw.eq("blogType","Go");
        blogDao.selectPage(page,qw);
//3 获取分页结果
        System.out.println("当前页码值："+page.getCurrent());
        System.out.println("每页显示数："+page.getSize());
        System.out.println("一共多少页："+page.getPages());
        System.out.println("一共多少条数据："+page.getTotal());
        System.out.println("数据："+page.getRecords());

        Map<String,String> map2 = new HashMap<>();
        map2.put("current", "page.getCurrent()");
        System.out.println(new Gson().toJson(map2));
    }

    @Autowired RedisTemplate redisTemplate;
  @Autowired
    PDao pDao;
  @Autowired ReplyDao replyDao;
  @Autowired
  private LinkDao linkDao;

  @Autowired
  private LinkService linkService;

    @Test
    public void  test(){

//        Link link = linkService.selectByFT(2,3);
//        link.setMessage("dadas");
//        linkDao.updateById(link);
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

        System.out.println(blogSet);


//        QueryWrapper qw = new QueryWrapper();
//        qw.eq("uId",1);
//        qw.orderByAsc("blogView");
//        List<Blog> blogList = blogDao.selectList(qw);
//        System.out.println(blogList);

//        QueryWrapper qw = new QueryWrapper();
//        //qw.eq("cId",1);
//        qw.orderByDesc("rTime");
//        List<Reply> replyList = replyDao.selectList(qw);
//
//System.out.println(replyDao.selectById(2));
//        String message="他？妈的.....他妈的。.....";
//        SenstiveUtils.init();
//        System.out.println(SenstiveUtils.replaceSensitiveWord(message));

             System.out.println(EmojiParser.parseToUnicode("你好啊可可:grinning:,:grin:,:joy:,:smiley:,:smile:,:sweat_smile:,:laughing:,:wink:,:blush:,:yum:,:sunglasses:,:heart_eyes:,:kissing_heart:,:kissing:,:kissing_smiling_eyes:,:kissing_closed_eyes:,:innocent:,:neutral_face:,:expressionless:,:no_mouth:"));
//        String key = "12";
//        String value = "13";
//        redisTemplate.opsForValue().set(key, value);
//        System.out.println(redisTemplate.opsForValue().get(key));

//        QueryWrapper qw = new QueryWrapper();
//        //UpdateWrapper qw1 = new UpdateWrapper();
////        qw1.set("uflag",1);
//        qw.eq("uId","1");
//        List<Blog> list = blogDao.selectList(qw);
//        //user.setUflag(1);
//        //userDao.updateById(user);
//        System.out.println(list);


//        User user =(User) userDao.selectBy(1);
//        System.out.println(user);
//        P p =pDao.getById(2);
//       System.out.println(p);

//        P p=pDao.selectById(2);
//        System.out.println(p);
    }

//    @Test
//    public void Page(){
//        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<Book>();
//        lqw.lt(Book::getAge, uq.getAge2());
//        if( null != uq.getAge()) {
//            lqw.gt(User::getAge, uq.getAge());
//        }
//        List<User> userList = userDao.selectList(lqw);
//        System.out.println(userList);
//    }
}
