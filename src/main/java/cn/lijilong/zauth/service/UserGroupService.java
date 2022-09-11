package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.UserGroupEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;


/**
 * 用户组(UserGroup)表服务接口
 *
 * @author lijilong
 * @since 2022-05-26 10:21:06
 */
public interface UserGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserGroupEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<UserGroupEntity> queryByPage(PageRequest pageRequest);

    /**
     * 查询用户所有组
     *
     * @param uid 用户id
     * @return 查询结果
     */
    Collection<String> queryByUserId(Long uid);


    /**
     * 新增数据
     *
     * @param userGroup 实例对象
     * @return 实例对象
     */
    UserGroupEntity insert(UserGroupEntity userGroup);

    /**
     * 修改数据
     *
     * @param userGroup 实例对象
     * @return 实例对象
     */
    UserGroupEntity update(UserGroupEntity userGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 获取用户组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    List<TreeDTO> getTree(String tag);

}
