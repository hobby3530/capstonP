package com.example.capstonedesign1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d("mes","입력받은 메시지 : " + message);
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.text_url);
        textView.setText(message);

        Intent intent1 = new Intent(this, MainActivity.class);

        final Button view = (Button) findViewById(R.id.btn_webview);
        final Button prev = (Button) findViewById(R.id.btn_prev);

        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent1);
            }
        });


    }

    //    public void sendMessage(View view) {
//        database = openOrCreateDatabase("anti_url.db", MODE_PRIVATE, null);
//        Intent intent = new Intent(this, MessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.text_ur);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }


//    void start() {
//        setContentView(R.layout.activity_main);
//        final TextView url = (TextView) findViewById(R.id.text_ur);
//        final Button check1 = (Button) findViewById(R.id.btn_url);
//        final TextView messagebox = (TextView) findViewById(R.id.text_message);
//        final Button check2 = (Button) findViewById(R.id.btn_message);
//
////        check1.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View v) {
////                database = openOrCreateDatabase("anti_url.db", MODE_PRIVATE, null);
////                Log.d("db","createDatabase 호출됨.");
////                //executeQuery();
////                check();
////            }
////        });
//
//        check2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                check();
//            }
//        });
//
//    }
//
//    public void check() {
//        setContentView(R.layout.activity_check);
//        final TextView url = (TextView) findViewById(R.id.text_url);
//        final TextView messagebox = (TextView) findViewById(R.id.text_infor);
//        final Button view = (Button) findViewById(R.id.btn_webview);
//        final Button prev = (Button) findViewById(R.id.btn_prev);
//        //url.setText("https://www.nover.com/");
//        messagebox.setText("url 메시지 파싱 결과안내");
//
//        view.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { web_view(); }
//        });
//
//        prev.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { start(); }
//        });
//    }
//
//    public void web_view() {
//        setContentView(R.layout.activity_webview);
//        final ImageView iv = (ImageView) findViewById(R.id.iv_web);
//        final Button prev2 = (Button) findViewById(R.id.btn_prev2);
//
//        prev2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                check();
//            }
//        });
//    }

}
