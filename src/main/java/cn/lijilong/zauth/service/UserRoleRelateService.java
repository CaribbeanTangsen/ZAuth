package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.UserRoleDTO;
import cn.lijilong.zauth.entity.UserRoleRelateEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;


/**
 * 用户角色映射(UserRoleRelate)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface UserRoleRelateService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserRoleRelateEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<UserRoleRelateEntity> queryByPage(PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param userRoleRelate 实例对象
     * @return 实例对象
     */
    UserRoleRelateEntity insert(UserRoleRelateEntity userRoleRelate);

    /**
     * 修改数据
     *
     * @param userRoleRelate 实例对象
     * @return 实例对象
     */
    UserRoleRelateEntity update(UserRoleRelateEntity userRoleRelate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     *批量插入
     *
     * @param userRoleDTO
     * @return 是否成功
     * */
    boolean saveAll(UserRoleDTO userRoleDTO);



}
