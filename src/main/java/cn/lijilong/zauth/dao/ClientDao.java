package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.dto.ClientInfoDTO;
import cn.lijilong.zauth.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户端(Client)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface ClientDao extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {

    Optional<ClientEntity> findByAppId(String appId);

    @Query("select new cn.lijilong.zauth.dto.ClientInfoDTO(r,g.name,g.id) from ClientEntity r " +
            "inner join ClientGroupMappingEntity rm on r.id = rm.clientId " +
            "left join ClientGroupEntity g on g.id = rm.groupId " +
            "where r.clientName like concat('%',?1,'%')  " +
            "and rm.groupId in ?2")
    Page<ClientInfoDTO> findAllByClientNameAndGroup(String value, List<Long> groupIds, PageRequest pageRequest);

    @Modifying
    @Transactional
    @Query(value = "update ClientEntity set deleted = 1 where id in (:ids)")
    Integer deleteByIds(List<Long> ids);
}

