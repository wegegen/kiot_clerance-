package com.example.checked;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button logout ,compreg ,staffreqest;
      private TableLayout tableLayout;
    private static final String TAG = "checked";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logout = findViewById( R.id.logoutbutton );
        staffreqest =findViewById( R.id.staffrequest );
        staffreqest.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin.this,staffreqestinformation.class);
                startActivity( intent );
            }
        } );
        logout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent( admin.this, MainActivity.class );
                startActivity( intent );
                Toast.makeText( admin.this, "successfully logged out", Toast.LENGTH_LONG ).show();
            }
        } );

        tableLayout = findViewById( R.id.tabled );
        FirebaseApp.initializeApp( this );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("datas");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear existing data in the table
                tableLayout.removeAllViews();

                // Create a header row for the table
                TableRow headerRow = new TableRow(getApplicationContext());
                headerRow.setBackgroundColor( Color.parseColor("#808080"));

                TextView idHeader = new TextView(getApplicationContext());
                idHeader.setText("Id_No");
                idHeader.setTextColor(Color.WHITE);
                headerRow.addView(idHeader);
                TextView blockNumHeader = new TextView(getApplicationContext());
                blockNumHeader.setText("Block_No");
                blockNumHeader.setTextColor(Color.WHITE);
                headerRow.addView(blockNumHeader);

                TextView dormNumHeader = new TextView(getApplicationContext());
                dormNumHeader.setText("Dorm_No");
                dormNumHeader.setTextColor(Color.WHITE);
                headerRow.addView(dormNumHeader);


                TextView dormitoryHeader = new TextView(getApplicationContext());
                dormitoryHeader.setText("DormReqest");
                dormitoryHeader.setTextColor(Color.WHITE);
                headerRow.addView(dormitoryHeader);

                TextView libraryHeader = new TextView(getApplicationContext());
                libraryHeader.setText("LibReqest");
                libraryHeader.setTextColor(Color.WHITE);
                headerRow.addView(libraryHeader);

                TextView caffeHeader = new TextView(getApplicationContext());
                caffeHeader.setText("CafReqest");
                caffeHeader.setTextColor(Color.WHITE);
                headerRow.addView(caffeHeader);

                TextView securityHeader = new TextView(getApplicationContext());
                securityHeader.setText("SecReqest");
                securityHeader.setTextColor(Color.WHITE);
                headerRow.addView(securityHeader);


                tableLayout.addView(headerRow);

                // Iterate through the data and add rows to the table
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
//                    HashMap<String,Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    String Dormitory = datas.child("Dormitory").getValue(String.class);
                    String Library = datas.child("Library").getValue(String.class);
                    String Caffe = datas.child("Caffe").getValue(String.class);
                    String security = datas.child("Security").getValue(String.class);

                    String id = datas.child("Id").getValue(String.class);
                    String dormNum = datas.child("Dormnum").getValue(String.class);
                    String blockNum = datas.child("Blocknumber").getValue(String.class);


                    TableRow tableRow = new TableRow(getApplicationContext());

                    TextView idValue = new TextView(getApplicationContext());
                    idValue.setText(id);
                    idValue.setPadding(4, 6, 4, 6);
                    tableRow.addView(idValue);

                    TextView blockNumValue = new TextView(getApplicationContext());
                    blockNumValue.setText(blockNum);
                    blockNumValue.setPadding(4, 6, 4, 6);
                    tableRow.addView(blockNumValue);

                    TextView dormNumValue = new TextView(getApplicationContext());
                    dormNumValue.setText(dormNum);
                    dormNumValue.setPadding(4, 6, 4, 6);
                    tableRow.addView(dormNumValue);


                    TextView column1 = new TextView(getApplicationContext());
                    column1.setText(Dormitory);
                    column1.setPadding(6, 6, 6, 6);
                    final DatabaseReference dormitoryRef = datas.getRef().child("Dormitory");
                    column1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView clickedCell = (TextView) view;
                            String originalText = clickedCell.getText().toString();
                            String updatedText = "";
                            if (!originalText.contains(":Cleared")) {
                                updatedText = originalText + ":Cleared";
                                clickedCell.setTextColor(Color.GREEN );

                            } else {
                                updatedText = originalText.replace(":Cleared", "");
                            }
                            clickedCell.setText(updatedText);
                            dormitoryRef.setValue(updatedText);
                        }
                    });
                    tableRow.addView(column1);


                    TextView column2 = new TextView(getApplicationContext());
                    column2.setText(Library);
                    column2.setPadding(6, 6, 6, 6);
                    final DatabaseReference libraryRef = datas.getRef().child("Library");
                    column2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView clickedCell = (TextView) view;
                            String originalText = clickedCell.getText().toString();
                            String updatedText = "";
                            if (!originalText.contains(":Cleared")) {
                                updatedText = originalText + ":Cleared";
                                clickedCell.setTextColor(Color.GREEN );
                            } else {
                                updatedText = originalText.replace(":Cleared", "");
                            }
                            clickedCell.setText(updatedText);
                            libraryRef.setValue(updatedText);
                        }
                    });
                    tableRow.addView(column2);

                    TextView column3 = new TextView(getApplicationContext());
                    column3.setText(Caffe);
                    column3.setPadding(6, 6, 6, 6);
                    final DatabaseReference CaffeRef = datas.getRef().child("Caffe");
                    column3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView clickedCell = (TextView) view;
                            String originalText = clickedCell.getText().toString();
                            String updatedText = "";
                            if (!originalText.contains(":Cleared")) {
                                updatedText = originalText + ":Cleared";
                                clickedCell.setTextColor(Color.WHITE );
                                clickedCell.setBackgroundColor( Color.GREEN );

                            } else {
                                updatedText = originalText.replace(":Cleared", "");
                            }
                            clickedCell.setText(updatedText);
                            CaffeRef.setValue(updatedText);
                        }
                    });
                    tableRow.addView(column3);

                    TextView column4 = new TextView(getApplicationContext());
                    column4.setText(security);
                    column4.setPadding(6, 6, 6, 6);
                    final DatabaseReference SecurityRef = datas.getRef().child("Security");
                    column4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView clickedCell = (TextView) view;
                            String originalText = clickedCell.getText().toString();
                            String updatedText = "";
                            if (!originalText.contains(":Cleared")) {
                                updatedText = originalText + ":Cleared";
                                clickedCell.setTextColor(Color.GREEN );
                            } else {
                                updatedText = originalText.replace(":Cleared", "");
                            }
                            clickedCell.setText(updatedText);
                            SecurityRef.setValue(updatedText);
                        }
                    });
                    tableRow.addView(column4);


                    tableLayout.addView(tableRow);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error occurred: " + databaseError.getMessage());
            }
        });
        compreg=  findViewById( R.id.computerreg );
        compreg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin.this,computerinformation.class);
                startActivity( intent );
            }
        } );

    }
}
