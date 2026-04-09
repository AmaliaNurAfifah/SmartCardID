package com.example.kartunamadigital;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

public class CreateCardActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;

    private String vCardData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        // Inisialisasi NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "Perangkat tidak mendukung NFC", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Pending intent untuk NFC
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFiltersArray = new IntentFilter[]{tagDetected};

        EditText name = findViewById(R.id.etName);
        EditText phone = findViewById(R.id.etPhone);
        EditText email = findViewById(R.id.etEmail);

        findViewById(R.id.btnWrite).setOnClickListener(v -> {
            //data yang diinput dikonversi dalam format vCard
            vCardData = "BEGIN:VCARD\nVERSION:3.0\n"
                    + "FN:" + name.getText().toString() + "\n"
                    + "TEL:" + phone.getText().toString() + "\n"
                    + "EMAIL:" + email.getText().toString() + "\n"
                    + "END:VCARD";

            Toast.makeText(this, "Tempelkan kartu NFC untuk menulis", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(
                    this,
                    pendingIntent,
                    intentFiltersArray,
                    null
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
    //method yang otomatis dipanggil saat user menempelkan NFC ke perangkat
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (tag != null && !vCardData.isEmpty()) {
            writeToTag(tag);
        }
    }

    private void writeToTag(Tag tag) {

        try {
            //data vCard diubah menjadi NDEF Record dengan tipe MIME (Multipurpose Internet Mail Extensions) text/x-vcard
            NdefRecord record = NdefRecord.createMime(
                    "text/x-vcard",
                    vCardData.getBytes(StandardCharsets.UTF_8)
            );
            //dibungkus menjadi NDEF Message sebelum ditulis ke NFC
            NdefMessage message = new NdefMessage(new NdefRecord[]{record});

            Ndef ndef = Ndef.get(tag);

            if (ndef != null) {

                ndef.connect(); //aplikasi terhubung ke NFC
                ndef.writeNdefMessage(message); //menuliskan data
                ndef.close();
                //jika berhasil maka data tersimpan di NFC
                Toast.makeText(this, "Card Successfully Created", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Tag does not support NDEF", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Failed to Write NFC", Toast.LENGTH_SHORT).show();
        }
    }
}