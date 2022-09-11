package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.dto.UserInfoDTO;
import cn.lijilong.zauth.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 用户(User)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface UserDao extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("from UserEntity where  email = ?1 or phone = ?1 or name = ?1")
    List<UserEntity> findAllByEmailOrPhoneOOrName(String value);

    @Query("select new cn.lijilong.zauth.dto.UserInfoDTO(u,g.name,g.id) from UserEntity u " +
            "inner join UserGroupMappingEntity gm on u.id = gm.userId " +
            "left join UserGroupEntity g on g.id = gm.groupId " +
            "where  (u.name like concat('%',?1,'%') or u.email like concat('%',?1,'%') or u.phone like concat('%',?1,'%') or u.userName like concat('%',?1,'%')) " +
            "and gm.groupId in ?2")
    Page<UserInfoDTO> findAllByNameOrEmailOrPhoneOrUserNameAndGroup(String value, Collection<Long> groupId, Pageable pageable);

    @Modifying
    @Query(value = "update UserEntity set deleted = 1 where id in (:ids)")
    Integer deleteByIds(List<Long> ids);
}

