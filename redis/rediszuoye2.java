package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.JedisPool;

import java.util.List;

/*
* 编程题，
使用 java 代码编写，
有一个双端队列集合， 里面有 10 条数据，
查询出  第5个人是什么数据，
左边弹出1个 ， 右边弹出1个，打印还剩多少条数据，
然后，再 第3个数据前面，插入一个数据，
然后，进行查询全部数据进行查看。
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})//模拟ssm框架运行后加载xml的容器
public class rediszuoye2 {
    @Autowired
    private JedisPool jedisPool;
    @Test
    public void test01() {
        //左增
        jedisPool.getResource().lpush("tname", "唐曾", "八戒","悟空","大娃", "二娃","三娃","四娃", "五娃","六娃","七娃");
        String tname = jedisPool.getResource().lindex("tname", 5);
        //System.out.println("tname = " + tname);
        String tname1 = jedisPool.getResource().type("tname");
        System.out.println("第五个人的类型是  " + tname1);

        String tname2 = jedisPool.getResource().rpop("tname");
        System.out.println("弹出第一个元素 是 " + tname2);

        String tname3 = jedisPool.getResource().lpop("tname");
        System.out.println("弹出集合最后一个元素是 " + tname3);


        Long linsert = jedisPool.getResource().linsert("tname", BinaryClient.LIST_POSITION.BEFORE, "悟空", "和尚");

        Long tname4 = jedisPool.getResource().llen("tname");
        System.out.println("还剩  " + tname4+"数据");


        List<String> tname5 = jedisPool.getResource().lrange("tname", 0, -1);
        System.out.println("所有数据 " + tname5);


    }
}
