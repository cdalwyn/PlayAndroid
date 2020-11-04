package com.czl.lib_base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.czl.lib_base.data.DataRepository
import java.lang.reflect.InvocationTargetException

/**
 * 通过反射动态实例化
 * ViewModel工厂
 */
class AppViewModelFactory(
    private val mApplication: MyApplication,
    private val mRepository: DataRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
//            return TestViewModel(mApplication, mRepository!!) as T
//        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
//            return MainViewModel(mApplication, mRepository!!) as T
//        }else if (modelClass.isAssignableFrom(CommonViewModel::class.java)){
//            return CommonViewModel(mApplication,mRepository!!) as T
//        }
        //反射动态实例化ViewModel
        return try {
            val className = modelClass.canonicalName
            val classViewModel = Class.forName(className!!)
            val cons = classViewModel.getConstructor(MyApplication::class.java, DataRepository::class.java)
            val viewModel = cons.newInstance(mApplication,mRepository) as ViewModel
            viewModel as T
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: InstantiationException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


//    companion object {
//        @Volatile
//        private var INSTANCE: AppViewModelFactory? = null
//
//        fun getInstance(application: Application): AppViewModelFactory? {
//            if (INSTANCE == null) {
//                synchronized(AppViewModelFactory::class.java) {
//                    if (INSTANCE == null) {
//                        // 已采用koin注入
//                        INSTANCE = AppViewModelFactory(application, Injection.provideDemoRepository())
//                    }
//                }
//            }
//            return INSTANCE
//        }
//    }
}