package com.czl.module_main.adapter

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.entity.HomeBannerBean
import com.czl.lib_base.extension.loadImage
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter.ImageTitleHolder
import com.czl.module_main.ui.fragment.HomeFragment
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MyBannerAdapter(
    mData: List<HomeBannerBean>?,
    val homeFragment: HomeFragment
) :
    BannerAdapter<HomeBannerBean, ImageTitleHolder>(mData) {

    private var mDiffer: AsyncListDiffer<HomeBannerBean>

    init {
        val diffCallback: DiffUtil.ItemCallback<HomeBannerBean> =
            object : DiffUtil.ItemCallback<HomeBannerBean>() {
                override fun areItemsTheSame(
                    oldItem: HomeBannerBean,
                    newItem: HomeBannerBean
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: HomeBannerBean,
                    newItem: HomeBannerBean
                ): Boolean {
                    return TextUtils.equals(oldItem.imagePath, newItem.imagePath)
                }
            }
        mDiffer = AsyncListDiffer(this, diffCallback)
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ImageTitleHolder {
        return ImageTitleHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_banner_item, parent, false)
        )
    }

    override fun onBindView(
        holder: ImageTitleHolder,
        data: HomeBannerBean,
        position: Int,
        size: Int
    ) {
        holder.imageView.loadImage(data.imagePath)
        holder.title.text = data.title
        holder.imageView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, data.url)
            homeFragment.viewModel.startContainerActivity(AppConstants.Router.Base.F_WEB,bundle)
        }
    }

    class ImageTitleHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.image)
        var title: TextView = view.findViewById(R.id.bannerTitle)
    }

    private fun submitList(data: List<HomeBannerBean>?) {
        mDiffer.submitList(data)
    }

    fun setData(
        banner: Banner<Any, BannerAdapter<*, *>>,
        data: List<HomeBannerBean>?
    ) {
        if (data == mDatas) {
            return
        }
        setDatas(data)
        submitList(data)
        banner.apply {
            setCurrentItem(1, false)
            setIndicatorPageChange()
        }
    }

    fun getItem(position: Int): HomeBannerBean? {
        return mDiffer.currentList[position]
    }

}