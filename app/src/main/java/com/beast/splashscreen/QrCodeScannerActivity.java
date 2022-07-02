package com.beast.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.Arrays;

public class QrCodeScannerActivity extends AppCompatActivity {
    Button btn;
    TextView scanText;
    CodeScannerView scannerView;
    CodeScanner cs;
    String data;
    String[] linesArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scanner);

        btn = findViewById(R.id.add_item_btn);
        scanText = findViewById(R.id.scananedText);
        scannerView = findViewById(R.id.scan);
        runCodeScanner();

        btn.setOnClickListener(view -> {
            if(!scanText.getText().toString().isEmpty()) {
                Log.i("XYZ","msg"+scanText.getText().toString());
                linesArr = scanText.getText().toString().split("\\R");
//                Toast.makeText(this, linesArr[2].toString().substring(8), Toast.LENGTH_SHORT).show();
                Log.i("IMGGG",linesArr[0].substring(6));
                DashBoard.items.add(new DynamicRvModel(linesArr[0],linesArr[1],linesArr[2],getImage(linesArr[0].substring(6))));
                Intent i = new Intent(getApplicationContext(),DashBoard.class);
                startActivity(i);
                finish();
            }
            else {
                Toast.makeText(this, "Error!! Please Scan QR Code Properly", Toast.LENGTH_SHORT).show();
            }




        });

    }

    private void runCodeScanner() {
        cs = new CodeScanner(this,scannerView);
        cs.setAutoFocusEnabled(true);
        cs.setFormats(CodeScanner.ALL_FORMATS);
        cs.setScanMode(ScanMode.CONTINUOUS);

        cs.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data = result.getText();
                        Log.i("AAA",data);
                        scanText.setText(data);
//

                    }
                });
            }
        });
    }
    public static  boolean hasPermissions(Context context,String... permissions)
    {
        if(context!=null && permissions!=null)
        {
            for(String permission:permissions)
            {
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cs != null) {
            cs.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(cs != null) {
            cs.releaseResources();
        }
        super.onPause();
    }
    public  void closeBTN(View v)
    {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
    public int getImage(String s)
    {
        switch (s.toLowerCase())
        {
            case "potato chips": {return R.drawable.potato_chips;}
            case "lays": {return R.drawable.lays;}
            case "maggi": {return R.drawable.maggi;}
            case "oreo": {return R.drawable.oreo;}
            case "cadbury": {return R.drawable.cadbury;}
            case "sprite": {return R.drawable.sprite;}
            case "mtr upma": {return R.drawable.mtr_upma;}
            case "nivea facewash gel": {return R.drawable.nivea_face;}
            case "nivea shower gel": {return R.drawable.nivea_shower;}
            case "corn": {return R.drawable.corn;}
            case "rice": {return R.drawable.rice;}
            case "surf excel": {return R.drawable.surf_excel;}
            case "axe": {return R.drawable.axe;}
            case "pringles": {return R.drawable.pringles;}

            default: return R.drawable.basket;
        }

    }
}