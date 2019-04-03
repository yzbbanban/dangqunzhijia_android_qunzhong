package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.SparseArray
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.MyHelpEditContract
import com.haidie.dangqun.mvp.event.ReloadMyHelpEvent
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.presenter.mine.MyHelpEditPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.RegexUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_my_help_edit.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/10/31 09:56
 * description  我的求助-编辑
 */
class MyHelpEditActivity : BaseActivity(),MyHelpEditContract.View {

    private var isRefresh = false
    private var mId: Int? = null
    private val page: Int = 1
    private var gender: Int?  = null    //性别,0表示女,1表示男
    private var helpType: Int?  = null    //困难类型0.其他, 1.因病,2.因残,3.因学,4.因灾,5.因技术,6.缺劳动力,7.缺资金
    private val isOnline: String = "1"  //是否是智慧互助-网上求助,0否 1是,这里填1
    private var mPvOptions: OptionsPickerView<String>? = null
    private var themeId: Int = Constants.NEGATIVE_ONE
    private val maxSelectNum = 1
    private var selectList = mutableListOf<LocalMedia>()
    private var uploadImage = SparseArray<String>()
    private var nativeImage = SparseArray<String>()
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { MyHelpEditPresenter() }
    companion object {
        fun startActivity(context: FragmentActivity,id : Int) {
            val intent = Intent(context, MyHelpEditActivity::class.java)
            intent.putExtra(Constants.ID,id)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_my_help_edit

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "网上求助"
        mLayoutStatusView = multipleStatusView
        themeId = R.style.picture_default_style
        initTypeView()
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbMale -> gender = 1
                R.id.rbFemale -> gender = 0
            }
        }
        llHelpType.setOnClickListener {
//            弹出类型选项
            if (mPvOptions != null) {
                mPvOptions!!.show()
            }
        }
        ivDelete1.setOnClickListener {
            rl1.visibility = View.GONE
            ivAdd.visibility = View.VISIBLE
            uploadImage.remove(1)
            nativeImage.remove(1)
        }
        ivDelete2.setOnClickListener {
            rl2.visibility = View.GONE
            ivAdd.visibility = View.VISIBLE
            uploadImage.remove(2)
            nativeImage.remove(2)
        }
        ivDelete3.setOnClickListener {
            rl3.visibility = View.GONE
            ivAdd.visibility = View.VISIBLE
            uploadImage.remove(3)
            nativeImage.remove(3)
        }
        ivAdd.setOnClickListener {
            PictureSelector.create(this@MyHelpEditActivity)
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
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        tvSubmit.setOnClickListener {
            CircularAnim.hide(tvSubmit)
                    .endRadius((progressBar.height/2).toFloat())
                    .go { progressBar.visibility = View.VISIBLE }
            val title = etTitle.text.toString()
            if (title.isEmpty()) {
                showShort("请输入标题")
                showSubmit()
                return@setOnClickListener
            }
            val description = etDescription.text.toString()
            if (description.isEmpty()) {
                showShort("请输入描述内容")
                showSubmit()
                return@setOnClickListener
            }
            val name = etName.text.toString()
            if (name.isEmpty()) {
                showShort("请输入求助人")
                showSubmit()
                return@setOnClickListener
            }
            val phone = etPhone.text.toString()
            if (phone.isEmpty()) {
                showShort("请输入手机号码")
                showSubmit()
                return@setOnClickListener
            }
            val identity = etIdentity.text.toString()
            if (identity.isEmpty()) {
                showShort("请输入身份证号")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                showShort("手机格式错误")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isIDCard18(identity)) {
                showShort("身份证格式错误")
                showSubmit()
                return@setOnClickListener
            }
            var part1 : MultipartBody.Part? = null
            if (!nativeImage.get(1).isNullOrEmpty()){
                val filePath = Constants.PATH_PIC + Constants.PIC + 1 + Constants.PNG
                val oldFile = File(nativeImage.get(1))
                CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC + 1 + Constants.PNG)
                val file = File(filePath)
                val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                part1 = MultipartBody.Part.createFormData(Constants.PIC + 1, file.name, requestFile)
            }
            var part2 : MultipartBody.Part? = null
            if (!nativeImage.get(2).isNullOrEmpty()){
                val filePath = Constants.PATH_PIC + Constants.PIC + 2 + Constants.PNG
                val oldFile = File(nativeImage.get(2))
                CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC + 2 + Constants.PNG)
                val file = File(filePath)
                val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                part2 = MultipartBody.Part.createFormData(Constants.PIC + 2, file.name, requestFile)
            }
            var part3 : MultipartBody.Part? = null
            if (!nativeImage.get(3).isNullOrEmpty()){
                val filePath = Constants.PATH_PIC + Constants.PIC + 3 + Constants.PNG
                val oldFile = File(nativeImage.get(3))
                CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC + 3 + Constants.PNG)
                val file = File(filePath)
                val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                part3 = MultipartBody.Part.createFormData(Constants.PIC + 3, file.name, requestFile)
            }
            mPresenter.getMyHelpEditData(toRequestBody(uid.toString()),toRequestBody(token),toRequestBody(mId.toString()),null,toRequestBody(helpType.toString()),
                    toRequestBody(isOnline),toRequestBody(name),toRequestBody(gender.toString()),toRequestBody(phone),toRequestBody(identity),
                    toRequestBody(etAddress.text.toString()),toRequestBody(title),toRequestBody(description),
                    if (uploadImage.get(1).isNullOrEmpty()) null else toRequestBody(uploadImage.get(1)),
                    if (uploadImage.get(2).isNullOrEmpty()) null else toRequestBody(uploadImage.get(2)),
                    if (uploadImage.get(3).isNullOrEmpty()) null else toRequestBody(uploadImage.get(3)),
                    part1,part2,part3)
        }
    }
    private fun showSubmit() {
        CircularAnim.show(tvSubmit).go()
        progressBar.visibility = View.GONE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    when {
                        rl1.visibility == View.GONE -> {
                            rl1.visibility = View.VISIBLE
                            ImageLoader.load(this,File(selectList[0].compressPath),iv1)
                            nativeImage.put(1,selectList[0].compressPath)
                        }
                        rl2.visibility == View.GONE -> {
                            rl2.visibility = View.VISIBLE
                            ImageLoader.load(this,File(selectList[0].compressPath),iv2)
                            nativeImage.put(2,selectList[0].compressPath)
                        }
                        rl3.visibility == View.GONE -> {
                            rl3.visibility = View.VISIBLE
                            ivAdd.visibility = View.GONE
                            ImageLoader.load(this,File(selectList[0].compressPath),iv3)
                            nativeImage.put(3,selectList[0].compressPath)
                        }
                    }
                }
            }
        }
    }
    private fun initTypeView() {
        val data = ArrayList<String>()
        data.add("其他")
        data.add("因病")
        data.add("因残")
        data.add("因学")
        data.add("因灾")
        data.add("因技术")
        data.add("缺劳动力")
        data.add("缺资金")
        mPvOptions = OptionsPickerBuilder(this@MyHelpEditActivity, OnOptionsSelectListener { options1, _, _, _ ->
            val type = data[options1]
            helpType = options1
            tvHelpType.text = type
        }).build()
        mPvOptions!!.setPicker(data)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getOnlineHelpDetailsData(uid,mId!!,token,page,Constants.SIZE)
    }
    override fun setOnlineHelpDetailsData(onlineHelpDetailsData: OnlineHelpDetailsData) {
        val title = onlineHelpDetailsData.title
        etTitle.text = getText(title)
        val content = onlineHelpDetailsData.content
        etDescription.text = getText(content)
        val pic1 = onlineHelpDetailsData.pic1
        if (pic1 != null && pic1.isNotEmpty()) {
            rl1.visibility = View.VISIBLE
            ImageLoader.load(this, pic1, iv1)
            uploadImage.put(1,pic1)
        }
        val pic2 = onlineHelpDetailsData.pic2
        if (pic2 != null && pic2.isNotEmpty()) {
            rl2.visibility = View.VISIBLE
            ImageLoader.load(this, pic2, iv2)
            uploadImage.put(2,pic2)
        }
        val pic3 = onlineHelpDetailsData.pic3
        if (pic3 != null && pic3.isNotEmpty()) {
            rl3.visibility = View.VISIBLE
            ivAdd.visibility = View.GONE
            ImageLoader.load(this, pic3, iv3)
            uploadImage.put(3,pic3)
        }
        val username = onlineHelpDetailsData.username
        etName.text = getText(username)
        val phone = onlineHelpDetailsData.phone
        etPhone.text = getText(phone)
        gender = onlineHelpDetailsData.gender
        when (gender) {
            0 ->  rbFemale.isChecked = true
            1 ->  rbMale.isChecked = true
        }
        val identity = onlineHelpDetailsData.identity
        etIdentity.text = getText(identity)
        val address = onlineHelpDetailsData.address
        etAddress.text = getText(address)
        helpType = onlineHelpDetailsData.troubletype
        tvHelpType.text = when(helpType){
            0 -> "其他"
            1 -> "因病"
            2 -> "因残"
            3 -> "因学"
            4 -> "因灾"
            5 -> "因技术"
            6 -> "缺劳动力"
            else -> "缺资金"
        }
    }
    private fun  getText(text : String) : Editable {
        return  SpannableStringBuilder(text)
    }
    override fun setMyHelpEditData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("编辑成功")
            RxBus.getDefault().post(ReloadMyHelpEvent())
            onBackPressed()
        }else{
            showShort(if (msg.isEmpty()) "编辑失败" else msg)
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }

    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}