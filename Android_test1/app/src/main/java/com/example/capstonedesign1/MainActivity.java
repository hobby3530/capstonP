package com.example.capstonedesign1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;

import static java.sql.DriverManager.println;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.capstonedesign1.MESSAGE";
    private Context mContext = null;
    SQLiteDatabase database;

    int flag = 0;
    int warnid = 0;
    static String message = null;
    static String sms = null;
    static String finalurl = null;
    static String search_message = null;
    static String sms_report1 = null;
    static String sms_report2 = null;
    String [] warn = {"*** !!위험합니다!! ***", "** !!주의하세요!! **", "* !!의심해보세요!! *"};
    String warn1 = null;
    String warn2 = null;
    int minLDA1 = 0;
    int minLDA2 = 0;
    int sameid1 = 0;
    int sameid2 = 0;
    double persent1 = 0;
    double persent2 = 0;
    String per1 = null;
    String per2 = null;
    String sameurl1 = null;
    String sameurl2 = null;
    String tmp_url = null;
    String[] s = null;
    char [] alp_start_ch = {'.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '-'};
    int [] alp_start_safe = {-2, -2, 857, 5874, 8511, 11129, 12779, 14779, 15619, 16805, 17786,
            19473, 87853, 141166, 209115, 251820, 291999, 332867, 370629, 405209, 443291,
            461551, 490159, 525500, 597118, 629111, 652273, 708922, 714360, 748777, 838373,
            903099, 919683, 942708, 969953, 979193, 990082, 999998};
    int [] alp_start_mal = {-2, 14, 67, 1332, 1629, 1789, 2007, 2153, 2258, 2329, 2451,
            2576, 4558, 5805, 7151, 8289, 9059, 9797, 10457, 11069, 11700,
            12009, 12434, 13082, 14605, 15133, 15534, 16511, 16597, 17327, 19088,
            20017, 20285, 20668, 21323, 21456, 21605, 21753};
    String [] extension_name = {".aac", ".adt", ".adts", ".accdb", ".accde", ".accdr", ".accdt", ".aif", ".aifc",
            ".aiff", ".aspx", ".avi", ".bat", ".bin", ".bmp", ".cab", ".cda", ".csv", ".dif", ".dll", ".doc", ".docm",
            ".docx", ".dot", ".dotx", ".eml", ".eps", ".exe", ".flv", ".gif", ".htm", ".html", ".ini", ".iso", ".jar",
            ".jpg", ".jpeg", ".m4a", ".mdb", ".mid", ".midi", ".mov", ".mp3", ".mp4", ".mpeg", ".mpg", ".msi", ".mui",
            ".PDF", ".pdf", ".png", ".pot", ".potm", ".potx", ".ppam", ".pps", ".ppsm", ".ppsx", ".ppt", ".pptm", ".pptx",
            ".psd", ".pst", ".pub", ".rar", ".rtf", ".sldm", ".sldx", ".swf", ".sys", ".tif", ".tiff", ".tmp", ".txt", ".vob",
            ".vsd", ".vsdm", ".vsdx", ".vss", ".vssm", ".vst", ".vstm", ".vstx", ".wav", ".wbk", ".wks", ".wma", ".wmd",
            ".wmv", ".wmz", ".wms", ".wpd", ".wp5", ".xla", ".xlam", ".xll", ".xlm", ".xls", ".xlsm", ".xlsx", ".xlt",
            ".xltm", ".xltx", ".xps"};
    String [] strange_sms = {"[국외발신]", "[국외 발신]", "[해외발신]", "[해외 발신]", "[국제발신]", "[국제 발신]", "(광고)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();

        try {
            // 1번 isCheckDB 함수 : DB가 있는지 확인
            boolean bResult = isCheckDB();    // DB가 있는지?
            Log.d("MiniApp", "DB Check=" + bResult);
            if (!bResult) {    // DB가 없으면 복사
                // 2번 copyDB 함수 : DB를 local에서 device로 복사
                copyDB(this);
            } else {

            }
        } catch (Exception e) {
        }
    }

    public boolean isCheckDB(){
        String filePath = "/data/data/com.example.capstonedesign1/databases/anti_url.db";
        File file = new File(filePath);
        if (file.exists()) {
            Log.d("db","데이터베이스가 존재함 true" ); // debug
            return true;
        }
        println("데이터베이스가 없음 false" ); // debug
        return false;
    }

    public void copyDB(Context mContext) {
        Log.d("MiniApp", "copyDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/com.example.capstonedesign1/databases";
        String filePath = "/data/data/com.example.capstonedesign1/databases/anti_url.db";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            Log.d("db","db복사시작");
            InputStream is = manager.open("url.db");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
                Log.d("db","폴더가있으면그냥넘어감");
            } else {
                Log.d("db","폴더가없어서만들어줌");
                folder.mkdirs();
            }

            if (file.exists()) {
                Log.d("db","파일이있어서삭제후재생성");
                file.delete();
                file.createNewFile();
            }
            Log.d("db","파일을 만들자");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.d("db","error나서 하다 못함//");
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }

    void start() {
        setContentView(R.layout.activity_main);
        final TextView editText = (TextView) findViewById(R.id.text_ur);
        //final EditText editText = (EditText) findViewById(R.id.text_ur);
        final Button check1 = (Button) findViewById(R.id.btn_url);
        final TextView messagebox = (TextView) findViewById(R.id.text_message);
        final Button check2 = (Button) findViewById(R.id.btn_message);

        check1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                database = openOrCreateDatabase("anti_url.db", MODE_PRIVATE, null);
                Log.d("db","createDatabase 호출됨.");
                //executeQuery();
                message = editText.getText().toString();
                Log.d("mes","message : " + message);
                Log.d("mes","message[0] : " + message.charAt(0));

                sms = messagebox.getText().toString();
                Log.d("mes","SMS : " + sms);
                if(!sms.equals(""))
                    search_mes();
                else
                    sms_report1 = "";

                get_final_url();
                if(finalurl != null)
                    search_safe();

                check();
            }
        });

        check2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                database = openOrCreateDatabase("anti_url.db", MODE_PRIVATE, null);
                Log.d("db","createDatabase 호출됨.");
                //executeQuery();
                message = editText.getText().toString();
                Log.d("mes","message : " + message);
                Log.d("mes","message[0] : " + message.charAt(0));

                sms = messagebox.getText().toString();
                Log.d("mes","SMS : " + sms);
                if(!sms.equals(""))
                    search_mes();
                else
                    sms_report1 = "";


                get_final_url();
                if(finalurl != null)
                    search_safe();

                check();
            }
        });

    }

    public void check() {
        setContentView(R.layout.activity_check);
        final TextView url = (TextView) findViewById(R.id.text_url);
        final TextView messagebox = (TextView) findViewById(R.id.text_infor);
        final Button view = (Button) findViewById(R.id.btn_webview);
        final Button prev = (Button) findViewById(R.id.btn_prev);
        final Button report = (Button) findViewById(R.id.btn_report);
        final ImageView warn = (ImageView) findViewById(R.id.iv_warn);
        final ImageView safe = (ImageView) findViewById(R.id.iv_safe);

        url.setText(message);
        messagebox.setText("<url 메시지 파싱 결과안내>\n\n" + search_message + sms_report1);

        if (persent1 + persent2 != 0 && persent1 < 65 && persent2 < 65)
            report.setVisibility(View.VISIBLE);

        if (warnid == 0)
            warn.setVisibility(View.VISIBLE);

        if (warnid == 1)
            safe.setVisibility(View.VISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //wv.loadUrl("file:///android_asset/naver.html");
                //get_view_html("https://www.google.com");
                new RealTask().execute();
                //web_view();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                persent1 = persent2 = 0;
                start(); }
        });

        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Report를 제출하였습니다!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 280);
                toast.show();
                submit_report();
            }
        });

    }

    public void web_view() {
        setContentView(R.layout.activity_webview);
        //final ImageView iv = (ImageView) findViewById(R.id.iv_web);
        final Button prev2 = (Button) findViewById(R.id.btn_prev2);
        final WebView wv = (WebView) findViewById(R.id.webview);

        wv.getSettings().setAllowContentAccess(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.loadUrl("file:///" + getExternalFilesDir(null) + "/htmlfile/testhtml.html");
        Log.d("html", wv.getUrl());

        wv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return true;
            }
        });

        database.close();

        prev2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                check();
            }
        });
    }

    public void get_final_url() {
        finalurl = null;
        warnid = 0;
        if(message.contains("https://")) {
            finalurl = message.split("://")[1].split("/")[0];
            String[] exefile = message.split("://")[1].split("/");

            if(exefile.length != 1 && exefile[exefile.length-1].contains(".")) {
                for(int i=0; i<extension_name.length; i++) {
                    if(exefile[exefile.length-1].contains(extension_name[i])) {
                        finalurl = null;
                        Log.d("error", "확장자 파일 인식됨");
                        search_message = "확장자 파일, 위험 사이트";
                        return;
                    }
                }
            }
            if(finalurl.contains("www")){
                finalurl = finalurl.substring(4);
            }
            Log.d("mes", "정상사이트: " + finalurl);
        }
        else if(message.contains("http://")) {
            finalurl = null;
            search_message = "http이므로 악성사이트";
            Log.d("mes", "http이므로 악성으로 판단");
        }
        else {
            finalurl = null;
            search_message = "프로토콜을 입력해주세요.";
            Log.d("mes", "프로토콜 입력이 필요함");
        }
    }

    public void search_safe() {
        if (flag == 0 || flag == -2)
            tmp_url = finalurl;
        int minLDA = finalurl.length();
        Cursor cursor1 = database.rawQuery("select sid, safeurl from safe_url", null);
        //safe_url : sid, safeurl / mal_url : mid, malurl
        int recordCount = cursor1.getCount();
        int c = 0;
        int count = alp_start_ch.length;
        int start_count = 0;
        int end_count = recordCount;

        for (c = 0; c < count; c++) {
            if (alp_start_ch[c] == tmp_url.charAt(0)) {
                start_count = alp_start_safe[c];
                cursor1.moveToPosition(start_count);
                Log.d("mes","배열 체크1 " + alp_start_ch[c]);
                Log.d("mes","배열 체크2 " + alp_start_safe[c]);
                end_count = alp_start_safe[c+1];
                Log.d("mes","start 체크 "+ start_count +", end count 체크 " + end_count);
            }
        }
        try {
            for (int i = start_count; i <= end_count; i++) {
                cursor1.moveToNext();
                String id = cursor1.getString(0);
                String url = cursor1.getString(1);

                if(tmp_url.equals(url) && flag != -2) {
                    Log.d("db","정상레코드 #" + (i+2) + " : " + id + ", " + url);
                    search_message = "** 화이트리스트 탐지결과 **\n 정상 url 사이트입니다!\n" + id + ", " + url;
                    flag = 0;
                    warnid = 1;
                    break;
                }

                if(i == end_count) {
                    flag++;
                    Log.d("db"," flag체크 : " + flag + ", 마지막 정상레코드 #" + i + " 해당 url를 찾지못했습니다!");

                    if (flag == 1 && tmp_url.split("\\.").length > 2) {
                        tmp_url = tmp_url.substring(tmp_url.split("\\.")[0].length()+1);
                        Log.d("mes","message_split : " + tmp_url);
                        search_safe();
                    }
                    else{
                        flag = 0;
                        search_mal();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            flag = 0;
            Log.d("db","찾지못했습니다!");
            search_message = "인덱스범위초과";
        }

        cursor1.close();
    }

    public void search_mal() {
        warnid = 0;
        String tmp_url = finalurl;
        Cursor cursor2 = database.rawQuery("select mid, malurl from mal_url", null);
        //safe_url : sid, safeurl / mal_url : mid, malurl
        int recordCount = cursor2.getCount();
        int c = 0;
        int count = alp_start_ch.length;
        int start_count = 0;
        int end_count = recordCount;

        for (c = 0; c < count; c++) {
            if (alp_start_ch[c] == tmp_url.charAt(0)) {
                start_count = alp_start_mal[c];

                cursor2.moveToPosition(start_count);
                Log.d("mes","배열 체크1 " + alp_start_ch[c]);
                Log.d("mes","배열 체크2 " + alp_start_mal[c]);
                end_count = alp_start_mal[c+1];
                Log.d("mes","start 체크 "+ start_count +", end count 체크 " + end_count);
            }
        }

        try {
            for (int i = start_count; i <= end_count; i++) {
                cursor2.moveToNext();
                // 본인의 데이터 타입이 string 인지 int인지에 맞게
                String id = cursor2.getString(0);
                String url = cursor2.getString(1);

                if(url.equals(tmp_url)) {
                    Log.d("db","악성레코드 #" + (i+2) + " : " + id + ", " + url);
//                    search_message = "** !!악성 url로 의심됩니다!! ** \n레코드 #" + (i+2) + " : " + id + ", " + url;
                    search_message = "** !!악성 url로 의심됩니다!! ** \n블랙리스트 탐지결과 악성 사이트입니다. 주의하세요! " + url;
                    break;
                }

                if(i == end_count) {
                    Log.d("db","마지막 악성레코드 #" + i + " 해당 url를 찾지못했습니다!");
                    flag = -2;
                    search_LDA();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("db","찾지못했습니다!");
            search_message = "인덱스범위초과";
        }

        cursor2.close();
    }

    public int getDistance(String s1, String s2) {
        int longStrLen = s1.length() + 1;
        int shortStrLen = s2.length() + 1; // 긴 단어 만큼 크기가 나올 것이므로, 가장 긴단어 에 맞춰 Cost를 계산
        int[] cost = new int[longStrLen];
        int[] newcost = new int[longStrLen]; // 초기 비용을 가장 긴 배열에 맞춰서 초기화 시킨다.
        for (int i = 0; i < longStrLen; i++) { cost[i] = i; } // 짧은 배열을 한바퀴 돈다.
        for (int j = 1; j < shortStrLen; j++) {
            // 초기 Cost는 1, 2, 3, 4...
            newcost[0] = j; // 긴 배열을 한바퀴 돈다.
            for (int i = 1; i < longStrLen; i++) {
                // 원소가 같으면 0, 아니면 1
                int match = 0;
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) { match = 1; }
                // 대체, 삽입, 삭제의 비용을 계산한다.
                int replace = cost[i - 1] + match;
                int insert = cost[i] + 1;
                int delete = newcost[i - 1] + 1;
                // 가장 작은 값을 비용에 넣는다.
                newcost[i] = Math.min(Math.min(insert, delete), replace);
                //System.out.print(newcost[i] + " ");
            } // 기존 코스트 & 새 코스트 스위칭
            int[] temp = cost;
            cost = newcost;
            newcost = temp;
            //System.out.println();
        }
        // 가장 마지막값 리턴
        return cost[longStrLen - 1];
        //lda = cost[longStrLen - 1];
    }

    public void search_LDA() {
        if (flag == -2)
            tmp_url = finalurl;
        minLDA1 = tmp_url.length();
        Cursor cursor1 = database.rawQuery("select sid, safeurl from safe_url", null);
        //safe_url : sid, safeurl / mal_url : mid, malurl
        int recordCount = cursor1.getCount();
        int c = 0;
        int n = 0;
        int count = alp_start_ch.length;
        int start_count = 0;
        int end_count = recordCount;

        for (c = 0; c < count; c++) {
            if (alp_start_ch[c] == tmp_url.charAt(0)) {
                start_count = alp_start_safe[c];
                cursor1.moveToPosition(start_count);
                Log.d("mes","배열 체크1 " + alp_start_ch[c]);
                Log.d("mes","배열 체크2 " + alp_start_safe[c]);
                end_count = alp_start_safe[c+1];
                Log.d("mes","start 체크 "+ start_count +", end count 체크 " + end_count);
            }
        }
        try {
            for (int i = start_count; i <= end_count; i++) {
                cursor1.moveToNext();
                String id = cursor1.getString(0);
                String url = cursor1.getString(1);
                n++;

                if (tmp_url.length()+5 > url.length() || tmp_url.length()-5 < url.length()) {
                    if (minLDA1 > getDistance(tmp_url, url)) {
                        minLDA1 = getDistance(tmp_url, url);
                        Log.d("mes", "i " + i + ", url: " + url + ", LDA체크 " + minLDA1);
                        sameid1 = i+2;
                        sameurl1 = url;
                        persent1 = (1.0-((double)minLDA1/(double)url.length()))*100;
                        per1 = String.format("%.1f", persent1);
                    }
                }

                if(i == end_count) {
                    flag++;
                    Log.d("db", " flag체크 : " + flag + ", 마지막 정상레코드 #" + i + ", 횟수: " + n);

                    if (flag == -1 && tmp_url.split("\\.").length > 2) {
                        minLDA2 = minLDA1; sameid2 = sameid1; sameurl2 = sameurl1; persent2 = persent1; per2 = per1;
                        tmp_url = tmp_url.substring(tmp_url.split("\\.")[0].length() + 1);
                        Log.d("mes", "LDA_split : " + tmp_url);
                        search_LDA();
                    }
                    else {
                        if ((persent1 <= 100 && persent1 >= 85) || (persent2 <= 100 && persent2 >= 85)) {
                            warn1 = warn[0] + "\n"; warn2 = warn[0] + "\n";
                        }
                        else if ((persent1 < 85 && persent1 >= 75) || (persent2 < 85 && persent2 >= 75)) {
                            warn1 = warn[1] + "\n"; warn2 = warn[1] + "\n";
                        }
                        else if ((persent1 < 75 && persent1 >= 65) || (persent2 < 75 && persent2 >= 65)) {
                            warn1 = warn[2] + "\n"; warn2 = warn[2] + "\n";
                        }
                        else {
                            search_message = "더 나은 탐지를 위해 아래의 제보하기 버튼을 눌러주세요!!";
                            flag = 0;
                            break;
                        }

                        if (flag == -1) {
                            search_message = warn1 + sameurl1 + "와 " + per1 + "% 유사한 사칭사이트로 의심됩니다!";
                            flag = 0;
                        }
                        else if (flag == 0 && persent1 <= persent2) {
                            search_message = warn2 + sameurl2 + "와 " + per2 + "% 유사한 사칭사이트로 의심됩니다!";
                            flag = 0;
                        }
                        else {
                            search_message = warn1 + sameurl1 + "와 " + per1 + "% 유사한 부가 사칭사이트로 의심됩니다!";
                            flag = 0;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            flag = 0;
            Log.d("db","찾지못했습니다!");
            search_message = "인덱스범위초과";
        }

        cursor1.close();
    }

    class RealTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder sb=new StringBuilder();
            BufferedWriter bw = null;
            //실시간 검색어를 가져오기위한 String객체(String 과는 차이가 있음)
            try {
                Document doc= Jsoup.connect(message).get();
                File dir = new File(getExternalFilesDir(null) + "/htmlfile");
                if(!dir.exists()){ dir.mkdir(); }
                File testhtml = new File(getExternalFilesDir(null) + "/htmlfile/testhtml.html");
                bw = new BufferedWriter(new FileWriter(testhtml, false));
                Log.d("error", "파일 쓰기 준비 완료");
                String gethtml = doc.toString();
                Log.d("error", "파일 문자열 변환 완료");
                if(gethtml.contains("document.location.href") || gethtml.contains("http-equiv") || gethtml.contains("window.location") || gethtml.contains("window.location.href")) {
                    gethtml = gethtml.replaceAll("document.location.href", "");
                    gethtml = gethtml.replaceAll("http-equiv=\"refresh\"", "http-equiv=\"\"");
                    gethtml = gethtml.replaceAll("window.location", "");
                    gethtml = gethtml.replaceAll("window.location.href", "");
                }
                bw.write(gethtml);
                //Log.d("html", doc.toString());
                //sb.append(doc.toString());
            } catch (IOException e) {
                try {
                    File testhtml = new File(getExternalFilesDir(null) + "/htmlfile/testhtml.html");
                    bw = new BufferedWriter(new FileWriter(testhtml, false));
                    bw.write("<html><head></head><body><h1>302 FOUND<hr><h2>해당 사이트는 없는 사이트입니다.</body>");
                }
                catch(Exception e2) {}
            }
            try {
                bw.close();
            }
            catch(Exception e) {}
            return sb.toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            web_view();
        }
    }

    public void search_mes() {
        for (int i = 0; i < strange_sms.length; i++) {
            if (sms.contains(strange_sms[i])) {
                sms_report1 = strange_sms[i] + " 이므로 주의가 필요합니다.\n";
                break;
            }
            else
                sms_report1 = "";
        }

        if (((sms.length() - (double)sms.replaceAll("[.,:*#?%^ ]","").length())/sms.length()) > 0.2)
            sms_report2 = "문장구조분석결과 특수문자 기준치 초과로 주의가 필요합니다.";
        else
            sms_report2 = "";

        if (!sms_report1.equals("") || !sms_report2.equals(""))
            sms_report1 = "\n\n** 문자메시지 탐지 결과 **\n" + sms_report1 + sms_report2;

        Log.d("sms", "sms_report1 : " + sms_report1 + " sms_report2 : " + sms_report2);
    }

    public void submit_report() {
        BufferedWriter bw = null;
        try {
            File dir = new File(getExternalFilesDir(null) + "/reportfile");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File testreport = new File(getExternalFilesDir(null) + "/reportfile/testreport.txt");
            bw = new BufferedWriter(new FileWriter(testreport, true));
            bw.write(message);
            bw.newLine();
            bw.close();
        }
        catch(Exception e) {}
    }

}