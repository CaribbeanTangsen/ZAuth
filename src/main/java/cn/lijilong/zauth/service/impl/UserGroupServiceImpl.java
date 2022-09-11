package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.UserGroupDao;
import cn.lijilong.zauth.dao.UserGroupMappingDAO;
import cn.lijilong.zauth.service.UserGroupService;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.UserGroupEntity;

import javax.annotation.Resource;

import cn.lijilong.zauth.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组(UserGroup)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-26 10:21:06
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Resource
    private UserGroupDao userGroupDao;
    @Resource
    private UserGroupMappingDAO userGroupMappingDAO;
    @Resource
    private UserService userService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserGroupEntity queryById(Long id) {
        return userGroupDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<UserGroupEntity> queryByPage(PageRequest pageRequest) {
        return userGroupDao.findAll(pageRequest);
    }

    /**
     * 查询用户所有组
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public Collection<String> queryByUserId(Long uid) {
        return userGroupDao.findAllByUserId(uid).stream().map(UserGroupEntity::getTag).collect(Collectors.toList());
    }

    /**
     * 新增数据
     *
     * @param userGroup 实例对象
     * @return 实例对象
     */
    @Override
    public UserGroupEntity insert(UserGroupEntity userGroup) {
        UserGroupEntity userGroupEntity = userGroupDao.findByTag(userGroup.getTag());
        if (userGroupEntity != null){
            throw new RuntimeException("组标识不呢重复！");
        }

        userGroup.setCreateTime(System.currentTimeMillis());
        userGroup.setRankNum(0);
        userGroup.setFixed(false);
        userGroup.setDaCode(IdUtil.fastSimpleUUID());
        return userGroupDao.save(userGroup);
    }

    /**
     * 修改数据
     *
     * @param userGroup 实例对象
     * @return 实例对象
     */
    @Override
    public UserGroupEntity update(UserGroupEntity userGroup) {
        if (userGroupDao.findById(userGroup.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        userGroup.setUpdateTime(System.currentTimeMillis());
        userGroup.setFixed(false);
        return userGroupDao.save(userGroup);
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
        Integer count = userGroupDao.findBySuperIds(groupIds);
        if (count > 0){
            throw new RuntimeException("包含下级组！");
        }

        groupIds.forEach(id -> {
            if (userService.queryByPage(PageRequest.of(0, 1), "", id).getTotalElements() > 0) {
                throw new RuntimeException("组下包含数据！");
            }
        });

        Collection<Long> byGroupIds = userGroupMappingDAO.findByGroupIds(groupIds);
        Long[] userIds = new Long[byGroupIds.size()];
        byGroupIds.toArray(userIds);
        if (userIds.length > 0){
            userService.deleteById(userIds);
        }
        userGroupDao.deleteAllById(groupIds);
        return true;
    }

    /**
     * 获取用户组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    @Override
    public List<TreeDTO> getTree(String tag) {
        List<UserGroupEntity> userGroups = userGroupDao.findAllByTagRecursive(tag);
        List<TreeDTO> treeDTOS = new ArrayList<>();
        userGroups.stream().filter(userGroupEntity -> userGroupEntity.getTag().equals(tag))
                .peek(userGroupEntity -> treeDTOS.add(new TreeDTO(userGroupEntity.getLabel(), String.valueOf(userGroupEntity.getValue()), userGroupEntity.isDisable(), TreeDTO.treeRecursive(userGroupEntity.getValue(), userGroups))))
                .collect(Collectors.toList());
        return treeDTOS;
    }

}
