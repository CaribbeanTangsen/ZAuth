package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.AuthInfoDTO;
import cn.lijilong.zauth.entity.AuthorityEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Collection;

import java.util.List;


/**
 * 权限(Authority)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:19
 */
public interface AuthorityService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AuthorityEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<AuthInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group);

    /**
     * 查询用户所有权限
     *
     * @param uid 用户id
     * @return 查询结果
     */
    Collection<String> queryByUserId(Long uid);

    /**
     * 新增数据
     *
     * @param authority 实例对象
     * @return 实例对象
     */
    AuthorityEntity insert(AuthInfoDTO authority);

    /**
     * 修改数据
     *
     * @param authority 实例对象
     * @return 实例对象
     */
    AuthorityEntity update(AuthInfoDTO authority);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    List<AuthorityEntity> queryAuthInfoByRoleId(List<Long> roleIds);

}
