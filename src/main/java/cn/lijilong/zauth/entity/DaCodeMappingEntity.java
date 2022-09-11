package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.GenericGenerator;

/**
 * 数据权限映射表(DaCodeMapping)实体类
 *
 * @author lijilong
 * @since 2022-05-23 13:28:04
 */
@Data
@Entity
@ApiModel("数据权限映射表")
@Table(name = "da_code_mapping",indexes = {@Index(columnList = "user_id"), @Index(columnList = "da_code")})
public class DaCodeMappingEntity {
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
    @Column(name = "user_id")
    private Long userId;
    /**
     * 数据id
     */
    @ApiModelProperty("数据id")
    @Column(name = "da_code", nullable = false)
    private String daCode;
    /**
     * 关联时间
     */
    @ApiModelProperty("关联时间")
    @Column(name = "relate_time", nullable = false)
    private Long relateTime;
    /**
     * 组标识
     */
    @ApiModelProperty("组标识")
    @Column(name = "group_tag")
    private String groupTag;

}

