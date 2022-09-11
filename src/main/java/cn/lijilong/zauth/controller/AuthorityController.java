package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.service.AuthorityService;
import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.config.PageSimple;
import cn.lijilong.zauth.config.RequestDataDTO;
import cn.lijilong.zauth.dto.AuthInfoDTO;
import cn.lijilong.zauth.entity.AuthorityEntity;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 权限(Authority)表控制层
 *
 * @author lijilong
 * @since 2022-05-23 13:59:58
 */
@Api(tags = "权限")
@RestController
@RequestMapping("/authority")
@CrossOrigin
public class AuthorityController {
    /**
     * 服务对象
     */
    @Resource
    private AuthorityService authorityService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_get")
    public RequestDataDTO<PageSimple<AuthInfoDTO>> queryByPage(@RequestParam Integer currentPage,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam(required = false) String value,
                                                               @RequestParam(required = false) Long group) {
        return RequestDataDTO.buildSuccess(PageSimple.build(this.authorityService.queryByPage(PageRequest.of(currentPage, pageSize), value == null ? "" : value, group == null ? 1 : group)));
    }

    /**
     * 查询用户所有权限
     *
     * @return 查询结果
     */
    @GetMapping("/tag")
    @ApiOperation("查询用户所有权限标识")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_get")
    public RequestDataDTO<Collection<String>> queryByUserId(@RequestParam Long uid) {
        return RequestDataDTO.buildSuccess(authorityService.queryByUserId(uid));
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_get")
    public RequestDataDTO<AuthorityEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.authorityService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_add")
    public RequestDataDTO<AuthorityEntity> add(@RequestBody AuthInfoDTO authority) {
        return RequestDataDTO.buildSuccess(this.authorityService.insert(authority));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_update")
    public RequestDataDTO<AuthorityEntity> edit(@RequestBody AuthInfoDTO authority) {
        return RequestDataDTO.buildSuccess(this.authorityService.update(authority));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.authorityService.deleteById(ids));
    }


    /**
     * 根据角色id获取权限信息
     *
     * @param roleIds
     * @return
     * */
    @GetMapping("/authInfo")
    @ApiOperation("根据角色id获取权限信息")
    @ZAuthFilter(sign = true, haveAuths = "zauth_auth_get")
    public RequestDataDTO<List<AuthorityEntity>> queryAuthInfoByRoleId(@RequestParam List<Long> roleIds){
        return RequestDataDTO.buildSuccess(this.authorityService.queryAuthInfoByRoleId(roleIds));
    }

}

