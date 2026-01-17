package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.*;
import com.fuse.ai.server.web.model.dto.response.LoginResponse;
import com.fuse.ai.server.web.model.vo.UserDetailVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    Boolean sendEmailCode(SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request);

    LoginResponse loginByEmail(LoginByEmailDTO  loginByEmailDTO);

    LoginResponse loginByGoogle(String code);

    UserDetailVO detail(UserJwtDTO userJwtDTO, String timeZone);

    Boolean update(UserJwtDTO userJwtDTO, UpdateUserDTO updateUserDTO);

}
