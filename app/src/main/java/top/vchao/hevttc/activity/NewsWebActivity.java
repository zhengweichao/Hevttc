package top.vchao.hevttc.activity;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.ToastUtil;

public class NewsWebActivity extends BaseActivity {

    @BindView(R.id.wv_news)
    WebView wvNews;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_web;
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showShort("该新闻已经失效！");
            return;
        }
        MyWebViewClient myWebViewClient = new MyWebViewClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvNews.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvNews.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        wvNews.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        wvNews.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        wvNews.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        wvNews.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        wvNews.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        wvNews.getSettings().setAppCacheEnabled(true);//是否使用缓存
        wvNews.getSettings().setDomStorageEnabled(true);//DOM Storage
        wvNews.setWebChromeClient(new WebChromeClient());
        wvNews.setWebViewClient(myWebViewClient);
        wvNews.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        //重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }


}
