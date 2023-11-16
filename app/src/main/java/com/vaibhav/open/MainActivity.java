package com.vaibhav.open;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vaibhav.open.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public Context mContext;
    DrAdb adb;
    private TextView result;
    private EditText ent_cmd;
    private ActivityMainBinding binding;
    private String TAG = "KEY Typed : ";
    private String backward_cmd[] = new String[10];

    public static void sleep(long milisec) throws InterruptedException {//debug purpose only
        Thread.sleep(milisec);
    }

    public static String check(@NonNull String chk_str) { // for checking invalid characters
        boolean found = false;
        //char abc[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '\'', '[', ']', '&', ' ', '%', '!', '=', '+', '_', '-', '*', '/', '^', '`', '~', '>', '<', '|', ':', ';', ','};
        for (char d : chk_str.toCharArray()) {
            switch (d) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'Q':
                case 'P':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'j':
                case 'i':
                case 'k':
                case 'g':
                case 'h':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case '\'':
                case '[':
                case ']':
                case '&':
                case ' ':
                case '%':
                case '!':
                case '=':
                case '+':
                case '_':
                case '-':
                case '*':
                case '/':
                case '^':
                case '`':
                case '~':
                case '>':
                case '<':
                case '|':
                case ':':
                case ';':
                case '.':
                case ',':
                    //                   found_used += 1;
                    break;
                default:
                    found = true;
            }
        }
        if (!found) {
            return "NONE";
        } else {
            return "FOUND";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adb = new DrAdb(this);
        mContext = this;

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitAll()
                .detectDiskWrites()
                .penaltyLog()
                .build()); //allows network operations on mainThread, without this it would give networkOnMainThreadException

        result = binding.textV;//find command result variable
        ent_cmd = binding.command;//find textedit command entering field
        Button R_command = binding.RCommand; //find button

        ent_cmd.setOnKeyListener((v, keyCode, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                update();
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                ent_cmd.setText("");
            }
            return true;
        });//set onkey listner for enter key and backspace(delete) key
        R_command.setOnClickListener(v -> {//set onclick listner to get entered command and pass to DrAdb class
            if (ent_cmd.getText().toString().length() > 0) {//check if command is more than 0 characters
                update();//call update method
            } else {//if command is not more than 0 characters
                result.setText(getString(R.string.ent_more));
            }
        });
    }

    public void update() {
        String cmd = ent_cmd.getText().toString().replaceAll("\\P{ASCII}", "");
        if (check(cmd).equals("NONE")) {
            backward_cmd[0] = cmd;
            String result_cmd = adb.Commander(cmd);
            result.setText(result_cmd);
//            Toast.makeText(mContext,result_cmd,Toast.LENGTH_LONG).show(); for debug purpose
        } else {
            result.setText(getString(R.string.invalid_cmd));
        }

    }

    public void ClearCommand(View v) { //clear textedit field to enter new command by ontouch to textedit element
        if (v.equals(ent_cmd)) {
            ent_cmd.setText("");
        }
    }
}