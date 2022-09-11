package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.UserGroupEntity;
import cn.lijilong.zcache.annotation.CacheToRedis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 用户组(UserGroup)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:06
 */
public interface UserGroupDao extends JpaRepository<UserGroupEntity, Long>, JpaSpecificationExecutor<UserGroupEntity> {

    @Query(value = "with recursive tmp as ( select ug1.* from user_group ug1 where ug1.tag = ?1 union all select ug2.* from user_group ug2 inner join tmp on tmp.id = ug2.super_id) select * from tmp order by rank_num", nativeQuery = true)
    List<UserGroupEntity> findAllByTagRecursive(String tag);

    @Query(value = "with recursive tmp as ( select ug1.* from user_group ug1 where ug1.id = ?1 union all select ug2.* from user_group ug2 inner join tmp on tmp.id = ug2.super_id) select * from tmp order by rank_num", nativeQuery = true)
    List<UserGroupEntity> findAllByIdRecursive(Long id);

    UserGroupEntity findByTag(String tag);

    @Query(value="select count(id) from UserGroupEntity where superId in (:groupIds)")
    Integer findBySuperIds(List<Long> groupIds);

    @CacheToRedis(keyParamName = "uid",preKey = "ugs-",clazz = UserGroupEntity.class)
    @Query("from UserGroupEntity ug left join UserGroupMappingEntity ugm on ug.id = ugm.groupId where ugm.userId = ?1")
    Collection<UserGroupEntity> findAllByUserId(Long uid);

}

