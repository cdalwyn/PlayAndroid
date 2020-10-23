package com.czl.lib_base.config

/**
 * @author Alwyn
 * @Date 2020/10/22
 * @Description 常量管理类
 */
interface AppConstants {
    object SpKey {
        const val LOGIN_NAME: String = "login_name"
    }

    /**
     * value规则： /(module后缀)/(所在类名)
     * 路由 A_ : Activity
     *     F_ : Fragment
     */
    interface Router{
        object Main{
            const val A_MAIN = "/main/MainActivity"
        }
        object Login{
            const val A_LOGIN = "/login/LoginActivity"
        }
        object User{
            const val F_FIRST = "/user/FirstFragment"
        }
    }
}