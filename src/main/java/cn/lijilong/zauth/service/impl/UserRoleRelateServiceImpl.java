package cn.lijilong.zauth.service.impl;

import cn.lijilong.zauth.dao.UserRoleRelateDao;
import cn.lijilong.zauth.dto.UserRoleDTO;
import cn.lijilong.zauth.entity.UserRoleRelateEntity;
import cn.lijilong.zauth.service.UserRoleRelateService;

import javax.annotation.Resource;

import cn.lijilong.zcache.ZCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户角色映射(UserRoleRelate)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
@Service
public class UserRoleRelateServiceImpl implements UserRoleRelateService {
    @Resource
    private UserRoleRelateDao userRoleRelateDao;
    @Resource
    private ZCache zCache;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserRoleRelateEntity queryById(Long id) {
        return userRoleRelateDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<UserRoleRelateEntity> queryByPage(PageRequest pageRequest) {
        return userRoleRelateDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param userRoleRelate 实例对象
     * @return 实例对象
     */
    @Override
    public UserRoleRelateEntity insert(UserRoleRelateEntity userRoleRelate) {
        return userRoleRelateDao.save(userRoleRelate);
    }

    /**
     * 修改数据
     *
     * @param userRoleRelate 实例对象
     * @return 实例对象
     */
    @Override
    public UserRoleRelateEntity update(UserRoleRelateEntity userRoleRelate) {
        if (userRoleRelateDao.findById(userRoleRelate.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        return userRoleRelateDao.save(userRoleRelate);
    }

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long[] ids) {
        userRoleRelateDao.deleteAllById(Arrays.asList(ids));
        return true;
    }

    /**
     *批量保存
     *
     * @param userRoleDTO
     * @return 是否保存成功
     * */
    @Override
    public boolean saveAll(UserRoleDTO userRoleDTO) {

        //删除原有关联
        userRoleRelateDao.deleteAllByUserId(userRoleDTO.getUserIds());
        List<UserRoleRelateEntity> entities = new ArrayList<>();
        Long ts = System.currentTimeMillis();
        userRoleDTO.getUserIds().forEach(userId ->{
            userRoleDTO.getRoleIds().forEach(roleId ->{
                UserRoleRelateEntity userRoleRelateEntity = new UserRoleRelateEntity();
                userRoleRelateEntity.setUserId(userId);
                userRoleRelateEntity.setRoleId(roleId);
                userRoleRelateEntity.setRelateTime(ts);
                entities.add(userRoleRelateEntity);
            });
        });
        userRoleRelateDao.saveAll(entities);

        //清除权限缓存
        userRoleDTO.getUserIds().forEach(id -> {
            zCache.delete("auths-" + id);
            zCache.delete("roles-" + id);

        });

        return true;
    }

}
