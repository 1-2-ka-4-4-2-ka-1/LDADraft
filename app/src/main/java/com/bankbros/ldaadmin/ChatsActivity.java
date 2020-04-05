package com.bankbros.ldaadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsActivity extends AppCompatActivity {

    private TextView chatTitle;
    private ImageView sendMessage;
    private EditText messageBox;
    private LinearLayout messagesAreaLayout;
    private ScrollView messageScrollView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private  DatabaseReference chatRef;
    private  DatabaseReference chatRefAdmin;

    private RegisteredUsersModel usersModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);


        chatTitle =findViewById(R.id.tv_chat_title);

        Intent i = getIntent();
         usersModel = (RegisteredUsersModel) i.getSerializableExtra("obj");


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        chatRef = databaseReference.child("users").child(usersModel.getUserId()).child("chats").getRef();
        chatRefAdmin = databaseReference.child("Admin").child("chats").child(usersModel.getUserId()+"_admin").getRef();


        sendMessage = findViewById(R.id.iv_send_message);
        messageBox = findViewById(R.id.ed_message_box);
        messagesAreaLayout = findViewById(R.id.messages_container);
        messageScrollView = findViewById(R.id.scrl_chat_scroll_view);

        setupClickListeners();
        messageScrollView.post(new Runnable() {
            public void run() {
                messageScrollView.fullScroll(messageScrollView.FOCUS_DOWN);
            }
        });
//        messageScrollView.scrollTo(0, messageScrollView.getTop());

    }


    public void setupClickListeners(){

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.child("Admin").child("NotificationTokens").child(usersModel.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot == null || dataSnapshot.getValue() == null) {
                            doSendMessage( messageBox.getText().toString());
                            return;
                        }

                        String token = dataSnapshot.getValue().toString();
                        Log.i("Token", "onDataChange: "+token);
                        MessagePostApi messagePostApi = MessagePostApi.retrofit.create(MessagePostApi.class);
                        Data newdata = new Data("New Message from Admin ",messageBox.getText().toString());
                        MessageApi messagePostApi1 = new MessageApi(newdata,token);
                        Call<String> call = messagePostApi.postMessage(messagePostApi1);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.i("Success", "onFailure: "+response.message()+response);

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i("Error", "onFailure: "+t.getMessage());
                            }
                        });

                        doSendMessage( messageBox.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });


        chatRefAdmin.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals("Admin")){
                    appendMessageUser("" + message, 2);
                }
                else{
                    appendMessageUser("" + message, 1);
                }

                Log.i("count-------", "onChildAdded: ");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void doSendMessage(String messageText){
        if(!messageText.equals("")){
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", messageText);
            map.put("user", "Admin");
            chatRef.push().setValue(map);
            chatRefAdmin.push().setValue(map);
            messageBox.setText("");
//            appendMessageUser("You:-\n"+messageText , 2);
        }

    }


    public void appendMessageUser(String message , int type){





        RelativeLayout relativeLayoutContainer = new RelativeLayout(ChatsActivity.this);
        RelativeLayout.LayoutParams paramsMain = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeLayoutContainer.setLayoutParams(paramsMain);


        RelativeLayout relativeLayout = new RelativeLayout(ChatsActivity.this);
        relativeLayout.setPadding(25,25,25,25);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        relativeLayout.setLayoutParams(params);

        TextView tempQuestionTextView = new TextView(ChatsActivity.this);
        tempQuestionTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tempQuestionTextView.setPadding(40,25,40,25);
        tempQuestionTextView.setTextSize(20);
        tempQuestionTextView.setMaxWidth(550);
        tempQuestionTextView.setTextColor(Color.BLACK);
        tempQuestionTextView.setText(message);
        relativeLayout.addView(tempQuestionTextView);

        if(type == 1) {
            relativeLayout.setBackgroundResource(R.drawable.chat_bubble_incoming);
            relativeLayoutContainer.setGravity(Gravity.LEFT);
        }
        else {
            relativeLayout.setBackgroundResource(R.drawable.chat_bubble_outgoing);
            relativeLayoutContainer.setGravity(Gravity.RIGHT);
        }

        relativeLayoutContainer.addView(relativeLayout);
        messagesAreaLayout.addView(relativeLayoutContainer);

//        messageScrollView.scrollTo(0, messageScrollView.getTop());
        messageScrollView.post(new Runnable() {
            public void run() {
                messageScrollView.fullScroll(messageScrollView.FOCUS_DOWN);
            }
        });
    }



}
