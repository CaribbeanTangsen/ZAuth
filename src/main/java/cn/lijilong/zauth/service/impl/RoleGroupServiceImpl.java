package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.RoleGroupDao;
import cn.lijilong.zauth.dao.RoleGroupMappingDao;
import cn.lijilong.zauth.service.RoleGroupService;
import cn.lijilong.zauth.service.RoleService;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.RoleGroupEntity;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色组(RoleGroup)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-26 10:21:05
 */
@Service
public class RoleGroupServiceImpl implements RoleGroupService {
    @Resource
    private RoleGroupDao roleGroupDao;
    @Resource
    private RoleGroupMappingDao roleGroupMappingDao;
    @Resource
    private RoleService roleService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RoleGroupEntity queryById(Long id) {
        return roleGroupDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<RoleGroupEntity> queryByPage(PageRequest pageRequest) {
        return roleGroupDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param roleGroup 实例对象
     * @return 实例对象
     */
    @Override
    public RoleGroupEntity insert(RoleGroupEntity roleGroup) {
        RoleGroupEntity roleGroupEntity = roleGroupDao.findByTag(roleGroup.getTag());
        if (roleGroupEntity != null){
            throw new RuntimeException("组标识不能重复！");
        }

        roleGroup.setCreateTime(System.currentTimeMillis());
        roleGroup.setDaCode(IdUtil.fastSimpleUUID());
        roleGroup.setFixed(false);
        return roleGroupDao.save(roleGroup);
    }

    /**
     * 修改数据
     *
     * @param roleGroup 实例对象
     * @return 实例对象
     */
    @Override
    public RoleGroupEntity update(RoleGroupEntity roleGroup) {
        if (roleGroupDao.findById(roleGroup.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        roleGroup.setUpdateTime(System.currentTimeMillis());
        roleGroup.setFixed(false);
        return roleGroupDao.save(roleGroup);
    }

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long[] ids) {
        if (ids.length == 0){
            throw new RuntimeException("请选择数据！");
        }
        List<Long> groupIds = Arrays.asList(ids);
        Integer count = roleGroupDao.findBySuperIds(groupIds);
        if (count > 0){
            throw new RuntimeException("包含下级组！");
        }

        groupIds.forEach(id -> {
            if (roleService.queryByPage(PageRequest.of(0, 1), "", id).getTotalElements() > 0) {
                throw new RuntimeException("组下包含数据！");
            }
        });

        Long[] roleIds = roleGroupMappingDao.findByGroupIds(groupIds);
        if (roleIds.length > 0){
            roleService.deleteById(roleIds);
        }
        roleGroupDao.deleteAllById(groupIds);
        return true;
    }

    /**
     * 获取角色组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    @Override
    public List<TreeDTO> getTree(String tag) {
        List<RoleGroupEntity> roleGroups = roleGroupDao.findAllByTagRecursive(tag);
        List<TreeDTO> treeDTOS = new ArrayList<>();
        roleGroups.stream().filter(roleGroupEntity -> roleGroupEntity.getTag().equals(tag))
                .peek(roleGroupEntity -> treeDTOS.add(new TreeDTO(roleGroupEntity.getLabel(), String.valueOf(roleGroupEntity.getValue()), roleGroupEntity.isDisable(), TreeDTO.treeRecursive(roleGroupEntity.getValue(), roleGroups))))
                .collect(Collectors.toList());
        return treeDTOS;
    }

}
