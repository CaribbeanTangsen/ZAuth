package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.UserRoleRelateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户角色映射(UserRoleRelate)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface UserRoleRelateDao extends JpaRepository<UserRoleRelateEntity, Long>, JpaSpecificationExecutor<UserRoleRelateEntity> {

    @Modifying
    @Query(value="delete from UserRoleRelateEntity where userId in (:userIds)")
    Integer deleteAllByUserId(List<Long> userIds);
}

