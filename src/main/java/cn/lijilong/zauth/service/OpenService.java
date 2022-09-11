package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.LoginDTO;
import cn.lijilong.zauth.dto.LoginStateDTO;

public interface OpenService {

    LoginStateDTO login(LoginDTO loginDTO);

}
