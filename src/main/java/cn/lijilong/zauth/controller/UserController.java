package cn.lijilong.zauth.controller;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.dto.UserInfoDTO;
import cn.lijilong.zauth.dto.UserRoleDTO;
import cn.lijilong.zauth.entity.UserEntity;
import cn.lijilong.zauth.service.UserService;
import cn.lijilong.zauth.config.PageSimple;
import cn.lijilong.zauth.config.RequestDataDTO;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户(User)表控制层
 *
 * @author lijilong
 * @since 2022-05-23 13:59:59
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    @ApiOperation("分页查询")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_get")
    public RequestDataDTO<PageSimple<UserInfoDTO>> queryByPage(@RequestParam Integer currentPage,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam(required = false) String value,
                                                               @RequestParam(required = false) Long group) {

        return RequestDataDTO.buildSuccess(PageSimple.build(this.userService.queryByPage(PageRequest.of(currentPage, pageSize), value == null ? "" : value, group == null ? 1 : group)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("查询单条数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_get")
    public RequestDataDTO<UserEntity> queryById(@PathVariable("id") Long id) {
        return RequestDataDTO.buildSuccess(this.userService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_add")
    public RequestDataDTO<UserEntity> add(@RequestBody UserInfoDTO user) {
        return RequestDataDTO.buildSuccess(this.userService.insert(user));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation("编辑数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_update")
    public RequestDataDTO<UserEntity> edit(@RequestBody UserInfoDTO user) {
        return RequestDataDTO.buildSuccess(this.userService.update(user));
    }

    /**
     * 删除数据
     *
     * @param ids 主键
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_delete")
    public RequestDataDTO<Boolean> deleteById(@RequestBody Long[] ids) {
        return RequestDataDTO.buildSuccess(this.userService.deleteById(ids));
    }

    /**
     * 关联角色
     *
     * @param userRoleDTO 角色关联关系
     * */
    @PutMapping("/userRole")
    @ApiOperation("关联角色")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_link")
    public RequestDataDTO<Boolean> relateRole(@RequestBody UserRoleDTO userRoleDTO){

        return RequestDataDTO.buildSuccess(this.userService.relateRole(userRoleDTO));
    }

    /**
     * 修改密码
     *
     */
    @PutMapping("/password/{uid}")
    @ApiOperation("修改密码")
    @ZAuthFilter(sign = true, haveAuths = "zauth_user_update")
    public RequestDataDTO<Boolean> updatePassword(@PathVariable String uid, @RequestBody Map<String,String> info) {
        String oPass = info.get("oPass");
        String nPass = info.get("nPass");
        if (oPass == null || nPass == null)
            throw new RuntimeException("参数错误");

        return RequestDataDTO.buildSuccess(this.userService.updatePassword(uid, oPass, nPass));
    }

}

