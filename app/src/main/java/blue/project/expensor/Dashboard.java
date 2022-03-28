package blue.project.expensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import blue.project.expensor.data.PersonalExpensesDbHelper;

public class Dashboard extends AppCompatActivity {
    private TextView userNameTextView;
    private ProfilePictureView profilePictureView;
    private CardView addExpenseTypeCardView;
    private CardView expensesCardView;
    private CardView expenseDetailsCardView;
    private CardView userProfileCardView;
    private TextView totalExpensesTextView;
    private ImageView googleProfilePhotoImageView;
    private String mPlatform;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.setTitle("Dashboard");

        userNameTextView  = findViewById(R.id.textView_dashboard_userName);
        profilePictureView = findViewById(R.id.profilePictureView);
        addExpenseTypeCardView = findViewById(R.id.card_addExpenseType);
        expensesCardView = findViewById(R.id.card_expenses);
        expenseDetailsCardView = findViewById(R.id.card_expense_details);
        userProfileCardView = findViewById(R.id.card_user_profile);
        totalExpensesTextView = findViewById(R.id.total_expenses);
        googleProfilePhotoImageView = findViewById(R.id.imageView_google_profile_photo);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);

        userNameTextView.setText(name);

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mPlatform = bundle.getString("p");
            if (mPlatform.equals("fb")) {
                googleProfilePhotoImageView.setVisibility(View.INVISIBLE);
                profilePictureView.setVisibility(View.VISIBLE);
                String id = sharedPreferences.getString("id", null);
                try {
                    profilePictureView.setProfileId(id);
                } catch (Exception exception) {
                    Toast.makeText(this, "Error in Image URL", Toast.LENGTH_SHORT).show();
                }
            } else if (mPlatform.equals("google")) {
                profilePictureView.setVisibility(View.INVISIBLE);
                googleProfilePhotoImageView.setVisibility(View.VISIBLE);
                GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(this);
                Uri photoUri = lastAccount.getPhotoUrl();
                Glide.with(this).load(photoUri).into(googleProfilePhotoImageView);
            }
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        addExpenseTypeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ExpenseType.class);
                startActivity(intent);
            }
        });

        expensesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, RecordExpense.class);
                startActivity(intent);
            }
        });

        expenseDetailsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ExpenseDetails.class);
                startActivity(intent);
            }
        });

        userProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                startActivity(intent);
            }
        });

        PersonalExpensesDbHelper personalExpensesDbHelper = new PersonalExpensesDbHelper(this);
        double totalExpenses = personalExpensesDbHelper.getTotalExpenses(this);
        String msg = "Total Expenses are Rs: " + totalExpenses;
        totalExpensesTextView.setText(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PersonalExpensesDbHelper personalExpensesDbHelper = new PersonalExpensesDbHelper(this);
        double totalExpenses = personalExpensesDbHelper.getTotalExpenses(this);
        String msg = "Total Expenses are Rs: " + totalExpenses;
        totalExpensesTextView.setText(msg);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                if (mPlatform.equals("fb")) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(Dashboard.this, LoginScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                } else if (mPlatform.equals("google")) {
                    try {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(Dashboard.this, LoginScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}