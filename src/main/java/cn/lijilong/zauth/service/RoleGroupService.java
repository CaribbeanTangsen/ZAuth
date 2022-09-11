package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.RoleGroupEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * 角色组(RoleGroup)表服务接口
 *
 * @author lijilong
 * @since 2022-05-26 10:21:05
 */
public interface RoleGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoleGroupEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<RoleGroupEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param roleGroup 实例对象
     * @return 实例对象
     */
    RoleGroupEntity insert(RoleGroupEntity roleGroup);

    /**
     * 修改数据
     *
     * @param roleGroup 实例对象
     * @return 实例对象
     */
    RoleGroupEntity update(RoleGroupEntity roleGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 获取角色组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    List<TreeDTO> getTree(String tag);
}
