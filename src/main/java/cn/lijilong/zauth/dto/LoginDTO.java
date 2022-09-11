package cn.lijilong.zauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登陆传入数据")
public class LoginDTO {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "回调地址")
    private String redirectUri;
    @ApiModelProperty(value = "鉴权模式",required = true)
    private String mode;
    @ApiModelProperty(value = "客户端id",required = true)
    private String appId;
    @ApiModelProperty(value = "客户端密钥")
    private String clientSecret;
    @ApiModelProperty(value = "code")
    private String code;
    @ApiModelProperty(value = "刷新密钥")
    private String refToken;
}
