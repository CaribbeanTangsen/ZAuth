package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.ClientGroupEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * 客户端组(ClientGroup)表服务接口
 *
 * @author lijilong
 * @since 2022-05-26 10:21:04
 */
public interface ClientGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ClientGroupEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<ClientGroupEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param clientGroup 实例对象
     * @return 实例对象
     */
    ClientGroupEntity insert(ClientGroupEntity clientGroup);

    /**
     * 修改数据
     *
     * @param clientGroup 实例对象
     * @return 实例对象
     */
    ClientGroupEntity update(ClientGroupEntity clientGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 获取客户端组树
     * @param tag 从当前标签开始获取，如果null则获取全部
     * @return 树
     */
    List<TreeDTO> getTree(String tag);
}
