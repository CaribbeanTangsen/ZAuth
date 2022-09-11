package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.dto.TreeDTO;
import cn.lijilong.zauth.entity.UserGroupEntity;
import cn.lijilong.zauth.service.UserGroupService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import cn.lijilong.zauth.config.PageSimple;
import io.swagger.annotations.ApiOperation;
import cn.lijilong.zauth.config.RequestDataDTO;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户组(UserGroup)表控制层
 *
 * @author lijilong
 * @since 2022-05-26 10:21:06
 */
@Api(tags = "用户组")
@RestController
@CrossOrigin
@RequestMapping("/userGroup")
public class UserGroupController {
    /**
     * 服务对象
     */
    @Resource
    private UserGroupService userGroupService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_get")
    public RequestDataDTO<PageSimple<UserGroupEntity>> queryByPage(@RequestParam Integer currentPage,
                                                                   @RequestParam Integer pageSize) {

        return RequestDataDTO.buildSuccess(PageSimple.build(this.userGroupService.queryByPage(PageRequest.of(currentPage, pageSize))));
    }

    /**
     * 查询用户所有组
     *
     * @return 查询结果
     */
    @GetMapping("/tag")
    @ApiOperation("查询用户所有组标识")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_get")
    public RequestDataDTO<Collection<String>> queryByUserId(@RequestParam Long uid) {
        return RequestDataDTO.buildSuccess(userGroupService.queryByUserId(uid));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_get")
    public RequestDataDTO<UserGroupEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.userGroupService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_add")
    public RequestDataDTO<UserGroupEntity> add(@RequestBody UserGroupEntity userGroup) {
        return RequestDataDTO.buildSuccess(this.userGroupService.insert(userGroup));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_update")
    public RequestDataDTO<UserGroupEntity> edit(@RequestBody UserGroupEntity userGroup) {
        return RequestDataDTO.buildSuccess(this.userGroupService.update(userGroup));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.userGroupService.deleteById(ids));
    }

    @GetMapping("/tree/")
    @ApiOperation("获取用户组树")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_group_get")
    public RequestDataDTO<List<TreeDTO>> getTree(@RequestParam(required = false) String tag) {
        return RequestDataDTO.buildSuccess(userGroupService.getTree(tag == null ? "DEFAULT_GROUP" : tag));
    }

}

