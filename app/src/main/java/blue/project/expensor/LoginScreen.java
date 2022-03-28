package blue.project.expensor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class LoginScreen extends AppCompatActivity {
    private final String LOG_TAG = "LoginScreen";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private String mPlatform;
    private static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Set activity title
        this.setTitle("LOG IN");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("p", "fb");
            startActivity(intent);
        }

        GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastAccount != null) {
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("p", "google");
            startActivity(intent);
        }

        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Facebook sign in
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPlatform = "fb";
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                        Log.i(LOG_TAG, graphResponse.toString());
                        try {
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String currentDate = simpleDateFormat.format(calendar.getTime());
                            Log.i(LOG_TAG, "Since: " + currentDate);

                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("id", id);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("since", currentDate);
                            editor.apply();

                            Intent intent = new Intent(LoginScreen.this, Dashboard.class);
                            intent.putExtra("p", mPlatform);
                            startActivity(intent);
                        } catch (JSONException jsonException) {
                            Log.e(LOG_TAG, "Exception in JSON");
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });

        // Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlatform = "google";
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(this);

            String userName = lastAccount.getDisplayName();
            String userEmail = lastAccount.getEmail();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = simpleDateFormat.format(calendar.getTime());

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("name", userName);
            editor.putString("email", userEmail);
            editor.putString("since", currentDate);
            editor.apply();

            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("p", mPlatform);
            startActivity(intent);
        } catch (ApiException e) {
            Log.e("signInResult:failed code=", e.getMessage());
        }
    }
}