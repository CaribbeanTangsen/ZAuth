package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户角色映射(UserRoleRelate)实体类
 *
 * @author lijilong
 * @since 2022-05-23 13:28:04
 */
@Data
@Entity
@ApiModel("用户角色映射")
@Table(name = "user_role_relate",indexes = {@Index(columnList = "user_id"), @Index(columnList = "role_id")})
public class UserRoleRelateEntity {
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
     * 用户id
     */
    @ApiModelProperty("用户id")
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    /**
     * 关联时间
     */
    @ApiModelProperty("关联时间")
    @Column(name = "relate_time", nullable = false)
    private Long relateTime;

}

