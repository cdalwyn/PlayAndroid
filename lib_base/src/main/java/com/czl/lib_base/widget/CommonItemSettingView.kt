package com.czl.lib_base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.czl.lib_base.R

class CommonItemSettingView : LinearLayout {
    //右边显示的内容
    private var tvContent: TextView? = null

    //条目title
    private var tvName: TextView? = null

    //左边图片
    private var ivPic: ImageView? = null

    //向右的箭头
    private var ivArrow: ImageView? = null
    private var llRoot: LinearLayout? = null
    var mtitle: String? = null
    var mcontent: String? = null

    //右边箭头
    var mArrow = false
    var imgid = 0
    var mBackgroundRes = 0

    //用代码new对象时
    constructor(context: Context?) : super(context) {
        initView()
    }

    //有属性时
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonItemSettingView)
        mtitle = ta.getString(R.styleable.CommonItemSettingView_mTitle)
        mcontent = ta.getString(R.styleable.CommonItemSettingView_mContent)
        imgid = ta.getResourceId(R.styleable.CommonItemSettingView_mIcon, imgid)
        mBackgroundRes =
            ta.getResourceId(R.styleable.CommonItemSettingView_mBackground, mBackgroundRes)
        mArrow = ta.getBoolean(R.styleable.CommonItemSettingView_mRight, true)
        ta.recycle()
        initView()
    }

    //有style样式时候
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        //把布局文件设置给当前的ServiceView
        inflate(context, R.layout.common_item_setting, this)
        tvName = findViewById<View>(R.id.tv_name) as TextView
        tvContent = findViewById<View>(R.id.tv_content) as TextView
        ivPic = findViewById<View>(R.id.iv_pic) as ImageView
        ivArrow = findViewById<View>(R.id.iv_arrow) as ImageView
        llRoot = findViewById(R.id.ll_root)
        setTitle(mtitle)
        setDesc(mcontent)
        setImg(imgid)
        setBackground(mBackgroundRes)
        setArrow(mArrow)
    }

    private fun setBackground(res: Int) {
        if (res != 0)
            llRoot?.setBackgroundResource(res)
    }

    //向外暴露接口
    fun setTitle(title: String?) {
        tvName?.text = title
    }

    fun setDesc(desc: String?) {
        tvContent?.text = desc
    }

    fun setImg(img: Int) {
        if (img != 0)
            ivPic?.setImageResource(img)
        else
            ivPic?.visibility = View.GONE
    }

    fun setArrow(isShow: Boolean) {
        ivArrow?.visibility = if (isShow) VISIBLE else GONE
    }
}