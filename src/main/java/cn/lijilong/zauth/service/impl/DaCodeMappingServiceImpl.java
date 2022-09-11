package cn.lijilong.zauth.service.impl;

import cn.lijilong.zauth.dao.DaCodeMappingDao;
import cn.lijilong.zauth.entity.DaCodeMappingEntity;
import cn.lijilong.zauth.service.DaCodeMappingService;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 数据权限映射表(DaCodeMapping)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
@Service
public class DaCodeMappingServiceImpl implements DaCodeMappingService {
    @Resource
    private DaCodeMappingDao daCodeMappingDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DaCodeMappingEntity queryById(Long id) {
        return daCodeMappingDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<DaCodeMappingEntity> queryByPage(PageRequest pageRequest) {
        return daCodeMappingDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param daCodeMapping 实例对象
     * @return 实例对象
     */
    @Override
    public DaCodeMappingEntity insert(DaCodeMappingEntity daCodeMapping) {
        return daCodeMappingDao.save(daCodeMapping);
    }

    /**
     * 修改数据
     *
     * @param daCodeMapping 实例对象
     * @return 实例对象
     */
    @Override
    public DaCodeMappingEntity update(DaCodeMappingEntity daCodeMapping) {
        if (daCodeMappingDao.findById(daCodeMapping.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        return daCodeMappingDao.save(daCodeMapping);
    }

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long[] ids) {
        daCodeMappingDao.deleteAllById(Arrays.asList(ids));
        return true;
    }

}
