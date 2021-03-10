[![Platform][1]][2]  [![GitHub license][3]][4]

[1]:https://img.shields.io/badge/platform-Android-blue.svg
[2]:https://github.com/cdalwyn/mvvmcomponent
[3]:https://img.shields.io/badge/license-Apache%202-blue.svg
[4]:https://github.com/cdalwyn/mvvmcomponent/blob/master/LICENSE

# :v::fist::wave:MvvmComponents
## 项目截图

![](https://github.com/cdalwyn/mvvmcomponent/blob/master/readme/readme1.png)![](https://github.com/cdalwyn/mvvmcomponent/blob/master/readme/readme2.png)![](https://github.com/cdalwyn/mvvmcomponent/blob/master/readme/readme3.png)

## 项目介绍

- **基于Mvvm模式集成谷歌官方推荐的JetPack组件库LiveData+ViewModel+DataBinding，以ARouter为组件路由实现的玩Android开放API安卓客户端**
- **数据源于玩Android 开放API，集成了登录注册、收藏、分享、搜索、浏览热门博客和项目、添加Todo待办功能等，涵盖了几乎所有玩Android开放接口**
- **项目结合okhttp+Retrofit+RxJava2+Gson组合实现网络请求、Glide图像加载、Koin实现依赖注入、阿里ARouter实现组件路由通信跳转、腾讯MMKV替代Sharedpreferences实现高性能本地缓存、基于LiveData的消息总线LiveEventbus事件分发等等**
- **以一个通用库模块+多业务组件的Mvvm组件化方案，编译、调试、多人开发更方便更快捷**

## 项目架构

![Google官方推荐架构图](https://github.com/cdalwyn/mvvmcomponent/blob/master/readme/mvvm架构图.png)

遵循如上Google Mvvm官方推荐架构，UI与数据分离，以ViewModel为中介进行通信，实现数据驱动UI。通过Koin依赖注入**本地数据+远程数据=数据仓库**，外部只需一行代码调用，隐藏具体实现，规避数据滥用、后期维护难等问题

采用单一容器ContainerActivity+多Fragment配合Fragmentation库、阿里ARouter通信跳转实现单activity多fragment组件化架构

![](https://github.com/cdalwyn/mvvmcomponent/blob/master/readme/包结构.png)

- lib_base：通用功能组件，支撑业务组件基础，提供其他业务组件实现能力
- module_login：业务组件，注册登录模块，以及启动页
- module_main：业务组件，app内Tab首页模块
- module_project：业务组件，app内Tab项目模块
- module_search：功能组件，提供搜索功能
- module_square：业务组件，app内Tab广场模块
- module_user：业务组件，用户管理以及系统设置模块
- module_web：功能组件，提供H5功能

## 未来版本

- [ ] 网络框架使用Retrofit+Kotlin Coroutines更轻便更简洁
- [ ] 依赖注入使用谷歌专为Android打造的Dagger Hilt实现 

## 感谢

- [MvvmHabit: Mvvm整合JetPack快速开发框架](https://github.com/goldze/MVVMHabit)
- [Agentweb: 轻量和极度灵活解决原生webview系列方案](https://github.com/Justson/AgentWeb)
- [Litepal: 轻松地使用SQLite数据库](https://github.com/guolindev/LitePal)
- [Koin: 实用的轻量级依赖注入框架](https://github.com/InsertKoinIO/koin)
- [LiveEventBus: 基于LiveData生命周期安全的消息总线](https://github.com/JeremyLiao/LiveEventBus)
- [LoadSir: 优雅地处理加载中，重试，无数据等](https://github.com/KingJA/LoadSir)
- [BaseRecyclerViewAdapterHelper: 强大灵活的列表适配器](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- ······
- **致敬所有为开源贡献的大佬！**

## License

```
   Copyright 2021 cdalwyn(陈志龙)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

