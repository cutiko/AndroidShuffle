package cl.cutiko.androidshuffle.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cl.cutiko.androidshuffle.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


}
