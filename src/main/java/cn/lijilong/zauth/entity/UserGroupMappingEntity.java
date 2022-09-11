package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 用户组映射(UserGroupMapping)实体类
 *
 * @author lijilong
 * @since 2022-05-29 10:08:31
 */
@Data
@Entity
@ApiModel("用户组映射")
@Table(name = "user_group_mapping", indexes = {@Index(columnList = "user_id", unique = true), @Index(columnList = "group_id")})
public class UserGroupMappingEntity {
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
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 组id
     */
    @ApiModelProperty("组id")
    @Column(name = "group_id", nullable = false)
    private Long groupId;

}

