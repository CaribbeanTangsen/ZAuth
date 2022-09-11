package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.AuthGroupEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * 权限组(AuthGroup)表服务接口
 *
 * @author lijilong
 * @since 2022-05-26 10:21:03
 */
public interface AuthGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AuthGroupEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<AuthGroupEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param authGroup 实例对象
     * @return 实例对象
     */
    AuthGroupEntity insert(AuthGroupEntity authGroup);

    /**
     * 修改数据
     *
     * @param authGroup 实例对象
     * @return 实例对象
     */
    AuthGroupEntity update(AuthGroupEntity authGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 获取权限组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    List<TreeDTO> getTree(String tag);
}
