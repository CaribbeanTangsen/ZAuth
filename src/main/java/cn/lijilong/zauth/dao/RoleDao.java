package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.dto.RoleInfoDTO;
import cn.lijilong.zauth.entity.RoleEntity;
import cn.lijilong.zcache.annotation.CacheToRedis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 角色(Role)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface RoleDao extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {

    @Query("select new cn.lijilong.zauth.dto.RoleInfoDTO(r,g.name,g.id) from RoleEntity r " +
            "inner join RoleGroupMappingEntity rm on r.id = rm.roleId " +
            "left join RoleGroupEntity g on g.id = rm.groupId " +
            "where r.roleName like concat('%',?1,'%')  " +
            "and rm.groupId in ?2")
    Page<RoleInfoDTO> findAllByRoleNameAndGroup(String value, Collection<Long> groupId, Pageable pageable);

    @Modifying
    @Query(value = "update RoleEntity set deleted = 1 where id in (:ids)")
    Integer deleteByIds(List<Long> ids);

    RoleEntity findByTag(String tag);

    @CacheToRedis(keyParamName = "uid", preKey = "roles-", clazz = RoleEntity.class)
    @Query("from RoleEntity  r left join UserRoleRelateEntity ur on ur.roleId = r.id where ur.userId = ?1")
    List<RoleEntity> findAllByUserId(Long uid);

    @Query(value = "select r from RoleEntity r " +
            "inner join UserRoleRelateEntity ur on r.id = ur.roleId " +
            "where ur.userId in (:userIds) " +
            "group by r.id")
    List<RoleEntity> findAllByUserId(List<Long> userIds);
}

