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
 * 客户端(Client)实体类
 *
 * @author lijilong
 * @since 2022-05-23 13:28:04
 */
@Data
@Entity
@ApiModel("客户端")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client",indexes = {@Index(columnList = "app_id",unique = true), @Index(columnList = "da_code")})
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update gateway set deleted = 1 where id = ?")
public class ClientEntity {
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
     * 客户端名称
     */
    @ApiModelProperty("客户端名称")
    @Column(name = "client_name", nullable = false)
    private String clientName;
    /**
     * 客户端描述
     */
    @ApiModelProperty("客户端描述")
    @Column(name = "client_remark")
    private String clientRemark;
    /**
     * 应用Id
     */
    @ApiModelProperty("应用Id")
    @Column(name = "app_id", nullable = false)
    private String appId;
    /**
     * 应用密钥
     */
    @ApiModelProperty("应用密钥")
    @Column(name = "app_secret", nullable = false)
    private String appSecret;
    /**
     * 授权方式
     */
    @ApiModelProperty("授权方式")
    @Column(name = "auth_method", nullable = false)
    private String authMethod;
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
     * 数据权限标识
     */
    @ApiModelProperty("数据权限标识")
    @Column(name = "da_code", nullable = false)
    private String daCode;

    /**
     * 逻辑删除
     */
    @ApiModelProperty("固定数据")
    @Column(name = "fixed", nullable = false)
    private Boolean fixed;

}

