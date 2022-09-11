package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.dto.ClientInfoDTO;
import cn.lijilong.zauth.service.ClientService;
import cn.lijilong.zauth.config.PageSimple;
import cn.lijilong.zauth.config.RequestDataDTO;
import cn.lijilong.zauth.entity.ClientEntity;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 客户端(Client)表控制层
 *
 * @author lijilong
 * @since 2022-05-23 13:59:58
 */
@Api(tags = "客户端")
@RestController
@RequestMapping("/client")
@CrossOrigin
public class ClientController {
    /**
     * 服务对象
     */
    @Resource
    private ClientService clientService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_get")
    public RequestDataDTO<PageSimple<ClientInfoDTO>> queryByPage(@RequestParam Integer currentPage,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam(required = false) String value,
                                                                 @RequestParam(required = false) Long group) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.clientService.queryByPage(PageRequest.of(currentPage, pageSize), value == null ? "" : value, group == null ? 1 : group)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_get")
    public RequestDataDTO<ClientEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.clientService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_add")
    public RequestDataDTO<ClientEntity> add(@RequestBody ClientInfoDTO client) {
        return RequestDataDTO.buildSuccess(this.clientService.insert(client));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_update")
    public RequestDataDTO<ClientEntity> edit(@RequestBody ClientInfoDTO client) {
        return RequestDataDTO.buildSuccess(this.clientService.update(client));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.clientService.deleteById(ids));
    }

}

