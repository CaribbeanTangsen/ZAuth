package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.ClientDao;
import cn.lijilong.zauth.dao.ClientGroupDao;
import cn.lijilong.zauth.dao.ClientGroupMappingDao;
import cn.lijilong.zauth.dto.ClientInfoDTO;
import cn.lijilong.zauth.entity.ClientGroupMappingEntity;
import cn.lijilong.zauth.service.ClientService;
import cn.lijilong.zauth.entity.ClientEntity;
import cn.lijilong.zauth.entity.ClientGroupEntity;

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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端(Client)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Resource
    private ClientDao clientDao;
    @Resource
    private ClientGroupDao clientGroupDao;
    @Resource
    private ClientGroupMappingDao clientGroupMappingDao;

    @Resource
    ZCache zCache;
    @Resource
    ZAuthContextUtil zAuthContextUtil;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ClientEntity queryById(Long id) {
        return clientDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<ClientInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group) {

        List<Long> groupIds = clientGroupDao.findAllByIdRecursive(group).stream().map(ClientGroupEntity::getId).collect(Collectors.toList());

        return clientDao.findAllByClientNameAndGroup(value, groupIds, pageRequest);
    }

    /**
     * 新增数据
     *
     * @param client 实例对象
     * @return 实例对象
     */
    @Override
    public ClientEntity insert(ClientInfoDTO client) {
        if (StringUtils.isEmpty(client.getGroupId())){
            throw new RuntimeException("客户端组Id不能为空！");
        }

        ClientEntity clientEntity = new ClientEntity();
        BeanUtils.copyProperties(client, clientEntity);
        long ts = System.currentTimeMillis();
        clientEntity.setCreateTime(ts);
        clientEntity.setUpdateTime(ts);
        clientEntity.setCreateUser(zAuthContextUtil.getUid());
        clientEntity.setDeleted(false);
        clientEntity.setFixed(false);
        clientEntity.setDaCode(IdUtil.fastSimpleUUID());
        clientEntity.setAppId(IdUtil.fastSimpleUUID());
        clientEntity.setAppSecret(IdUtil.fastSimpleUUID());
        ClientEntity entity = clientDao.save(clientEntity);

        ClientGroupMappingEntity clientGroupMapping = new ClientGroupMappingEntity();
        clientGroupMapping.setClientId(entity.getId());
        clientGroupMapping.setGroupId(Long.parseLong(client.getGroupId()));
        clientGroupMappingDao.save(clientGroupMapping);

        return entity;
    }

    /**
     * 修改数据
     *
     * @param client 实例对象
     * @return 实例对象
     */
    @Override
    public ClientEntity update(ClientInfoDTO client) {
        if (StringUtils.isEmpty(client.getGroupId())){
            throw new RuntimeException("客户端组Id不能为空！");
        }
        if (clientDao.findById(client.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        zCache.delete("c-" + client.getId());
        ClientEntity clientEntity = new ClientEntity();
        BeanUtils.copyProperties(client, clientEntity);
        clientEntity.setUpdateTime(System.currentTimeMillis());
        clientEntity.setFixed(false);
        clientEntity.setUpdateUser(zAuthContextUtil.getUid());
        ClientEntity entity = clientDao.saveAndFlush(clientEntity);

        ClientGroupMappingEntity clientGroupMappingEntity = clientGroupMappingDao.findByClientId(client.getId());
        if (clientGroupMappingEntity != null){
            clientGroupMappingEntity.setGroupId(Long.parseLong(client.getGroupId()));
            clientGroupMappingDao.saveAndFlush(clientGroupMappingEntity);
        } else {
            ClientGroupMappingEntity clientGroupMapping = new ClientGroupMappingEntity();
            clientGroupMapping.setClientId(entity.getId());
            clientGroupMapping.setGroupId(Long.parseLong(client.getGroupId()));
            clientGroupMappingDao.save(clientGroupMapping);
        }

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
        for (Long id : ids) {
            zCache.delete("c-" + id);
        }
        clientDao.deleteByIds(authIds);
        clientGroupMappingDao.deleteByClientId(authIds);
        return true;
    }

    @Override
//    @CacheToRedis(keyParamName = "appId", preKey = "c-", clazz = ClientEntity.class)
    public ClientEntity queryByAppid(String appid) {
        return clientDao.findByAppId(appid).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

}
