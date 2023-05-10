package com.example.checked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
    EditText idEditText, nameEditText, emailEditText, passwordEditText;
    Spinner statusSpinner, deptspinner;
    Button registerButton , Loginbtn;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Loginbtn = findViewById( R.id.loginbtn );
        idEditText = findViewById(R.id.id);
        nameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.emaileditText);
        passwordEditText = findViewById(R.id.password_edit_text);
        statusSpinner = findViewById(R.id.user_type_spinner);
        deptspinner = findViewById(R.id.department);
        registerButton = findViewById(R.id.register_button);


        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.usertype, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(this,
                R.array.deptname, android.R.layout.simple_spinner_item);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptspinner.setAdapter(deptAdapter);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids = idEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String status = statusSpinner.getSelectedItem().toString();
                String dept = deptspinner.getSelectedItem().toString();


                if (TextUtils.isEmpty(ids) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ) {
                    Toast.makeText(signup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();

                                        // Save user data to Realtime Database
                                        HashMap<String, String> userData = new HashMap<>();
                                        userData.put("id", ids);
                                        userData.put("name", name);
                                        userData.put("email", email);
                                        userData.put("status", status);
                                        userData.put("department", dept);


                                        DatabaseReference userRef = mDatabase.child("users").child(userId);
                                        userRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(signup.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(signup.this, "Registration faileds", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}













//fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
//        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//
//
//        // Check if the device has a fingerprint sensor
//        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected()) {
//        // Device does not have a fingerprint sensor, display error message
//        Toast.makeText(this, "Fingerprint sensor not available", Toast.LENGTH_SHORT).show();
//        return;
//        }
//
//        // Check if the user has enrolled at least one fingerprint
//        if (!fingerprintManager.hasEnrolledFingerprints()) {
//        // User has not enrolled any fingerprints, display error message
//        Toast.makeText(this, "No fingerprints enrolled", Toast.LENGTH_SHORT).show();
//        return;
//        }
//
////        // Check if the user has secured their device with a lock screen
////        if (!keyguardManager.isKeyguardSecure()) {
////        // User has not secured their device with a lock screen, display error message
////        Toast.makeText(this, "Lock screen not set up", Toast.LENGTH_SHORT).show();
////        return;
////        }
//
//        // Register the user's fingerprint
//        FingerprintManager.EnrollmentCallback enrollmentCallback = new FingerprintManager.EnrollmentCallback() {
//@Override
//public void onEnrollmentProgress(int remaining) {
//        // Enrollment is in progress, display progress message
//        Toast.makeText(RegisterActivity.this, "Enrolling fingerprint... " + remaining + " remaining", Toast.LENGTH_SHORT).show();
//        }
//
//@Override
//public void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
//        // Enrollment needs help, display help message
//        Toast.makeText(RegisterActivity.this, "Enrollment help: " + helpString, Toast.LENGTH_SHORT).show();
//        }
//
//@Override
//public void onEnrollmentError(int errMsgId, CharSequence errString) {
//        // Enrollment encountered an error, display error message
//        Toast.makeText(RegisterActivity.this, "Enrollment error: " + errString, Toast.LENGTH_SHORT).show();
//        }
//
//@Override
//public void onEnrollmentComplete() {
//        // Enrollment is complete, retrieve the user's fingerprint data and save it to the database
//        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(new Cipher()); // TODO: Replace with actual encryption cipher
//        FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
//@Override
//public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//        // User's fingerprint has been authenticated, save the user's fingerprint data to the database
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("userss").child("fingerprintData");
//        databaseReference.setValue(result.getCryptoObject().getCipher().getIV()); // TODO: Save the actual fingerprint data
//        }
//
//@Override
//public void onAuthenticationFailed() {
//        // User's fingerprint could not be authenticated, display error message
//        Toast.makeText(RegisterActivity.this, "Fingerprint authentication failed", Toast.LENGTH_SHORT).show();
//        }
//        };
//        fingerprintManager.authenticate(cryptoObject, null, 0, authenticationCallback, null);
//        }
//        };
//        fingerprintManager.enroll(null, null, 0, enrollmentCallback, null);
//



//
