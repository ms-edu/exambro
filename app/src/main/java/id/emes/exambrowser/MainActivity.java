package id.emes.exambrowser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int QR_SCAN_REQUEST = 200;
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        setContentView(R.layout.activity_main);

        // ── Cek apakah ada header background image ────────────────────────
        applyHeaderBackground();

        etUrl = findViewById(R.id.etUrl);
        Button btnStart        = findViewById(R.id.btnStart);
        LinearLayout btnScanQr = findViewById(R.id.btnScanQr);

        btnStart.setOnClickListener(v -> launchExam(etUrl.getText().toString().trim()));

        btnScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRScanActivity.class);
            startActivityForResult(intent, QR_SCAN_REQUEST);
        });
    }

    /**
     * Jika file header_bg ada di drawable dan bukan placeholder warna,
     * tampilkan sebagai background gambar dengan overlay gelap agar teks tetap terbaca.
     */
    private void applyHeaderBackground() {
        try {
            // Coba load drawable header_bg (PNG yang di-inject saat build)
            int resId = getResources().getIdentifier("header_bg", "drawable", getPackageName());
            if (resId == 0) return; // tidak ada, pakai warna default

            Drawable d = getResources().getDrawable(resId, getTheme());
            if (d == null) return;

            ImageView imgBg        = findViewById(R.id.imgHeaderBg);
            View      overlay      = findViewById(R.id.headerOverlay);
            LinearLayout content   = findViewById(R.id.headerContent);

            // Set gambar
            imgBg.setImageDrawable(d);
            imgBg.setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE);

            // Hapus background solid dari konten agar transparan di atas gambar
            content.setBackgroundColor(android.graphics.Color.TRANSPARENT);

        } catch (Exception e) {
            // Gagal load gambar — pakai warna solid default, tidak masalah
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST && resultCode == RESULT_OK && data != null) {
            String url = data.getStringExtra("scanned_url");
            if (url != null && !url.isEmpty()) {
                etUrl.setText(url);
                launchExam(url);
            }
        }
    }

    private void launchExam(String url) {
        if (url.isEmpty()) {
            Toast.makeText(this, "Masukkan URL ujian terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra("exam_url", url);
        startActivity(intent);
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
