package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.RoleAuthDTO;
import cn.lijilong.zauth.entity.RoleAuthorityRelateEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;


/**
 * 角色权限映射(RoleAuthorityRelate)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface RoleAuthorityRelateService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoleAuthorityRelateEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<RoleAuthorityRelateEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param roleAuthorityRelate 实例对象
     * @return 实例对象
     */
    RoleAuthorityRelateEntity insert(RoleAuthorityRelateEntity roleAuthorityRelate);

    /**
     * 修改数据
     *
     * @param roleAuthorityRelate 实例对象
     * @return 实例对象
     */
    RoleAuthorityRelateEntity update(RoleAuthorityRelateEntity roleAuthorityRelate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 批量插入
     *
     * @param roleAuthDTO
     * @return 是否成功
     * */
    boolean saveAll(RoleAuthDTO roleAuthDTO);

}
