package com.ontech.com.parkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import www.sanju.motiontoast.MotionToast;

public class EntryActivity extends AppCompatActivity {

    SurroundCardView user1, facilitator;
    Animation animFadein, fade_out;
    LinearLayout next, name, number;
    TextView textView, get_Otp, textView2, textView3, verify,textView1;
    View login_layout, otp_verify;
    int val = 0;
    FirebaseUser user;
    String DeviceToken;
    private String verificationId;
    private FirebaseAuth mAuth;
    int downspeed;
    int upspeed;
    ImageView back,back2;
    EditText edtPhone, edtName;
    PinView pinView;
    DatabaseReference user_reference, facilitator_reference;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        setStatusBarTransparent();


        edtName = findViewById(R.id.name_edt);
        user1 = findViewById(R.id.user);
        facilitator = findViewById(R.id.facilitator);
        next = findViewById(R.id.conti);
        back = findViewById(R.id.back_img);
        textView = findViewById(R.id.textView);
        login_layout = findViewById(R.id.yb);
        mAuth = FirebaseAuth.getInstance();
        otp_verify = findViewById(R.id.otp);
        name = findViewById(R.id.name);
        number = findViewById(R.id.layoutSearch);
        textView2 = findViewById(R.id.textView19);
        textView1 = findViewById(R.id.textView20);
        verify = findViewById(R.id.verify);
        textView3 = findViewById(R.id.textView25);
        user_reference = FirebaseDatabase.getInstance().getReference().child("users");
        facilitator_reference = FirebaseDatabase.getInstance().getReference().child("facilitator");
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);

        edtPhone = findViewById(R.id.phone_number);
        pinView = findViewById(R.id.pin_view);
        back2 = findViewById(R.id.back_img2);
        get_Otp = findViewById(R.id.get_otp);


        getting_device_token();

        //first page

        next.setOnClickListener(v -> {
            if (val == 0) {
                Toast.makeText(EntryActivity.this, "Please select anyone option above", Toast.LENGTH_SHORT).show();
            } else {
                offanimate(user1);
                offanimate(facilitator);
                offanimate(next);
                offanimate(textView);

                new Handler(Looper.myLooper()).postDelayed(() -> {
                    login_layout.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    login_layout.startAnimation(animFadein);
                }, 600);
            }
        });

        user1.setOnClickListener(v -> {
            if (!user1.isCardSurrounded()) {
                user1.setSurroundStrokeWidth(R.dimen.width_card);
                user1.surround();
                facilitator.release();
                val = 1;
            }
        });

        facilitator.setOnClickListener(v -> {
            if (!facilitator.isCardSurrounded()) {
                facilitator.setSurroundStrokeWidth(R.dimen.width_card);
                facilitator.surround();
                user1.release();
                val = 2;
            }
        });

        back.setOnClickListener(v->{
            login_layout.startAnimation(fade_out);
            login_layout.setVisibility(View.GONE);
            onAnimate(user1);
            onAnimate(facilitator);
            onAnimate(next);
            onAnimate(textView);
            back.setVisibility(View.GONE);
        });


        //second page
        get_Otp.setOnClickListener(v -> {
            if ((edtPhone.getText().toString().length() == 10) && (edtName.getText().toString().length() != 0 ) ) {
                offanimate(name);
                offanimate(number);
                offanimate(textView2);
                offanimate(get_Otp);
                offanimate(back);
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    otp_verify.setVisibility(View.VISIBLE);
                    back2.setVisibility(View.VISIBLE);
                    otp_verify.startAnimation(animFadein);

                }, 600);
                textView3.setText("Successfully sent a verification code on " + edtPhone.getText().toString());
                String phone = "+91" + edtPhone.getText().toString();
                sendVerificationCode(phone);
                Toast.makeText(this, "Please wait while we process.", Toast.LENGTH_SHORT).show();

            } else {
                if (edtPhone.getText().toString().length() < 10)
                    Toast.makeText(EntryActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                else if (edtName.getText().toString().length() == 0)
                    Toast.makeText(EntryActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            }
        });

        back2.setOnClickListener(v->{
            otp_verify.startAnimation(fade_out);
            otp_verify.setVisibility(View.GONE);
            onAnimate(name);
            onAnimate(number);
            onAnimate(textView2);
            onAnimate(get_Otp);
            onAnimate(back);
            back2.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        });


        //third page

        verify.setOnClickListener(v-> {
         /*   if (Objects.requireNonNull(pinView.getText()).toString().trim().length() == 6) {
                String otp_text = Objects.requireNonNull(pinView.getText()).toString().trim();
                Log.e("pinView", "==========");
                verifyCode(otp_text);
            } else {
                Toast.makeText(this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
            }
*/
            if (TextUtils.isEmpty(pinView.getText().toString())) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(EntryActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                verifyCode(pinView.getText().toString());
            }

        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ch=s+"";
                if(ch.length()==6){
                    String otp_text= Objects.requireNonNull(pinView.getText()).toString().trim();
                    Log.e("pinView","==========");
                    verifyCode(otp_text);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                pinView.setText(code);
                Log.e("inside code block","==========");
                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.e("error",e+"");
        }
    };
    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Log.e("task successfull", "Success");
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            if (val == 1){
                                user_reference.child(user.getUid()).child("name").setValue(edtName.getText().toString());
                                user_reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                                user_reference.child(user.getUid()).child("token").setValue(DeviceToken);
                                user_reference.child(user.getUid()).child("uid").setValue(user.getUid());
                                sendToMain();
                            }
                            if (val == 2){
                                facilitator_reference.child(user.getUid()).child("name").setValue(edtName.getText().toString());
                                facilitator_reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                                facilitator_reference.child(user.getUid()).child("token").setValue(DeviceToken);
                                facilitator_reference.child(user.getUid()).child("uid").setValue(user.getUid());
                                sendToMain();
                            }
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.e("task result", task.getException().getMessage());
                            pinView.setError("Wrong Pin");
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /*private void signIn(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();
                assert user != null;
                if (val == 1){
                    user_reference.child(user.getUid()).child("name").setValue(edtName.getText().toString());
                    user_reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                    user_reference.child(user.getUid()).child("token").setValue(DeviceToken);
                    user_reference.child(user.getUid()).child("uid").setValue(user.getUid());
                    sendToMain();
                }
                if (val == 2){
                    facilitator_reference.child(user.getUid()).child("name").setValue(edtName.getText().toString());
                    facilitator_reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                    facilitator_reference.child(user.getUid()).child("token").setValue(DeviceToken);
                    facilitator_reference.child(user.getUid()).child("uid").setValue(user.getUid());
                    sendToMain();
                }

            } else {
                MotionToast.Companion.darkColorToast(EntryActivity.this,
                        "Failed ☹️",
                        "Verification Failed!!",
                        MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(EntryActivity.this, R.font.lexend));
            }
        });
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null){
            Home_gateway();
        }
    }
    private void Home_gateway() {
        Intent mainIntent = new Intent(EntryActivity.this , MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void sendToMain(){
        //todo: yaha pe change krna hai as per use
        getSharedPreferences("is_first_membership_payment_01", Context.MODE_PRIVATE).edit()
                .putString("0_or_1_first_payment","").apply();
        startActivity(new Intent(EntryActivity.this , MainActivity.class));
        finish();
    }

    private void setStatusBarTransparent() {
        Window window = EntryActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    void offanimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",-800f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",0);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }
    void onAnimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",0f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",100);
        alpha2.setDuration(500);
        AnimatorSet animset =new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }

    private void getting_device_token() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if(nc!=null) {
            downspeed = nc.getLinkDownstreamBandwidthKbps()/1000;
            upspeed = nc.getLinkUpstreamBandwidthKbps()/1000;
        }else{
            downspeed=0;
            upspeed=0;
        }

        if((upspeed!=0 && downspeed!=0) || getWifiLevel()!=0) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                if (!TextUtils.isEmpty(token)) {
                    Log.d("token", "retrieve token successful : " + token);
                } else {
                    Log.w("token121", "token should not be null...");
                }
            }).addOnFailureListener(e -> {
                //handle e
            }).addOnCanceledListener(() -> {
                //handle cancel
            }).addOnCompleteListener(task ->
            {
                try {
                    DeviceToken = task.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public int getWifiLevel()
    {
        try {
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            return WifiManager.calculateSignalLevel(linkSpeed, 5);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}