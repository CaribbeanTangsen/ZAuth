package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.dto.RoleAuthDTO;
import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.config.PageSimple;
import cn.lijilong.zauth.config.RequestDataDTO;
import cn.lijilong.zauth.dto.RoleInfoDTO;
import cn.lijilong.zauth.entity.RoleEntity;
import cn.lijilong.zauth.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 角色(Role)表控制层
 *
 * @author lijilong
 * @since 2022-05-23 13:59:59
 */
@Api(tags = "角色")
@RestController
@RequestMapping("/role")
@CrossOrigin
public class RoleController {
    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_get")
    public RequestDataDTO<PageSimple<RoleInfoDTO>> queryByPage(@RequestParam Integer currentPage,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam(required = false) String value,
                                                               @RequestParam(required = false) Long group) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.roleService.queryByPage(PageRequest.of(currentPage, pageSize), value == null ? "" : value, group == null ? 1 : group)));
    }

    /**
     * 查询用户所有角色
     *
     * @return 查询结果
     */
    @GetMapping("/tag")
    @ApiOperation("查询用户所有角色标识")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_get")
    public RequestDataDTO<Collection<String>> queryByUserId(@RequestParam Long uid) {
        return RequestDataDTO.buildSuccess(roleService.queryByUserId(uid));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_get")
    public RequestDataDTO<RoleEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.roleService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_add")
    public RequestDataDTO<RoleEntity> add(@RequestBody RoleInfoDTO role) {
        return RequestDataDTO.buildSuccess(this.roleService.insert(role));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_update")
    public RequestDataDTO<RoleEntity> edit(@RequestBody RoleInfoDTO role) {
        return RequestDataDTO.buildSuccess(this.roleService.update(role));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.roleService.deleteById(ids));
    }

    /**
     * 关联权限
     *
     * @param roleAuthDTO
     */
    @PutMapping("/roleAuth/")
    @ApiOperation("关联权限")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_link")
    public RequestDataDTO<Boolean> authRelate(@RequestBody RoleAuthDTO roleAuthDTO){
        return RequestDataDTO.buildSuccess(this.roleService.authRelate(roleAuthDTO));
    }

    /**
     * 根据userId获取关联角色信息
     *
     * @param userIds
     * @return
     * */
    @GetMapping("/roleInfo")
    @ApiOperation("根据userId获取角色信息")
    @ZAuthFilter(sign = true, haveAuths = "zauth_role_get")
    public RequestDataDTO<Collection<RoleEntity>> queryReRoleInfo(@RequestParam List<Long> userIds){
        return RequestDataDTO.buildSuccess(this.roleService.queryReRoleInfo(userIds));
    }

}

