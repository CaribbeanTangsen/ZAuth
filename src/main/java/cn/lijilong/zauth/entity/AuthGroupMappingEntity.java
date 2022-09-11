package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 权限组映射(AuthGroupMapping)实体类
 *
 * @author gaojie
 * @since 2022-05-31 14:20:30
 */
@Data
@Entity
@ApiModel("权限组映射")
@Table(name = "auth_group_mapping", indexes = {@Index(columnList = "auth_id", unique = true), @Index(columnList = "group_id")})
public class AuthGroupMappingEntity {
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
     * 权限id
     */
    @ApiModelProperty("权限id")
    @Column(name = "auth_id", nullable = false)
    private Long authId;
    /**
     * 组id
     */
    @ApiModelProperty("组id")
    @Column(name = "group_id", nullable = false)
    private Long groupId;
}
