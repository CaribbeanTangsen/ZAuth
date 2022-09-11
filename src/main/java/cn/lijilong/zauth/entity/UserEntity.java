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
 * 用户(User)实体类
 *
 * @author lijilong
 * @since 2022-05-24 10:47:16
 */
@Data
@Entity
@ApiModel("用户")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user",indexes = {@Index(columnList = "email",unique = true), @Index(columnList = "phone",unique = true),@Index(columnList = "da_code")})
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update gateway set deleted = 1 where id = ?")
public class UserEntity{
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
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * 密码盐
     */
    @ApiModelProperty("密码盐")
    @Column(name = "salt", nullable = false)
    private String salt;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    @Column(name = "email")
    private String email;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Column(name = "phone", nullable = false)
    private String phone;
    /**
     * 令牌
     */
    @ApiModelProperty("令牌")
    @Column(name = "token")
    private String token;
    /**
     * 刷新令牌
     */
    @ApiModelProperty("刷新令牌")
    @Column(name = "refresh_token")
    private String refreshToken;
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    @Column(name = "enable", nullable = false)
    private Boolean enable;
    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    @Column(name = "reg_date", nullable = false)
    private Long regDate;
    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    @Column(name = "timeout_date")
    private Long timeoutDate;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_date")
    private Long updateDate;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("逻辑删除")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    @Column(name = "user_name", nullable = false)
    private String userName;
    /**
     * 数据权限id
     */
    @ApiModelProperty("数据权限id")
    @Column(name = "da_code")
    private String daCode;
    /**
     * 授权方式
     */
    @ApiModelProperty("授权方式")
    @Column(name = "auth_method")
    private String authMethod;
    /**
     * 最后登陆时间
     */
    @ApiModelProperty("最后登陆时间")
    @Column(name = "last_login")
    private Long lastLogin;
    /**
     * 是否允许多用户在线
     */
    @ApiModelProperty("是否允许多用户在线")
    @Column(name = "repeat_online", nullable = false)
    private Boolean repeatOnline;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("固定数据")
    @Column(name = "fixed", nullable = false)
    private Boolean fixed;

}

