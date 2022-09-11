package cn.lijilong.zauth.service.impl;

import cn.lijilong.zauth.dao.RoleAuthorityRelateDao;
import cn.lijilong.zauth.dto.RoleAuthDTO;
import cn.lijilong.zauth.entity.RoleAuthorityRelateEntity;
import cn.lijilong.zauth.service.RoleAuthorityRelateService;

import javax.annotation.Resource;

import cn.lijilong.zcache.ZCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色权限映射(RoleAuthorityRelate)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
@Service
public class RoleAuthorityRelateServiceImpl implements RoleAuthorityRelateService {
    @Resource
    private RoleAuthorityRelateDao roleAuthorityRelateDao;
    @Resource
    ZCache zCache;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RoleAuthorityRelateEntity queryById(Long id) {
        return roleAuthorityRelateDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<RoleAuthorityRelateEntity> queryByPage(PageRequest pageRequest) {
        return roleAuthorityRelateDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param roleAuthorityRelate 实例对象
     * @return 实例对象
     */
    @Override
    public RoleAuthorityRelateEntity insert(RoleAuthorityRelateEntity roleAuthorityRelate) {
        return roleAuthorityRelateDao.save(roleAuthorityRelate);
    }

    /**
     * 修改数据
     *
     * @param roleAuthorityRelate 实例对象
     * @return 实例对象
     */
    @Override
    public RoleAuthorityRelateEntity update(RoleAuthorityRelateEntity roleAuthorityRelate) {
        if (roleAuthorityRelateDao.findById(roleAuthorityRelate.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        return roleAuthorityRelateDao.save(roleAuthorityRelate);
    }

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long[] ids) {
        roleAuthorityRelateDao.deleteAllById(Arrays.asList(ids));
        return true;
    }

    @Override
    public boolean saveAll(RoleAuthDTO roleAuthDTO) {

        //删除原有关联
        roleAuthorityRelateDao.deleteAllByRoleId(roleAuthDTO.getRoleIds());

        List<RoleAuthorityRelateEntity> entities = new ArrayList<>();
        Long ts = System.currentTimeMillis();
        roleAuthDTO.getRoleIds().forEach(roleId ->{
            roleAuthDTO.getAuthIds().forEach(authId ->{
                RoleAuthorityRelateEntity roleAuthorityRelateEntity = new RoleAuthorityRelateEntity();
                roleAuthorityRelateEntity.setRoleId(roleId);
                roleAuthorityRelateEntity.setAuthId(authId);
                roleAuthorityRelateEntity.setRelateTime(ts);
                entities.add(roleAuthorityRelateEntity);
            });
        });

        roleAuthorityRelateDao.saveAll(entities);

        //清除所有权限缓存
        zCache.deleteAll("auths-");
        zCache.deleteAll("roles-");

        return true;
    }

}
