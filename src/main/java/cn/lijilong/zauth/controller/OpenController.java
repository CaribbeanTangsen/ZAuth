package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.config.RequestDataDTO;
import cn.lijilong.zauth.dto.LoginDTO;
import cn.lijilong.zauth.dto.LoginStateDTO;
import cn.lijilong.zauth.service.OpenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "开放接口")
@RequestMapping("/open")
@CrossOrigin
public class OpenController {

    @Resource
    OpenService openService;

    @ZAuthFilter(any = true)
    @PostMapping("/login")
    @ApiOperation("登陆")
    public RequestDataDTO<LoginStateDTO> login(@RequestBody LoginDTO loginDTO) {
        return RequestDataDTO.buildSuccess(openService.login(loginDTO));
    }

}
