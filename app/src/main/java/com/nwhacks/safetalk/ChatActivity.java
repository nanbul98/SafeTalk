package com.nwhacks.safetalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Amanda on 2018-01-14.
 */

<<<<<<< HEAD
public class ChatActivity {

=======
public class ChatActivity extends AppCompatActivity {

    private String messageText;
    private String messageUser;
    private String messageUserId;
    private long messageTime;

    public ChatMessage (String messageText, String messageUser, String messageUserId) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
    }

    public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {

        private MainActivity activity;

        public MessageAdapter(MainActivity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
            this.activity = activity;
        }
        @Override
        protected void populateView(View v, ChatMessage model, int position) {
            TextView messageText = (TextView) v.findViewById(R.id.message_text);
            TextView messageUser = (TextView) v.findViewById(R.id.message_user);
            TextView messageTime = (TextView) v.findViewById(R.id.message_time);

            messageText.setText(model.getMessageText());
            messageUser.setText(model.getMessageUser());

            // Format the date before showing it
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
        }

    public ChatMessage() {

    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_box);

        displayChatMessages();
    }

    private void displayChatMessages() {

    }
    
>>>>>>> 985d94049a1f9811533088696cd061d7fef2a36f
}
