package com.example.demo;

import com.example.demo.entity.SysUser;
import com.example.demo.service.ISysUserService;
import com.example.demo.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;
    @Test
    void contextLoads() {
        Long infraction = redisCache.increment("infraction-127.0.0.1", 3);
    }

    @Test
    void testInsert() {
        SysUser user = new SysUser("root3", "root", "17366636923");
        sysUserService.insertUser(user);
    }

    @Test
    void testUpdate() {
        sysUserService.updatePasswordByUserName("root", "123456");
    }

    @Test
    void testCount() {
        int num = sysUserService.selectCountByUserName("root");
        System.out.println(num);

    }
}
