package com.yg.api.testcase;

import com.yg.api.ApplicationTests;
import com.yg.api.common.utils.CookieUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public abstract class BaseTest extends ApplicationTests {
    @Test
    void contextLoads() {
        System.out.println(baseUser);
    }

    // 可以在这里定义一些公用的测试方法或属性
    @BeforeClass
    public void test() {
        CookieUtil.getCookie(baseUser, basePassword);
    }

}
