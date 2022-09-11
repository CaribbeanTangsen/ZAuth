package cn.lijilong.zauth.dto;

import cn.lijilong.zauth.entity.ClientEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("客户端信息")
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO extends ClientEntity {

    public ClientInfoDTO(ClientEntity clientEntity, String groupName, Long groupId){
        super(clientEntity.getId(), clientEntity.getClientName(), clientEntity.getClientRemark(), clientEntity.getAppId(), clientEntity.getAppSecret(), clientEntity.getAuthMethod(), clientEntity.getCreateUser(), clientEntity.getUpdateUser(), clientEntity.getCreateTime(), clientEntity.getUpdateTime(), clientEntity.getDeleted(), clientEntity.getDaCode(),clientEntity.getFixed());
        this.groupId = String.valueOf(groupId);
        this.groupName = groupName;
    }

    @ApiModelProperty("客户端组名称")
    private String groupName;
    @ApiModelProperty("客户端组id")
    private String groupId;
}
