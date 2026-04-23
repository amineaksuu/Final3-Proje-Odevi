Otonom Araçlar İçin Maksimum Marjinli Güvenlik Koridoru Algoritması

Bu proje, otonom araçların engeller arasından en güvenli şekilde geçmesini sağlayan, Destek Vektör Makineleri (SVM) tabanlı bir rota optimizasyon algoritmasıdır. Hazır kütüphane kullanılmadan, saf Java ile sıfırdan implemente edilmiştir.

🚀 Projenin Amacı

Otonom araç navigasyonunda sadece engelden kaçmak yeterli değildir. Sensör gürültüleri ve mekanik sapmalar göz önüne alındığında, aracın engellere matematiksel olarak maksimum uzaklıkta kalması (Maximum Margin) gerekir. Bu algoritma, engeller arasında güvenli bir "koridor" inşa eder.

🧠 Teknik Detaylar

Matematiksel Model

Algoritma, engelleri iki sınıfa (+1 ve -1) ayırır ve aradaki boşluğu maksimize eden hiperdüzlemi bulur:

Karar Sınırı: w.x + b = 0

Kayıp Fonksiyonu: Hinge Loss (Menteşe Kaybı)

Optimizasyon: Stokastik Gradyan İnişi (SGD)

Algoritmik Karmaşıklık (Big-O)

Eğitim: O(E.N) (Doğrusal zamanlı eğitim)

Çıkarım (Inference): O(1) (Sabit zamanlı anlık karar verme)

🛠️ Öne Çıkan Özellikler

Sıfır Bellek Sızıntısı: Eğitim döngüleri içerisinde yeni nesne üretimi yapılmaz (Zero-Object Creation), sadece primitif double dizileri kullanılır. Bu, Garbage Collector kaynaklı duraklamaları (Stop-the-world) engeller.

Single Responsibility (SRP): Kod mimarisi; Veri Yönetimi, Çözücü Motor ve GUI olarak katmanlara ayrılmıştır.

Data Scrubbing: DatasetManager sınıfı, sensörlerden gelen hatalı (0.0) verileri otomatik olarak filtreler.

💻 Kurulum ve Çalıştırma

Projeyi klonlayın

Klasöründeki dosyaları Java IDE'nizde (IntelliJ, Eclipse vb.) açın.

Main.java dosyasını çalıştırın.

📊 Örnek Çıktı

Uygulama çalıştığında:

Kırmızı/Mavi Noktalar: Farklı engel sınıflarını,

Siyah Çizgi: Optimal rotayı,

Kenar Çizgileri: Güvenlik koridoru sınırlarını (Marjinleri) temsil eder.

Hazırlayan: Amine Aksu (1240505053)
