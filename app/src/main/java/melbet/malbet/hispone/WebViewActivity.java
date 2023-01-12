package melbet.malbet.hispone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    MenuItem mButtonNext;
    MenuItem mButtonPrev;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        //setSupportActionBar(findViewById(R.id.my_toolbar));

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        if (url != null && !url.isEmpty()) {
            webView = findViewById(R.id.url_view);
            //setupFindListener();

            CookieManager.getInstance().setAcceptCookie(true);
            configureWebSettings(webView.getSettings());

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }

/*    void setupFindListener() {
        webView.setFindListener((activeMatchOrdinal, numberOfMatches, isDoneCounting) -> {
            if (numberOfMatches > 0) {
                mButtonNext.setVisible(true);
                mButtonPrev.setVisible(true);
            }
            if (numberOfMatches == 0) {
                Toast.makeText(getApplicationContext(), R.string.no_matches, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
        mButtonNext = menu.findItem(R.id.next);
        mButtonPrev = menu.findItem(R.id.prev);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(false);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mButtonNext.setVisible(false);
                mButtonPrev.setVisible(false);

                //https://stackoverflow.com/questions/41725983/toolbar-icons-disappearing-after-expanding-the-searchview-in-android
                supportInvalidateOptionsMenu();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    webView.findAllAsync(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.next:
                webView.findNext(true);
                break;
            case R.id.prev:
                webView.findNext(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * блокирует кнопку назад в вебвью
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebSettings(WebSettings settings) {
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
    }
}
