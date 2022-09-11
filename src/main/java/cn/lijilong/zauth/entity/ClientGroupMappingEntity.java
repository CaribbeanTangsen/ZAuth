package cn.lijilong.zauth.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 客户端组映射(ClientGroupMapping)实体类
 *
 * @author gaojie
 * @since 2022-05-31 16:39:30
 */
@Data
@Entity
@ApiModel("客户端组映射")
@Table(name = "client_group_mapping", indexes = {@Index(columnList = "client_id", unique = true), @Index(columnList = "group_id")})
public class ClientGroupMappingEntity {
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
     * 客户端id
     */
    @ApiModelProperty("客户端id")
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    /**
     * 组id
     */
    @ApiModelProperty("组id")
    @Column(name = "group_id", nullable = false)
    private Long groupId;
}
