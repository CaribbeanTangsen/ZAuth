package cn.lijilong.zauth.service;

import cn.lijilong.zauth.dto.ClientInfoDTO;
import cn.lijilong.zauth.entity.ClientEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

/**
 * 客户端(Client)表服务接口
 *
 * @author lijilong
 * @since 2022-05-23 13:57:20
 */
public interface ClientService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ClientEntity queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<ClientInfoDTO> queryByPage(PageRequest pageRequest, String value, Long group);

    /**
     * 新增数据
     *
     * @param client 实例对象
     * @return 实例对象
     */
    ClientEntity insert(ClientInfoDTO client);

    /**
     * 修改数据
     *
     * @param client 实例对象
     * @return 实例对象
     */
    ClientEntity update(ClientInfoDTO client);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long[] id);

    /**
     * 通过appid获取数据
     *
     * @param appid appid
     * @return 客户端实体
     */
    ClientEntity queryByAppid(String appid);

}
