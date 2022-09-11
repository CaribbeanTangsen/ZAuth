package cn.lijilong.zauth.dao;

import cn.lijilong.zauth.entity.DaCodeMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据权限映射表(DaCodeMapping)表数据库访问层
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface DaCodeMappingDao extends JpaRepository<DaCodeMappingEntity, Long>, JpaSpecificationExecutor<DaCodeMappingEntity> {

}

