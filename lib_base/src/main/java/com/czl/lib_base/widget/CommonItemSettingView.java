package com.czl.lib_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czl.lib_base.R;

public class CommonItemSettingView extends LinearLayout {

    private TextView tv_content; //右边显示的内容
    private TextView tv_name;   //条目title
    private ImageView iv_pic;   //左边图片
    private ImageView iv_arrow; //向右的箭头

    String mtitle, mcontent;
    boolean mArrow; //右边箭头
    int imgid;

    //用代码new对象时
    public CommonItemSettingView(Context context) {
        super(context);
        initView();
    }

    //有属性时
    public CommonItemSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonItemSettingView);
        mtitle = ta.getString(R.styleable.CommonItemSettingView_mTitle);
        mcontent = ta.getString(R.styleable.CommonItemSettingView_mContent);
        imgid = ta.getResourceId(R.styleable.CommonItemSettingView_mIcon, imgid);
        mArrow = ta.getBoolean(R.styleable.CommonItemSettingView_mRight, true);
        ta.recycle();
        initView();
    }

    //有style样式时候
    public CommonItemSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //把布局文件设置给当前的ServiceView
        View.inflate(getContext(), R.layout.common_item_setting, this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        setTitle(mtitle);
        setDesc(mcontent);
        setImg(imgid);
        setArrow(mArrow);
    }

    //向外暴露接口
    public void setTitle(String title) {
        tv_name.setText(title);
    }

    public void setDesc(String desc) {
        tv_content.setText(desc);
    }

    public void setImg(int img) {
        iv_pic.setImageResource(img);
    }

    public void setArrow(boolean isShow) {
        iv_arrow.setVisibility(isShow ? VISIBLE : GONE);
    }

}
