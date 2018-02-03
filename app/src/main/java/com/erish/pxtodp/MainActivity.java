package com.erish.pxtodp;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer, String> dpiHashMap = new HashMap<>();

    TextView currentTextView;

    Spinner spinner;

    TextView pxTextView;

    TextView dpTextView;

    ArrayAdapter<String> spinnerAdapter;

    int currentDpi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTextView = findViewById(R.id.current_text_view);
        spinner = findViewById(R.id.spinner);
        pxTextView = findViewById(R.id.px_text_view);
        dpTextView = findViewById(R.id.dp_text_view);

        //init dpi
        dpiHashMap.put(120, "ldpi");
        dpiHashMap.put(160, "mdpi");
        dpiHashMap.put(213, "tvdpi");
        dpiHashMap.put(240, "hdpi");
        dpiHashMap.put(320, "xhdpi");
        dpiHashMap.put(400, "400dpi");
        dpiHashMap.put(480, "xxhdpi");
        dpiHashMap.put(640, "xxxhdpi");

        //init spinner
        List<String> spinnerItems = new ArrayList<>();

        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);

        spinnerItems.addAll(new ArrayList(dpiHashMap.values()));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        //set default dpi value
        String dpiValue = getDpi();

        currentTextView.setText("Current dpi : " + dpiValue);
        spinner.setSelection(spinnerAdapter.getPosition(dpiValue));

        //set values
        pxTextView.setText("WIDTH PX : " + getWidthPixel() + " HEIGHT PX : " + getHeightPixel());
        dpTextView.setText("WIDTH DP : " + convertPixelsToDp(getWidthPixel()) + " HEIGHT DP : " + convertPixelsToDp(getHeightPixel()));

        //set click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                spinner.setSelection(spinnerAdapter.getPosition(str));

                //calculate
                for (int key : dpiHashMap.keySet()) {
                    if (dpiHashMap.get(key).equals(str)) {
                        currentDpi = key;
                        break;
                    }
                }

                dpTextView.setText("WIDTH DP : " + convertPixelsToDp(getWidthPixel()) + " HEIGHT DP : " + convertPixelsToDp(getHeightPixel()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);

        String result = "NOT FOUND";

        currentDpi = metrics.densityDpi;

        if (dpiHashMap.containsKey(currentDpi)) {
            result = dpiHashMap.get(currentDpi);
        }

        return result;
    }

    public float convertPixelsToDp(float px){
        float dp = px / (currentDpi / 160f);

        return dp;
    }

    private int getWidthPixel() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    private int getHeightPixel() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }
}
