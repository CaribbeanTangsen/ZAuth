package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.RoleGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色组(RoleGroup)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:05
 */
public interface RoleGroupDao extends JpaRepository<RoleGroupEntity, Long>, JpaSpecificationExecutor<RoleGroupEntity> {

    @Query(value = "with recursive tmp as ( select rg1.* from role_group rg1 where rg1.tag = ?1 union all select rg2.* from role_group rg2 inner join tmp on tmp.id = rg2.super_id) select * from tmp ", nativeQuery = true)
    List<RoleGroupEntity> findAllByTagRecursive(String tag);

    @Query(value = "with recursive tmp as ( select rg1.* from role_group rg1 where rg1.id = ?1 union all select rg2.* from role_group rg2 inner join tmp on tmp.id = rg2.super_id) select * from tmp ", nativeQuery = true)
    List<RoleGroupEntity> findAllByIdRecursive(Long id);

    RoleGroupEntity findByTag(String tag);

    @Query(value="select count(id) from RoleGroupEntity where superId in (:groupIds)")
    Integer findBySuperIds(List<Long> groupIds);
}

