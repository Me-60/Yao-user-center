package com.me.usercenter.controller;

import com.me.usercenter.annotation.AuthCheck;
import com.me.usercenter.annotation.Printable;
import com.me.usercenter.common.BaseResponse;
import com.me.usercenter.common.ServiceErrorDescription;
import com.me.usercenter.common.ServiceStatusCode;
import com.me.usercenter.constant.UserConstant;
import com.me.usercenter.exception.BusinessException;
import com.me.usercenter.model.domain.User;
import com.me.usercenter.model.dto.UserDTO;
import com.me.usercenter.model.enums.UserRoleEnum;
import com.me.usercenter.model.request.UserLoginRequest;
import com.me.usercenter.model.request.UserRegisterRequest;
import com.me.usercenter.service.UserService;
import com.me.usercenter.utils.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户接口
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/8/29 18:35
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@RestController
@RequestMapping("/user")
@Tag(name = "user/controller")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "register")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.NULL_PARAMETER_ERROR.getDescription());
        }

        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String verifyPassword = userRegisterRequest.getVerifyPassword();

        if (StringUtils.isAnyBlank(username, password, verifyPassword)) {
            throw new BusinessException(ServiceStatusCode.REGISTER_ERROR, ServiceErrorDescription.NULL_DATA_ERROR.getDescription());
        }

        long registerResult = userService.userRegister(username, password, verifyPassword);

        return ResultUtils.serviceSuccess(registerResult);
    }

    @Operation(summary = "login")
    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        String sessionId = session.getId();

        System.out.println(sessionId);
        if (userLoginRequest == null) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.NULL_PARAMETER_ERROR);
        }

        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();

        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ServiceStatusCode.LOGIN_ERROR, ServiceErrorDescription.NULL_DATA_ERROR);
        }

        UserDTO userLogin = userService.userLogin(username, password, httpServletRequest);

        return ResultUtils.serviceSuccess(userLogin);
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    @AuthCheck(userRole = {UserRoleEnum.USER_ROLE, UserRoleEnum.ADMIN_ROLE})
    public BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            throw new BusinessException(ServiceStatusCode.LOGOUT_ERROR, ServiceErrorDescription.NO_PERMISSIONS_ERROR);
        }

        boolean logoutResult = userService.userLogout(httpServletRequest);

        return ResultUtils.serviceSuccess(logoutResult);
    }

    @Operation(summary = "query")
    @GetMapping("/query")
    @AuthCheck(userRole = UserRoleEnum.ADMIN_ROLE)
    public BaseResponse<List<UserDTO>> userQuery(@RequestParam(required = false) String username, HttpServletRequest httpServletRequest) {

        List<UserDTO> userList = userService.userQuery(username, httpServletRequest);
        return ResultUtils.serviceSuccess(userList);
    }

    @Operation(summary = "delete")
    @PostMapping("/delete")
    @AuthCheck(userRole = UserRoleEnum.ADMIN_ROLE)
    public BaseResponse<Boolean> userDelete(@RequestParam Long id, HttpServletRequest httpServletRequest) {

        if (id == null) {
            throw new BusinessException(ServiceStatusCode.DELETE_ERROR, ServiceErrorDescription.NULL_PARAMETER_ERROR);
        }

        boolean delete = userService.userDelete(id, httpServletRequest);

        return ResultUtils.serviceSuccess(delete);
    }

    @Operation(summary = "currentUser")
    @GetMapping("/currentUser")
    @AuthCheck(userRole = {UserRoleEnum.USER_ROLE, UserRoleEnum.ADMIN_ROLE})
    public BaseResponse<UserDTO> currentUser(HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            throw new BusinessException(ServiceStatusCode.SYSTEM_ERROR, ServiceErrorDescription.NULL_PARAMETER_ERROR);
        }

        UserDTO currentedUser = userService.currentUser(httpServletRequest);

        return ResultUtils.serviceSuccess(currentedUser);
    }
}
