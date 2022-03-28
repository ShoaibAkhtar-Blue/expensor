package blue.project.expensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView sinceTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userNameTextView = findViewById(R.id.textView_userName);
        userEmailTextView = findViewById(R.id.textView_userEmail);
        sinceTextView = findViewById(R.id.textView_since);
        backButton = findViewById(R.id.button_back_UserProfile);

        this.setTitle("User Profile");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "name");
        String email = sharedPreferences.getString("email", "email");
        String since = sharedPreferences.getString("since", "since");

        userNameTextView.setText(name);
        userEmailTextView.setText(email);
        sinceTextView.setText(since);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}