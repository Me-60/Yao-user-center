package com.me.usercenter.service;

import com.me.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.me.usercenter.model.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 用户接口
*
* @author 25876
* @description 针对表【tb_user(用户信息表)】的数据库操作Service
* @createDate 2024-08-30 12:21:10
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param verifyPassword 校验密码
     * @return 若成功注册，返回值为用户 ID
     */
    long userRegister(String username, String password, String verifyPassword);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param request 用户登录请求
     * @return 若成功登录，返回值为用户信息
     */
    UserDTO userLogin(String username, String password, HttpServletRequest request);

    /**
     * 用户查询
     *
     * @param username 用户名
     * @param httpServletRequest 用户查询请求
     * @return 若查询成功，返回值为用户查询列表
     */
    List<UserDTO> userQuery(String username, HttpServletRequest httpServletRequest);

    /**
     * 用户删除
     *
     * @param id 用户 ID
     * @param httpServletRequest 用户删除请求
     * @return 若删除成功，返回值为 true
     */
    boolean userDelete(long id, HttpServletRequest httpServletRequest);

    /**
     * 获取当前用户
     *
     * @param httpServletRequest 当前用户请求
     * @return 当前用户脱敏信息
     */
    UserDTO currentUser(HttpServletRequest httpServletRequest);

    /**
     * 用户注销登录
     *
     * @param httpServletRequest 注销登录请求
     * @return 注销成功为 true，反之为 false
     */
    boolean userLogout(HttpServletRequest httpServletRequest);
}
