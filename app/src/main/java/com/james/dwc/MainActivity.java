package com.james.dwc;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {

    public Button btnmode1, btnmode2, btnmode3, btnmode4, btnsterstart, btnsterstop, btnemer, btnconnect;
    public ImageButton btnup, btndown;
    public ImageView imgCon;
    public EditText lblip;
    private Socket socket;

    public byte[] txByte = {(byte)0x7B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x7D};

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public String strtmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnmode1 = (Button) findViewById(R.id.btnmode1);
        btnmode2 = (Button) findViewById(R.id.btnmode2);
        btnmode3 = (Button) findViewById(R.id.btnmode3);
        btnmode4 = (Button) findViewById(R.id.btnmode4);
        btnup = (ImageButton) findViewById(R.id.btnup);
        btndown = (ImageButton) findViewById(R.id.btndown);
        btnsterstart = (Button) findViewById(R.id.btnsterstart);
        btnsterstop = (Button) findViewById(R.id.btnsterstop);
        btnemer = (Button) findViewById(R.id.btnemer);
        btnconnect = (Button) findViewById(R.id.btnconnect);

        btnmode1.setEnabled(false);
        btnmode2.setEnabled(false);
        btnmode3.setEnabled(false);
        btnmode4.setEnabled(false);
        btnup.setEnabled(false);
        btndown.setEnabled(false);
        btnsterstop.setEnabled(false);
        btnsterstart.setEnabled(false);
        btnemer.setEnabled(false);

        imgCon = (ImageView) findViewById(R.id.imgcon);
        imgCon.setImageResource(R.drawable.disconnected);
        imgCon.setTag("off");

        lblip = (EditText) findViewById(R.id.lblip);
//        IP 초기값을 불러온다
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        strtmp = pref.getString("MyIP", "192.168.1.3");
        lblip.setText(strtmp);

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnmode1:
                        txByte[1] = 0x00;
                        txByte[2] = 0x01;
                        break;
                    case R.id.btnmode2:
                        txByte[1] = 0x00;
                        txByte[2] = 0x02;
                        break;
                    case R.id.btnmode3:
                        txByte[1] = 0x00;
                        txByte[2] = 0x03;
                        break;
                    case R.id.btnmode4:
                        txByte[1] = 0x00;
                        txByte[2] = 0x04;
                        break;
                    case R.id.btnsterstart:
                        txByte[1] = 0x02;
                        txByte[2] = 0x01;
                        break;
                    case R.id.btnsterstop:
                        txByte[1] = 0x02;
                        txByte[2] = 0x00;
                        break;
                    case R.id.btnemer:
                        txByte[1] = (byte)0xFF;
                        txByte[2] = 0x00;
                        break;
                    case R.id.btnup:
                        txByte[1] = 0x01;
                        txByte[2] = 0x02;
                        break;
                    case R.id.btndown:
                        txByte[1] = 0x01;
                        txByte[2] = 0x01;
                        break;
                }

                OnSendData(txByte);
            }
        };

        btnmode1.setOnClickListener(onClickListener);
        btnmode2.setOnClickListener(onClickListener);
        btnmode3.setOnClickListener(onClickListener);
        btnmode4.setOnClickListener(onClickListener);
        btnsterstart.setOnClickListener(onClickListener);
        btnsterstop.setOnClickListener(onClickListener);
        btnemer.setOnClickListener(onClickListener);
        btnup.setOnClickListener(onClickListener);
        btndown.setOnClickListener(onClickListener);

        Log.i(TAG, "Application createad");

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                connect button click
                if (imgCon.getTag() == "off") {
                    Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                    btnmode1.setEnabled(true);
                    btnmode2.setEnabled(true);
                    btnmode3.setEnabled(true);
                    btnmode4.setEnabled(true);
                    btnup.setEnabled(true);
                    btndown.setEnabled(true);
                    btnsterstop.setEnabled(true);
                    btnsterstart.setEnabled(true);
                    btnemer.setEnabled(true);
                    lblip.setEnabled(false);
                    String addr = lblip.getText().toString().trim(); // trim : 좌우공백제거

                    ConnectThread thread = new ConnectThread(addr);  //
//                    ConnectThread thread = new ConnectThread("192.168.0.82", 4001); // Test PC

                    //키보드 자동 내리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lblip.getWindowToken(), 0);

//                    IP저장
                    editor.putString("MyIP", lblip.getText().toString());
                    editor.apply();
                    thread.start();

                }
//                disconnect button click
                else {
                    try {
                        socket.close();
                        Toast.makeText(getApplicationContext(), "DisConnecting...", Toast.LENGTH_SHORT).show();
                        imgCon.setImageResource(R.drawable.disconnected);
                        imgCon.setTag("off");
                        btnconnect.setText("연결");

                        btnmode1.setEnabled(false);
                        btnmode2.setEnabled(false);
                        btnmode3.setEnabled(false);
                        btnmode4.setEnabled(false);
                        btnup.setEnabled(false);
                        btndown.setEnabled(false);
                        btnsterstop.setEnabled(false);
                        btnsterstart.setEnabled(false);
                        btnemer.setEnabled(false);
                        lblip.setEnabled(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "DisConnect 실패", Toast.LENGTH_SHORT).show();
                    }

                }

            }






        });




    }   // end of main activity


    // fixme: Socket Connect.
    class ConnectThread extends Thread {
        String hostname;

        public ConnectThread(String addr) {
            hostname = addr;
        }

        public void run() {
            try { //클라이언트 소켓 생성

                int port = 4001;
                socket = new Socket(hostname, port);
                Log.d(TAG, "Socket 생성, 연결.");

//                Toptext = findViewById(R.id.text1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InetAddress addr = socket.getInetAddress();
                        String tmp = addr.getHostAddress();
//                        Toptext.setText(tmp + " 연결 완료");
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
//
//                        DiconButton.setEnabled(true);
//                        ConnButton.setEnabled(false);
//                        StartButton.setEnabled(true);

                        imgCon.setImageResource(R.drawable.connected);
                        imgCon.setTag("on");
                        btnconnect.setText("종료");
                    }
                });




            } catch (UnknownHostException uhe) { // 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.

                Log.e(TAG, " 생성 Error : 호스트의 IP 주소를 식별할 수 없음.(잘못된 주소 값 또는 호스트 이름 사용)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : 호스트의 IP 주소를 식별할 수 없음.(잘못된 주소 값 또는 호스트 이름 사용)", Toast.LENGTH_SHORT).show();
//                        Toptext.setText("Error : 호스트의 IP 주소를 식별할 수 없음.(잘못된 주소 값 또는 호스트 이름 사용)");
                    }
                });

            } catch (IOException ioe) { // 소켓 생성 과정에서 I/O 에러 발생.

                Log.e(TAG, " 생성 Error : 네트워크 응답 없음");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : 네트워크 응답 없음", Toast.LENGTH_SHORT).show();
//                        Toptext.setText("네트워크 연결 오류");
                    }
                });


            } catch (SecurityException se) { // security manager에서 허용되지 않은 기능 수행.

                Log.e(TAG, " 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)", Toast.LENGTH_SHORT).show();
//                        Toptext.setText("Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");
                    }
                });


            } catch (IllegalArgumentException le) { // 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.

                Log.e(TAG, " 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생.(0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), " Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생.(0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)", Toast.LENGTH_SHORT).show();
//                        Toptext.setText("Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생.(0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");
                    }
                });


            }




        }
    }


    void OnSendData(byte[] aug) {
        // 데이터  전송하기
        try {

//            String OutData = "[" + strID.getText().toString().trim() + "," + ipNumber.getText().toString().trim() + "," + portNumber.getText().toString().trim() + ",";
//            if (radioGroup.getCheckedRadioButtonId() == R.id.radioEn) OutData += "1,";
//            else OutData += "0,";
//            OutData += iTxInterval.getText().toString().trim() + ",";     // Interval
//            OutData += strSSID.getText().toString().trim() + ",";
//            OutData += strPW.getText().toString().trim() + "]";
//
//            byte[] data = OutData.getBytes();
//            byte[] data = aug.getBytes();
            OutputStream output = socket.getOutputStream();
            output.write(aug);
//            socket.close();
//                    Toptext.setText("설정값을 전송하였습니다.");
//                    Log.d(TAG, "AT+START\\n COMMAND 송신");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "데이터 송신 오류");
        }


//        if (!bThread) {
//            StartThread sthread = new StartThread();
//            sthread.start();
//            bThread = true;
//        }
    }



    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}