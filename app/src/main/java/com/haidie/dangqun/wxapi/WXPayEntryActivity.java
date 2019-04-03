package com.haidie.dangqun.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.haidie.dangqun.Constants;
import com.haidie.dangqun.mvp.event.PayEvent;
import com.haidie.dangqun.rx.RxBus;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {}
	@Override
	public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCord = resp.errCode;
            if (errCord == 0) {
                RxBus.Companion.getDefault().post(new PayEvent(true));
            } else {
                RxBus.Companion.getDefault().post(new PayEvent(false));
            }
            //这里接收到了返回的状态码可以进行相应的操作，
            // 如果不想在这个页面操作可以把状态码存在本地然后finish掉这个页面，这样就回到了你调起支付的那个页面
            //获取到你刚刚存到本地的状态码进行相应的操作就可以了
            finish();
        }
    }
}