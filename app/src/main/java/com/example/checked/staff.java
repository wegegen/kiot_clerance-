package com.example.checked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class staff extends AppCompatActivity {
      private TextView curdop;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText idstaff, idsearch, yourstatus,name,projectortxt,securitytxt,myinfotxtedy;
    private Button checkcomp,request,searchme;
    private Button logout;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_staff );
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        logout = findViewById( R.id.logoutbutton );


        idstaff = findViewById( R.id.idstaff );
        name =findViewById( R.id.name );
        yourstatus = findViewById( R.id.status );
        projectortxt = findViewById( R.id.projector );
        securitytxt = findViewById( R.id.security );

        idsearch = findViewById( R.id.myid );
        checkcomp = findViewById( R.id.compcheck );
        request = findViewById( R.id.Request );
        searchme = findViewById( R.id.searchme );
        myinfotxtedy = findViewById( R.id.myinfotxtedy );


        logout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(staff.this,MainActivity.class);
                startActivity( intent );
                Toast.makeText(staff.this,"successfully logedout" ,Toast.LENGTH_LONG ).show();

            }
        } );

        request.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Id = idstaff.getText().toString();
                String Full_Name = name.getText().toString();
                String Status = yourstatus.getText().toString();
                String Security = securitytxt.getText().toString();
                String Projector = projectortxt.getText().toString();
                if (TextUtils.isEmpty(Id)) {
                    Toast.makeText(staff.this, "Id is required", Toast.LENGTH_SHORT).show();
                }
                else {

                    String key = mDatabase.child( "staffrequest" ).push().getKey();
                    Map<String, Object> datas = new HashMap<>();
                    datas.put( "Id", Id );
                    datas.put( "Full_Name", Full_Name );
                    datas.put( "Status", Status );
                    datas.put( "Security", Security );
                    datas.put( "Projector", Projector );

//                }
                    mDatabase.child( "staffrequest" ).child( key ).setValue( datas );
                    idstaff.setText( "" );
                    name.setText( "" );
                    yourstatus.setText( "" );
                    projectortxt.setText( "" );
                    securitytxt.setText( "" );

                    Toast.makeText( staff.this, "Your are request success ", Toast.LENGTH_SHORT ).show();


                }

            }
        } );

        searchme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchid = idsearch.getText().toString();

                mDatabase.child("staffrequest").orderByChild("Id").equalTo(searchid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            StringBuilder TOTALINFORMATION = new StringBuilder();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String Id = dataSnapshot.child( "Id" ).getValue(String.class);
                                String fullname = dataSnapshot.child( "Full_Name" ).getValue(String.class);
                                String status = dataSnapshot.child("Status").getValue(String.class);
                                String projector = dataSnapshot.child("Projector").getValue(String.class);
                                String security = dataSnapshot.child("Security").getValue(String.class);


                                TOTALINFORMATION.append("ID: ").append(Id).append("\n")
                                        .append("Full Name ").append(fullname).append("\n")
                                        .append("Status/Work: ").append(status).append("\n")
                                        .append("Projector: ").append(projector).append("\n")
                                        .append("Security: ").append(security).append("\n");
                            }

                            myinfotxtedy.setText(TOTALINFORMATION.toString());
                        } else {
                            myinfotxtedy.setText("This Id is not requested ");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("staff", "Database Error: " + error.getMessage());
                    }
                });
            }
        });
        checkcomp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staff.this,staffcomputer.class);
                startActivity( intent );
            }
        } );
    }
}