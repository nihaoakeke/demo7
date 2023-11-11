package com.blue.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    /**
//     * 给一个指定的 key 值附加过期时间
//     *
//     * @param key
//     * @param time
//     * @return
//     */
//    public boolean expire(String key, long time) {
//        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
//    }
//    /**
//     * 根据key 获取过期时间
//     *
//     * @param key
//     * @return
//     */
//    public long getTime(String key) {
//        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
//    }
//    /**
//     * 根据key 获取过期时间
//     *
//     * @param key
//     * @return
//     */
//    public boolean hasKey(String key) {
//        return redisTemplate.hasKey(key);
//    }
//    /**
//     * 移除指定key 的过期时间
//     *
//     * @param key
//     * @return
//     */
//    public boolean persist(String key) {
//        return redisTemplate.boundValueOps(key).persist();
//    }
//
//    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -
//
//    /**
//     * 根据key获取值
//     *
//     * @param key 键
//     * @return 值
//     */
//    public Object get(String key) {
//        return key == null ? null : redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 将值放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @return true成功 false 失败
//     */
//    public void set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    /**
//     * 将值放入缓存并设置时间
//     *
//     * @param key   键
//     * @param value 值
//     * @param time  时间(秒) -1为无期限
//     * @return true成功 false 失败
//     */
//    public void set(String key, String value, long time) {
//        if (time > 0) {
//            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
//        } else {
//            redisTemplate.opsForValue().set(key, value);
//        }
//    }
//
//    /**
//     * 批量添加 key (重复的键会覆盖)
//     *
//     * @param keyAndValue
//     */
//    public void batchSet(Map<String, String> keyAndValue) {
//        redisTemplate.opsForValue().multiSet(keyAndValue);
//    }
//
//    /**
//     * 批量添加 key-value 只有在键不存在时,才添加
//     * map 中只要有一个key存在,则全部不添加
//     *
//     * @param keyAndValue
//     */
//    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
//        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
//    }
//
//    /**
//     * 对一个 key-value 的值进行加减操作,
//     * 如果该 key 不存在 将创建一个key 并赋值该 number
//     * 如果 key 存在,但 value 不是长整型 ,将报错
//     *
//     * @param key
//     * @param number
//     */
//    public Long increment(String key, long number) {
//        return redisTemplate.opsForValue().increment(key, number);
//    }
//
//    /**
//     * 对一个 key-value 的值进行加减操作,
//     * 如果该 key 不存在 将创建一个key 并赋值该 number
//     * 如果 key 存在,但 value 不是 纯数字 ,将报错
//     *
//     * @param key
//     * @param number
//     */
//    public Double increment(String key, double number) {
//        return redisTemplate.opsForValue().increment(key, number);
//    }
//
//    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -
//
//    /**
//     * 将数据放入set缓存
//     *
//     * @param key 键
//     * @return
//     */
//    public void sSet(String key, String value) {
//        redisTemplate.opsForSet().add(key, value);
//    }
//
//    /**
//     * 获取变量中的值
//     *
//     * @param key 键
//     * @return
//     */
//    public Set<Object> members(String key) {
//        return redisTemplate.opsForSet().members(key);
//    }
//
//    /**
//     * 随机获取变量中指定个数的元素
//     *
//     * @param key   键
//     * @param count 值
//     * @return
//     */
//    public void randomMembers(String key, long count) {
//        redisTemplate.opsForSet().randomMembers(key, count);
//    }
//
//    /**
//     * 随机获取变量中的元素
//     *
//     * @param key 键
//     * @return
//     */
//    public Object randomMember(String key) {
//        return redisTemplate.opsForSet().randomMember(key);
//    }
//
//    /**
//     * 弹出变量中的元素
//     *
//     * @param key 键
//     * @return
//     */
//    public Object pop(String key) {
//        return redisTemplate.opsForSet().pop("setValue");
//    }
//
//    /**
//     * 获取变量中值的长度
//     *
//     * @param key 键
//     * @return
//     */
//    public long size(String key) {
//        return redisTemplate.opsForSet().size(key);
//    }
//
//    /**
//     * 根据value从一个set中查询,是否存在
//     *
//     * @param key   键
//     * @param value 值
//     * @return true 存在 false不存在
//     */
//    public boolean sHasKey(String key, Object value) {
//        return redisTemplate.opsForSet().isMember(key, value);
//    }
//
//    /**
//     * 检查给定的元素是否在变量中。
//     *
//     * @param key 键
//     * @param obj 元素对象
//     * @return
//     */
//    public boolean isMember(String key, Object obj) {
//        return redisTemplate.opsForSet().isMember(key, obj);
//    }
//
//    /**
//     * 转移变量的元素值到目的变量。
//     *
//     * @param key     键
//     * @param value   元素对象
//     * @param destKey 元素对象
//     * @return
//     */
//    public boolean move(String key, String value, String destKey) {
//        return redisTemplate.opsForSet().move(key, value, destKey);
//    }
//
//    /**
//     * 批量移除set缓存中元素
//     *
//     * @param key    键
//     * @param values 值
//     * @return
//     */
//    public void remove(String key, Object... values) {
//        redisTemplate.opsForSet().remove(key, values);
//    }
//
//    /**
//     * 通过给定的key求2个set变量的差值
//     *
//     * @param key     键
//     * @param destKey 键
//     * @return
//     */
//    public Set<Set> difference(String key, String destKey) {
//        return redisTemplate.opsForSet().difference(key, destKey);
//    }
//
//
//    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -
//
//    /**
//     * 加入缓存
//     *
//     * @param key 键
//     * @param map 键
//     * @return
//     */
//    public void add(String key, Map<String, String> map) {
//        redisTemplate.opsForHash().putAll(key, map);
//    }
//
//    /**
//     * 获取 key 下的 所有  hashkey 和 value
//     *
//     * @param key 键
//     * @return
//     */
//    public Map<Object, Object> getHashEntries(String key) {
//        return redisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * 验证指定 key 下 有没有指定的 hashkey
//     *
//     * @param key
//     * @param hashKey
//     * @return
//     */
//    public boolean hashKey(String key, String hashKey) {
//        return redisTemplate.opsForHash().hasKey(key, hashKey);
//    }
//
//    /**
//     * 获取指定key的值string
//     *
//     * @param key  键
//     * @param key2 键
//     * @return
//     */
//    public String getMapString(String key, String key2) {
//        return redisTemplate.opsForHash().get("map1", "key1").toString();
//    }
//
//    /**
//     * 获取指定的值Int
//     *
//     * @param key  键
//     * @param key2 键
//     * @return
//     */
//    public Integer getMapInt(String key, String key2) {
//        return (Integer) redisTemplate.opsForHash().get("map1", "key1");
//    }
//
//    /**
//     * 弹出元素并删除
//     *
//     * @param key 键
//     * @return
//     */
//    public String popValue(String key) {
//        return redisTemplate.opsForSet().pop(key).toString();
//    }
//
//    /**
//     * 删除指定 hash 的 HashKey
//     *
//     * @param key
//     * @param hashKeys
//     * @return 删除成功的 数量
//     */
    public Long delete(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }
//
//    /**
//     * 给指定 hash 的 hashkey 做增减操作
//     *
//     * @param key
//     * @param hashKey
//     * @param number
//     * @return
//     */
//    public Long increment(String key, String hashKey, long number) {
//        return redisTemplate.opsForHash().increment(key, hashKey, number);
//    }
//
//    /**
//     * 给指定 hash 的 hashkey 做增减操作
//     *
//     * @param key
//     * @param hashKey
//     * @param number
//     * @return
//     */
//    public Double increment(String key, String hashKey, Double number) {
//        return redisTemplate.opsForHash().increment(key, hashKey, number);
//    }
//
//    /**
//     * 获取 key 下的 所有 hashkey 字段
//     *
//     * @param key
//     * @return
//     */
//    public Set<Object> hashKeys(String key) {
//        return redisTemplate.opsForHash().keys(key);
//    }
//
//    /**
//     * 获取指定 hash 下面的 键值对 数量
//     *
//     * @param key
//     * @return
//     */
//    public Long hashSize(String key) {
//        return redisTemplate.opsForHash().size(key);
//    }
//
//    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -
//
//    /**
//     * 在变量左边添加元素值
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public void leftPush(String key, Object value) {
//        redisTemplate.opsForList().leftPush(key, value);
//    }
//
//    /**
//     * 获取集合指定位置的值。
//     *
//     * @param key
//     * @param index
//     * @return
//     */
//    public Object index(String key, long index) {
//        return redisTemplate.opsForList().index("list", 1);
//    }
//
//    /**
//     * 获取指定区间的值。
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public List<Object> range(String key, long start, long end) {
//        return redisTemplate.opsForList().range(key, start, end);
//    }
//
//    /**
//     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
//     * 如果中间参数值存在的话。
//     *
//     * @param key
//     * @param pivot
//     * @param value
//     * @return
//     */
//    public void leftPush(String key, String pivot, String value) {
//        redisTemplate.opsForList().leftPush(key, pivot, value);
//    }
//
//    /**
//     * 向左边批量添加参数元素。
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public void leftPushAll(String key, String... values) {
////        redisTemplate.opsForList().leftPushAll(key,"w","x","y");
//        redisTemplate.opsForList().leftPushAll(key, values);
//    }
//
//    /**
//     * 向集合最右边添加元素。
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public void leftPushAll(String key, String value) {
//        redisTemplate.opsForList().rightPush(key, value);
//    }
//
//    /**
//     * 向左边批量添加参数元素。
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public void rightPushAll(String key, String... values) {
//        //redisTemplate.opsForList().leftPushAll(key,"w","x","y");
//        redisTemplate.opsForList().rightPushAll(key, values);
//    }
//
//    /**
//     * 向已存在的集合中添加元素。
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public void rightPushIfPresent(String key, Object value) {
//        redisTemplate.opsForList().rightPushIfPresent(key, value);
//    }
//
//    /**
//     * 向已存在的集合中添加元素。
//     *
//     * @param key
//     * @return
//     */
//    public long listLength(String key) {
//        return redisTemplate.opsForList().size(key);
//    }
//
//    /**
//     * 移除集合中的左边第一个元素。
//     *
//     * @param key
//     * @return
//     */
//    public void leftPop(String key) {
//        redisTemplate.opsForList().leftPop(key);
//    }
//
//    /**
//     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
//     *
//     * @param key
//     * @return
//     */
//    public void leftPop(String key, long timeout, TimeUnit unit) {
//        redisTemplate.opsForList().leftPop(key, timeout, unit);
//    }
//
//    /**
//     * 移除集合中右边的元素。
//     *
//     * @param key
//     * @return
//     */
//    public void rightPop(String key) {
//        redisTemplate.opsForList().rightPop(key);
//    }
//
//    /**
//     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
//     *
//     * @param key
//     * @return
//     */
//    public void rightPop(String key, long timeout, TimeUnit unit) {
//        redisTemplate.opsForList().rightPop(key, timeout, unit);
//    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
//    @SuppressWarnings("unchecked")
//    public void del(String ... key){
//        if(key!=null&&key.length>0){
//            if(key.length==1){
//                redisTemplate.delete(key[0]);
//            }else{
//                redisTemplate.delete(CollectionUtils.arrayToList(key));
//            }
//        }
//    }

    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public  Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value,long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item,-by);
    }

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key,long time,Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time>0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index,Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 模糊查询获取key值
     * @param pattern
     * @return
     */
    public Set keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     * @param channel
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message){
        redisTemplate.convertAndSend(channel,message);
    }



    /**
     * 根据起始结束序号遍历Redis中的list
     * @param listKey
     * @param start  起始序号
     * @param end  结束序号
     * @return
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }
    /**
     * 弹出右边的值 --- 并且移除这个值
     * @param listKey
     */
    public Object rifhtPop(String listKey){
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    /**----------------zSet相关操作------------------*/
    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
        return stringRedisTemplate.opsForZSet().add(key, values);
    }

    public Long zRemove(String key, Object... values) {
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     * @return
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     * @param key
     * @param value
     * @return
     */
    public Long zRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     * @param key
     * @param value
     * @return
     */
    public Long zReverseRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key ,value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zReverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取集合中的某一个元素
     * @param key
     * @param value
     * @return
     */

    public Double zScore(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除某一个键值操作
     * @param key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }


}
