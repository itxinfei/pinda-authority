package com.itheima.pinda.zuul.api;

import com.itheima.pinda.authority.dto.auth.ResourceQueryDTO;
import com.itheima.pinda.authority.entity.auth.Resource;
import com.itheima.pinda.base.R;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 资源API熔断
 * @author Administrator
 */
@Component
public class ResourceApiFallback implements ResourceApi {

    @Override
    public R<List> list() {
        return R.fail("权限服务不可用，无法获取资源列表");
    }

    @Override
    public R<List<Resource>> visible(ResourceQueryDTO resource) {
        return R.fail("权限服务不可用，无法获取用户权限");
    }
}
