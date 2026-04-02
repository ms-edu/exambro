package id.emes.exambrowser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        etUrl = findViewById(R.id.etUrl);
        Button btnStart  = findViewById(R.id.btnStart);
        Button btnScanQr = findViewById(R.id.btnScanQr);

        btnStart.setOnClickListener(v -> launchExam(etUrl.getText().toString().trim()));

        btnScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRScanActivity.class);
            startActivityForResult(intent, QR_SCAN_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST && resultCode == RESULT_OK && data != null) {
            String scannedUrl = data.getStringExtra("scanned_url");
            if (scannedUrl != null && !scannedUrl.isEmpty()) {
                etUrl.setText(scannedUrl);
                launchExam(scannedUrl);
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
