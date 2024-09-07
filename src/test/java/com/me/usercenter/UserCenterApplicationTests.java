package com.me.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mixedEncrypt() {
        final String mixedEncryptSalt = "meme";
        String password = "123456";
        String mixedEncryptPassword = DigestUtils.md5DigestAsHex((mixedEncryptSalt + password).getBytes());
        System.out.println(mixedEncryptPassword);
    }
}
