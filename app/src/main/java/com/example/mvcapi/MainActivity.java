package com.example.mvcapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail;
    private Button btnRegister, btnLogin;
    private RecyclerView rvUsers;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        rvUsers = findViewById(R.id.rvUsers);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> register());
        btnLogin.setOnClickListener(v -> login());
    }

    private void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();

        RegisterRequest request = new RegisterRequest(username, password, email);

        apiService.register(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Error: ", t);
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        LoginRequest request = new LoginRequest(username, password);

        apiService.login(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Error: ", t);
            }
        });
    }

    private void loadUsers() {
        apiService.getUsers().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //setupRecyclerView(response.body().getUsers());
                    UserResponse userResponse = response.body();
                    List<User> users = userResponse.getUsers();
                    if (users != null && !users.isEmpty()) {
                        setupRecyclerView(users);
                        // Add a log or toast to verify data
                        Log.d("API_SUCCESS", "Received " + users.size() + " users");
                    } else {
                        Toast.makeText(MainActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error body: " + errorBody);
                        Toast.makeText(MainActivity.this,
                                "Failed to load users: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error parsing error body", e);
                        Toast.makeText(MainActivity.this,
                                "Failed to load users: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load users!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<User> users) {
        UserAdapter adapter = new UserAdapter(users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);
    }
}