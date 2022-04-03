package com.czl.lib_base.data.bean

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.czl.lib_base.BR
import com.czl.lib_base.annotation.TodoPriority
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
data class UserBean(
    @SerializedName("admin")
    val admin: Boolean,
    @SerializedName("chapterTops")
    val chapterTops: List<Any>,
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("collectIds")
    val collectIds: List<Any>,
    @SerializedName("email")
    val email: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("publicName")
    val publicName: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("username")
    val username: String
)

data class ArticleBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("collect")
        val collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) {
        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}

data class CollectArticleBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("author")
        val author: String,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("originId")
        val originId: Int,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    )
}

data class HomeBannerBean(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int,
    @SerializedName("imagePath")
    val imagePath: String = "",
    @SerializedName("isVisible")
    val isVisible: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int,
    @SerializedName("url")
    val url: String = ""
) : Serializable

data class HomeArticleBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    var datas: ArrayList<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
//        @SerializedName("collect")
//        var collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        @Bindable
        var topFlag: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.topFlag)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        ) : Serializable
    }
}

data class ProjectBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
//        @SerializedName("collect")
//        var collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        ) : Serializable
    }
}

data class SearchDataBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable() {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}

data class SearchHotKeyBean(
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("visible")
    val visible: Int
) : Serializable

data class ProjectSortBean(
    @SerializedName("children")
    val children: List<Any>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("visible")
    val visible: Int
) : Serializable

data class UserScoreBean(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val username: String
)

data class UserShareBean(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: ShareArticles
) {
    data class CoinInfo(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )

    data class ShareArticles(
        @SerializedName("curPage")
        val curPage: Int,
        @SerializedName("datas")
        val datas: List<Data>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("over")
        val over: Boolean,
        @SerializedName("pageCount")
        val pageCount: Int,
        @SerializedName("size")
        val size: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Data(
            @SerializedName("apkLink")
            val apkLink: String,
            @SerializedName("audit")
            val audit: Int,
            @SerializedName("author")
            val author: String,
            @SerializedName("canEdit")
            val canEdit: Boolean,
            @SerializedName("chapterId")
            val chapterId: Int,
            @SerializedName("chapterName")
            val chapterName: String,
            @SerializedName("collect")
            val collect: Boolean,
            @SerializedName("courseId")
            val courseId: Int,
            @SerializedName("desc")
            val desc: String,
            @SerializedName("descMd")
            val descMd: String,
            @SerializedName("envelopePic")
            val envelopePic: String,
            @SerializedName("fresh")
            val fresh: Boolean,
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("niceDate")
            val niceDate: String,
            @SerializedName("niceShareDate")
            val niceShareDate: String,
            @SerializedName("origin")
            val origin: String,
            @SerializedName("prefix")
            val prefix: String,
            @SerializedName("projectLink")
            val projectLink: String,
            @SerializedName("publishTime")
            val publishTime: Long,
            @SerializedName("realSuperChapterId")
            val realSuperChapterId: Int,
            @SerializedName("selfVisible")
            val selfVisible: Int,
            @SerializedName("shareDate")
            val shareDate: Long,
            @SerializedName("shareUser")
            val shareUser: String,
            @SerializedName("superChapterId")
            val superChapterId: Int,
            @SerializedName("superChapterName")
            val superChapterName: String,
            @SerializedName("tags")
            val tags: List<Any>,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("userId")
            val userId: Int,
            @SerializedName("visible")
            val visible: Int,
            @SerializedName("zan")
            val zan: Int
        )
    }
}

data class UserScoreDetailBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("date")
        val date: Long,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("reason")
        val reason: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("userName")
        val userName: String
    )
}

data class UserRankBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )
}

data class CollectWebsiteBean(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("visible")
    val visible: Int
)

data class SquareListBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }
    }
}

data class SystemTreeBean(
    @SerializedName("children")
    val children: List<Children>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerializedName("visible")
    val visible: Int
) {
    data class Children(
        @SerializedName("children")
        val children: List<Any>,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("order")
        val order: Int,
        @SerializedName("parentChapterId")
        val parentChapterId: Int,
        @SerializedName("userControlSetTop")
        val userControlSetTop: Boolean,
        @SerializedName("visible")
        val visible: Int,
        @Transient
        var group: String
    )
}

data class NavigationBean(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("name")
    val name: String
) {
    data class Article(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("collect")
        val collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Any,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    )
}

data class SystemDetailBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable() {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }
    }
}

data class ShareUserDetailBean(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: ShareArticles
) {
    data class CoinInfo(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )

    data class ShareArticles(
        @SerializedName("curPage")
        val curPage: Int,
        @SerializedName("datas")
        val datas: List<Data>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("over")
        val over: Boolean,
        @SerializedName("pageCount")
        val pageCount: Int,
        @SerializedName("size")
        val size: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Data(
            @SerializedName("apkLink")
            val apkLink: String,
            @SerializedName("audit")
            val audit: Int,
            @SerializedName("author")
            val author: String,
            @SerializedName("canEdit")
            val canEdit: Boolean,
            @SerializedName("chapterId")
            val chapterId: Int,
            @SerializedName("chapterName")
            val chapterName: String,
            @SerializedName("courseId")
            val courseId: Int,
            @SerializedName("desc")
            val desc: String,
            @SerializedName("descMd")
            val descMd: String,
            @SerializedName("envelopePic")
            val envelopePic: String,
            @SerializedName("fresh")
            val fresh: Boolean,
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("niceDate")
            val niceDate: String,
            @SerializedName("niceShareDate")
            val niceShareDate: String,
            @SerializedName("origin")
            val origin: String,
            @SerializedName("prefix")
            val prefix: String,
            @SerializedName("projectLink")
            val projectLink: String,
            @SerializedName("publishTime")
            val publishTime: Long,
            @SerializedName("realSuperChapterId")
            val realSuperChapterId: Int,
            @SerializedName("selfVisible")
            val selfVisible: Int,
            @SerializedName("shareDate")
            val shareDate: Long,
            @SerializedName("shareUser")
            val shareUser: String,
            @SerializedName("superChapterId")
            val superChapterId: Int,
            @SerializedName("superChapterName")
            val superChapterName: String,
            @SerializedName("tags")
            val tags: List<Any>,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("userId")
            val userId: Int,
            @SerializedName("visible")
            val visible: Int,
            @SerializedName("zan")
            val zan: Int
        ) : BaseObservable() {
            @Bindable
            @SerializedName("collect")
            var collect: Boolean = false
                set(value) {
                    field = value
                    notifyPropertyChanged(BR.collect)
                }
        }
    }
}

class TodoBean() : Parcelable {
    var curPage = 0
    var offset = 0

    @SerializedName("over")
    var isOver = false
    var pageCount = 0
    var size = 0
    var total = 0
    var datas: List<Data> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        curPage = parcel.readInt()
        offset = parcel.readInt()
        isOver = parcel.readByte() != 0.toByte()
        pageCount = parcel.readInt()
        size = parcel.readInt()
        total = parcel.readInt()
        datas = parcel.createTypedArrayList(Data.CREATOR)!!
    }

    class Data() : BaseObservable(), Parcelable {
        var completeDate: Long? = null
        var completeDateStr: String? = null
        var content: String? = null
        var date: Long = 0
        var dateStr: String? = null
        var id = 0
        var priority = 0
        var title: String? = null
        var type = 0
        var userId = 0

        @Bindable
        var dateExpired = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.dateExpired)
            }

        @Bindable
        var status = 0
            set(status) {
                field = status
                notifyPropertyChanged(BR.status)
            }

        constructor(parcel: Parcel) : this() {
            completeDate = parcel.readLong()
            completeDateStr = parcel.readString()
            content = parcel.readString()
            date = parcel.readLong()
            dateStr = parcel.readString()
            id = parcel.readInt()
            priority = parcel.readInt()
            title = parcel.readString()
            type = parcel.readInt()
            userId = parcel.readInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dateExpired = parcel.readBoolean()
            } else {
                dateExpired = parcel.readInt() == 1
            }
            status = parcel.readInt()
        }


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(completeDate ?: 0)
            parcel.writeString(completeDateStr)
            parcel.writeString(content)
            parcel.writeLong(date)
            parcel.writeString(dateStr)
            parcel.writeInt(id)
            parcel.writeInt(priority)
            parcel.writeString(title)
            parcel.writeInt(type)
            parcel.writeInt(userId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                parcel.writeBoolean(dateExpired)
            } else {
                parcel.writeInt(if (dateExpired) 1 else 0)
            }
            parcel.writeInt(status)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Data

            if (completeDate != other.completeDate) return false
            if (completeDateStr != other.completeDateStr) return false
            if (content != other.content) return false
            if (date != other.date) return false
            if (dateStr != other.dateStr) return false
            if (id != other.id) return false
            if (priority != other.priority) return false
            if (title != other.title) return false
            if (type != other.type) return false
            if (userId != other.userId) return false
            if (dateExpired != other.dateExpired) return false
            if (status != other.status) return false

            return true
        }

        override fun hashCode(): Int {
            var result = completeDate?.hashCode() ?: 0
            result = 31 * result + (completeDateStr?.hashCode() ?: 0)
            result = 31 * result + (content?.hashCode() ?: 0)
            result = 31 * result + date.hashCode()
            result = 31 * result + (dateStr?.hashCode() ?: 0)
            result = 31 * result + id
            result = 31 * result + priority
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + type
            result = 31 * result + userId
            result = 31 * result + dateExpired.hashCode()
            result = 31 * result + status
            return result
        }

        override fun toString(): String {
            return "Data(completeDate=$completeDate, completeDateStr=$completeDateStr, content=$content, date=$date, dateStr=$dateStr, id=$id, priority=$priority, title=$title, type=$type, userId=$userId, dateExpired=$dateExpired, status=$status)"
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TodoBean

        if (curPage != other.curPage) return false
        if (offset != other.offset) return false
        if (isOver != other.isOver) return false
        if (pageCount != other.pageCount) return false
        if (size != other.size) return false
        if (total != other.total) return false
        if (datas != other.datas) return false

        return true
    }

    override fun hashCode(): Int {
        var result = curPage
        result = 31 * result + offset
        result = 31 * result + isOver.hashCode()
        result = 31 * result + pageCount
        result = 31 * result + size
        result = 31 * result + total
        result = 31 * result + (datas.hashCode())
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(curPage)
        parcel.writeInt(offset)
        parcel.writeByte(if (isOver) 1 else 0)
        parcel.writeInt(pageCount)
        parcel.writeInt(size)
        parcel.writeInt(total)
        parcel.writeTypedList(datas)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "TodoBean(curPage=$curPage, offset=$offset, isOver=$isOver, pageCount=$pageCount, size=$size, total=$total, datas=$datas)"
    }

    companion object CREATOR : Parcelable.Creator<TodoBean> {
        override fun createFromParcel(parcel: Parcel): TodoBean {
            return TodoBean(parcel)
        }

        override fun newArray(size: Int): Array<TodoBean?> {
            return arrayOfNulls(size)
        }
    }
}
