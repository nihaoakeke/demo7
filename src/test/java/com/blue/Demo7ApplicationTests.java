package com.blue;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.dao.*;
import com.blue.domain.*;
import com.blue.service.BloagService;
import com.blue.service.UserService;
import com.blue.utils.ComparatorUtils;
import com.blue.utils.KeyUtils;
import com.blue.utils.PwdCheckUtil;
import com.blue.utils.SenstiveUtils;
import com.google.gson.Gson;
import com.vdurmont.emoji.EmojiParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Demo7ApplicationTests {
    @Autowired RedisTemplate redisTemplate;
    @Autowired
    private FollowLinkDao followLinkDao;

    @Autowired
    private BloagService bloagService;


    @Autowired
    BlogDao blogDao;

    @Autowired
    UserMapper userMapper;
    
    @Autowired
    CommentDao commentDao;

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private TypeDao typeDao;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogTypeDao blogTypeDao;

    @Test
    public void test()
    {

        List<Blog> blogList = bloagService.list();
        Comparator<Blog> weightedComparator = new ComparatorUtils(0.7, 0.3);
        blogList.sort(weightedComparator);
        System.out.println(blogList);
//        String key1="23";
//        String key2="24";
//        System.out.println(KeyUtils.gengerateKey(key1,key2));
//        String[] answer = KeyUtils.getKets(KeyUtils.gengerateKey(key1,key2));
//        System.out.println(answer[0]);
//        System.out.println(answer[1]);
//        QueryWrapper wrapper1 = new QueryWrapper<>();
////        wrapper1.select("user_name");
//        wrapper1.eq("user_id",1);
////        List<User> list1 = userMapper.selectList(wrapper1);
//        List<Map<Object, Object>> list1 = userMapper.selectMaps(wrapper1);
////        list1=userMapper.selectList(wrapper1);

//        QueryWrapper wrapper1 = new QueryWrapper<>();
//        wrapper1.eq("chat_id",1);
//        List<Chat> blogList = chatDao.selectList(wrapper1);
//        System.out.println(blogList);
//        QueryWrapper wrapper1 = new QueryWrapper();
//        wrapper1.select("type_id");
//        wrapper1.eq("blog_id",2);
//        List<BlogType> typeList=blogTypeDao.selectList(wrapper1);
//        List<Map<Object, Object>> list1 = blogTypeDao.selectMaps(wrapper1);
//        System.out.println(list1);
//        List<Blog> blogList = bloagService.selectBlogBytid(3);
//        System.out.println(blogList);
        String pwd = "123456..xinleileL";
        if(!PwdCheckUtil.checkPasswordLength(pwd, "8", null)
                || !PwdCheckUtil.checkContainCase(pwd)
                || !PwdCheckUtil.checkContainDigit(pwd)
                || !PwdCheckUtil.checkContainSpecialChar(pwd)
        ){
            System.out.println("error");
        }
        else{
            System.out.println("YES");
        }


//        QueryWrapper qw = new QueryWrapper();
//        qw.eq("blog_id",2);
//        List<BlogType> blogTypeList =blogTypeDao.selectList(qw);
//        for (BlogType blogType :blogTypeList){
//            System.out.println(blogType.getTypeId());
//        }

//        User user1 =new User();
//        user1.setUname("eee");
//        user1.setUpassword("123");
//        user1.setUEmail("123456789@qq.com");
//        userMapper.insert(user1);
//        System.out.println(user1);
//        List<User> list1 = userMapper.selectList(wrapper1);
//        QueryWrapper wrapper2 = new QueryWrapper<>();
//        wrapper2.select("blogName");
//        wrapper2.eq("blogId",1);

//        list1.addAll(list2);
//        System.out.println(blogList);
    }

    /**
     * 测试过滤敏感词
     */
//    @Test
//    public void test00(){
//        String text = "可恶dasdas可，恶";
//        SenstiveUtils.init();
//        String replaveWords = SenstiveUtils.replaceSensitiveWord(text);
//        System.out.println(replaveWords);
//    }
//
    /**
     * 测试emoji表情
     */
    @Test
    public void test01()
    {
        System.out.println(EmojiParser.parseToUnicode("你好啊可可:grinning:,:grin:,:joy:,:smiley:,:smile:,:sweat_smile:,:laughing:,:wink:,:blush:,:yum:,:sunglasses:,:heart_eyes:,:kissing_heart:,:kissing:,:kissing_smiling_eyes:,:kissing_closed_eyes:,:innocent:,:neutral_face:,:expressionless:,:no_mouth:"));
    }
//
//    /**
//     * 测试分页操作
//     */
//    @Test
//    public void test02()
//    {
//
////1 创建IPage分页对象,设置分页参数,1为当前页码，3为每页显示的记录数
//        IPage<Blog> page=new Page<>(2,2);
////2 执行分页查询
//
//        QueryWrapper qw = new QueryWrapper();
////        qw.lt("age",18);
//        qw.eq("blogType","Go");
//        blogDao.selectPage(page,qw);
////3 获取分页结果
//        System.out.println("当前页码值："+page.getCurrent());
//        System.out.println("每页显示数："+page.getSize());
//        System.out.println("一共多少页："+page.getPages());
//        System.out.println("一共多少条数据："+page.getTotal());
//        System.out.println("数据："+page.getRecords());
//
//        Map<String,String> map2 = new HashMap<>();
//        map2.put("current", String.valueOf(page.getCurrent()));
//        System.out.println(new Gson().toJson(map2));
//    }
//
//    /**
//     * 测试redis
//      */
//    @Test
//    public void test03(){
//        String key = "12";
//        String value = "13";
//        redisTemplate.opsForValue().set(key, value);
//        System.out.println(redisTemplate.opsForValue().get(key));
//    }
    /**
     * 测试一下获取博客类别
     */
//    @Test
//    public void test04(){
//        Set<String> blogSet = new HashSet<>();
//        QueryWrapper qw = new QueryWrapper();
//        qw.select("blogType");
//        List<Blog> blogList = blogDao.selectList(qw);
//        Iterator<Blog> iterator = blogList.iterator();
//        while (iterator.hasNext()){ //判断迭代器是否有元素
//            Blog blog = iterator.next();//获取集合下一个元素
//            String name = blog.getBlogType();
//            blogSet.add(name);
//        }
//        System.out.println(blogSet);
//    }

}
