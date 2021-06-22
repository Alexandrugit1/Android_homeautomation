package com.example.homeautomation.AuthenticationPart;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.R;
import com.example.homeautomation.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityRegister extends AppCompatActivity
{
    private AuthenticationPartViewModel viewModel;
    private Retrofit retrofit;
    private JsonPlaceHolderAPI api;
    private TextView login;
    private Button registerButton;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText PIN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setVariables();
        setOnClickListeners();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setVariables()
    {
        viewModel = new ViewModelProvider(this).get(AuthenticationPartViewModel.class);
        retrofit = new Retrofit.Builder()
                .baseUrl(GlobalVariables.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderAPI.class);
        login = findViewById(R.id.registerLogin);
        registerButton = findViewById(R.id.registerButton);
        name = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        PIN = findViewById(R.id.registerPIN);
    }

    private void setOnClickListeners()
    {
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String userName = String.valueOf(name.getText()).trim();
                String userEmail = String.valueOf(email.getText()).trim();
                String userPassword = String.valueOf(password.getText());
                String userPIN = String.valueOf(PIN.getText()).trim();

                if(validate(userName, userEmail, userPassword, userPIN))
                    registerUser(userName, userEmail, userPassword, userPIN);
                else showMessage("Registration failed");
            }
        });
    }

    private boolean validate(String name, String email, String password, String PIN)
    {
        boolean isValid = false;

        if(nameIsValid(name)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length() >= viewModel.getMinimumNumberOfPasswordCharacters()
                && pinIsValid(PIN)
        )
            isValid = true;
        else if(name.isEmpty()
                && email.isEmpty() &&
                password.isEmpty() &&
                PIN.isEmpty()) // daca niciun input nu contine vreun caracter
            showMessage("No input contains characters");
        else if(name.isEmpty()) // daca numele nu contine niciun caracter
            showMessage("Name should not be empty");
        else if(email.isEmpty()) // daca email-ul nu contine niciun caracter
            showMessage("Email should not be empty");
        else if(password.isEmpty()) // daca parola nu contine niciun caracter
            showMessage("Password should not be empty");
        else if(PIN.isEmpty()) // daca parola nu contine niciun caracter
            showMessage("PIN should not be empty");
        else showMessage("Please complete all the inputs");

        return isValid;
    }

    private boolean nameIsValid(String name)
    {
        boolean isValid = true;
        int numberOfCharacters = name.length();
        int numberOfWords = name.split(" ").length;

        if(numberOfCharacters < viewModel.getMinimumNumberOfNameCharacters())
            isValid = false;
        else if(numberOfWords < 2)
            isValid = false;
        else for(char character : name.toCharArray())
            // if the character is not ' ', '-' nor letter
            if(!(character == 32 || character == 45 || character >= 65 && character <= 90 || character >= 97 && character <= 122))
            {
                isValid = false;
                break;
            }

        return isValid;
    }

    private boolean pinIsValid(String PIN)
    {
        boolean isValid = true;
        int currentIndex = -1;

        if(PIN.length() != viewModel.getNumberOfPINCharacters())
            isValid = false;
        else for(char digit : PIN.toCharArray())
        {
            ++currentIndex;

            if(currentIndex == 0 && digit != '1' && digit != '2' && digit != '5' && digit != '6')
            {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("HardwareIds")
    private String getDeviceIMEI()
    {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void registerUser(String userName, String userEmail, String userPassword, String userPIN)
    {
        final UserDetails details = new UserDetails(
                userName,
                userEmail,
                userPassword,
                userPIN,
                '0',
                '0'
        );

        details.setUserIMEI(null);

        Call<String> call = api.registerUser(
                details.getUserName(),
                details.getUserEmail(),
                details.getUserPassword(),
                details.getUserPIN()
        );

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(@NonNull Call<String > call, @NonNull Response<String> response)
            {
                if(!response.isSuccessful())
                {
                    showMessage("Registration failed");
                    return;
                }

                String responseString = response.body();

                if(responseString != null)
                {
                    if(responseString.equals("ok"))
                    {
                        showMessage("Register successful");
                        setUserIMEI(details);
                        login.performClick();
                    }
                    else showMessage("The user already exists");
                }
                else showMessage("Registration failed");
            }

            @Override
            public void onFailure(@NonNull Call<String > call, @NonNull Throwable t)
            {
                Toast.makeText(ActivityRegister.this, "Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                        Toast.makeText(ActivityRegister.this, "response is not successful, code: " + setImeiResponse.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String setImeiResponseString = setImeiResponse.body();

                    if(setImeiResponseString != null)
                        Log.d("setIMEI", setImeiResponseString);
                }

                @Override
                public void onFailure(@NonNull Call<String > call, @NonNull Throwable t)
                {
                    Toast.makeText(ActivityRegister.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}