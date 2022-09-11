package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.AuthGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限组(AuthGroup)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:02
 */
public interface AuthGroupDao extends JpaRepository<AuthGroupEntity, Long>, JpaSpecificationExecutor<AuthGroupEntity> {

    @Query(value = "with recursive tmp as ( select ag1.* from auth_group ag1 where ag1.tag = ?1 union all select ag2.* from auth_group ag2 inner join tmp on tmp.id = ag2.super_id) select * from tmp ", nativeQuery = true)
    List<AuthGroupEntity> findAllByTagRecursive(String tag);

    @Query(value = "with recursive tmp as ( select ag1.* from auth_group ag1 where ag1.id = ?1 union all select ag2.* from auth_group ag2 inner join tmp on tmp.id = ag2.super_id) select * from tmp ", nativeQuery = true)
    List<AuthGroupEntity> findAllByIdRecursive(Long id);

    AuthGroupEntity findByTag(String tag);

    @Query(value="select count(id) from AuthGroupEntity where superId in (:groupIds)")
    Integer countBySuperIdIn(List<Long> groupIds);
}

