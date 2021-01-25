package com.czl.module_project.adapter

import android.graphics.Bitmap
import android.os.Bundle
import android.util.SparseArray
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectItemGridBinding
import com.czl.module_project.ui.fragment.ContentFragment


/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description 瀑布流适配器
 */
class ProjectItemGridAdapter(val contentFragment: ContentFragment) :
    BaseQuickAdapter<ProjectBean.Data, BaseDataBindingHolder<ProjectItemGridBinding>>(R.layout.project_item_grid) {

    // 存储图片的高度
    private val sizeSparseArray: SparseArray<Int?> = SparseArray()

    override fun convert(
        holder: BaseDataBindingHolder<ProjectItemGridBinding>,
        item: ProjectBean.Data
    ) {
        holder.dataBinding?.apply {
            tvTitle.text = item.title
            adapter = this@ProjectItemGridAdapter
            this.item = item
            // 设置图片宽高
            val layoutParams = ivProjectItem.layoutParams
            val screenWidth = context.resources.displayMetrics.widthPixels
            val mWidth = screenWidth / 2
            layoutParams.width = mWidth
            if (sizeSparseArray.get(getItemPosition(item)) != null) {
                val mHeight = sizeSparseArray.get(getItemPosition(item))
                layoutParams.height = mHeight!!
                ivProjectItem.layoutParams = layoutParams
            }
            // 加载图片配置
            val options: RequestOptions = RequestOptions()
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.default_project_img)
            Glide.with(context)
                .asBitmap()
                .apply(options)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (sizeSparseArray.get(getItemPosition(item)) == null) {
                            resource?.let { bitmap ->
                                sizeSparseArray.put(
                                    getItemPosition(item),
                                    bitmap.height
                                )
                            }
                        }
                        return false
                    }
                })
                .load(item.envelopePic)
                .into(ivProjectItem)
            executePendingBindings()
        }
    }

    val onItemClickCommand: BindingCommand<Any?> = BindingCommand(BindingConsumer {
        if (it is ProjectBean.Data) {
            contentFragment.startContainerActivity(
                AppConstants.Router.Web.F_WEB,
                Bundle().apply { putString(AppConstants.BundleKey.WEB_URL, it.link) })
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<ProjectBean.Data>() {
        override fun areItemsTheSame(
            oldItem: ProjectBean.Data,
            newItem: ProjectBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProjectBean.Data,
            newItem: ProjectBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }
    }
}