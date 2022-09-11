package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.RoleDao;
import cn.lijilong.zauth.dao.RoleGroupDao;
import cn.lijilong.zauth.dao.RoleGroupMappingDao;
import cn.lijilong.zauth.dto.RoleAuthDTO;
import cn.lijilong.zauth.entity.RoleGroupMappingEntity;
import cn.lijilong.zauth.service.RoleAuthorityRelateService;
import cn.lijilong.zauth.service.RoleService;
import cn.lijilong.zauth.dto.RoleInfoDTO;
import cn.lijilong.zauth.entity.RoleEntity;
import cn.lijilong.zauth.entity.RoleGroupEntity;

import javax.annotation.Resource;

import cn.lijilong.zcache.ZCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.lijilong.zauth.service.UserService;
import cn.lijilong.zauth.util.ZAuthContextUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色(Role)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private RoleGroupDao roleGroupDao;
    @Resource
    private RoleGroupMappingDao roleGroupMappingDao;
    @Resource
    private ZCache zCache;
    @Resource
    private UserService userService;
    @Resource
    ZAuthContextUtil zAuthContextUtil;

    @Resource
    private RoleAuthorityRelateService roleAuthorityRelateService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RoleEntity queryById(Long id) {
        return roleDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<RoleInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group) {
        List<Long> groupIds = roleGroupDao.findAllByIdRecursive(group).stream().map(RoleGroupEntity::getId).collect(Collectors.toList());

        return roleDao.findAllByRoleNameAndGroup(value, groupIds, pageRequest);
    }

    /**
     * 查询用户所有角色
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public Collection<String> queryByUserId(Long uid) {
        return roleDao.findAllByUserId(uid).stream().map(RoleEntity::getTag).collect(Collectors.toSet());
    }

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Override
    public RoleEntity insert(RoleInfoDTO role) {
        RoleEntity re = roleDao.findByTag(role.getTag());
        if (re != null){
            throw new RuntimeException("角色标识不能重复！");
        }
        if (StringUtils.isEmpty(role.getGroupId())){
            throw new RuntimeException("角色组Id不能为空！");
        }

        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(role, roleEntity);
        long ts = System.currentTimeMillis();
        roleEntity.setCreateTime(ts);
        roleEntity.setUpdateTime(ts);
        roleEntity.setFixed(false);
        roleEntity.setCreateUser(zAuthContextUtil.getUid());
        roleEntity.setDeleted(false);
        roleEntity.setDaCode(IdUtil.fastSimpleUUID());
        RoleEntity entity = roleDao.save(roleEntity);

        RoleGroupMappingEntity roleGroupMapping = new RoleGroupMappingEntity();
        roleGroupMapping.setRoleId(entity.getId());
        roleGroupMapping.setGroupId(Long.parseLong(role.getGroupId()));
        roleGroupMappingDao.save(roleGroupMapping);

        return entity;
    }

    /**
     * 修改数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Override
    public RoleEntity update(RoleInfoDTO role) {
        if (StringUtils.isEmpty(role.getGroupId())){
            throw new RuntimeException("用户组Id不能为空！");
        }
        if (roleDao.findById(role.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(role, roleEntity);
        roleEntity.setUpdateTime(System.currentTimeMillis());
        roleEntity.setFixed(false);
        roleEntity.setUpdateUser(zAuthContextUtil.getUid());
        RoleEntity entity = roleDao.saveAndFlush(roleEntity);


        RoleGroupMappingEntity roleGroupMappingEntity = roleGroupMappingDao.findByRoleId(role.getId());
        if (roleGroupMappingEntity != null){
            roleGroupMappingEntity.setGroupId(Long.parseLong(role.getGroupId()));
            roleGroupMappingDao.saveAndFlush(roleGroupMappingEntity);
        } else {
            RoleGroupMappingEntity roleGroupMapping = new RoleGroupMappingEntity();
            roleGroupMapping.setRoleId(entity.getId());
            roleGroupMapping.setGroupId(Long.parseLong(role.getGroupId()));
            roleGroupMappingDao.save(roleGroupMapping);
        }




        zCache.deleteAll("roles-");
        zCache.deleteAll("auths-");
        return entity;
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
        List<Long> roleIds = Arrays.asList(ids);
        roleDao.deleteByIds(roleIds);
        roleGroupMappingDao.deleteByRoleId(roleIds);

        zCache.deleteAll("roles-");
        zCache.deleteAll("auths-");
        return true;
    }

    @Override
    public boolean authRelate(RoleAuthDTO roleAuthDTO) {
        if(roleAuthDTO.getRoleIds() == null || roleAuthDTO.getRoleIds().isEmpty()){
            return false;
        }
        /*if(roleAuthDTO.getAuthIds() == null || roleAuthDTO.getAuthIds().isEmpty()){
            return false;
        }*/
        zCache.deleteAll("roles-");
        return roleAuthorityRelateService.saveAll(roleAuthDTO);
    }

    @Override
    public List<RoleEntity> queryReRoleInfo(List<Long> userIds) {

        return roleDao.findAllByUserId(userIds);
    }

}
