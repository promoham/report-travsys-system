package mono.eagle.com.travsys;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import models.MyObjectBox;
import models.User;

public class WelcomeActivity extends Activity {
    public static BoxStore dbBox;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reportsRef = database.getReference("reports");
    DatabaseReference usersRef = database.getReference("users");
    DatabaseReference arsRef = database.getReference("ars");
    DatabaseReference problemsRef = database.getReference("problems");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        notification();
        dbBox = MyObjectBox.builder().androidContext(this).build();
        try {
            final Box<User> userBox = dbBox.boxFor(User.class);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userBox.removeAll();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        user.setId(0);
                        userBox.put(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception ignored) {
        }

        if (LocalData.get(WelcomeActivity.this, "userName").length() > 0) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }

    void notification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("thug", "Key: " + key + " Value: " + value);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("reports");

        String msg = getString(R.string.msg_subscribed);


        String token = FirebaseInstanceId.getInstance().getToken();
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String mssg = getString(R.string.msg_token_fmt, token);
        Log.v("token", mssg);


    }

    public void login(View view) {
        Box<User> userBox = dbBox.boxFor(User.class);
        List<User> users = userBox.getAll();
        String name = ((EditText) findViewById(R.id.txtUserName)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtCode)).getText().toString();
        if (users.size() < 1) {
            userBox.put(
                    new User() {{
                        setName("1");
                        setPassword("1");
                        setAdmin(true);
                    }}
            );
        }
        users = userBox.getAll();

        boolean found = false;
        long currentUserId = 0;
        for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                found = true;
                currentUserId = user.getId();
            }
        }

        if (found) {
            LocalData.add(WelcomeActivity.this, "userName", User.getNameById(currentUserId));
            LocalData.add(WelcomeActivity.this, "userIsAdmin", String.valueOf(User.getIsAdminById(currentUserId)));
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "wrong user name or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbBox = null;
        super.onDestroy();
    }
}