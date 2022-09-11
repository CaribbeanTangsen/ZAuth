package cn.lijilong.zauth.dto;

import cn.lijilong.zauth.entity.RoleEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("角色信息")
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfoDTO extends RoleEntity {

    public RoleInfoDTO (RoleEntity roleEntity, String groupName, Long groupId){
        super(roleEntity.getId(), roleEntity.getRoleName(), roleEntity.getCreateUser(), roleEntity.getUpdateUser(), roleEntity.getCreateTime(), roleEntity.getUpdateTime(), roleEntity.getDeleted(), roleEntity.getRoleRemark(), roleEntity.getDaCode(), roleEntity.getTag(), roleEntity.getFixed());
        this.groupId = String.valueOf(groupId);
        this.groupName = groupName;
    }

    @ApiModelProperty("角色组名称")
    private String groupName;
    @ApiModelProperty("角色组id")
    private String groupId;

}
