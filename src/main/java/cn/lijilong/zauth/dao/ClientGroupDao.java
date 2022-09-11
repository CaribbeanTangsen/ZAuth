package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.ClientGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 客户端组(ClientGroup)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:04
 */
public interface ClientGroupDao extends JpaRepository<ClientGroupEntity, Long>, JpaSpecificationExecutor<ClientGroupEntity> {

    @Query(value = "with recursive tmp as ( select cg1.* from client_group cg1 where cg1.tag = ?1 union all select cg2.* from client_group cg2 inner join tmp on tmp.id = cg2.super_id) select * from tmp ", nativeQuery = true)
    List<ClientGroupEntity> findAllByTagRecursive(String tag);

    @Query(value = "with recursive tmp as ( select cg1.* from client_group cg1 where cg1.id = ?1 union all select cg2.* from client_group cg2 inner join tmp on tmp.id = cg2.super_id) select * from tmp ", nativeQuery = true)
    List<ClientGroupEntity> findAllByIdRecursive(Long id);

    ClientGroupEntity findByTag(String tag);

//    @Query(value="select count(id) from ClientGroupEntity where superId in (:groupIds)")
    Integer countBySuperIdIn(List<Long> groupIds);
}

