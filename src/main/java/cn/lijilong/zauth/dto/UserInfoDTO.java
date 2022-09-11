package cn.lijilong.zauth.dto;

import cn.lijilong.zauth.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO extends UserEntity {

    public UserInfoDTO(UserEntity userEntity, String groupName, Long groupId) {
        super(userEntity.getId(), userEntity.getName(), userEntity.getPassword(), userEntity.getSalt(), userEntity.getEmail(), userEntity.getPhone(), userEntity.getToken(), userEntity.getRefreshToken(), userEntity.getEnable(), userEntity.getRegDate(), userEntity.getTimeoutDate(), userEntity.getUpdateDate(), userEntity.getDeleted(), userEntity.getUserName(), userEntity.getDaCode(), userEntity.getAuthMethod(), userEntity.getLastLogin(), userEntity.getRepeatOnline(), userEntity.getFixed());
        this.groupId = String.valueOf(groupId);
        this.groupName = groupName;
    }

    @ApiModelProperty("用户组名称")
    private String groupName;
    @ApiModelProperty("用户组id")
    private String groupId;

    @ApiModelProperty("用户旧密码")
    private String oPass;
    @ApiModelProperty("用户新密码")
    private String nPass;
}
