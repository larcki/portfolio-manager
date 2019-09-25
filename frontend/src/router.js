import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [
        {path: "*", redirect: "/404"}
    ]
});

router.beforeEach((to, _, next) => {
    document.title = to.meta.title;
    next();
});

export default router;
