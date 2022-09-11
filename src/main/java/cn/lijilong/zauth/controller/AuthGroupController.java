package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.AuthGroupEntity;
import cn.lijilong.zauth.service.AuthGroupService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import cn.lijilong.zauth.config.PageSimple;
import io.swagger.annotations.ApiOperation;
import cn.lijilong.zauth.config.RequestDataDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限组(AuthGroup)表控制层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:02
 */
@Api(tags = "权限组")
@RestController
@CrossOrigin
@RequestMapping("/authGroup")
public class AuthGroupController {
    /**
     * 服务对象
     */
    @Resource
    private AuthGroupService authGroupService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_get")
    public RequestDataDTO<PageSimple<AuthGroupEntity>> queryByPage(@RequestParam Integer currentPage,
                                                                   @RequestParam Integer pageSize) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.authGroupService.queryByPage(PageRequest.of(currentPage, pageSize))));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_get")
    public RequestDataDTO<AuthGroupEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.authGroupService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_add")
    public RequestDataDTO<AuthGroupEntity> add(@RequestBody AuthGroupEntity authGroup) {
        return RequestDataDTO.buildSuccess(this.authGroupService.insert(authGroup));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_update")
    public RequestDataDTO<AuthGroupEntity> edit(@RequestBody AuthGroupEntity authGroup) {
        return RequestDataDTO.buildSuccess(this.authGroupService.update(authGroup));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.authGroupService.deleteById(ids));
    }

    @GetMapping("/tree/")
    @ApiOperation("获取权限组树")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_group_get")
    public RequestDataDTO<List<TreeDTO>> getTree(@RequestParam(required = false) String tag) {
        return RequestDataDTO.buildSuccess(authGroupService.getTree(tag == null ? "DEFAULT_GROUP" : tag));
    }
}

