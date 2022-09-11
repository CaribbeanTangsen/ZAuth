package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lijilong.zauth.dao.ClientGroupDao;
import cn.lijilong.zauth.dao.ClientGroupMappingDao;
import cn.lijilong.zauth.service.ClientGroupService;
import cn.lijilong.zauth.service.ClientService;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.ClientGroupEntity;

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
 * 客户端组(ClientGroup)表服务实现类
 *
 * @author lijilong
 * @since 2022-05-26 10:21:04
 */
@Service
public class ClientGroupServiceImpl implements ClientGroupService {
    @Resource
    private ClientGroupDao clientGroupDao;
    @Resource
    private ClientGroupMappingDao clientGroupMappingDao;
    @Resource
    private ClientService clientService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ClientGroupEntity queryById(Long id) {
        return clientGroupDao.findById(id).orElseThrow(() -> new RuntimeException("未找到数据！"));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<ClientGroupEntity> queryByPage(PageRequest pageRequest) {
        return clientGroupDao.findAll(pageRequest);
    }

    /**
     * 新增数据
     *
     * @param clientGroup 实例对象
     * @return 实例对象
     */
    @Override
    public ClientGroupEntity insert(ClientGroupEntity clientGroup) {
        ClientGroupEntity clientGroupEntity = clientGroupDao.findByTag(clientGroup.getTag());
        if (clientGroupEntity != null){
            throw new RuntimeException("组标识不能重复！");
        }

        clientGroup.setCreateTime(System.currentTimeMillis());
        clientGroup.setDaCode(IdUtil.fastSimpleUUID());
        clientGroup.setFixed(false);
        return clientGroupDao.save(clientGroup);
    }

    /**
     * 修改数据
     *
     * @param clientGroup 实例对象
     * @return 实例对象
     */
    @Override
    public ClientGroupEntity update(ClientGroupEntity clientGroup) {
        if (clientGroupDao.findById(clientGroup.getId()).isEmpty()) {
            throw new RuntimeException("未找到数据！");
        }
        clientGroup.setUpdateTime(System.currentTimeMillis());
        clientGroup.setFixed(false);
        return clientGroupDao.save(clientGroup);
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
        Integer count = clientGroupDao.countBySuperIdIn(groupIds);
        if (count > 0){
            throw new RuntimeException("包含下级组！");
        }

        groupIds.forEach(id -> {
            if (clientService.queryByPage(PageRequest.of(0, 1), "", id).getTotalElements() > 0) {
                throw new RuntimeException("组下包含数据！");
            }
        });

        Collection<Long> byGroupIds = clientGroupMappingDao.findByGroupIds(groupIds);
        Long[] clientIds = new Long[byGroupIds.size()];
        byGroupIds.toArray(clientIds);
        if (clientIds.length > 0){
            clientService.deleteById(clientIds);
        }
        clientGroupDao.deleteAllById(groupIds);
        return true;
    }

    /**
     * 获取客户端组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    @Override
    public List<TreeDTO> getTree(String tag) {
        List<ClientGroupEntity> clientGroups = clientGroupDao.findAllByTagRecursive(tag);
        List<TreeDTO> treeDTOS = new ArrayList<>();
        clientGroups.stream().filter(clientGroupEntity -> clientGroupEntity.getTag().equals(tag))
                .peek(clientGroupEntity -> treeDTOS.add(new TreeDTO(clientGroupEntity.getLabel(), String.valueOf(clientGroupEntity.getValue()), clientGroupEntity.isDisable(), TreeDTO.treeRecursive(clientGroupEntity.getValue(), clientGroups))))
                .collect(Collectors.toList());
        return treeDTOS;
    }

}
