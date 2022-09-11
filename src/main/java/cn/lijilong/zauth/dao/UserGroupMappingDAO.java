package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.UserGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 用户组映射(RoleGroupMapping)表数据库访问层
 *
 * @author gaojie
 * @since 2022-05-31 8:34:05
 */
public interface UserGroupMappingDAO extends JpaRepository<UserGroupMappingEntity, Long>, JpaSpecificationExecutor<UserGroupMappingEntity> {

    UserGroupMappingEntity findByUserId(Long id);

    @Modifying
    @Query(value="delete from UserGroupMappingEntity where userId in (:userIds)")
    void deleteByUserId(List<Long> userIds);

    @Query(value="select userId from UserGroupMappingEntity where groupId in (:groupIds)")
    Collection<Long> findByGroupIds(List<Long> groupIds);

}
