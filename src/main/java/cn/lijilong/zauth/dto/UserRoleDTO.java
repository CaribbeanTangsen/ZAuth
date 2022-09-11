package cn.lijilong.zauth.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleDTO {

    private List<Long> userIds;

    private List<Long> roleIds;
}
