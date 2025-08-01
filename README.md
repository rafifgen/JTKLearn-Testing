## 💻 **Deskripsi Proyek**
Proyek ini merupakan web automation testing untuk aplikasi **JTK Learn** menggunakan **Selenium**, **Cucumber**, dan **TestNG**. Pengujian dilakukan secara otomatis berdasarkan skenario yang ditulis dalam format Gherkin.

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

## 📈 **Reportase**
Reportase dari tes yang sudah dijalankan dapat dilihat di [Versi_1_tautan berikut](https://rafifgen.github.io/JTKLearn-Testing/target/cucumber-report-html/cucumber-html-reports/overview-features.html) - [Versi_2_tautan berikut](https://rafifgen.github.io/JTKLearn-Testing/report_test.html).


## 📜 **Lisensi**
Proyek ini dibuat untuk keperluan akademik dan tidak memiliki lisensi resmi. Silakan gunakan dengan bijak.
