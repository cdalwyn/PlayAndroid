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
    interface Router {
        object Base {
            const val F_WEB = "/base/WebFragment"
        }
        object Main {
            const val A_TEST = "/main/TestActivity"
            const val A_MAIN = "/main/MainActivity"
            const val F_HOME = "/main/HomeFragment"
        }

        object Login {
            const val A_LOGIN = "/login/LoginActivity"
        }

        object User {
            const val F_FIRST = "/user/FirstFragment"
            const val F_USER = "/user/UserFragment"
        }

        object Square {
            const val F_SQUARE = "/square/SquareFragment"
        }

        object Project {
            const val F_PROJECT = "/project/ProjectFragment"
        }

        object Search{
            const val F_SEARCH = "/search/SearchFragment"
        }
    }

    object BundleKey {
        const val MAIN2FIRST = "main2first"
        const val WEB_URL = "web_url"
    }
}