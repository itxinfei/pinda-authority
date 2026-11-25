package com.itheima.pinda.authority.controller.auth;

import com.itheima.pinda.authority.biz.service.auth.RoleService;
import com.itheima.pinda.authority.biz.service.auth.UserService;
import com.itheima.pinda.authority.biz.service.core.OrgService;
import com.itheima.pinda.authority.biz.service.core.StationService;
import com.itheima.pinda.authority.entity.auth.Role;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.entity.core.Station;
import com.itheima.pinda.base.BaseController;
import com.itheima.pinda.base.R;
import com.itheima.pinda.dozer.DozerUtils;
import com.itheima.pinda.user.feign.UserQuery;
import com.itheima.pinda.user.model.SysOrg;
import com.itheima.pinda.user.model.SysRole;
import com.itheima.pinda.user.model.SysStation;
import com.itheima.pinda.user.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 内部接口：仅限服务间 Feign 直连调用（不经过网关鉴权）。
 * 路径置于 /inner 前缀下，网关无任何与之匹配的资源，外部访问一律返回 401，
 * 从根本上杜绝原 /anno/id/{id} 匿名暴露用户详情的问题。
 */
@Slf4j
@RestController
@RequestMapping("/inner")
@Api(value = "UserInner", tags = "用户内部接口")
public class UserInnerController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StationService stationService;
    @Autowired
    private DozerUtils dozer;

    /**
     * 根据用户id查询用户详情（含组织/岗位/角色），仅供服务间调用
     *
     * @param id    用户id
     * @param query 控制是否返回组织、岗位、角色等关联信息
     */
    @ApiOperation(value = "查询用户详细(内部)", notes = "仅供服务间 Feign 调用，外部不可直接访问")
    @PostMapping(value = "/id/{id}")
    public R<SysUser> getById(@PathVariable Long id, @RequestBody UserQuery query) {
        User user = userService.getById(id);
        if (user == null) {
            return success(null);
        }
        SysUser sysUser = dozer.map(user, SysUser.class);

        if (query.getFull() || query.getOrg()) {
            Org org = orgService.getById(user.getOrgId());
            if (org != null) {
                sysUser.setOrg(dozer.map(org, SysOrg.class));
            }
        }
        if (query.getFull() || query.getStation()) {
            Station station = stationService.getById(user.getStationId());
            if (station != null) {
                sysUser.setStation(dozer.map(station, SysStation.class));
            }
        }
        if (query.getFull() || query.getRoles()) {
            List<Role> list = roleService.findRoleByUserId(id);
            sysUser.setRoles(dozer.mapList(list, SysRole.class));
        }

        return success(sysUser);
    }
}
