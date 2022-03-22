package com.example.crypto_pay_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    ImageView avatar;
    ImageButton editAvatar;
    ImageButton back;
    ImageButton qrCode;
    ImageButton logOut;
    TextView editInfo;
    TextView editContact;
    TextView name;

    LinearLayout locateFrame;
    LinearLayout birthFrame;
    LinearLayout genderFrame;
    LinearLayout statusFrame;
    LinearLayout occuFrame;
    LinearLayout eduFrame;
    LinearLayout homeFrame;
    LinearLayout addressFrame;
    LinearLayout mailFrame;

    TextView location;
    TextView birth;
    TextView gender;
    TextView status;
    TextView occu;
    TextView edu;
    TextView home;
    TextView address;
    TextView mail;

    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    EditAvatarActivity editAvatarActivity = new EditAvatarActivity();

//    public static final int MY_REQUEST_CODE = 10;
//    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == RESULT_OK){
//                        Intent intent = result.getData();
//                        if (intent == null){
//                            return;
//                        }
//                        Uri uri = intent.getData();
//
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                            editAvatarActivity.setBitmapImageView(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        creatUI();

        getInfo();

        initUI();
    }

    private void  creatUI(){
        avatar = findViewById(R.id.avatar);

        back = findViewById(R.id.back_icon);
        editAvatar = findViewById(R.id.edit_avatar);
        qrCode = findViewById(R.id.money_code);
        logOut = findViewById(R.id.log_out_button);
        editInfo = findViewById(R.id.to_edit_introduction);
        editContact = findViewById(R.id.to_edit_contact);
        name = findViewById(R.id.name);

        locateFrame = findViewById(R.id.locate_frame);
        birthFrame = findViewById(R.id.birth_frame);
        genderFrame = findViewById(R.id.gender_frame);
        statusFrame = findViewById(R.id.status_frame);
        occuFrame = findViewById(R.id.occu_frame);
        eduFrame = findViewById(R.id.edu_frame);
        homeFrame = findViewById(R.id.home_frame);
        addressFrame = findViewById(R.id.address_frame);
        mailFrame = findViewById(R.id.mail_frame);

        location = findViewById(R.id.city);
        birth = findViewById(R.id.birth_day);
        gender = findViewById(R.id.gender);
        status = findViewById(R.id.status);
        occu = findViewById(R.id.occupation);
        edu = findViewById(R.id.education);
        home = findViewById(R.id.home_town);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.email);

    }

    private void  getInfo(){
        Uri photoUrl = currentUser.getPhotoUrl();
        Glide.with(this).load(photoUrl).error(R.drawable.avatardefault).into(avatar);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    name.setText(String.valueOf(child.child("name").getValue()));

                    mail.setText(String.valueOf(child.child("mail").getValue()));
                    mailFrame.setVisibility(View.VISIBLE);

                    if(!String.valueOf(child.child("city").getValue()).equals("unknown")){
                        location.setText(String.valueOf(child.child("city").getValue()));
                        locateFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("birth").getValue()).equals("unknown")){
                        birth.setText(String.valueOf(child.child("birth").getValue()));
                        birthFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("gender").getValue()).equals("unknown")){
                        gender.setText(String.valueOf(child.child("gender").getValue()));
                        genderFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("status").getValue()).equals("unknown")){
                        status.setText(String.valueOf(child.child("status").getValue()));
                        statusFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("occupation").getValue()).equals("unknown")){
                        occu.setText(String.valueOf(child.child("occupation").getValue()));
                        occuFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("education").getValue()).equals("unknown")){
                        edu.setText(String.valueOf(child.child("education").getValue()));
                        eduFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("hometown").getValue()).equals("unknown")){
                        home.setText(String.valueOf(child.child("hometown").getValue()));
                        homeFrame.setVisibility(View.VISIBLE);
                    }

                    if(!String.valueOf(child.child("address").getValue()).equals("unknown")){
                        address.setText(String.valueOf(child.child("address").getValue()));
                        addressFrame.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditInfoActivity.class);
                startActivity(intent);
            }
        });

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditContactActivity.class);
                startActivity(intent);
            }
        });

        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,editAvatarActivity.getClass());
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_REQUEST_CODE){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openGallery();
//            }
//            else{
//                Toast.makeText(this,"Vui lòng cấp quyền truy cập thư viện ảnh",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void openGallery(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh đại diện"));
//    }

}