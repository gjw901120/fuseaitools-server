package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.*;
import com.fuse.ai.server.web.model.dto.response.LoginResponse;
import com.fuse.ai.server.web.model.vo.UserDetailVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    Boolean sendEmailCode(SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request);

    Boolean registerByEmail(RegisterByEmailDTO registerByEmailDTO);

    LoginResponse loginByEmail(LoginByEmailDTO  loginByEmailDTO);

    LoginResponse loginByGoogle(LoginByGoogleDTO loginByGoogleDTO);

    UserDetailVO detail(UserJwtDTO userJwtDTO);

    Boolean update(UserJwtDTO userJwtDTO, UpdateUserDTO updateUserDTO);

}
