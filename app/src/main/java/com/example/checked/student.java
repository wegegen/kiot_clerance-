package com.example.checked;
import com.android.volley.Request;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class student extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button logout, searchMe, checkComp;
    private DatabaseReference mDatabase;
    private EditText myIdTxt, myInfoTxtEdy, dormInfoEditText, libraInfoEditText, caffeInfoEditText, securInfoEditText, idInfoEditText, blockInfoEditText, dormNumInfoEditText;
    private Button requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.logoutbutton);

        checkComp = findViewById(R.id.compcheck);
        checkComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(student.this, letmecheck.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(student.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(student.this, "Successfully logged out", Toast.LENGTH_LONG).show();
            }
        });

        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        dormInfoEditText = findViewById(R.id.DOR);
        libraInfoEditText = findViewById(R.id.libinfo);
        caffeInfoEditText = findViewById(R.id.cofeinfo);
        securInfoEditText = findViewById(R.id.SECURITIINFO);

        idInfoEditText = findViewById(R.id.idinfo);
        blockInfoEditText = findViewById(R.id.blockinfo);
        dormNumInfoEditText = findViewById(R.id.dormnuninfo);

        requestButton = findViewById(R.id.Request);

        // Set button onClickListener
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idInfoEditText.getText().toString();
                String dormNum = dormNumInfoEditText.getText().toString();
                String blockNumber = blockInfoEditText.getText().toString();

                String dormitory = dormInfoEditText.getText().toString();
                String library = libraInfoEditText.getText().toString();
                String caffe = caffeInfoEditText.getText().toString();
                String security = securInfoEditText.getText().toString();

                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(student.this, "ID is required", Toast.LENGTH_SHORT).show();
                } else {
                    String key = mDatabase.child("datas").push().getKey();
                    Map<String, Object> datas = new HashMap<>();
                    datas.put("Dormitory", dormitory);
                    datas.put("Library", library);
                    datas.put("Security", security);
                    datas.put("Caffe", caffe);
                    datas.put("Id", id);
                    datas.put("Dormnum", dormNum);
                    datas.put("Blocknumber", blockNumber);

                    mDatabase.child("datas").child(key).setValue(datas);
                    idInfoEditText.setText("");
                    dormNumInfoEditText.setText("");
                    blockInfoEditText.setText("");

                    dormInfoEditText.setText("");
                    libraInfoEditText.setText("");
                    caffeInfoEditText.setText("");
                    securInfoEditText.setText("");

                    Toast.makeText(student.this, "You have successfully requested", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myInfoTxtEdy = findViewById(R.id.myinfos);
        searchMe = findViewById(R.id.searchme);
        myIdTxt = findViewById(R.id.myiddd);

        searchMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchId = myIdTxt.getText().toString();

                mDatabase.child( "datas" ).orderByChild( "Id" ).equalTo(searchId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String myInfo = "";
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                myInfo += "Dormitory: " + ds.child("Dormitory").getValue() + "\n";
                                myInfo += "Library: " + ds.child("Library").getValue() + "\n";
                                myInfo += "Caffe: " + ds.child("Caffe").getValue() + "\n";
                                myInfo += "Security: " + ds.child("Security").getValue() + "\n";
                                myInfo += "Dorm Number: " + ds.child("Dormnum").getValue() + "\n";
                                myInfo += "Block Number: " + ds.child("Blocknumber").getValue() + "\n\n";
                            }
                            myInfoTxtEdy.setText(myInfo);
                        } else {
                            myInfoTxtEdy.setText("No data found for this ID");
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        myInfoTxtEdy.setText("Error fetching data");
                    }
                });
            }
        });
    }
}





























//public class student extends AppCompatActivity {
//  //  private BiometricPrompt biometricPrompt;
//    //private BiometricPrompt.PromptInfo promptInfo;
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//    private Button logout,searchme ,checkcomp;
//    private DatabaseReference mDatabase;
//
//   // private CheckBox dormCheckBox, libraryCheckBox, caffeCheckBox, securityCheckBox;
//    private EditText myidtxt,myinfotxtedy,dormInfoEditText, libraInfoEditText, caffeInfoEditText, securInfoEditText, idInfoEditText, blockInfoEditText, dormNumInfoEditText;
//    private Button requestButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_student );
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        logout = findViewById( R.id.logoutbutton );
//
//
//
//        checkcomp=findViewById( R.id.compcheck );
//        checkcomp.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(student.this,letmecheck.class);
//                startActivity( intent );
//            }
//        } );
//
//
//        logout.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                Intent intent = new Intent( student.this, MainActivity.class );
//                startActivity( intent );
//                Toast.makeText( student.this, "successfully logged out", Toast.LENGTH_LONG ).show();
//            }
//        } );
//
//
//        // Initialize Firebase database
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        dormInfoEditText = findViewById( R.id.DOR );
//        libraInfoEditText = findViewById( R.id.libinfo );
//        caffeInfoEditText = findViewById( R.id.cofeinfo );
//        securInfoEditText = findViewById( R.id.SECURITIINFO );
//
//
//        idInfoEditText = findViewById( R.id.idinfo );
//        blockInfoEditText = findViewById( R.id.blockinfo );
//        dormNumInfoEditText = findViewById( R.id.dormnuninfo );
//
//        requestButton = findViewById( R.id.Request );
//
//        // Set button onClickListener
//        requestButton.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String Id = idInfoEditText.getText().toString();
//                String Dormnum = dormNumInfoEditText.getText().toString();
//                String Blocknumber = blockInfoEditText.getText().toString();
//
//                String Dormitory = dormInfoEditText.getText().toString();
//                String Library = libraInfoEditText.getText().toString();
//                String Caffe = caffeInfoEditText.getText().toString();
//                String Security = securInfoEditText.getText().toString();
//                if (TextUtils.isEmpty(Id)) {
//                    Toast.makeText(student.this, "Id is required", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //  boolean Dormitory = dormCheckBox.isChecked();
//                    //boolean Library = libraryCheckBox.isChecked();
//                    // boolean Security = securityCheckBox.isChecked();
//                    //boolean Caffe = caffeCheckBox.isChecked();
//                    String key = mDatabase.child( "datas" ).push().getKey();
//                    Map<String, Object> datas = new HashMap<>();
//                    datas.put( "Dormitory", Dormitory );
//                    datas.put( "Library", Library );
//                    datas.put( "Security", Security );
//                    datas.put( "Caffe", Caffe );
//                    datas.put( "Id", Id );
//                    datas.put( "Dormnum", Dormnum );
//                    datas.put( "Blocknumber", Blocknumber );
//
////                }
//                    mDatabase.child( "datas" ).child( key ).setValue( datas );
//                    idInfoEditText.setText( "" );
//                    dormNumInfoEditText.setText( "" );
//                    blockInfoEditText.setText( "" );
//
//                    dormInfoEditText.setText( "" );
//                    libraInfoEditText.setText( "" );
//                    caffeInfoEditText.setText( "" );
//                    securInfoEditText.setText( "" );
//
//                    // dormCheckBox.setChecked( false );
//                    // libraryCheckBox.setChecked( false );
//                    //  securityCheckBox.setChecked( false );
//                    // caffeCheckBox.setChecked( false );
//
//                    Toast.makeText( student.this, "You are successfully requested", Toast.LENGTH_SHORT ).show();
//
//
//                }
//
//            }
//        } );
//
//
//
//        myinfotxtedy = findViewById( R.id.myinfos );
//        searchme = findViewById( R.id.searchme );
//        myidtxt = findViewById( R.id.myiddd );
//
//        searchme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchid = myidtxt.getText().toString();
////                mDatabase = FirebaseDatabase.getInstance().getReference("data").child(Id);
//               // mDatabase = FirebaseDatabase.getInstance().getReference("datas/" + Ids);
//
//                mDatabase.child("datas").orderByChild("Id").equalTo(searchid).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            StringBuilder TOTALINFORMATION = new StringBuilder();
//
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                String blocknum = dataSnapshot.child( "Blocknumber" ).getValue(String.class);
//                                String dormnum = dataSnapshot.child( "Dormnum" ).getValue(String.class);
//                                String dormitory = dataSnapshot.child("Dormitory").getValue(String.class);
//                                String library = dataSnapshot.child("Library").getValue(String.class);
//                                String security = dataSnapshot.child("Security").getValue(String.class);
//                                String caffe = dataSnapshot.child("Caffe").getValue(String.class);
//
//                                TOTALINFORMATION.append("Dormnum ").append(dormnum).append("\n")
//                                        .append("Blocknumber ").append(blocknum).append("\n")
//                                        .append("Dormitory: ").append(dormitory).append("\n")
//                                        .append("Library: ").append(library).append("\n")
//                                        .append("Security: ").append(security).append("\n")
//                                        .append("Caffe: ").append(caffe).append("\n");
//                            }
//
//                            myinfotxtedy.setText(TOTALINFORMATION.toString());
//                        } else {
//                            myinfotxtedy.setText("This Id is not requested ");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("student", "Database Error: " + error.getMessage());
//                    }
//                });
//            }
//        });
//
//    }
//}




