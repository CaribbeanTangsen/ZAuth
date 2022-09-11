package cn.lijilong.zauth.entity;

import cn.lijilong.zauth.dto.TreeEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 用户组(UserGroup)实体类
 *
 * @author lijilong
 * @since 2022-05-26 10:21:06
 */
@Data
@Entity
@ApiModel("用户组")
@Table(name = "user_group", indexes = {@Index(columnList = "tag", unique = true), @Index(columnList = "da_code")})
public class UserGroupEntity implements TreeEntity<Long,Long> {
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
     * 用户组名称
     */
    @ApiModelProperty("用户组名称")
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 用户组标识
     */
    @ApiModelProperty("用户组标识")
    @Column(name = "tag", nullable = false)
    private String tag;
    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    @Column(name = "super_id")
    private Long superId;
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    @Column(name = "create_user")
    private Long createUser;
    /**
     * 修改者
     */
    @ApiModelProperty("修改者")
    @Column(name = "update_user")
    private Long updateUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time", nullable = false)
    private Long createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @Column(name = "update_time")
    private Long updateTime;
    /**
     * 数据权限标识
     */
    @ApiModelProperty("数据权限标识")
    @Column(name = "da_code", nullable = false)
    private String daCode;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    @Column(name = "rank_num", nullable = false)
    private Integer rankNum;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("固定数据")
    @Column(name = "fixed", nullable = false)
    private Boolean fixed;

    @Override
    public Long getValue() {
        return id;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public boolean isDisable() {
        return this.getSuperId() == null;
    }

}

