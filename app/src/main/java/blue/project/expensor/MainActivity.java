package blue.project.expensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}