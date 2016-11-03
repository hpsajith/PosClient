package com.ites.pos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ites.pos.main_activity.R;

public class NewBill extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        TextView tableIdD = (TextView) findViewById(R.id.tableId);

        Intent i = getIntent();
        String tableId = i.getStringExtra("tableId");
        tableIdD.setText(tableId);
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }
}
