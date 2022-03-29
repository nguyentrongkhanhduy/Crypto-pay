package com.example.crypto_pay_2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.crypto_pay_2.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class RecieveActivity extends AppCompatActivity {

    ImageButton back;
    ImageView qrCode;
    TextView name;
    TextView phone;
    TextView amount;
    TextView typeCoin;

    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recieve);

        initUI();

        createUI();

        getInfo();
    }

    private void initUI(){
        back = findViewById(R.id.back_icon);
        qrCode = findViewById(R.id.qr_code);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        typeCoin = findViewById(R.id.type);

        loadingPB = findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);
    }

    private void createUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecieveActivity.super.onBackPressed();
            }
        });
    }

    private void getInfo(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String nameQR = extras.getString("name");
            String phoneQR = extras.getString("phone");
            String amountQR = extras.getString("amount");
            String typeQR = extras.getString("type");

            name.setText(nameQR);
            phone.setText(phoneQR);
            amount.setText(amountQR);
            typeCoin.setText(typeQR);

            generateQR(phoneQR,amountQR,typeQR);
        }

        loadingPB.setVisibility(View.GONE);
    }

    private void generateQR(String phoneQR, String amountQR, String typeQR) {
        String value = phoneQR + " " + amountQR + " " + typeQR;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(value, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}