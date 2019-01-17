package com.example.nfctool.dragossimplenfctool;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DataModelAdapter extends RecyclerView.Adapter<DataModelAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textData;
        public Button goToButton;

        public ViewHolder(View itemView) {
            super(itemView);

            textData = itemView.findViewById(R.id.textData);
            goToButton = itemView.findViewById(R.id.goToButton);
        }
    }

    public List<DataModel> firebaseData;
    private MainActivity refMain;
    private String tempData;

    public DataModelAdapter(List<DataModel> firebaseData, MainActivity refMain) {
        this.firebaseData = firebaseData;
        this.refMain = refMain;
    }

    @Override
    public DataModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View dataModelView = inflater.inflate(R.layout.row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(dataModelView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DataModelAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DataModel poster = firebaseData.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.textData;
        textView.setText(poster.getTextData());
        tempData = poster.getTextData();
        Button button = viewHolder.goToButton;
        button.setText("Open Browser");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String[] poster_data = tempData.split("\\r?\\n");
                refMain.startSecondActivity(poster_data[1]);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return firebaseData.size();
    }
}
