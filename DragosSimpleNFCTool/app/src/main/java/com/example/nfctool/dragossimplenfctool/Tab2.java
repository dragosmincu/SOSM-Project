package com.example.nfctool.dragossimplenfctool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Tab2 extends Fragment {

    private MainActivity refMain;
    private RecyclerView rcV;
    private DataModelAdapter dmAdp;

    public Tab2(MainActivity refMain) {
        this.refMain = refMain;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab2, container, false);

        rcV = (RecyclerView) rootView.findViewById(R.id.rvData);
        dmAdp = new DataModelAdapter(loadFirebaseData(), refMain);
        rcV.setAdapter(dmAdp);
        rcV.setLayoutManager(new LinearLayoutManager(refMain));

        return rootView;
    }

    public List<DataModel> loadFirebaseData() {
        return refMain.getFirebaseData();
    }

    public void reloadData() {
        dmAdp.firebaseData = loadFirebaseData();
        dmAdp.notifyDataSetChanged();
    }
}