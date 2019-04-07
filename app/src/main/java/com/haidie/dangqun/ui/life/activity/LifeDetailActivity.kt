package com.haidie.dangqun.ui.life.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.life.androidinterface.AndroidLifeDetailInterface
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_life_detail.*
import java.net.URLEncoder

/**
 * Created by admin2
 *  on 2018/08/21  9:09
 * description  生活详情
 */
class LifeDetailActivity : BaseActivity() {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var postData: String? = null
    private var url: String? = null
    companion object {
        fun startActivity(context: FragmentActivity, url: String) {
            val intent = Intent(context, LifeDetailActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun onPause() {
        mAgentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }
    override fun onResume() {
        mAgentWeb!!.webLifeCycle.onResume()
        super.onResume()
    }
    override fun onDestroy() {
        mAgentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }
    override fun getLayoutId(): Int = R.layout.activity_life_detail
    override fun initData() {
        url = intent.getStringExtra(Constants.URL_KEY)
        postData = URLEncoder.encode(token,Constants.UTF_8)
    }
    override fun initView() {
        try {
            AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
            AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(frame_layout_life_detail, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                    .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                    .createAgentWeb()
                    .ready()
                    .go(UrlConstant.BASE_URL_HOST + url)
            mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidLifeDetailInterface(this))
        }catch (e:Exception){

        }
    }
    override fun start() {}
    fun goToPersonalInformationArticle(url: String){
        PersonalInformationArticleActivity.startActivity(this, url)
    }

    fun toPicturePreview(url: Array<String>){
        val localMedia = ArrayList<LocalMedia>()
        url.forEach {
            val localMedia1 = LocalMedia()
            localMedia1.path = UrlConstant.BASE_URL_HOST + it
            localMedia.add(localMedia1)
        }
        PictureSelector.create(this@LifeDetailActivity)
                .themeStyle(R.style.picture_default_style)
                .openExternalPreview(0, localMedia)
    }
}