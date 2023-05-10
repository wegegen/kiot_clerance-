package com.example.checked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import android.Manifest;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class computerinformation extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private EditText idEditText;
    private ImageButton scanButton, refreshbtn;
    private Button backs;
    private DatabaseReference myRef;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int QR_CODE_SCAN_REQUEST_CODE = 200;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);

        backs = findViewById(R.id.back);
        idEditText = findViewById(R.id.id);
        scanButton = findViewById(R.id.imageButton);
        refreshbtn = findViewById(R.id.imagerefresh);

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(computerinformation.this, admin.class);
                startActivity(intent);
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference("computerinfo");

        scanButton.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(computerinformation.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                launchQRCodeScanner();
            } else {
                ActivityCompat.requestPermissions(computerinformation.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
    }

    private void launchQRCodeScanner() {
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        String qrData = result.getText();
        String ids = idEditText.getText().toString();

        scannerView.stopCamera();

        saveUserData(ids, qrData);

        // Refresh the activity
        finish();
        startActivity(getIntent());
    }

    private void saveUserData(String id, String qrData) {
        UserData userData = new UserData(id, qrData);

        myRef.child(id).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(computerinformation.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(computerinformation.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error saving user data", task.getException());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (scannerView != null) {
            scannerView.stopCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (scannerView != null) {
            scannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission required to scan QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


































































//public class computerinformation extends AppCompatActivity implements ZXingScannerView.ResultHandler {
//    private EditText idEditText;
//    private ImageButton scanButton ,refreshbtn ;
//    private Button backs;
//    private DatabaseReference myRef;
//
//    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
//    private static final int QR_CODE_SCAN_REQUEST_CODE = 200;
//    private ZXingScannerView scannerView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_comp);
//        backs = findViewById( R.id.back );
//        idEditText = findViewById(R.id.id);
//        scanButton = findViewById(R.id.imageButton);
//        refreshbtn = findViewById( R.id.imagerefresh );
//
//        refreshbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                startActivity(getIntent());
//            }
//        });
//
//
//
//
//
//
//
//
//
//
//        backs.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(computerinformation.this,admin.class);
//                startActivity( intent );
//            }
//        } );
//
//        myRef = FirebaseDatabase.getInstance().getReference("computerinfo");
//
//        scanButton.setOnClickListener(view -> {
//
//
//            if (ContextCompat.checkSelfPermission( computerinformation.this, Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_GRANTED) {
//                launchQRCodeScanner();
//            } else {
//                ActivityCompat.requestPermissions( computerinformation.this,
//                        new String[]{Manifest.permission.CAMERA},
//                        CAMERA_PERMISSION_REQUEST_CODE);
//            }
//        });
//
//    }
//
//    private void launchQRCodeScanner() {
//        scannerView = new ZXingScannerView(this);
//        setContentView(scannerView);
//        scannerView.setResultHandler(this);
//        scannerView.startCamera();
//
//    }
//
//    @Override
//    public void handleResult(com.google.zxing.Result result) {
//        String qrData = result.getText();
//        String ids = idEditText.getText().toString();
//
//        scannerView.stopCamera();
//        setContentView(R.layout.activity_comp);
//
//        saveUserData(ids, qrData);
//
//    }
//
//    private void saveUserData(String id, String qrData) {
//        UserData userData = new UserData(id, qrData);
//
//        myRef.child(id).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText( computerinformation.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText( computerinformation.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
//                    Log.e("Firebase", "Error saving user data", task.getException());
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (scannerView != null) {
//            scannerView.stopCamera();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (scannerView != null) {
//            scannerView.startCamera();
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchQRCodeScanner();
//            } else {
//                Toast.makeText( this, "Camera permission required to scan QR code", Toast.LENGTH_SHORT ).show();
//            }
//        }
//
//    }
//}