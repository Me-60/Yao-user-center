package com.me.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.usercenter.common.ServiceErrorDescription;
import com.me.usercenter.common.ServiceStatusCode;
import com.me.usercenter.constant.UserConstant;
import com.me.usercenter.exception.BusinessException;
import com.me.usercenter.model.dto.UserDTO;
import com.me.usercenter.model.enums.UserRoleEnum;
import com.me.usercenter.service.UserService;
import com.me.usercenter.model.domain.User;
import com.me.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 用户控制层
*
* @author 25876
* @description 针对表【tb_user(用户信息表)】的数据库操作Service实现
* @createDate 2024-08-30 12:21:10
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    /**
     * 混合加密盐值
     */
    private final static String MIXED_ENCRYPT_SALT = "meme";

    @Override
    public long userRegister(String username, String password, String verifyPassword) {

        // 1. 账户、密码校验
        // （1）验证非空
        if (StringUtils.isAnyBlank(username, password, verifyPassword)) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.NULL_DATA_ERROR);
        }

        if (!checkUserInfoFormat(username, password)) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.FORMAT_ERROR);
        }

        // （5）二次校验密码，确保两次密码输入正确
        if (!verifyPassword.equals(password)) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.VERIFY_ERROR);
        }

        // （6）用户名不可以重复
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        long count = this.count(query);
        if (count > 0) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.REPEAT_ERROR);
        }

        // 2. 密码加密
        String mixedEncryptPassword = DigestUtils.md5DigestAsHex((MIXED_ENCRYPT_SALT + password).getBytes());

        // 3. 向数据库存储用户注册信息
        User validUser = new User();
        validUser.setUsername(username);
        validUser.setPassword(mixedEncryptPassword);
        boolean save = this.save(validUser);

        if (!save) {
            throw new BusinessException(ServiceStatusCode.SYSTEM_ERROR, ServiceErrorDescription.DATA_SAVE_ERROR);
        }

        return validUser.getId();
    }

    @Override
    public UserDTO userLogin(String username, String password, HttpServletRequest request) {

        // 1. 校验用户名和密码是否符合注册规定
        //（1）验证非空
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.NULL_DATA_ERROR);
        }

        if (!checkUserInfoFormat(username, password)) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.FORMAT_ERROR);
        }

        // 2. 校验密码是否正确
        //（1）密码加密
        String mixedEncryptPassword = DigestUtils.md5DigestAsHex((MIXED_ENCRYPT_SALT + password).getBytes());

        //（2）查询用户名对应的密码加密
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        User regiesteredUser = this.getOne(query);

        //（3）确认用户已注册和密码比对
        if (regiesteredUser == null) {
            log.info("user login failed, username was entered incorrectly ");
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.ENTER_ERROR);
        }

        if (!mixedEncryptPassword.equals(regiesteredUser.getPassword())) {
            log.info("user login failed, password was entered incorrectly");
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.ENTER_ERROR);
        }

        // （4）违规用户无法登陆
        String userRole = regiesteredUser.getUserRole();
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(userRole);

        if (userRoleEnum == null) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.USER_ROLE_ERROR);
        }

        if (UserRoleEnum.ILLEGAL_ROLE == userRoleEnum) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.ILLEGAL_ERROR);
        }

        // 3. 用户信息脱敏
        UserDTO safetyUser = getSafetyUser(regiesteredUser);

        // 4. 记录用户的登录态
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public List<UserDTO> userQuery(String username, HttpServletRequest httpServletRequest) {

        // 鉴权，仅管理员可查询
        if (!isAdmin(httpServletRequest)) {
            throw new BusinessException(ServiceStatusCode.QUERY_ERROR, ServiceErrorDescription.NO_PERMISSIONS_ERROR);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }

        List<User> list = this.list(userQueryWrapper);
        return this.getSafetyUserList(list);
    }

    @Override
    public boolean userDelete(long id, HttpServletRequest httpServletRequest) {

        // 鉴权，仅管理员可删除
        if (!isAdmin(httpServletRequest)) {
            throw new BusinessException(ServiceStatusCode.DELETE_ERROR, ServiceErrorDescription.NO_PERMISSIONS_ERROR);
        }

        if (id < 0) {
            throw new BusinessException(ServiceStatusCode.DELETE_ERROR, ServiceErrorDescription.ID_ERROR);
        }

        return this.removeById(id);
    }

    @Override
    public UserDTO currentUser(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        Object currentUserObj = session.getAttribute(UserConstant.USER_LOGIN_STATE);

        if (!(currentUserObj instanceof UserDTO)) {
            throw new BusinessException(ServiceStatusCode.CURRENT_USER_ERROR, ServiceErrorDescription.NOT_LOGIN_ERROR);
        }

        UserDTO currentUser = (UserDTO) currentUserObj;

        Long userId = currentUser.getId();

        if (userId < 0) {
            throw new BusinessException(ServiceStatusCode.CURRENT_USER_ERROR, ServiceErrorDescription.ID_ERROR);
        }

        User user = this.getById(userId);

        System.out.println("session 剩余访问时间为：" + session.getLastAccessedTime());

        return this.getSafetyUser(user);
    }

    @Override
    public boolean userLogout(HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            throw new BusinessException(ServiceStatusCode.LOGOUT_ERROR, ServiceErrorDescription.NULL_PARAMETER_ERROR);
        }

        HttpSession session = httpServletRequest.getSession();

        Object user = session.getAttribute(UserConstant.USER_LOGIN_STATE);

        // 确保用户已登录后再进行删除用户登录态
        if (!(user instanceof UserDTO)) {
            throw new BusinessException(ServiceStatusCode.LOGOUT_ERROR, ServiceErrorDescription.NOT_LOGIN_ERROR);
        }

        session.removeAttribute(UserConstant.USER_LOGIN_STATE);

        return true;
    }

    /**
     * 校验用户名和密码格式
     *
     * @param username 用户名
     * @param password 密码
     * @return 若格式正确，返回值为 true，反之为 false
     */
    private boolean checkUserInfoFormat(String username, String password) {

        // （2）用户名长度不小于 9 位
        if (username.length() < 9) {
            return false;
        }

        // （3）用户名仅包含字母、数字、下划线
        final String regex = "^[A-Za-z0-9_]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return false;
        }

        // （4）密码长度不小于 9 位
        if (password.length() < 9) {
            return false;
        }

        return true;
    }

    /**
     * 获取脱敏用户信息
     *
     * @param sourceUser 源用户信息
     * @return 返回值为脱敏用户信息
     */
    private UserDTO getSafetyUser(User sourceUser) {

        if (sourceUser == null) {
            throw new BusinessException(ServiceStatusCode.SYSTEM_ERROR, ServiceErrorDescription.NULL_ERROR);
        }

        UserDTO safetyUser = new UserDTO();

        safetyUser.setId(sourceUser.getId());
        safetyUser.setName(sourceUser.getName());
        safetyUser.setAvatarUrl(sourceUser.getAvatarUrl());
        safetyUser.setUsername(sourceUser.getUsername());
        safetyUser.setGender(sourceUser.getGender());
        safetyUser.setPhone(sourceUser.getPhone());
        safetyUser.setEmail(sourceUser.getEmail());
        safetyUser.setStatus(sourceUser.getStatus());
        safetyUser.setCreateTime(sourceUser.getCreateTime());
        safetyUser.setUserRole(sourceUser.getUserRole());

        return safetyUser;
    }

    /**
     * 获取脱敏用户信息列表
     *
     * @param sourceUserList 源用户信息列表
     * @return 返回值为脱敏用户信息列表
     */
    private List<UserDTO> getSafetyUserList(List<User> sourceUserList) {

        if (sourceUserList == null) {
            throw new BusinessException(ServiceStatusCode.SYSTEM_ERROR, ServiceErrorDescription.NULL_ERROR);
        }

        ArrayList<UserDTO> safetyUserList = new ArrayList<>();

        for (User sourceUser : sourceUserList) {

            UserDTO safetyUser = this.getSafetyUser(sourceUser);

            safetyUserList.add(safetyUser);
        }

        return safetyUserList;
    }

    /**
     * 鉴权
     *
     * @param httpServletRequest 用户请求
     * @return 若权限为管理员，返回值为 true，反之为 false
     */
    private boolean isAdmin(HttpServletRequest httpServletRequest) {

        Object user = httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);

        if (!(user instanceof UserDTO)) {
            return false;
        }

        UserDTO userLogin = (UserDTO) user;

        return UserConstant.ADMIN_ROLE.equals(userLogin.getUserRole());
    }
}




