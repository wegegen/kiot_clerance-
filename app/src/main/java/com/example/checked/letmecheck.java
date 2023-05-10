package com.example.checked;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;
import android.content.pm.PackageManager;
        import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class letmecheck extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private EditText idEditText;
    private ImageButton scanButton ,refreshbtn;
    private Button backButton;
    private DatabaseReference myRef;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int QR_CODE_SCAN_REQUEST_CODE = 200;

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_checme );

        idEditText = findViewById( R.id.id );
        scanButton = findViewById( R.id.scanner );
         backButton = findViewById(R.id.back);
         refreshbtn= findViewById( R.id.imagerefresh );


        myRef = FirebaseDatabase.getInstance().getReference( "computerinfo" );


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        scanButton.setOnClickListener( view -> {
            if (ContextCompat.checkSelfPermission( letmecheck.this, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED) {
                launchQRCodeScanner();
            } else {
                ActivityCompat.requestPermissions( letmecheck.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE );
            }
        } );

        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(letmecheck.this,student.class);
                startActivity( intent );
            }
        } );
    }

    private void launchQRCodeScanner() {
        scannerView = new ZXingScannerView( this );
        setContentView( scannerView );
        scannerView.setResultHandler( this );
        scannerView.startCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        String qrData = result.getText();
        String id = idEditText.getText().toString();

        scannerView.stopCamera();
        setContentView( R.layout.activity_comp );

        checkUserData( id, qrData );
    }

    private void checkUserData(String id, String qrData) {
        myRef.child( id ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String storedData = snapshot.child( "qrData" ).getValue( String.class );
                    if (qrData.equals( storedData )) {
                        Toast.makeText( letmecheck.this, "WELCOME YOU HAVE BEEN ALLOWED", Toast.LENGTH_SHORT ).show();
                    } else {
                        Toast.makeText( letmecheck.this, "SORRY YOU COULD NOT ALLOWED", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText( letmecheck.this, "ID not found", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "Firebase", "Error reading user data", error.toException() );
            }
        } );
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
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchQRCodeScanner();
            } else {
                Toast.makeText( this, "Camera permission is required to use the QR code scanner", Toast.LENGTH_SHORT ).show();
            }
        }
    }
}
