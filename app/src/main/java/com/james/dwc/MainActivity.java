package com.james.dwc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    Button btnmode1, btnmode2, btnmode3, btnmode4, btnsterstart, btnsterstop, btnemer, btnconnect;
    ImageButton btnup, btndown;
    ImageView imgCon;


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

        imgCon = (ImageView) findViewById(R.id.imgcon);


        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnmode1:

                        break;
                    case R.id.btnmode2:
                        break;
                    case R.id.btnmode3:
                        break;
                    case R.id.btnmode4:
                        break;
                    case R.id.btnsterstart:
                        break;
                    case R.id.btnsterstop:
                        break;
                    case R.id.btnemer:
                        break;
                }
            }
        };

        btnmode1.setOnClickListener(onClickListener);
        btnmode2.setOnClickListener(onClickListener);
        btnmode3.setOnClickListener(onClickListener);
        btnmode4.setOnClickListener(onClickListener);
        btnsterstart.setOnClickListener(onClickListener);
        btnsterstop.setOnClickListener(onClickListener);
        btnemer.setOnClickListener(onClickListener);




        btnconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgCon.getTag() =="off") {
                    imgCon.setImageResource(R.drawable.connected);
                    imgCon.setTag("on");
                }
                else {
                    imgCon.setImageResource(R.drawable.disconnected);
                    imgCon.setTag("off");
                }

            }
        });





    }

}