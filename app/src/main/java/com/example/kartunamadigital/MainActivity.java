package com.example.kartunamadigital;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private boolean scanMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnScan = findViewById(R.id.btnScan);
        //menghubungkan aplikasi dengan hardware NFC pada perangkat
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //supaya aplikasi tetap menerima event NFC meski sedang berjalan
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );

        // Tombol Create Card
        btnCreate.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CreateCardActivity.class
            );

            startActivity(intent);
        });

        // Tombol Scan NFC
        btnScan.setOnClickListener(v -> {
            //validasi NFC
            if (nfcAdapter == null) {

                new AlertDialog.Builder(this)
                        .setTitle("NFC Not Supported")
                        .setMessage("This device does not support NFC.")
                        .setPositiveButton("OK", null)
                        .show();

                return;
            }

            if (!nfcAdapter.isEnabled()) {

                new AlertDialog.Builder(this)
                        .setTitle("NFC Disabled")
                        .setMessage("NFC is disabled. Please enable NFC in settings.")
                        .setPositiveButton("OK", null)
                        .show();

                return;
            }

            // Jika NFC aktif
            scanMode = true;

            showScanPopup();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(
                    this,
                    pendingIntent,
                    null,
                    null
            ); //mengaktifkan NFC agar mendapat prioritas saat membaca tag
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this); //NFC dinonaktifkan agar tidak menganggu aplikasi lain dan menghemat baterai
        }
    }
    //method otomatis dipanggil ketika perangkat mendeteksi tag NFC
    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if (!scanMode) return;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //data dari NFC dibaca dalam bentuk NDEF Message, kemudian payload diambil dan dikonversi menjadi string agar dapat ditampilkan
        Parcelable[] rawMessages =
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        String data;

        if (rawMessages != null && rawMessages.length > 0) {

            NdefMessage message = (NdefMessage) rawMessages[0];

            data = new String(
                    message.getRecords()[0].getPayload(),
                    StandardCharsets.UTF_8
            );

        } else {

            data = "Empty tag or unknown format";
        }

        showResultDialog(data, tag);

        scanMode = false;
    }

    private void showScanPopup() {

        new AlertDialog.Builder(this)
                .setTitle("Ready to scan")
                .setMessage("Tap the NFC card to the back of your device.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showResultDialog(String data, Tag tag) {

        new AlertDialog.Builder(this)
                .setTitle("Tag detected!")
                .setMessage(data)
                .setNegativeButton("Close",
                        (d, w) -> d.dismiss())
                .setPositiveButton("Detail",
                        (d, w) -> {

                            new AlertDialog.Builder(this)
                                    .setTitle("Tag Details")
                                    .setMessage(
                                            "Content:\n" + data +
                                                    "\n\nTag ID:\n" + bytesToHex(tag.getId()) +
                                                    "\n\nTechnology:\n" + String.join("\n", tag.getTechList())
                                    )
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                .show();
    }

    private String bytesToHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02X:", b));
        }

        return sb.toString();
    }
}