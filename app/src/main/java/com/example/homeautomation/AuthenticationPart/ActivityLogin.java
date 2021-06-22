package com.example.homeautomation.AuthenticationPart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.homeautomation.ConnectionPart.ActivityConnectAccessHome;
import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.R;
import com.example.homeautomation.UserDetails;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityLogin extends AppCompatActivity
{
    private SharedPreferences preferences;
    private Retrofit retrofit;
    private JsonPlaceHolderAPI api;
    private Button loginButton;
    private TextView register;
    private EditText email, password;
    private AuthenticationPartViewModel viewModel;
    private final String mainPackageName = "com.example.homeautomation";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_login);
        setVariables();
        setOnClickListeners();
    }

    private void setVariables()
    {
        preferences = getSharedPreferences("APP_USER", MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(AuthenticationPartViewModel.class);
        retrofit = new Retrofit.Builder().baseUrl(GlobalVariables.getBaseURL()).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(JsonPlaceHolderAPI.class);
        loginButton = findViewById(R.id.loginButton);
        register = findViewById(R.id.loginRegister);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
    }

    private void setOnClickListeners()
    {
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String userEmail = String.valueOf(email.getText()).trim();
                String userPassword = String.valueOf(password.getText());

                if(validate(userEmail, userPassword))
                    authenticateUser(userEmail, userPassword);
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToActivity(ActivityRegister.class);
            }
        });
    }

    private boolean validate(String email, String password)
    {
        boolean isValid = false;

        if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= viewModel.getMinimumNumberOfPasswordCharacters())
            isValid = true;
        else if(email.isEmpty() && password.isEmpty()) // daca atat email-ul, cat si parola nu contin niciun caracter
            showMessage("Email and password should not be empty");
        else if(email.isEmpty()) // daca email-ul nu contine niciun caracter
            showMessage("Email should not be empty");
        else if(password.isEmpty()) // daca parola nu contine niciun caracter
            showMessage("Password should not be empty");
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() < viewModel.getMinimumNumberOfPasswordCharacters()) // daca email-ul nu are forma valida si parola este prea scurta
            showMessage("Both email address and password are not valid");
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) // daca email-ul nu are forma valida, dar parola este in regula
            showMessage("Email address is not valid");
        else showMessage("Password should have at least " + viewModel.getMinimumNumberOfPasswordCharacters() + " characters"); // daca parola este prea scurta, dar email-ul este in regula

        return isValid;
    }

    private void goToActivity(Class<? extends Activity> activity)
    {
        final Intent intent=new Intent(ActivityLogin.this, activity);

        switch(activity.getName())
        {
            case mainPackageName + ".AuthenticationPart.ActivityRegister":
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case mainPackageName + ".ConnectionPart.ActivityConnectAccessHome":
                new CountDownTimer(250, 1000)
                {

                    @Override
                    public void onTick(long millisUntilFinished)
                    {

                    }

                    @Override
                    public void onFinish()
                    {
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }.start();
                break;
        }
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void authenticateUser(String userEmail, String userPassword)
    {
        Call<UserDetails> call = api.authenticateUser(userEmail, userPassword);

        call.enqueue(new Callback<UserDetails>()
        {
            @Override
            public void onResponse(@NonNull Call<UserDetails > call, @NonNull Response<UserDetails> response)
            {
                if(!response.isSuccessful())
                {
                    showMessage("Response is not successful, code: " + response.code());
                    return;
                }

                UserDetails currentUser = response.body();

                if(currentUser != null)
                {
                    saveUserToSharedPreferences(currentUser);

                    if(currentUser.getUserIMEI() == null)
                        setUserIMEI(currentUser);

                    showMessage("Login successful");
                    goToActivity(ActivityConnectAccessHome.class);
                }
                else showMessage("Incorrect credentials");
            }

            @Override
            public void onFailure(@NonNull Call<UserDetails > call, @NonNull Throwable t)
            {
                showMessage(t.getMessage());
            }
        });
    }

    private void saveUserToSharedPreferences(UserDetails user)
    {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString("currentUser", json);
        editor.apply();
    }

    @SuppressLint("HardwareIds")
    private String getDeviceIMEI()
    {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void setUserIMEI(UserDetails details)
    {
        String IMEI = getDeviceIMEI();

        if(IMEI != null)
        {
            Call<String> callSetIMEI = api.setUserIMEI(details.getUserPIN(), IMEI);

            callSetIMEI.enqueue(new Callback<String>()
            {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> setImeiResponse)
                {
                    if(!setImeiResponse.isSuccessful())
                    {
                        showMessage("Response is not successful, code: " + setImeiResponse.code());
                        return;
                    }

                    String setImeiResponseString = setImeiResponse.body();

                    if(setImeiResponseString != null)
                        Log.d("setIMEI", setImeiResponseString);
                }

                @Override
                public void onFailure(@NonNull Call<String > call, @NonNull Throwable t)
                {
                    showMessage(t.getMessage());
                }
            });
        }
    }
}