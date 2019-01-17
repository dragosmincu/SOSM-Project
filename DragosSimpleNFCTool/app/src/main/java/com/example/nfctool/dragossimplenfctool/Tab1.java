package com.example.nfctool.dragossimplenfctool;

import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class Tab1 extends Fragment {

    private TextView text;
    private Button button;
    private Button url_button;
    private MainActivity refMain;

    public Tab1(MainActivity refMain) {
        this.refMain = refMain;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1, container, false);

        text = (TextView) rootView.findViewById(R.id.text);
        button = (Button) rootView.findViewById(R.id.button);
        url_button = (Button) rootView.findViewById(R.id.url_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (text.getText().equals("Hold a tag against the back of your device")) {
                    refMain.displaySnackbar("No Data!");
                    return;
                }
                if (text.getText().equals("Not in SmartPoster format!")) {
                    refMain.displaySnackbar("Invalid Data!");
                    return;
                }

                String[] poster_data = text.getText().toString().split("\\r?\\n");
                refMain.storeInDatabase(poster_data[0], poster_data[1]);

                refMain.displaySnackbar("Stored In Online Database!");
            }
        });

        url_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (text.getText().equals("Hold a tag against the back of your device")) {
                    refMain.displaySnackbar("No Data!");
                    return;
                }
                if (text.getText().equals("Not in SmartPoster format!")) {
                    refMain.displaySnackbar("Invalid Data!");
                    return;
                }

                String[] poster_data = text.getText().toString().split("\\r?\\n");
                refMain.startSecondActivity(poster_data[1]);
            }
        });

        return rootView;
    }

    public void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        SmartPoster poster = SmartPoster.parsePosterRecords(msgs[0].getRecords());
        if (poster != null)
            text.setText(poster.getRecordData());
        else
            text.setText("Not in SmartPoster format!");
    }
}
