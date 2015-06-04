package com.miles.newshack2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText[] editTexts;
    TextView textViewTotal;
    Spinner spinner;
    int articleId = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner = (Spinner) findViewById(R.id.spinner);
        
        editTexts = new EditText[] {
                (EditText) findViewById(R.id.editText1),
                (EditText) findViewById(R.id.editText2),
                (EditText) findViewById(R.id.editText3),
                (EditText) findViewById(R.id.editText4),
                (EditText) findViewById(R.id.editText5),
        };
        
        updateEditTexts();
        
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/hero_light.otf");
        Typeface myTypeface2 = Typeface.createFromAsset(getAssets(), "fonts/hero.otf");
        textViewTotal.setTypeface(myTypeface);
        ((Button) findViewById(R.id.buttonSubmit)).setTypeface(myTypeface2);
        
        for (int i = 0; i < editTexts.length; i ++) {
            editTexts[i].setTypeface(myTypeface);
        }
        
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                calculateTotal();
                h.postDelayed(this, 20);
            }
        });
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                articleId = position;
                updateEditTexts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                
            }
        });
    }
    
    
    private void calculateTotal() {
        try {
            int total = 0;
            for (int i = 0; i < editTexts.length; i ++) {
                if (editTexts[i].getText().toString().length() > 0) {
                    total += Integer.valueOf(editTexts[i].getText().toString());
                }
            }
            textViewTotal.setText(String.valueOf(total));
        }
        catch (Exception e) {
            
        }
    }
    
    
    public void onClickSubmit(View v) {
        SharedPreferences sp = getSharedPreferences("values" + articleId, MODE_PRIVATE);
        Editor e = sp.edit();
        e.clear();
        
        for (int i = 0; i < editTexts.length; i ++) {
            if (editTexts[i].length() > 0) {
                e.putInt(String.valueOf(i), Integer.valueOf(editTexts[i].getText().toString()));
            }
        }
        
        e.commit();
        
        Toast.makeText(this, "Pushed", Toast.LENGTH_SHORT).show();
    }
    
    
    private void updateEditTexts() {
        SharedPreferences sp = getSharedPreferences("values" + articleId, MODE_PRIVATE);
        
        for (int i = 0; i < editTexts.length; i ++) {
            int value = sp.getInt(String.valueOf(i), -1);
            if (value != -1) {
                editTexts[i].setText(String.valueOf(value));
            }
            else {
                editTexts[i].setText("");
            }
        }
    }
    
}
