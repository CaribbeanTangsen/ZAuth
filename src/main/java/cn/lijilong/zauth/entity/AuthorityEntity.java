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
 * 权限(Authority)实体类
 *
 * @author lijilong
 * @since 2022-05-23 13:28:03
 */
@Data
@Entity
@ApiModel("权限")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authority", indexes = {@Index(columnList = "auth_mark", unique = true), @Index(columnList = "da_code")})
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update gateway set deleted = 1 where id = ?")
public class AuthorityEntity {
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
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    @Column(name = "auth_name", nullable = false)
    private String authName;
    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    @Column(name = "auth_mark", nullable = false)
    private String authMark;
    /**
     * 数据权限标识
     */
    @ApiModelProperty("数据权限标识")
    @Column(name = "da_code", nullable = false)
    private String daCode;
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
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time", nullable = false)
    private Long createTime;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("固定数据")
    @Column(name = "fixed", nullable = false)
    private Boolean fixed;

}

