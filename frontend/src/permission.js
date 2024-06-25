import router from "./router";
import store from "./store";
import { Message } from "element-ui";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { getToken } from "@/utils/auth"; // get token from cookie
import getPageTitle from "@/utils/get-page-title";
NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ["/login"]; // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start();

  // set page title
  document.title = getPageTitle(to.meta.title);
  // determine whether the user has logged in
  const hasToken = getToken();
  console.log(hasToken);
  if (hasToken) {
    if (!store.state.models.modelsFlag) {
      await store.dispatch("models/fetchModels").then((menus) => {
        store.dispatch("GenerateRoutes", menus).then((accessRoutes) => {
          // 将动态路由信息添加到router.options.routes 生成侧边栏
          router.options.routes = [
            ...router.options.routes,
            ...accessRoutes,
            { path: "*", redirect: "/404", hidden: true },
          ];
          // { path: "*", redirect: "/404", hidden: true } 一定要最后添加
          router.addRoutes([
            ...accessRoutes,
            { path: "*", redirect: "/404", hidden: true },
          ]); // 动态添加可访问路由表
        });
        next({ ...to, replace: true }); // hack方法 确保addRoutes已完成
        console.log(router);
      });
    } else {
      return next();
    }
    if (to.path === "/login") {
      // if is logged in, redirect to the home page
      next({ path: "/" });
      NProgress.done();
    } else {
      const hasGetUserInfo = store.getters.name;
      if (hasGetUserInfo) {
        next();
      } else {
        try {
          // get user info
          await store.dispatch("user/getInfo");

          next();
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch("user/resetToken");
          Message.error(error || "Has Error");
          next(`/login?redirect=${to.path}`);
          NProgress.done();
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
