# SmartCardID – NFC Digital Business Card

SmartCardID adalah aplikasi Android yang memanfaatkan teknologi **Near Field Communication (NFC)** untuk membuat dan membaca **kartu nama digital**. Aplikasi ini memungkinkan pengguna menyimpan informasi kontak ke dalam **NFC Tag** dan membacanya kembali hanya dengan menempelkan kartu NFC ke perangkat Android yang mendukung NFC.

Aplikasi ini dibuat sebagai contoh implementasi penggunaan NFC pada perangkat Android untuk pertukaran informasi secara cepat dan praktis.

---

## ✨ Fitur Utama

- **Create Digital Card**  
  Membuat kartu nama digital dengan memasukkan data seperti nama, nomor telepon, dan email, kemudian menyimpannya ke dalam NFC Tag.

- **Scan NFC Card**  
  Membaca data yang tersimpan pada NFC Tag dan menampilkannya di aplikasi.

- **Tag Detection**  
  Aplikasi dapat mendeteksi NFC Tag yang ditempelkan ke perangkat.

- **Tag Detail Information**  
  Menampilkan detail tambahan dari NFC Tag seperti:
  - Tag ID (UID)
  - Teknologi NFC yang digunakan

- **Empty Tag Detection**  
  Jika NFC Tag kosong atau tidak memiliki format yang dikenali, aplikasi tetap akan mendeteksi tag dan menampilkan informasi bahwa tag kosong.

---

## 🛠 Teknologi yang Digunakan

- **Java**
- **Android Studio**
- **Android NFC API**
- **NDEF (NFC Data Exchange Format)**

---

## 📱 Cara Kerja Aplikasi

### Membuat Kartu Nama Digital
1. Buka aplikasi **SmartCardID**
2. Pilih menu **Create Digital Card**
3. Masukkan data kontak
4. Tempelkan NFC Tag ke perangkat
5. Data akan ditulis ke NFC Tag

### Membaca Kartu NFC
1. Pilih menu **Scan NFC Card**
2. Tempelkan NFC Tag ke bagian belakang perangkat
3. Aplikasi akan menampilkan informasi yang tersimpan pada tag

---

## 📋 Persyaratan Perangkat

Untuk menggunakan aplikasi ini diperlukan:

- Perangkat Android yang mendukung **NFC**
- NFC dalam keadaan **aktif**
- NFC Tag **13.56 MHz** (contoh: NTAG atau Mifare Ultralight)

---

## 📄 Contoh Data yang Disimpan

Data disimpan dalam format **vCard**.

```text
BEGIN:VCARD
VERSION:3.0
FN:John Doe
TEL:08123456789
EMAIL:johndoe@email.com
END:VCARD
