package cn.lijilong.zauth.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * 角色(Role)实体类
 *
 * @author lijilong
 * @since 2022-05-24 19:17:52
 */
@Data
@Entity
@ApiModel("角色")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role",indexes = {@Index(columnList = "tag",unique = true), @Index(columnList = "da_code")})
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update gateway set deleted = 1 where id = ?")
public class RoleEntity {
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
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @Column(name = "role_name", nullable = false)
    private String roleName;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @Column(name = "create_user")
    private Long createUser;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @Column(name = "update_user")
    private Long updateUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time", nullable = false)
    private Long createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Long updateTime;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("逻辑删除")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    @Column(name = "role_remark")
    private String roleRemark;
    /**
     * 数据权限标识
     */
    @ApiModelProperty("数据权限标识")
    @Column(name = "da_code", nullable = false)
    private String daCode;
    /**
     * 角色标识
     */
    @ApiModelProperty("角色标识")
    @Column(name = "tag", nullable = false)
    private String tag;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("固定数据")
    @Column(name = "fixed", nullable = false)
    private Boolean fixed;

}

