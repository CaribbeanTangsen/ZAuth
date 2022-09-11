package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.AuthGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 权限组映射(AuthGroupMapping)表数据库访问层
 *
 * @author gaojie
 * @since 2022-05-31 14:20:30
 */
public interface AuthGroupMappingDao extends JpaRepository<AuthGroupMappingEntity, Long>, JpaSpecificationExecutor<AuthGroupMappingEntity> {

    AuthGroupMappingEntity findByAuthId(Long id);

    @Modifying
    @Transactional
    @Query(value="delete from AuthGroupMappingEntity where authId in (:authIds)")
    void deleteAllByAuthIdIn(List<Long> authIds);

    @Query(value="select authId from AuthGroupMappingEntity where groupId in (:groupIds)")
    Collection<Long> findByGroupIds(List<Long> groupIds);
}
