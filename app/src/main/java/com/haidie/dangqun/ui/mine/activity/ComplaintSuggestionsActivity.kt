package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_complaint_suggestions.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by admin2
 *  on 2018/08/18  11:36
 * description  投诉建议
 */
class ComplaintSuggestionsActivity : BaseActivity() {

    private var selectList: MutableList<LocalMedia> = mutableListOf()
    private val maxSelectNum = 3
    private var adapter: GridImageAdapter? = null
    private var themeId: Int = 0
    override fun getLayoutId(): Int = R.layout.activity_complaint_suggestions
    override fun initData() {}
    override fun initView() {
        iv_close.visibility = View.VISIBLE
        iv_close.setOnClickListener{ onBackPressed() }
        tv_title.text = "投诉建议"
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@ComplaintSuggestionsActivity, onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler_view.layoutManager = GridLayoutManager(this@ComplaintSuggestionsActivity,4)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
    }
    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener {
        override fun onAddPicClick() {
            PictureSelector.create(this@ComplaintSuggestionsActivity)
                    .openGallery(PictureMimeType.ofImage())
                    .theme(themeId)
                    .maxSelectNum(maxSelectNum)
                    .minSelectNum(1)
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .previewImage(true)
                    .isCamera(true)
                    .isZoomAnim(true)
                    .imageFormat(PictureMimeType.PNG)
                    .compress(true)
                    .synOrAsy(true)
                    .glideOverride(160, 160)
                    .selectionMedia(selectList)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        }
    }
    override fun start() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    adapter!!.setList(selectList)
                    adapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}