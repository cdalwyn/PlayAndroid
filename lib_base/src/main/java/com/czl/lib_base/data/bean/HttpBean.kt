package com.czl.lib_base.data.bean

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.czl.lib_base.BR
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("isVisible")
    val isVisible: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("url")
    val url: String
)

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
    ) : BaseObservable() {
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
        )
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
)

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
)

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

@Parcelize
data class TodoBean(
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
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("completeDate")
        val completeDate: String? = "",
        @SerializedName("completeDateStr")
        val completeDateStr: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("date")
        val date: Long,
        @SerializedName("dateStr")
        val dateStr: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("priority")
        @TodoPriority
        val priority: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        @TodoType
        val type: Int,
        @SerializedName("userId")
        val userId: Int
    ) : BaseObservable(), Parcelable {
        @Bindable
        @SerializedName("status")
        var status: Int = -1
            set(value) {
                field = value
                notifyPropertyChanged(BR.status)
            }

    }
}

