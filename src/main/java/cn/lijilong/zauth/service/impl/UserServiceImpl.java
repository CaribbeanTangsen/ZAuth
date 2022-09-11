package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.lijilong.zauth.dao.UserDao;
import cn.lijilong.zauth.dao.UserGroupDao;
import cn.lijilong.zauth.dao.UserGroupMappingDAO;
import cn.lijilong.zauth.dto.UserInfoDTO;
import cn.lijilong.zauth.dto.UserRoleDTO;
import cn.lijilong.zauth.entity.UserEntity;
import cn.lijilong.zauth.entity.UserGroupMappingEntity;
import cn.lijilong.zauth.entity.UserGroupEntity;
import cn.lijilong.zauth.exception.UserException;
import cn.lijilong.zauth.service.UserRoleRelateService;
import cn.lijilong.zauth.service.UserService;

import javax.annotation.Resource;

import cn.lijilong.zcache.ZCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.lijilong.zauth.util.MD5Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户(User)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserGroupDao userGroupDao;
    @Resource
    private UserGroupMappingDAO userGroupMappingDAO;
    @Resource
    private UserRoleRelateService roleRelateService;
    @Resource
    ZCache zCache;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserEntity queryById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<UserInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group) {

        List<Long> groupIds = userGroupDao.findAllByIdRecursive(group).stream().map(UserGroupEntity::getId).collect(Collectors.toList());

        return userDao.findAllByNameOrEmailOrPhoneOrUserNameAndGroup(value, groupIds, pageRequest);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public UserEntity insert(UserInfoDTO user) {
        if (StringUtils.isEmpty(user.getGroupId())){
            throw new RuntimeException("用户组Id不能为空！");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        long ts = System.currentTimeMillis();
        userEntity.setRegDate(ts);
        userEntity.setUpdateDate(ts);
        userEntity.setDaCode(IdUtil.fastSimpleUUID());
        userEntity.setDeleted(false);
        userEntity.setFixed(false);
        userEntity.setSalt(IdUtil.fastSimpleUUID());
        userEntity.setPassword(SecureUtil.md5(user.getPassword() + ":" + userEntity.getSalt()));
        UserEntity entity = userDao.save(userEntity);

        UserGroupMappingEntity userGroupMapping = new UserGroupMappingEntity();
        userGroupMapping.setUserId(entity.getId());
        userGroupMapping.setGroupId(Long.parseLong(user.getGroupId()));
        userGroupMappingDAO.save(userGroupMapping);

        return entity;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public UserEntity update(UserInfoDTO user) {
        if (StringUtils.isEmpty(user.getGroupId())){

            throw new RuntimeException("用户组Id不能为空！");
        }
        if (userDao.findById(user.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setUpdateDate(System.currentTimeMillis());
        userEntity.setFixed(false);
        UserEntity entity = userDao.saveAndFlush(userEntity);

        UserGroupMappingEntity userGroupMappingEntity = userGroupMappingDAO.findByUserId(user.getId());
        if (userGroupMappingEntity != null){
            userGroupMappingEntity.setGroupId(Long.parseLong(user.getGroupId()));
            userGroupMappingDAO.saveAndFlush(userGroupMappingEntity);
        } else {
            UserGroupMappingEntity userGroupMapping = new UserGroupMappingEntity();
            userGroupMapping.setUserId(entity.getId());
            userGroupMapping.setGroupId(Long.parseLong(user.getGroupId()));
            userGroupMappingDAO.save(userGroupMapping);
        }

        //清除缓存
//        zCache.delete("ugs-" + userEntity.getId());
        zCache.delete("ut-" + userEntity.getId());
        userEntity.setToken(null);
        userEntity.setRefreshToken(null);
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
        List<Long> userIds = Arrays.asList(ids);
        if (userIds.contains(1L)){
            throw new RuntimeException("超级管理员不允许删除！");
        }
        for (Long id : ids) {
            //清除缓存
//            zCache.delete("ugs-" + id);
            zCache.delete("ut-" + id);
        }
        userDao.deleteByIds(userIds);
        userGroupMappingDAO.deleteByUserId(userIds);
        return true;
    }

    /**
     * 关联角色
     *
     * @param userRoleDTO
     * */
    @Override
    public boolean relateRole(UserRoleDTO userRoleDTO) {

        if(userRoleDTO.getUserIds() == null || userRoleDTO.getUserIds().isEmpty()){
            return false;
        }
       /* if(userRoleDTO.getRoleIds() == null || userRoleDTO.getRoleIds().isEmpty()){
            return false;
        }*/
        userRoleDTO.getUserIds().forEach(uid->{
            zCache.delete("ut-" + uid);
        });
        return roleRelateService.saveAll(userRoleDTO);
    }

    /**
     * 更改用户密码
     * @param uid
     * @param oPass
     * @param nPass
     * @return
     */
    @Override
    public boolean updatePassword(String uid, String oPass, String nPass) {
        UserEntity userEntity = queryById(Long.valueOf(uid));
        String omd5 = MD5Utils.stringToMD5(oPass + ":" + userEntity.getSalt());
        if (!omd5.equals(userEntity.getPassword())) {
            throw new UserException("原密码错误！");
        }
        userEntity.setSalt(IdUtil.fastSimpleUUID());
        userEntity.setPassword(MD5Utils.stringToMD5(nPass + ":" + userEntity.getSalt()));
        zCache.delete("ut-" + userEntity.getId());
        zCache.delete("roles-" + userEntity.getId());
        zCache.delete("auths-" + userEntity.getId());
        userEntity.setToken(null);
        userEntity.setRefreshToken(null);
        userDao.flush();
        return true;
    }

}
