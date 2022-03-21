/*!

=========================================================
* Argon Dashboard React - v1.2.1
=========================================================

* Product Page: https://www.creative-tim.com/product/argon-dashboard-react
* Copyright 2021 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/argon-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Index from "views/Index.js";
import Profile from "views/examples/Profile.js";
import Register from "views/examples/Register.js";
import Login from "views/examples/Login.js";
import Tables from "views/examples/Tables.js";
import ForgotPassword from "./views/examples/ForgotPassword";
import ShareFile from "./views/examples/ShareFile";

var routes = [
  {
    path: "/index",
    name: "메인 페이지",
    icon: "ni ni-tv-2 text-primary",
    component: Index,
    layout: "/admin",
    style: {}
  },
  {
    path: "/user-profile",
    name: "내 정보 보기",
    icon: "ni ni-single-02 text-yellow",
    component: Profile,
    layout: "/admin",
    style: {}
  },
  {
    path: "/tables",
    name: "저장소", // Tables
    icon: "ni ni-bullet-list-67 text-red",
    component: Tables,
    layout: "/admin",
    style: {}
  },
  {
    path: "/login",
    component: Login,
    layout: "/auth",
    invisible: true
  },
  {
    path: "/register",
    component: Register,
    layout: "/auth",
    invisible: true
  },
  {
    path: "/forgot-password",
    component: ForgotPassword,
    layout: "/auth",
    invisible: true
  },
  {
    path: "/share-file",
    name: "파일 공유",
    icon: "ni ni-spaceship text-primary",
    component: ShareFile,
    layout: "/admin",
  }
];
export default routes;
