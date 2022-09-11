package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.RoleGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色组映射(RoleGroupMapping)表数据库访问层
 *
 * @author gaojie
 * @since 2022-05-30 18:04:05
 */
public interface RoleGroupMappingDao extends JpaRepository<RoleGroupMappingEntity, Long>, JpaSpecificationExecutor<RoleGroupMappingEntity> {

    RoleGroupMappingEntity findByRoleId(Long id);

    @Modifying
    @Query(value="delete from RoleGroupMappingEntity where roleId in (:roleIds)")
    Integer deleteByRoleId(List<Long> roleIds);

    @Query(value="select roleId from RoleGroupMappingEntity where groupId in (:groupIds)")
    Long[] findByGroupIds(List<Long> groupIds);
}
