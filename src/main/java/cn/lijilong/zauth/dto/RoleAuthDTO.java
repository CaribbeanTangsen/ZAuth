package cn.lijilong.zauth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleAuthDTO {
    private List<Long> roleIds;

    private List<Long> authIds;
}