package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.RoleAuthDTO;
import cn.lijilong.zauth.dto.RoleInfoDTO;
import cn.lijilong.zauth.entity.RoleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;


/**
 * 角色(Role)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface RoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoleEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<RoleInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group);


    /**
     * 查询用户所有角色
     *
     * @param uid 用户id
     * @return 查询结果
     */
    Collection<String> queryByUserId(Long uid);

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    RoleEntity insert(RoleInfoDTO role);

    /**
     * 修改数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    RoleEntity update(RoleInfoDTO role);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 通过主键删除数据
     *
     * @param roleAuthDTO
     * @return 是否成功
     */
    boolean authRelate(RoleAuthDTO roleAuthDTO);

    List<RoleEntity> queryReRoleInfo(List<Long> userIds);
}
