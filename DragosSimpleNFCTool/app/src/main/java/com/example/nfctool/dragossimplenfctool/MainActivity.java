package com.example.nfctool.dragossimplenfctool;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private DatabaseReference mDatabase;
    private DataSnapshot mSnapshot;

    private Tab1 tab1;
    private Tab2 tab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("SmartPosters");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSnapshot = dataSnapshot;
                tab2.reloadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //nothing
            }
        };
        mDatabase.addValueEventListener(listener);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tab1 = new Tab1(this);
        tab2 = new Tab2(this);
        adapter.addFragment(tab1, "Reader");
        adapter.addFragment(tab2, "Online Stored Data");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            displaySnackbar("Possible poster detected! Information Updated!");

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

                tab1.displayMsgs(msgs);
            }
        }
    }

    public void displaySnackbar(String toDisplay) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), toDisplay, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void storeInDatabase(String key, String value) {
        mDatabase.child(key).setValue(value);
    }

    public List<DataModel> getFirebaseData() {
        List<DataModel> data_from_posters = new ArrayList<>();

        data_from_posters.add(new DataModel("NewInfo\nhttps://en.wikipedia.org/wiki/Bird_Box_(film)"));
        data_from_posters.add(new DataModel("NewProduct\nhttps://www.emag.ro/brands/brand/alienware"));
        data_from_posters.add(new DataModel(("NewSearchEngine\nhttps://www.bing.com/")));

        /*for (Map.Entry<String, String> data : ((Map<String, String>)(mSnapshot.getValue())).entrySet()) {
            data_from_posters.add(new DataModel((data.getKey() + "\n" + data.getValue())));
        }*/

        return data_from_posters;
    }

    public void startSecondActivity(String url_name) {

        Intent myIntent = new Intent(this, Main2Activity.class);
        myIntent.putExtra("url_name", url_name); //Optional parameters
        startActivity(myIntent);
    }
}
