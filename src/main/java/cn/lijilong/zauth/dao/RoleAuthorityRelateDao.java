package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.RoleAuthorityRelateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色权限映射(RoleAuthorityRelate)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface RoleAuthorityRelateDao extends JpaRepository<RoleAuthorityRelateEntity, Long>, JpaSpecificationExecutor<RoleAuthorityRelateEntity> {

    @Modifying
    @Query(value = "delete from RoleAuthorityRelateEntity  where roleId in (:roleIds)")
    Integer deleteAllByRoleId(List<Long> roleIds);
}

