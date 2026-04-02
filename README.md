# Emes Exam Browser 🛡
**Safe Exam Browser untuk Android — Tanpa Iklan**

Aplikasi browser khusus ujian online yang mengunci navigasi perangkat,
mencegah peserta membuka aplikasi atau tab lain selama ujian berlangsung.

---

## Fitur Utama
- ✅ Input URL ujian saat launch (fleksibel untuk berbagai platform CBT)
- ✅ Tombol **Back** & **Home** diblokir selama sesi ujian
- ✅ Mode fullscreen — status bar & navigation bar tersembunyi
- ✅ Keluar hanya dengan **PIN pengawas**
- ✅ Progress bar loading halaman
- ✅ Layar tidak mati otomatis selama ujian
- ✅ **Tanpa iklan** — bersih untuk kalangan sendiri
- ✅ Mendukung JavaScript & DOM Storage (kompatibel semua platform CBT)

---

## Cara Setup di Android Studio

### Prasyarat
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17+
- Android SDK API 21 (Android 5.0) — API 34

### Langkah
1. Buka Android Studio → **File > Open** → pilih folder `EmesExamBrowser`
2. Tunggu Gradle sync selesai
3. Buat folder icon di `app/src/main/res/mipmap-*` atau gunakan **Image Asset** di Android Studio
   (klik kanan folder `res` → New → Image Asset → pilih gambar logo)
4. Klik **Run** atau build APK via **Build > Build Bundle(s)/APK(s) > Build APK(s)**

---

## Mengganti PIN Pengawas

Buka file:
```
app/src/main/java/id/emes/exambrowser/ExamActivity.java
```

Cari baris:
```java
private static final String EXIT_PIN = "1234";
```

Ganti `"1234"` dengan PIN yang Anda inginkan, lalu rebuild APK.

---

## Struktur Project

```
EmesExamBrowser/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/id/emes/exambrowser/
│       │   ├── MainActivity.java       ← Layar home, input URL
│       │   └── ExamActivity.java       ← WebView + kiosk mode + PIN exit
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml
│           │   ├── activity_exam.xml
│           │   └── dialog_exit_pin.xml
│           ├── drawable/               ← Background shapes
│           └── values/
│               └── styles.xml
├── build.gradle
└── settings.gradle
```

---

## Distribusi APK

Setelah build berhasil, file APK ada di:
```
app/build/outputs/apk/release/app-release.apk
```

Kirim APK via WhatsApp / Google Drive / link download langsung ke HP peserta.
Tidak perlu melalui Play Store.

> **Catatan:** Peserta perlu mengaktifkan **"Install dari sumber tidak dikenal"**
> di pengaturan HP mereka sebelum install APK.

---

## Teknis: Cara Kerja Kunci Navigasi

```java
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK ||
        keyCode == KeyEvent.KEYCODE_HOME ||
        keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
        // Intercept — tidak diteruskan ke sistem
        return true;
    }
    return super.onKeyDown(keyCode, event);
}
```

Catatan: `KEYCODE_HOME` bisa diblokir di sebagian besar perangkat, namun
beberapa launcher (terutama Android versi baru dengan gesture navigation)
mungkin tetap bisa di-swipe. Untuk keamanan maksimal, gunakan **Device Owner Mode**
(perlu MDM seperti AirWatch/SOTI) atau aktifkan **Pinning Layar** manual
di Pengaturan → Keamanan → Pinning Layar sebelum ujian dimulai.

---

Dibuat dengan ❤️ untuk kemudahan ujian online tanpa iklan.
