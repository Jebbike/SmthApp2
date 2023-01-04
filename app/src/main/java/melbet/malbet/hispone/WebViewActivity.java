package melbet.malbet.hispone;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        if(url != null && !url.isEmpty()) {
            webView = findViewById(R.id.url_view);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }

    /**
     * блокирует кнопку назад в вебвью
     */
    @Override
    public void onBackPressed() {
    }
}
