package com.me.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.usercenter.annotation.Printable;
import com.me.usercenter.model.domain.User;
import com.me.usercenter.model.request.UserLoginRequest;
import com.me.usercenter.model.request.UserRegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/8/29 13:43
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testInsertUser() {

        User user = new User();

        user.setName("meme");
        user.setUsername("meme123456");
        user.setAvatarUrl("https://localhost:6969/me/895");
        user.setGender(0);
        user.setPassword("123456");
        user.setPhone("189658369");
        user.setEmail("96583325@qq.com");

        boolean save = userService.save(user);

        Assertions.assertTrue(save);
    }

    @Test
    public void testUserRegister() {

        String username = "meme96961";
        String password = "123456789";
        String verifyPassword = "123456789";

        long l = userService.userRegister(username, password, verifyPassword);

        Assertions.assertSame(-1L, l);
    }

    @Test
    public void testQueryUser() {

        String username = "meme9696";

        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        User regiesteredUser = userService.getOne(query);

        Assertions.assertNotNull(regiesteredUser);
    }

    @Test
    public void testLogicDelete() {

        String username = "meme96961";

        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        User regiesteredUser = userService.getOne(query);

        System.out.println(regiesteredUser);

        Assertions.assertNotNull(regiesteredUser);
    }

    @Test
    public void testNull() {

        User user = null;

        if (!(user instanceof User)) {
            System.out.println("成功了");
        }
    }

    @Test
    public void testQueryNull() {

        String username = null;

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        List<User> list = userService.list(userQueryWrapper);

        System.out.println(list);
    }

    @Test
    public void testClassName() throws InstantiationException, IllegalAccessException {

        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("8888");
        registerRequest.setPassword("8888888");
        registerRequest.setVerifyPassword("8888888");

        Class<? extends UserRegisterRequest> aClass = registerRequest.getClass();

        UserRegisterRequest request = aClass.newInstance();

        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Printable.class)) {
                System.out.println(field.get(registerRequest));
                field.set(request, field.get(registerRequest));
            } else {
                Class<?> fieldType = field.getType();
                Object object = fieldType.newInstance();
                field.set(request, object);
            }
        }

        System.out.println(request);
    }

    @Test
    void testRedis() {

        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        String key = "phone";
        String value = "13530952666";

        opsForValue.set(key, value);

        System.out.println(key + "=" + opsForValue.get(key));
    }
}
