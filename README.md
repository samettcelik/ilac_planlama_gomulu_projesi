 İlaç Takip Mobil Uygulaması – Kotlin (Bluetooth Bağlantılı)
Bu mobil uygulama, Arduino tabanlı gömülü ilaç hatırlatma sistemiyle entegre çalışan, Bluetooth destekli, canlı saatli ve günlük ilaç kaydı yapabilen bir Android uygulamasıdır.

Proje Amacı:

Yaşlı bireyler, Alzheimer hastaları veya ilaç almayı sıklıkla unutan kişiler için geliştirilen bu sistemde:

Kullanıcılar, günlük ilaç saatlerini uygulama üzerinden tanımlayabilir.

Uygulama, bu saatleri Bluetooth üzerinden gömülü cihaza iletir.

Gömülü sistem, alarm zamanlarında hem sesli hem de görsel uyarılarla ilaç alımını hatırlatır.

Uygulama üzerinden alarm testi veya servo sıfırlama işlemleri yapılabilir.

Gerçek zamanlı saat ekranı ve günlük ilaç listesi kullanıcıya sunulur.

 Özellikler

🔹 Canlı Saat Görüntüleme – Ana ekran, sistem saatini anlık olarak gösterir.

🔹 Bluetooth ile Cihaz Bağlantısı – HC-05 Bluetooth modülü ile Arduino cihazına bağlanma.

🔹 Günlük İlaç Tanımlama – Saat, dakika ve ilaç adı girerek günlük alarm tanımlanabilir.

🔹 Alarm Gönderme – "SET:HH:MM:İLAÇ_ADI" formatında Arduino’ya veri gönderilir.

🔹 İlaç Listesi Görüntüleme – Günlük girilen ilaç saatleri ve ilaç adları listelenir.

🔹 Bluetooth Cihaz Listesi – Mevcut Bluetooth cihazları listelenerek eşleştirme yapılabilir.

🧰 Kullanılan Teknolojiler

Kotlin ------------	Android uygulama geliştirme dili
Android SDK-----------	API 21+ uyumlu
BluetoothAdapter----------- Bluetooth haberleşmesi için
ViewModel / LiveData-----------	Saat takibi ve UI güncellemeleri için
Material Design-----------	UI bileşenleri için
