import  { constantRoutes } from "@/router";
import Layout from "@/layout";
const permission = {
  state: {
    routes: [],
    addRoutes: [],
    sidebarRouters: [], 
    topbarRouters: [], 
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes;
      state.routes = constantRoutes.concat(routes);
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes);
    },
    SET_TOPBAR_ROUTES: (state, routes) => {
      state.topbarRouters = routes;
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes;
    },
  },
  actions: {
    /**
     * 生成路由
     *
     * @param commit commit 函数
     * @param menus  路由参数
     */
    GenerateRoutes({ commit }, menus) {
      return new Promise((resolve) => {
        const sidebarRoutes = filterAsyncRouter(menus);
        const rewriteRoutes = filterAsyncRouter(menus);
        commit("SET_ROUTES", rewriteRoutes);
        commit("SET_SIDEBAR_ROUTERS", constantRoutes.concat(sidebarRoutes));
        commit("SET_DEFAULT_ROUTES", sidebarRoutes);
        commit("SET_TOPBAR_ROUTES", sidebarRoutes);
        resolve(rewriteRoutes);
      });
    },
  },
};

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap) {
  asyncRouterMap = asyncRouterMap.map((model) => ({
    path: `/resource/${model.id}`,
    name: model.name,
    component: () => import(`@/views/resource/index`),
    meta: { title: model.name, icon: "el-icon-receiving" },
  }));
  const resourceRouter = [
    {
      path: "/resource",
      component: Layout,
      redirect: "/resource",
      name: "资源管理",
      meta: { title: "资源管理", icon: "el-icon-files" },
      children: asyncRouterMap,
    },
  ];
  return resourceRouter;
}

export const loadView = (view) => {
  // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve);
};

export default permission;
