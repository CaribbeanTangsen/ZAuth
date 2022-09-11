package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.AuthGroupDao;
import cn.lijilong.zauth.dao.AuthGroupMappingDao;
import cn.lijilong.zauth.dao.AuthorityDao;
import cn.lijilong.zauth.dto.AuthInfoDTO;
import cn.lijilong.zauth.entity.AuthGroupEntity;
import cn.lijilong.zauth.entity.AuthGroupMappingEntity;
import cn.lijilong.zauth.service.AuthorityService;
import cn.lijilong.zauth.entity.AuthorityEntity;

import javax.annotation.Resource;

import cn.lijilong.zcache.ZCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.lijilong.zauth.util.ZAuthContextUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限(Authority)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:19
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private AuthorityDao authorityDao;
    @Resource
    private AuthGroupDao authGroupDao;
    @Resource
    private AuthGroupMappingDao authGroupMappingDao;
    @Resource
    private ZCache zCache;
    @Resource
    ZAuthContextUtil zAuthContextUtil;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AuthorityEntity queryById(Long id) {
        return authorityDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<AuthInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group) {

        List<Long> groupIds = authGroupDao.findAllByIdRecursive(group).stream().map(AuthGroupEntity::getId).collect(Collectors.toList());

        return authorityDao.findAllByAuthNameAndGroup(value, groupIds, pageRequest);
    }

    /**
     * 查询用户所有权限
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public Collection<String> queryByUserId(Long uid) {
        return authorityDao.findAllByUserId(uid).stream().map(AuthorityEntity::getAuthMark).collect(Collectors.toSet());
    }

    /**
     * 新增数据
     *
     * @param authority 实例对象
     * @return 实例对象
     */
    @Override
    public AuthorityEntity insert(AuthInfoDTO authority) {
        AuthorityEntity auth = authorityDao.findByAuthMark(authority.getAuthMark());
        if (auth != null){
            throw new RuntimeException("权限标识不能重复！");
        }
        if (StringUtils.isEmpty(authority.getGroupId())){
            throw new RuntimeException("权限组Id不能为空！");
        }

        AuthorityEntity authorityEntity = new AuthorityEntity();
        BeanUtils.copyProperties(authority, authorityEntity);
        long ts = System.currentTimeMillis();
        authorityEntity.setCreateTime(ts);
        authorityEntity.setUpdateTime(ts);
        authorityEntity.setDeleted(false);
        authorityEntity.setFixed(false);
        authorityEntity.setCreateUser(zAuthContextUtil.getUid());
        authorityEntity.setDaCode(IdUtil.fastSimpleUUID());
        AuthorityEntity entity = authorityDao.save(authorityEntity);

        AuthGroupMappingEntity authGroupMapping = new AuthGroupMappingEntity();
        authGroupMapping.setAuthId(entity.getId());
        authGroupMapping.setGroupId(Long.parseLong(authority.getGroupId()));
        authGroupMappingDao.save(authGroupMapping);
        return entity;
    }

    /**
     * 修改数据
     *
     * @param authority 实例对象
     * @return 实例对象
     */
    @Override
    public AuthorityEntity update(AuthInfoDTO authority) {
        if (StringUtils.isEmpty(authority.getGroupId())){
            throw new RuntimeException("权限组Id不能为空！");
        }
        if (authorityDao.findById(authority.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }

        AuthorityEntity authorityEntity = new AuthorityEntity();
        BeanUtils.copyProperties(authority, authorityEntity);
        authorityEntity.setUpdateTime(System.currentTimeMillis());
        authorityEntity.setFixed(false);
        authorityEntity.setUpdateUser(zAuthContextUtil.getUid());
        AuthorityEntity entity = authorityDao.saveAndFlush(authorityEntity);

        AuthGroupMappingEntity authGroupMappingEntity = authGroupMappingDao.findByAuthId(authority.getId());
        if (authGroupMappingEntity != null){
            authGroupMappingEntity.setGroupId(Long.parseLong(authority.getGroupId()));
            authGroupMappingDao.saveAndFlush(authGroupMappingEntity);
        } else {
            AuthGroupMappingEntity authGroupMapping = new AuthGroupMappingEntity();
            authGroupMapping.setAuthId(entity.getId());
            authGroupMapping.setGroupId(Long.parseLong(authority.getGroupId()));
            authGroupMappingDao.save(authGroupMapping);
        }

        //清除所有权限缓存
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
        List<Long> authIds = Arrays.asList(ids);
        authorityDao.deleteByIds(authIds);
        authGroupMappingDao.deleteAllByAuthIdIn(authIds);
        //清除所有权限缓存
        zCache.deleteAll("auths-");
        return true;
    }

    @Override
    public List<AuthorityEntity> queryAuthInfoByRoleId(List<Long> roleIds) {

        return authorityDao.findAllByRoleId(roleIds);
    }

}
