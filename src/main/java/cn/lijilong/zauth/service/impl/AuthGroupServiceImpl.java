package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.service.AuthorityService;
import cn.lijilong.zauth.dao.AuthGroupMappingDao;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.AuthGroupEntity;
import cn.lijilong.zauth.dao.AuthGroupDao;
import cn.lijilong.zauth.service.AuthGroupService;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组(AuthGroup)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-26 10:21:03
 */
@Service
public class AuthGroupServiceImpl implements AuthGroupService {
    @Resource
    private AuthGroupDao authGroupDao;
    @Resource
    private AuthGroupMappingDao authGroupMappingDao;
    @Resource
    private AuthorityService authorityService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AuthGroupEntity queryById(Long id) {
        return authGroupDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<AuthGroupEntity> queryByPage(PageRequest pageRequest) {
        return authGroupDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param authGroup 实例对象
     * @return 实例对象
     */
    @Override
    public AuthGroupEntity insert(AuthGroupEntity authGroup) {
        AuthGroupEntity authGroupEntity = authGroupDao.findByTag(authGroup.getTag());
        if (authGroupEntity != null) {
            throw new RuntimeException("组标识不能重复！");
        }

        authGroup.setCreateTime(System.currentTimeMillis());
        authGroup.setDaCode(IdUtil.fastSimpleUUID());
        authGroup.setFixed(false);
        return authGroupDao.save(authGroup);
    }

    /**
     * 修改数据
     *
     * @param authGroup 实例对象
     * @return 实例对象
     */
    @Override
    public AuthGroupEntity update(AuthGroupEntity authGroup) {
        if (authGroupDao.findById(authGroup.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        authGroup.setUpdateTime(System.currentTimeMillis());
        authGroup.setFixed(false);
        return authGroupDao.save(authGroup);
    }

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long[] ids) {
        if (ids.length == 0) {
            throw new RuntimeException("请选择数据！");
        }
        List<Long> groupIds = Arrays.asList(ids);
        Integer count = authGroupDao.countBySuperIdIn(groupIds);
        if (count > 0) {
            throw new RuntimeException("包含下级组！");
        }

        groupIds.forEach(id -> {
            if (authorityService.queryByPage(PageRequest.of(0, 1), "", id).getTotalElements() > 0) {
                throw new RuntimeException("组下包含数据！");
            }
        });

        Collection<Long> findGroupIds = authGroupMappingDao.findByGroupIds(groupIds);
        Long[] authIds = new Long[findGroupIds.size()];
        findGroupIds.toArray(authIds);
        if (authIds.length > 0) {
            authorityService.deleteById(authIds);
        }
        authGroupDao.deleteAllById(groupIds);
        return true;
    }

    /**
     * 获取权限组树
     *
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    @Override
    public List<TreeDTO> getTree(String tag) {
        List<AuthGroupEntity> authGroups = authGroupDao.findAllByTagRecursive(tag);
        List<TreeDTO> treeDTOS = new ArrayList<>();
        authGroups.stream().filter(authGroupEntity -> authGroupEntity.getTag().equals(tag))
                .peek(authGroupEntity -> treeDTOS.add(new TreeDTO(authGroupEntity.getLabel(), String.valueOf(authGroupEntity.getValue()), authGroupEntity.isDisable(), TreeDTO.treeRecursive(authGroupEntity.getValue(), authGroups))))
                .collect(Collectors.toList());
        return treeDTOS;
    }

}
