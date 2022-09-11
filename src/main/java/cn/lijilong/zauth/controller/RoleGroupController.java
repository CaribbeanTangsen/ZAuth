package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.RoleGroupEntity;
import cn.lijilong.zauth.service.RoleGroupService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import cn.lijilong.zauth.config.PageSimple;
import io.swagger.annotations.ApiOperation;
import cn.lijilong.zauth.config.RequestDataDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色组(RoleGroup)表控制层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:04
 */
@Api(tags = "角色组")
@RestController
@CrossOrigin
@RequestMapping("/roleGroup")
public class RoleGroupController {
    /**
     * 服务对象
     */
    @Resource
    private RoleGroupService roleGroupService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_get")
    public RequestDataDTO<PageSimple<RoleGroupEntity>> queryByPage(@RequestParam Integer currentPage,
                                                                   @RequestParam Integer pageSize) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.roleGroupService.queryByPage(PageRequest.of(currentPage, pageSize))));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_get")
    public RequestDataDTO<RoleGroupEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.roleGroupService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_add")
    public RequestDataDTO<RoleGroupEntity> add(@RequestBody RoleGroupEntity roleGroup) {
        return RequestDataDTO.buildSuccess(this.roleGroupService.insert(roleGroup));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_update")
    public RequestDataDTO<RoleGroupEntity> edit(@RequestBody RoleGroupEntity roleGroup) {
        return RequestDataDTO.buildSuccess(this.roleGroupService.update(roleGroup));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.roleGroupService.deleteById(ids));
    }

    @GetMapping("/tree/")
    @ApiOperation("获取角色组树")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_group_get")
    public RequestDataDTO<List<TreeDTO>> getTree(@RequestParam(required = false) String tag) {
        return RequestDataDTO.buildSuccess(roleGroupService.getTree(tag == null ? "DEFAULT_GROUP" : tag));
    }
}

