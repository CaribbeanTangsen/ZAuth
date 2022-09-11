package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.GenericGenerator;

/**
 * 角色权限映射(RoleAuthorityRelate)实体类
 *
 * @author lijilong
 * @since 2022-05-23 13:28:04
 */
@Data
@Entity
@ApiModel("角色权限映射")
@Table(name = "role_authority_relate",indexes = {@Index(columnList = "role_id"), @Index(columnList = "auth_id")})
public class RoleAuthorityRelateEntity {
    /**
     * id
     */
    @Id
    @ApiModelProperty("id")
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "SnowflakeIdGenerator")
    private Long id;
    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    @Column(name = "auth_id", nullable = false)
    private Long authId;
    /**
     * 关联时间
     */
    @ApiModelProperty("关联时间")
    @Column(name = "relate_time", nullable = false)
    private Long relateTime;

}

