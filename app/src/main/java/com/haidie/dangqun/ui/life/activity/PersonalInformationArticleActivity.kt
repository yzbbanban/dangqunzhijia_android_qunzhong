package com.haidie.dangqun.ui.life.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.mine.androidinterface.AndroidBackDetailInterface
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_personal_information_article.*

/**
 * Create by   Administrator
 *      on     2018/09/08 16:23
 * description  个人资料+个人文章列表
 */
class PersonalInformationArticleActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null
    companion object {
        fun startActivity(context: FragmentActivity, url: String) {
            val intent = Intent(context, PersonalInformationArticleActivity::class.java)
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
    override fun getLayoutId(): Int = R.layout.activity_personal_information_article
    override fun initData() { url = intent.getStringExtra(Constants.URL_KEY) }
    override fun initView() {
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_HOST + url)
        //注入对象
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))
    }
    override fun start() {}
}