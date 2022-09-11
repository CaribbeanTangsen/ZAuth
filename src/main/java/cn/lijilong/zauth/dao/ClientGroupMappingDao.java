package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.ClientGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 客户端组映射(AuthGroupMapping)表数据库访问层
 *
 * @author gaojie
 * @since 2022-05-31 16:43:30
 */
public interface ClientGroupMappingDao extends JpaRepository<ClientGroupMappingEntity, Long>, JpaSpecificationExecutor<ClientGroupMappingEntity> {

    ClientGroupMappingEntity findByClientId(Long id);

    @Modifying
    @Query(value="delete from ClientGroupMappingEntity where clientId in (:clientIds)")
    void deleteByClientId(List<Long> clientIds);

    @Query(value="select clientId from ClientGroupMappingEntity where groupId in (:groupIds)")
    Collection<Long> findByGroupIds(List<Long> groupIds);
}
