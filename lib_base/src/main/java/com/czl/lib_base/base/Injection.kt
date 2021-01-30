package com.czl.lib_base.base


/**
 * 注入全局的数据仓库，可以考虑使用Dagger2/Koin。（根据项目实际情况搭建，千万不要为了架构而架构）
 * 当前项目已采用Koin注入
 */
object Injection {
//    fun provideDemoRepository(): DataRepository? {
//        //本地数据源
//        val localDataSource: LocalDataSource = LocalDataImpl.INSTANCE
//        // 网络数据源
//        val service = RetrofitClient.getInstance().create(DemoApiService::class.java)
//        val httpDataSource: HttpDataSource = HttpDataImpl.getInstance(service)!!
//        //两条分支组成一个数据仓库
//        return DataRepository.getInstance(localDataSource, httpDataSource)
//    }
}