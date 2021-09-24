package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*
redis 的测试，测试和ssm项目结合
ssm项目如何使用redis 第一种方法是 使用jedis 类似于jdbc
//第一步：在applicationContext.xml中的注释去掉
//第二步 在db.properties 中的redis配置的注释去掉
springmvc 中的单元测试。
为什么要用juint 单元测试，因为在框架中，传统的main方法已经无法处理
如req请求，等等，无法满足测试需求了
单元测试的好处是在最小的代码结构单元中 找出bug ,最快速的找出bug锁在 的位置
迅速解决，1个dao方法1个测试1个controller 1个测试，1个service 1个测试。

* */
/*
* import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})//模拟ssm框架运行后加载xml的容器
public class MyRedisTest {
    @Autowired
    private JedisPool jedisPool;
    //测试string类型
    @Test
    public void test01() throws InterruptedException {
        String pcode = jedisPool.getResource().set("pcode", "4758");
        System.out.println("pcode = " + pcode);


        //查询 pcode 这个key在不在  ---exists，
        Boolean b = jedisPool.getResource().exists("pcode");
        if(b){
            System.out.println("key 存在 b="+b);
            // 如果在，把他设置成120 倒计时，且值也改为7788
           jedisPool.getResource().setex("pcode",120,"7788");
            // 并且在10s后输出所剩下的倒计时。
            Thread.sleep(1000*10);
            Long ttl = jedisPool.getResource().ttl("pcode");
            System.out.println("ttl = " + ttl);
            //输出完毕后，将该key 设置成永久的key
            Long along = jedisPool.getResource().persist("pcode");//注意：他返回值不是-1
            Long ttl2 = jedisPool.getResource().ttl("pcode");
            System.out.println("ttl2 = " + ttl2);
        }else {
            System.out.println("b ="+b+", key 不存在");
        }

    }
    //测试 常用命令
    @Test
    public void test02(){
        //查询所有的key: keys *
        Set<String> keys = jedisPool.getResource().keys("*");
        for (String key : keys) {
           // System.out.println("key = " + key);
            String value = jedisPool.getResource().get(key);
            System.out.println("key    " +key +":"+"value  "+ value);
            //自增
            Long incr = jedisPool.getResource().incr(key);
            System.out.println("incr = " + incr);
            String value1 = jedisPool.getResource().get(key);
            System.out.println("key    " +key +":"+"value1 = " + value1);
        }
    }
    //测试  hash
    @Test
    public void test03(){
        //增加
        jedisPool.getResource().hset("food","name","苹果");
        jedisPool.getResource().hset("food","color","红色");
       //查询
        String color = jedisPool.getResource().hget("food", "color");
        System.out.println("color = " + color);

        //查 k
        Set<String> food = jedisPool.getResource().hkeys("food");
        for (String key : food) {
            System.out.println("key = " + key);
        }
        //查 k v
        Map<String, String> food1 = jedisPool.getResource().hgetAll("food");
        for (String s : food1.keySet()) {
            System.out.println("s = " + s);
        }
    }

    //测试  list
    @Test
    public void test04(){
        //左增
        jedisPool.getResource().lpush("names", "唐曾", "八戒");

        //遍历
        List<String> names = jedisPool.getResource().lrange("names", 0, -1);
        for (String name : names) {
            System.out.println("name = " + name);
        }
        //左边删除
        String names1 = jedisPool.getResource().lpop("names");
        System.out.println("names1 = " + names1);
        List<String> names2 = jedisPool.getResource().lrange("names", 0, -1);
        for (String s : names2) {
            System.out.println("s ======= " + s);
        }
    }
    //测试  set
    @Test
    public void test05(){
        jedisPool.getResource().sadd("pnames","李四","王五");
        Set<String> pnames = jedisPool.getResource().smembers("pnames");
        for (String pname : pnames) {
            System.out.println("pname = " + pname);
        }
        Long pnames1 = jedisPool.getResource().scard("pnames");
        System.out.println("pnames1 = " + pnames1);

        //指定删除
        jedisPool.getResource().srem("keys","张三");
        //随机删除
        jedisPool.getResource().spop("pnames");
    }
    //测试  zset
    @Test
    public void test06(){
        //添加
        jedisPool.getResource().zadd("xnames",1.0,"大娃");
        jedisPool.getResource().zadd("xnames",2.0,"二娃");
        jedisPool.getResource().zadd("xnames",3.0,"三娃");
        jedisPool.getResource().zadd("xnames",4.0,"四娃");
        //遍历
        Set<String> xnames = jedisPool.getResource().zrange("xnames", 0, -1);
        for (String xname : xnames) {
            System.out.println("xname = " + xname);
        }
        //查询总条数
        Long xnames1 = jedisPool.getResource().zcard("xnames");
        System.out.println("xnames1 = " + xnames1);

        //指定删除 一个或多个
        Long xnames2 = jedisPool.getResource().zrem("xnames", "三娃");
        System.out.println("xnames2 = " + xnames2);
    }
}
