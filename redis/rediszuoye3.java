package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})//模拟ssm框架运行后加载xml的容器
public class rediszuoye3 {
    @Autowired
    private JedisPool jedisPool;

    //1、 Keys相关的命令操作：
    @Test
    public void test01() {
        //（1） 查看key是否存在：
        Boolean b = jedisPool.getResource().exists("pkey");
        if (b) {
            System.out.println("key 存在 b=" + b);
        } else {
            System.out.println("b =" + b + ", key 不存在");
        }

        //（2） 查找满足 x开头的   的keys（需要在数据库中添加若干个 x开头的 几个单词）：
        jedisPool.getResource().set("xkey","小黑");
        jedisPool.getResource().set("xkey1","小白");
        jedisPool.getResource().set("xkey2","小李");
        jedisPool.getResource().set("xkey3","小宁");
        jedisPool.getResource().set("xkey4","小宁2");
        Set<String> keys = jedisPool.getResource().keys("x*");
        for (String key : keys) {
            System.out.println("key = " + key);
        }

        //（3）查看key的超时时间：
        Long pkey = jedisPool.getResource().ttl("pkey");
        System.out.println("超时时间 = " + pkey);

        //（4） 遍历key：
        Set<String> keysd = jedisPool.getResource().keys("*");
        for (String key : keysd) {
            System.out.println("所有的key = " + key);
        }

        //（5） 返回key的值的序列化：
        byte[] pkeys = jedisPool.getResource().dump("pkey");
        System.out.println("pkeys = " + pkeys);
    }


    //2、 string类型数据的命令操作：
    @Test
    public void test02(){
       // （1） 设置键值：
       jedisPool.getResource().set("aname","1");
       jedisPool.getResource().set("aname","2");
       jedisPool.getResource().set("aname","3");
       jedisPool.getResource().set("aname","4");
       jedisPool.getResource().set("aname","5");
       jedisPool.getResource().set("aname","6");
       //（2） 读取键值：
        jedisPool.getResource().get("aname");
        //（3） 数值类型自增1：
        Long aname = jedisPool.getResource().incr("aname");
        System.out.println("自增为 = " + aname);

        //4） 数值类型自减1：
        Long aname1 = jedisPool.getResource().decr("aname");
        System.out.println("自减为 = " + aname1);

        //（5） 查看值的长度：
        Long aname2 = jedisPool.getResource().strlen("aname");
        System.out.println("长度为 = " + aname2);
    }

     //3、 list类型数据的命令操作：
     @Test
     public void tesyt03(){
        //（1）对列表city插入元素：nanjing Suzhou Hangzhou wuxi
        jedisPool.getResource().lpush("city","南京","苏州","杭州","无锡");

        //（2）将列表city里的头部的元素移除
        String city = jedisPool.getResource().rpop("city");
        System.out.println("city移除的头元素为 " + city);
        //（3）将name列表的尾部元素移除到number列表的头部
         jedisPool.getResource().lpush("name","大哈","二哈","三哈","四哈");
         jedisPool.getResource().lpush("number","1","2","3","4");
         jedisPool.getResource().rpoplpush("name","number");
        //（4） 对一个已存在的列表插入新元素
         Long rpushx = jedisPool.getResource().rpushx("city", "张家港");
         System.out.println("city的新元素为 " + rpushx);
         //（5）查看list的值长度
         Long city1 = jedisPool.getResource().llen("city");
         System.out.println("city1 = " + city1);
     }

            //4、 hash类型数据的命令操作：
            @Test
            public void test04(){
            //（1） 设置一个hash表，order表里包括的键值信息有：id：1,customer_name：张三
            jedisPool.getResource().hset("order","id","1");
            jedisPool.getResource().hset("order","customer_name","张三");

            //2） 创建一个hash表，表里的键值批量插入
            // jedisPool.getResource().hmset("order","2","");
            //（3） 获取order对应的map的所有key
                jedisPool.getResource().hkeys("orders");
            //4） 获取order对应的map的键值数量
                Long order = jedisPool.getResource().hlen("order");
                //（5） 获取order表里的id值
                String hget = jedisPool.getResource().hget("order", "id");
            }
    }
