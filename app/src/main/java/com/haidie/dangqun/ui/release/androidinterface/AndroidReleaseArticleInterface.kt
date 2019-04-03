package com.haidie.dangqun.ui.release.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.release.activity.ReleaseArticleActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidReleaseArticleInterface (activity: ReleaseArticleActivity){
    private val releaseArticleActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        releaseArticleActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        releaseArticleActivity.releaseSuccess()
    }

    companion object {
        var mCurrentPhotoPath: String? = null//拍照存储的路径,例如：/storage/emulated/0/Pictures/20170608104809.jpg
    }
    /**
     * 打开相机拍照
     */
    @JavascriptInterface
    fun takePicture() {
        releaseArticleActivity.takePicture()
    }
    /**
     * 打开本地相册选择图片
     */
    @JavascriptInterface
    fun choosePic() {
        releaseArticleActivity.choosePicture()
    }
}