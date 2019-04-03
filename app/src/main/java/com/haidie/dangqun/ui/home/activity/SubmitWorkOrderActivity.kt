package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.SubmitWorkOrderContract
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.mvp.presenter.home.SubmitWorkOrderPresenter
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_submit_work_order.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/09/07 17:37
 * description  提交工单
 */
class SubmitWorkOrderActivity : BaseActivity(), SubmitWorkOrderContract.View {

    private val mPresenter by lazy { SubmitWorkOrderPresenter() }
    private var mId: String? = null
    private var pvOptions: OptionsPickerView<String>? = null
    private var mData: List<ServiceCategoryData.ServiceCategoryItemData>? = null
    private var cid: Int = Constants.NEGATIVE_ONE
    private var selectList = mutableListOf<LocalMedia>()
    private val maxSelectNum = 3
    private var adapter: GridImageAdapter? = null
    private var themeId: Int = Constants.NEGATIVE_ONE
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_submit_work_order

    override fun initData() {
        mId = intent.getStringExtra(Constants.ID)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "提交工单"

        tv_work_order_type.setOnClickListener {
            tv_work_order_type.requestFocus()
            clearFocus()
            mPresenter.getServiceCategoryData(mId!!)

        }
        initRecyclerView()
        tv_submit_work_order.setOnClickListener {
            clearFocus()
            CircularAnim.hide(tv_submit_work_order)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go { progress_bar.visibility = View.VISIBLE }
            if (tv_work_order_type.text.toString().isEmpty()) {
                showShort("请选择工单类型")
                showSubmit()
                return@setOnClickListener
            }
            val title = et_headline.text.toString()
            if (TextUtils.isEmpty(title)) {
                showShort("请输入工单标题")
                showSubmit()
                return@setOnClickListener
            }
            if (selectList.size == 0) {
                showShort("请添加照片")
                showSubmit()
                return@setOnClickListener
            }
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            for (i in selectList.indices) {
                val filePath = Constants.PATH_PIC + Constants.PIC + (i + 1) + Constants.PNG
                val oldFile = File(selectList[i].compressPath)
                CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC + (i + 1) + Constants.PNG)
                val file = File(filePath)
                val imageBody = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                builder.addFormDataPart(Constants.PIC + (i + 1), file.name, imageBody)
            }
            val parts = builder.build().parts()
            mPresenter.getToWorkOrderData(toRequestBody("$uid"),toRequestBody("$cid"),toRequestBody(title),
                    toRequestBody(et_content.text.toString()),toRequestBody(token),parts)
        }
    }

    private fun clearFocus() {
        et_headline.clearFocus()
        et_content.clearFocus()
        closeKeyboard(et_headline, this)
    }

    private fun showSubmit() {
        CircularAnim.show(tv_submit_work_order).go()
        progress_bar.visibility = View.GONE
    }
    private fun initRecyclerView() {
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@SubmitWorkOrderActivity, onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler_view.layoutManager = GridLayoutManager(this@SubmitWorkOrderActivity, 4)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        adapter!!.mItemClickListener =  object : GridImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    PictureSelector.create(this@SubmitWorkOrderActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                }
            }
        }
    }

    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener{
        override fun onAddPicClick() {
            clearFocus()
            PictureSelector.create(this@SubmitWorkOrderActivity)
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
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}

    override fun setServiceCategoryData(serviceCategoryData: ServiceCategoryData) {
        mData = serviceCategoryData.list
        pvOptions = OptionsPickerBuilder(this) {
            options1, _, _, _ ->
            val serviceCategoryItemData = mData!![options1]
            cid = serviceCategoryItemData.id
            tv_work_order_type.text = serviceCategoryItemData.name
        }.build()
        val showList = mutableListOf<String>()
        mData!!.forEach {
            showList.add(it.name)
        }
        pvOptions!!.setPicker(showList)
        pvOptions!!.show()
    }

    override fun setToWorkOrderData(isSuccess: Boolean,msg: String) {
        if (isSuccess) {
            showShort("提交成功")
            //返回到上级页面
            onBackPressed()
        }else{
            showShort(msg)
            showSubmit()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}