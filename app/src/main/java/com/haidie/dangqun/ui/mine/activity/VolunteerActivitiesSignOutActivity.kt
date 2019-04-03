package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.VolunteerActivitiesSignOutContract
import com.haidie.dangqun.mvp.presenter.mine.VolunteerActivitiesSignOutPresenter
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_volunteer_activities_sign_out.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/11/06 11:21
 * description  志愿者活动签退
 */
class VolunteerActivitiesSignOutActivity : BaseActivity(),VolunteerActivitiesSignOutContract.View {

    private var themeId: Int = Constants.NEGATIVE_ONE
    private var adapter: GridImageAdapter? = null
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var mId: Int? = null
    private var selectList = mutableListOf<LocalMedia>()
    private val maxSelectNum = 1
    private val mPresenter by lazy { VolunteerActivitiesSignOutPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_volunteer_activities_sign_out

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "扫码签退"
        initRecyclerView()

        tvSignOut.setOnClickListener {
            CircularAnim.hide(tvSignOut)
                    .endRadius((progressBar.height / 2).toFloat())
                    .go { progressBar.visibility = View.VISIBLE }
            val content = etContent.text.toString()
            if (content.isEmpty()) {
                showShort("请输入标题")
                showSignOut()
                return@setOnClickListener
            }
            if (selectList.isEmpty()) {
                showShort("请添加图片")
                showSignOut()
                return@setOnClickListener
            }
            val filePath = Constants.PATH_PIC + Constants.PIC_PNG
            val oldFile = File(selectList[0].compressPath)
            CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC_PNG)
            val file = File(filePath)
            val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
            val part = MultipartBody.Part.createFormData(Constants.PIC, file.name, requestFile)
            mPresenter.getVolunteerActivitiesSignOutData(toRequestBody("$uid"), toRequestBody("$mId"),toRequestBody(token),
                    toRequestBody(content),part)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showSignOut() {
        CircularAnim.show(tvSignOut).go()
        progressBar.visibility = View.GONE
    }
    private fun initRecyclerView() {
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@VolunteerActivitiesSignOutActivity, onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recyclerView.layoutManager = GridLayoutManager(this@VolunteerActivitiesSignOutActivity, 4)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter!!.mItemClickListener =  object : GridImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    PictureSelector.create(this@VolunteerActivitiesSignOutActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                }
            }
        }
    }
    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener{
        override fun onAddPicClick() {
            clearFocus()
            PictureSelector.create(this@VolunteerActivitiesSignOutActivity)
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
    private fun clearFocus() {
        etContent.clearFocus()
        closeKeyboard(etContent, this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    override fun start() {
    }
    override fun setVolunteerActivitiesSignOutData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("签退成功")
            onBackPressed()
        }else{
            showShort(if (msg.isEmpty()) "签退失败" else msg)
            showSignOut()
        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}