package com.example.funnychat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.funnychat.util.AssetsHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.funnychat.util.Hash.checkHashedString;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = findViewById(R.id.login);
        final EditText password = findViewById(R.id.password);
        final Button signInButton = findViewById(R.id.sign_in_button);

        final AssetsHandler assetsHandler = new AssetsHandler(this);

        signInButton.setOnClickListener(v -> {
            JSONObject usersObj = assetsHandler.parseJSONfile("users.json");
            if (usersObj != null) {
                try {
                    JSONArray usersArray = usersObj.getJSONArray("users");
                    JSONObject userFound = null;
                    for (int i = 0; i < usersArray.length(); i++) {
                        final JSONObject userJSon = usersArray.getJSONObject(i);
                        if (userJSon.getString("name").equals(login.getText().toString()) && !userJSon.getBoolean("technical")) {
                            userFound = userJSon;
                            break;
                        }
                    }
                    if (userFound == null) {
                        Toast toast = Toast.makeText(this.getApplicationContext(), R.string.user_not_found, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (checkHashedString(password.getText().toString(), userFound.getString("password"))) {
                            Intent intent = new Intent();
                            intent.putExtra("selfUserId", userFound.getInt("id"));
                            setResult(RESULT_OK,intent);

                            //прячем клавиатуру
                            InputMethodManager imm = (InputMethodManager) this.getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            finish();
                        } else {
                            Toast toast = Toast.makeText(this.getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                } catch (JSONException e) {
                    Toast toast = Toast.makeText(this.getApplicationContext(), R.string.user_data_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}