package com.itheima.pinda.authority.controller.common;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.authority.entity.common.LoginLog;
import com.itheima.pinda.authority.biz.service.common.LoginLogService;
import com.itheima.pinda.base.BaseController;
import com.itheima.pinda.base.R;
import com.itheima.pinda.database.mybatis.conditions.Wraps;
import com.itheima.pinda.database.mybatis.conditions.query.LbqWrapper;
import com.itheima.pinda.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 前端控制器
 * 登录日志
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/loginLog")
@Api(value = "LoginLog", tags = "登录日志")
public class LoginLogController extends BaseController {
    @Autowired
    private LoginLogService loginLogService;
    /**
     * 分页查询登录日志
     */
    @ApiOperation(value = "分页查询登录日志", notes = "分页查询登录日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示几条", dataType = "long", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/page")
    public R<IPage<LoginLog>> page(LoginLog data) {
        IPage<LoginLog> page = this.getPage();
        // 构建值不为null的查询条件
        LbqWrapper<LoginLog> query = Wraps.<LoginLog>lbQ()
                .eq(LoginLog::getUserId, data.getUserId())
                .likeRight(LoginLog::getAccount, data.getAccount())
                .likeRight(LoginLog::getRequestIp, data.getRequestIp())
                .like(LoginLog::getLocation, data.getLocation())
                .leFooter(LoginLog::getCreateTime, this.getEndCreateTime())
                .geHeader(LoginLog::getCreateTime, this.getStartCreateTime())
                .orderByDesc(LoginLog::getId);
        this.loginLogService.page(page, query);
        return this.success(page);
    }

    /**
     * 查询登录日志
     */
    @ApiOperation(value = "查询登录日志", notes = "查询登录日志")
    @GetMapping("/{id}")
    public R<LoginLog> get(@PathVariable Long id) {
        return this.success(this.loginLogService.getById(id));
    }

    /**
     * 删除登录日志
     */
    @ApiOperation(value = "删除日志", notes = "根据id物理删除登录日志")
    @DeleteMapping
    @SysLog("删除登录日志")
    public R<Boolean> delete(@RequestParam("ids[]") List<Long> ids) {
        this.loginLogService.removeByIds(ids);
        return this.success(true);
    }
}