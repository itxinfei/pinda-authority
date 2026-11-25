# 变更记录

格式：日期 | 模块 | 更新内容 | 开发者

2026-07-24 | 权限服务(pd-auth-server) | 修复登录认证未校验用户启用状态(status)，禁用账户现被正确拒绝(JWT_USER_ENABLED) | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 接线密码错误次数与账户锁定：登录失败累加 password_error_num，达到阈值(5)锁定账户，成功登录重置计数 | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 资源新增/修改/删除/级联删除时主动失效网关全局资源缓存(CacheKey.RESOURCE_NEED_TO_CHECK)，解决新增接口最长1小时不生效问题 | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 修复登录验证码校验失败分支返回 success(null) 的死代码，改由全局异常处理器统一返回失败；登录失败透传具体原因(禁用/锁定/凭证错误) | 心飞为你飞
2026-07-24 | 网关(pd-gateway) | 修复 AccessFilter 在 userId 为 null 时 new Long(userId) 的 NPE，无 token 时统一返回 401 | 心飞为你飞
2026-07-24 | 网关(pd-gateway) | 抽取 URL 归一化逻辑到 BaseFilter.normalizeUri，避免忽略令牌判定与权限标识符计算两处逻辑分叉 | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 密码锁定阈值由硬编码常量改为可配置项：新增 AuthorityProperties(pinda.authority.max-password-error-num，默认5)，通过 Nacos 可动态调整，无需改代码 | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 修正 login() 错误透传的泛型类型不匹配(R<User> 误作 R<LoginDTO> 返回)，改为 R.fail(code,msg) 透传禁用/锁定/凭证错误的具体原因 | 心飞为你飞
2026-07-24 | 权限服务(pd-auth-server) | 修复菜单删除不级联子菜单导致孤儿数据：removeByIds 递归收集自身及全部子孙菜单 id 一并删除，并级联清理其关联资源(同步失效网关资源缓存) | 心飞为你飞
