import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },

  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },

  {
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    children: [
      {
        path: "/dashboard",
        name: "Dashboard",
        component: () => import("@/views/dashboard/index"),
        meta: { title: "主页", icon: "dashboard" },
      },
    ],
  },
  {
    path: "/model",
    component: Layout,
    redirect: "/model",
    name: "模型管理",
    meta: { title: "模型管理", icon: "el-icon-s-help" },
    children: [
      {
        path: "/model/manage",
        name: "模型配置",
        component: () => import("@/views/model/manage/index"),
        meta: { title: "模型配置", icon: "el-icon-s-operation" },
      },
      {
        path: "/model/relation",
        name: "模型关系",
        component: () => import("@/views/model/relation/index"),
        meta: { title: "模型关系", icon: "el-icon-set-up" },
      },
    ],
  },

  {
    path: "/relation",
    component: Layout,
    redirect: "/relation",
    name: "关系类型",
    children: [
      {
        path: "/relation",
        name: "关系类型",
        component: () => import("@/views/relation/index"),
        meta: { title: "关系类型", icon: "el-icon-coin" },
      },
    ],
  },
  // 404 page must be placed at the end !!!
  
  
];
  //防止连续点击多次路由报错;
let routerPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch((err) => err);
};

// export default router
export default new Router({
   mode: 'hash', 
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes,
});

