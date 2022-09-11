package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.service.ClientGroupService;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.ClientGroupEntity;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import cn.lijilong.zauth.config.PageSimple;
import io.swagger.annotations.ApiOperation;
import cn.lijilong.zauth.config.RequestDataDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户端组(ClientGroup)表控制层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:03
 */
@Api(tags = "客户端组")
@RestController
@CrossOrigin
@RequestMapping("/clientGroup")
public class ClientGroupController {
    /**
     * 服务对象
     */
    @Resource
    private ClientGroupService clientGroupService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_get")
    public RequestDataDTO<PageSimple<ClientGroupEntity>> queryByPage(@RequestParam Integer currentPage,
                                                                     @RequestParam Integer pageSize) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.clientGroupService.queryByPage(PageRequest.of(currentPage, pageSize))));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_get")
    public RequestDataDTO<ClientGroupEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.clientGroupService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_add")
    public RequestDataDTO<ClientGroupEntity> add(@RequestBody ClientGroupEntity clientGroup) {
        return RequestDataDTO.buildSuccess(this.clientGroupService.insert(clientGroup));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_update")
    public RequestDataDTO<ClientGroupEntity> edit(@RequestBody ClientGroupEntity clientGroup) {
        return RequestDataDTO.buildSuccess(this.clientGroupService.update(clientGroup));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.clientGroupService.deleteById(ids));
    }

    @GetMapping("/tree/")
    @ApiOperation("获取客户端组树")
    @ZAuthFilter(sign = true, haveAuths = "zauth_client_group_get")
    public RequestDataDTO<List<TreeDTO>> getTree(@RequestParam(required = false) String tag) {
        return RequestDataDTO.buildSuccess(clientGroupService.getTree(tag == null ? "DEFAULT_GROUP" : tag));
    }
}

