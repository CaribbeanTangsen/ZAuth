package cn.lijilong.zauth.dto;

import cn.lijilong.zauth.entity.AuthorityEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("权限信息")
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfoDTO extends AuthorityEntity {

    public AuthInfoDTO(AuthorityEntity authorityEntity, String groupName, Long groupId){
        super(authorityEntity.getId(), authorityEntity.getAuthName(), authorityEntity.getAuthMark(), authorityEntity.getDaCode(), authorityEntity.getCreateUser(), authorityEntity.getUpdateUser(), authorityEntity.getUpdateTime(), authorityEntity.getDeleted(), authorityEntity.getCreateTime(),authorityEntity.getFixed());
        this.groupId = String.valueOf(groupId);
        this.groupName = groupName;
    }

    @ApiModelProperty("权限组名称")
    private String groupName;
    @ApiModelProperty("权限组id")
    private String groupId;
}
