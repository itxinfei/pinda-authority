package com.itheima.pinda.authority.biz.service.auth.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.authority.biz.dao.auth.MenuMapper;
import com.itheima.pinda.authority.biz.service.auth.MenuService;
import com.itheima.pinda.authority.biz.service.auth.ResourceService;
import com.itheima.pinda.authority.entity.auth.Menu;
import com.itheima.pinda.utils.NumberHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.itheima.pinda.utils.StrPool.DEF_PARENT_ID;
/**
 * 业务实现类
 * 菜单
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private ResourceService resourceService;

    @Override
    public List<Menu> findVisibleMenu(String group, Long userId) {
        List<Menu> visibleMenu = baseMapper.findVisibleMenu(group, userId);
        return visibleMenu;
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        // 级联收集自身及全部子孙菜单，避免子菜单因 parent_id 悬空成为孤儿
        List<Long> allIds = findAllMenuIds(ids);
        boolean result = super.removeByIds(allIds);
        if (result) {
            resourceService.removeByMenuId(allIds);
        }
        return result;
    }

    /**
     * 递归收集菜单及其所有子孙菜单的 id
     *
     * @param rootIds 待删除的根菜单 id
     * @return 包含 rootIds 及其全部后代的 id 列表
     */
    private List<Long> findAllMenuIds(List<Long> rootIds) {
        List<Long> allIds = new ArrayList<>(rootIds);
        List<Long> current = new ArrayList<>(rootIds);
        // 逐层向下查找 parent_id 在 current 中的菜单，直至没有更深层子菜单
        while (!current.isEmpty()) {
            List<Long> children = this.lambdaQuery().in(Menu::getParentId, current).list()
                    .stream().map(Menu::getId).collect(Collectors.toList());
            if (children.isEmpty()) {
                break;
            }
            allIds.addAll(children);
            current = children;
        }
        return allIds;
    }

    @Override
    public boolean save(Menu menu) {
        menu.setIsEnable(NumberHelper.getOrDef(menu.getIsEnable(), true));
        menu.setIsPublic(NumberHelper.getOrDef(menu.getIsPublic(), false));
        menu.setParentId(NumberHelper.getOrDef(menu.getParentId(), DEF_PARENT_ID));

        super.save(menu);
        return true;
    }
}