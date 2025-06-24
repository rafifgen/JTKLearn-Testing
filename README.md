# 📘 **Tugas Kelompok: Pengujian Perangkat Lunak**

## 🧑‍🤝‍🧑 **Anggota Kelompok**
| NIM        | Nama                     |
|------------|--------------------------|
| 221524044  | Mahardika Pratama        |
| 221524052  | Naia Siti Az-zahra       |
| 221524058  | Salsabil Khoirunisa      |

---

## 💻 **Deskripsi Proyek**
Proyek ini merupakan web automation testing untuk aplikasi **Pengelolaan Dana Pendidikan Sekolah Zaidan Educare** menggunakan **Selenium**, **Cucumber**, dan **TestNG**. Pengujian dilakukan secara otomatis berdasarkan skenario yang ditulis dalam format Gherkin.

---

## 📦 **Struktur Direktori**
```
b9-ppl-zaidan-educare-selenium-cucumber
├─ .mvn                             # Konfigurasi Maven Wrapper
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ mvnw, mvnw.cmd                   # Script Maven Wrapper (Linux/Windows)
├─ pom.xml                          # Konfigurasi utama Maven (dependensi, plugin, dsb)
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ zaidan
│  │  │        └─ testng
│  │  │           ├─ actions                        # Kelas aksi untuk interaksi halaman (Page Actions)
│  │  │           │  ├─ HomePageActions.java
│  │  │           │  ├─ LoginPageActions.java
│  │  │           │  └─ LogoutActions.java
│  │  │           ├─ locators                       # Kelas locator untuk mendefinisikan elemen web
│  │  │           │  ├─ HomePageLocators.java
│  │  │           │  ├─ LoginPageLocators.java
│  │  │           │  └─ LogoutLocators.java
│  │  │           └─ utils                          # Kelas utilitas (setup WebDriver)
│  │  │              └─ HelperClass.java
│  │  └─ resources
│  │     └─ application.properties                  # Konfigurasi aplikasi (jika diperlukan)
│  └─ test
│     ├─ java
│     │  └─ com
│     │     └─ zaidan
│     │        └─ testng
│     │           ├─ definitions                    # Step definition untuk Cucumber/TestNG
│     │           │  ├─ Hooks.java
│     │           │  ├─ LoginPageDefinitions.java
│     │           │  └─ LogoutDefinitions.java
│     │           └─ runner                         # Runner untuk menjalankan test suite
│     │              └─ CucumberRunnerTests.java
│     └─ resources
│        └─ features                                # File feature Cucumber (Gherkin)
│           ├─ LoginPage.feature
│           └─ Logout.feature
├─ target                           # Hasil build Maven, laporan pengujian, dan file sementara
├─ test-output                      # Output dan laporan hasil pengujian TestNG
└─ testng.xml                       # Konfigurasi suite TestNG
```

---

## 🛠️ **Tools & Dependencies**
- **Java**: Bahasa pemrograman utama
- **Maven**: Build automation & dependency management
- **Selenium**: Framework automation browser
- **Cucumber**: Penulisan skenario pengujian berbasis Gherkin
- **TestNG**: Framework eksekusi dan laporan pengujian
- **WebDriverManager**: Mengelola otomatisasi driver Chrome/Firefox tanpa konfigurasi manual.
- **Net Masterthought Cucumber Reporting**: Menghasilkan laporan pengujian dalam bentuk HTML yang interaktif dan mudah dipahami

---

## ▶️ **Cara Menjalankan Pengujian**
1. Pastikan Java dan Maven sudah terinstal.
2. Jalankan perintah berikut di terminal:
   ```bash
   mvn clean test
   ```
3. Laporan pengujian akan tersedia di:
   - 'target/cucumber-report-html/' (Net Masterthought Report)
   - 'test-output/' (Laporan TestNG)

---

## 📈 **Hasil Laporan Pengujian Otomatis**
Laporan hasil pengujian otomatis dapat diakses melalui tautan berikut:
- [Report](https://mahardikapratama.github.io/b9-ppl-zaidan-educare-selenium-cucumber/target/cucumber-report-html/cucumber-html-reports/overview-failures.html)

---

## 📜 **Lisensi**
Proyek ini dibuat untuk keperluan akademik dan tidak memiliki lisensi resmi. Silakan gunakan dengan bijak.