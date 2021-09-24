package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

/*
* 用java代码写，把咱们班33个人的名字 形成 一个集合，
运行后随机点一个人的名字，就把这个人的名字移除。 再次
点名是 点 32个人的随机中的一个。
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})//模拟ssm框架运行后加载xml的容器
public class rediszuoye1 {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void test01(){
        //判断是否存在
        Boolean namee1 = jedisPool.getResource().exists("namee");
        //如果存在
        if (namee1==false){
            System.out.println("重新输入");
            //set数据类型  增加
            jedisPool.getResource().sadd("namee",
                    "白世纪",
                    "陈红利",
                    "陈世纪",
                    "陈洋洋",
                    "付春辉",
                    "高芳芳",
                    "郭旭",
                    "胡艺果",
                    "杜晓梦",
                    "贾礼博",
                    "李雪莹",
                    "李祎豪",
                    "林梦娇",
                    "刘顺顺",
                    "卢光辉",
                    "吕亚伟",
                    "宁静静",
                    "牛志洋",
                    "史倩影",
                    "宋健行",
                    "孙超阳",
                    "孙乾力",
                    "田君垚",
                    "汪高洋",
                    "王学斌",
                    "杨天枫",
                    "杨原辉",
                    "袁仕奇",
                    "张浩宇",
                    "张晓宇",
                    "张志鹏",
                    "赵博苛",
                    "邹开源");
        }else {
           // 随机删除
            String spop = jedisPool.getResource().spop("namee");
            System.out.println("点到名字的是 " + spop);
            //查看剩余条数
            Long namee2 = jedisPool.getResource().scard("namee");
            System.out.println("还剩余" + namee2 + "个人");
        }
    }

}
