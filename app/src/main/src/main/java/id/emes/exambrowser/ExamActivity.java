package id.emes.exambrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExamActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private TextView tvSiteUrl;

    // PIN pengawas — ganti sesuai kebutuhan
    private static final String EXIT_PIN = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen + keep screen on
        getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        setContentView(R.layout.activity_exam);

        webView    = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        tvSiteUrl   = findViewById(R.id.tvSiteUrl);
        Button btnExit = findViewById(R.id.btnExit);

        setupWebView();

        String url = getIntent().getStringExtra("exam_url");
        if (url != null) {
            tvSiteUrl.setText(extractHost(url));
            webView.loadUrl(url);
        }

        btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        // Blokir akses file lokal
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                tvSiteUrl.setText(extractHost(url));
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });
    }

    private String extractHost(String url) {
        try {
            return android.net.Uri.parse(url).getHost();
        } catch (Exception e) {
            return url;
        }
    }

    // ─── BLOKIR TOMBOL BACK ───────────────────────────────────────────
    @Override
    public void onBackPressed() {
        // Tidak lakukan apa-apa — tombol Back dikunci
        Toast.makeText(this, "Navigasi dikunci selama ujian", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ||
            keyCode == KeyEvent.KEYCODE_HOME ||
            keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            Toast.makeText(this, "Navigasi dikunci selama ujian", Toast.LENGTH_SHORT).show();
            return true; // intercept, jangan proses lebih lanjut
        }
        return super.onKeyDown(keyCode, event);
    }

    // ─── DIALOG EXIT + PIN ────────────────────────────────────────────
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_pin, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText etPin  = dialogView.findViewById(R.id.etPin);
        etPin.setHintTextColor(0x66FFFFFF); // semi-transparent white hint
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel  = dialogView.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(v -> {
            String entered = etPin.getText().toString().trim();
            if (entered.equals(EXIT_PIN)) {
                dialog.dismiss();
                finish(); // Kembali ke layar Home
            } else {
                etPin.setText("");
                etPin.setError("PIN salah, coba lagi");
                Toast.makeText(this, "PIN tidak valid", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}
