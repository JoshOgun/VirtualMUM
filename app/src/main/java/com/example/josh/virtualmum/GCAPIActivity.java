package com.example.josh.virtualmum;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;

public class GCAPIActivity extends AppCompatActivity implements View.OnClickListener {

    private Cursor mCursor = null;
    private static final String[] COLS = new String[]
            { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcapi);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GCAPIActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    1);        }
        else {

            mCursor = getContentResolver().query(


                    CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
            mCursor.moveToFirst();


            Button b = (Button) findViewById(R.id.next);


            b.setOnClickListener(this);
            b = (Button) findViewById(R.id.previous);


            b.setOnClickListener(this);
            onClick(findViewById(R.id.previous));
        }
    }

    @Override
    public void onClick(View v) {
        TextView tv = (TextView)findViewById(R.id.data);


        String title = "N/A";


        Long start = 0L;


        switch(v.getId()) {
            case R.id.next:
                if(!mCursor.isLast()) mCursor.moveToNext();
                break;
            case R.id.previous:
                if(!mCursor.isFirst()) mCursor.moveToPrevious();
                break;
        }


        Format df = DateFormat.getDateFormat(this);
        Format tf = DateFormat.getTimeFormat(this);
        try {
            title = mCursor.getString(0);


            start = mCursor.getLong(1);


        } catch (Exception e) {
//ignore
        }
        tv.setText(title+" on "+df.format(start)+" at "+tf.format(start));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(GCAPIActivity.this, "Permission denied to read your Calendar", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
