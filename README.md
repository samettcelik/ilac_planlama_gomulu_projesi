# 💊 İlaç Takip Mobil Uygulaması – Kotlin (Bluetooth Bağlantılı)

**İzmir Bakırçay Üniversitesi**  
**Bilgisayar Mühendisliği - Gömülü Sistemler Dersi Projesi**  
📅 **Tarih:** 25.05.2025  

## 👨‍💻 Ekip Üyeleri
- **Samet Çelik**  
- **Sonay Karaaslan**  

---

## 🎯 Proje Amacı

Bu mobil uygulama, **Arduino tabanlı gömülü bir ilaç hatırlatma sistemiyle entegre çalışan**, Bluetooth destekli ve canlı saatli bir **Android ilaç takip sistemi** olarak tasarlanmıştır.

> Özellikle **yaşlı bireyler**, **Alzheimer hastaları** veya **düzenli ilaç kullanan bireyler** için geliştirilmiştir.

🔹 **Kullanıcılar**, günlük ilaç saatlerini uygulama üzerinden tanımlar.  
🔹 Uygulama bu saatleri **Bluetooth üzerinden cihaza iletir.**  
🔹 Gömülü cihaz, alarm saatinde sesli ve görsel olarak uyarı verir.  
🔹 Kullanıcı, ilaç alımını geciktirirse sistem ikinci kez hatırlatır.  
🔹 Alarm test etme ve servo sıfırlama işlemleri mobil uygulama üzerinden yapılabilir.

---

## 📱 Uygulama Özellikleri

| Özellik | Açıklama |
|--------|----------|
| ⏰ **Canlı Saat Görüntüleme** | Ana ekran sistem saatini gerçek zamanlı gösterir. |
| 📡 **Bluetooth Cihaz Bağlantısı** | HC-05 modülü üzerinden Arduino ile iletişim kurar. |
| 📝 **Günlük Alarm Tanımlama** | Kullanıcı saat, dakika ve ilaç adı girerek alarm tanımlar. |
| 🚀 **Alarm Gönderme** | `"SET:HH:MM:İLAÇ_ADI"` formatında cihazla iletişim kurar. |
| 📋 **İlaç Listesi Görüntüleme** | Günlük tanımlanan alarmlar listelenebilir. |
| 🔎 **Bluetooth Cihaz Tarama** | Yakındaki cihazlar listelenip bağlanılabilir. |
| 🛠️ **Komutlar** | `SET`, `TEST_ALARM`, `RESET_SERVO` gibi komutlar gönderilebilir. |

---

## 🔗 Sistem Entegrasyonu

Bu mobil uygulama, aşağıdaki donanımlarla entegre çalışır:

- **Arduino Uno**
- **HC-05 Bluetooth Modülü**
- **16x2 I2C LCD Ekran**
- **SG90 Servo Motor**
- **Buzzer ve LED Uyarı Modülleri**
- **"İlacı Aldım" Butonu (INT0)**

Bluetooth haberleşmesi sayesinde cihazla kablosuz bağlantı kurularak alarmlar Arduino tarafına aktarılır.

---

## 🖼️ Uygulama Ekran Görüntüleri

<table>
  <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/6e439d4b-8650-4cc7-ad52-2b0a1a3cff63" width="250"/><br><b>Giriş Ekranı</b></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/d3755244-7568-4b64-a7e9-8083dc6fdfc3" width="250"/><br><b>Ana Ekran</b></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/b5e1d961-8507-483b-9114-df1d39208a82" width="250"/><br><b>Alarm Kurma</b></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/a13aba1c-bd99-442d-af99-468af2b49873" width="250"/><br><b>Alarm Kaydı</b></td>
  </tr>
</table>

---

## ⚙️ Teknolojiler

- **Dil:** Kotlin
- **SDK:** Android SDK 21+
- **Bluetooth:** `BluetoothAdapter`, `BluetoothDevice`
- **UI:** Material Design Bileşenleri
- **Veri Saklama:** SharedPreferences (Günlük ilaç listesi)

---

## 📥 Kullanım Talimatı

1. **Android Studio** ile projeyi açın.
2. Gerekli izinleri AndroidManifest.xml dosyasına ekleyin:  
   - `BLUETOOTH_CONNECT`  
   - `BLUETOOTH_SCAN`  
   - `BLUETOOTH_ADMIN`  
3. Uygulamayı çalıştırın, HC-05 cihazına bağlanın.
4. Alarm tanımlayın ve cihazla eşleştirerek sistemin otomatik çalışmasını sağlayın.

---

## 📢 Komut Formatları

| Komut | Açıklama |
|-------|----------|
| `SET:HH:MM:İLAÇ_ADI` | Yeni alarm oluşturur |
| `TEST_ALARM` | Test alarmı tetikler |
| `RESET_SERVO` | Servo motoru başlangıç konumuna getirir |

---

## 👥 Katkıda Bulunanlar

- 🧑‍💻 **Samet Çelik** – Gömülü sistem entegrasyonu & Bluetooth haberleşme  
- 👩‍💻 **Sonay Karaaslan** – Mobil uygulama arayüz tasarımı & komut yönetimi

---

## 📄 Lisans

Bu proje **eğitim amaçlıdır** ve yalnızca İzmir Bakırçay Üniversitesi Gömülü Sistemler dersi kapsamında kullanılmıştır. Ticari kullanımlar için izin alınmalıdır.

---

## 📚 Kaynaklar

- [Arduino.cc - Arduino Uno](https://www.arduino.cc/en/Main/ArduinoBoardUno)  
- [Adafruit - DS3231 RTC Modülü](https://learn.adafruit.com/adafruit-ds3231-precision-rtc-breakout)  
- [Science Buddies - Arduino Pill Dispenser](https://www.sciencebuddies.org/science-fair-projects/project-ideas/Elec_p105/electricity-electronics/automatic-pill-dispenser)  
- [Arduino Project Hub - HC-05 Kullanımı](https://create.arduino.cc/projecthub/projects/tags/hc-05)  
- [Tinkercad Circuits](https://www.tinkercad.com/)  
- [MIT App Inventor](https://appinventor.mit.edu/)  
- [LiquidCrystal_I2C Library](https://github.com/johnrickman/LiquidCrystal_I2C)  
- [TowerPro SG90 Datasheet](https://www.ee.ic.ac.uk/pcheung/teaching/DE1_EE/stores/sg90_datasheet.pdf)  
- [ATmega328P Datasheet](https://ww1.microchip.com/downloads/en/DeviceDoc/Atmel-7810-Automotive-Microcontrollers-ATmega328P_Datasheet.pdf)  
- [Bluetooth on Android - Android Developers](https://developer.android.com/guide/topics/connectivity/bluetooth)

---
