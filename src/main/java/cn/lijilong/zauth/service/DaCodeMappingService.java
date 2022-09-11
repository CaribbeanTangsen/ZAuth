package cn.lijilong.zauth.service;

import cn.lijilong.zauth.entity.DaCodeMappingEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;


/**
 * 数据权限映射表(DaCodeMapping)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface DaCodeMappingService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DaCodeMappingEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<DaCodeMappingEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param daCodeMapping 实例对象
     * @return 实例对象
     */
    DaCodeMappingEntity insert(DaCodeMappingEntity daCodeMapping);

    /**
     * 修改数据
     *
     * @param daCodeMapping 实例对象
     * @return 实例对象
     */
    DaCodeMappingEntity update(DaCodeMappingEntity daCodeMapping);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

}
