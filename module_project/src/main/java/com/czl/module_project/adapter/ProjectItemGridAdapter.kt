package com.czl.module_project.adapter

import android.graphics.Bitmap
import android.os.Bundle
import android.util.SparseArray
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
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
class ProjectItemGridAdapter(private val contentFragment: ContentFragment) :
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
            val screenWidth = ScreenUtils.getAppScreenWidth()
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
                .load(item.envelopePic)
                .into(object :ImageViewTarget<Bitmap>(ivProjectItem){
                    override fun setResource(resource: Bitmap?) {
                        resource?.let {
                            view.setImageBitmap(resource)
                            //获取原图的宽高
                            val width = resource.width
                            val height = resource.height
                            //获取imageView的宽
                            val imageViewWidth = ivProjectItem.width
                            //计算缩放比例
                            val sy = (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()
                            //计算图片等比例放大后的高
                            val imageViewHeight = (height * sy).toInt()
                            val params = ivProjectItem.layoutParams
                            params.height = imageViewHeight
                            ivProjectItem.layoutParams = params
                            val itemPosition = getItemPosition(item)
                            if (sizeSparseArray.get(itemPosition)==null){
                                sizeSparseArray.put(itemPosition,imageViewHeight)
                            }
                        }

                    }

                })
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
            return oldItem == newItem
        }
    }
}