package com.auto.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.net.http.SslError; // ✅ 正确位置 // 系统框架类，必须导入
import android.webkit.SslErrorHandler; // 系统框架类，必须导入
import android.webkit.WebResourceRequest; // 处理链接跳转

public class BrowserFragment extends Fragment {

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        webView = view.findViewById(R.id.webView);

        // ———— 1. WebView 基础配置 ————
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 若网页无需 JS，可改为 false（减少警告）
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 允许混合内容
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // ———— 2. WebViewClient 配置（处理证书 + 链接跳转） ————
        webView.setWebViewClient(new WebViewClient() {
            // ✅ 方法重写：参数与父类完全一致（WebView, SslErrorHandler, SslError）
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 这里写“证书不安全时弹窗让用户选择”的逻辑（示例仅强制加载）
                handler.proceed(); // 测试阶段：忽略证书错误，强制加载
            }

            // ✅ 方法重写：拦截链接，强制在 WebView 内打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        // ———— 3. 加载目标网页 ————
        webView.loadUrl("https://wiki.colasoft.cn");

        // ———— 4. （可选）刷新按钮逻辑 ————
        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(v -> webView.reload());

        return view;
    }

    // ———— 5. 页面销毁时释放 WebView（避免内存泄漏） ————
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
    }
}