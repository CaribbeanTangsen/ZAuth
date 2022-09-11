package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 角色组映射(RoleGroupMapping)实体类
 *
 * @author gaojie
 * @since 2022-05-30 17:06:30
 */
@Data
@Entity
@ApiModel("角色组映射")
@Table(name = "role_group_mapping", indexes = {@Index(columnList = "role_id", unique = true), @Index(columnList = "group_id")})
public class RoleGroupMappingEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "SnowflakeIdGenerator")
    @ApiModelProperty("id")
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    /**
     * 组id
     */
    @ApiModelProperty("组id")
    @Column(name = "group_id", nullable = false)
    private Long groupId;
}
