package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.UserInfoDTO;
import cn.lijilong.zauth.dto.UserRoleDTO;
import cn.lijilong.zauth.entity.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;


/**
 * 用户(User)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:21
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<UserInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    UserEntity insert(UserInfoDTO user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    UserEntity update(UserInfoDTO user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 关联角色
     *
     * @param userRoleDTO
     * */
    boolean relateRole(UserRoleDTO userRoleDTO);

    /**
     * 修改密码
     *
     */
    boolean updatePassword(String uid, String oPass, String nPass);


}
