import Vue from "vue";
import Router from "vue-router";
import Dashboard from './views/Dashboard'
import Assets from './views/Assets'

Vue.use(Router);

const router = new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [
        {
            path: "/",
            name: 'Dashboard',
            component: Dashboard
        },
        {
            path: "/assets",
            name: 'Assets',
            component: Assets
        },
        {path: "*", redirect: "/404"}
    ]
});

router.beforeEach((to, _, next) => {
    document.title = to.meta.title;
    next();
});

export default router;
