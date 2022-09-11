package cn.lijilong.zauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("登陆状态")
@AllArgsConstructor
@NoArgsConstructor
public class LoginStateDTO {
    @ApiModelProperty("令牌")
    private String token;
    @ApiModelProperty("刷新令牌")
    private String refToken;
    @ApiModelProperty("授权代码")
    private String code;
    @ApiModelProperty("回调地址")
    private String redirectUri;
    @ApiModelProperty(value = "登陆状态",required = true)
    private boolean state;
    @ApiModelProperty("提示信息")
    private String info;

    public static LoginStateDTO makeByCodeMode(String code, String redirectUri, boolean state, String info) {
        return new LoginStateDTO(null, null, code, redirectUri, state, info);
    }

    public static LoginStateDTO makeByPassword(String token, String refToken, boolean state, String info) {
        return new LoginStateDTO(token, refToken, null, null, state, info);
    }

}
