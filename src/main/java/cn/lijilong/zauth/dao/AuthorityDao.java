package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.dto.AuthInfoDTO;
import cn.lijilong.zauth.entity.AuthorityEntity;
import cn.lijilong.zcache.annotation.CacheToRedis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限(Authority)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:19
 */
public interface AuthorityDao extends JpaRepository<AuthorityEntity, Long>, JpaSpecificationExecutor<AuthorityEntity> {

    @Query("select new cn.lijilong.zauth.dto.AuthInfoDTO(r,g.name,g.id) from AuthorityEntity r " +
            "inner join AuthGroupMappingEntity rm on r.id = rm.authId " +
            "left join AuthGroupEntity g on g.id = rm.groupId " +
            "where r.authName like concat('%',?1,'%')  " +
            "and rm.groupId in ?2")
    Page<AuthInfoDTO> findAllByAuthNameAndGroup(String value, List<Long> groupIds, PageRequest pageRequest);

    @Modifying
    @Query(value = "update AuthorityEntity set deleted = 1 where id in (:ids)")
    Integer deleteByIds(List<Long> ids);

    AuthorityEntity findByAuthMark(String authMark);

    @Query(value = "select a from AuthorityEntity a " +
            "inner join RoleAuthorityRelateEntity  ra on a.id = ra.authId " +
            "where ra.roleId in (:roleIds)" +
            "group by a.id")
    List<AuthorityEntity> findAllByRoleId(List<Long> roleIds);

    @CacheToRedis(keyParamName = "uid",preKey = "auths-",clazz = AuthorityEntity.class)
    @Query(value = "from AuthorityEntity a left join RoleAuthorityRelateEntity ra on ra.authId = a.id left join UserRoleRelateEntity ur on ur.roleId = ra.roleId where ur.userId = ?1")
    List<AuthorityEntity> findAllByUserId(Long uid);
}

