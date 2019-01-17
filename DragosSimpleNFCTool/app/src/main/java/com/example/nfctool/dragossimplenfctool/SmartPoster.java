package com.example.nfctool.dragossimplenfctool;

import android.net.Uri;
import android.nfc.NdefRecord;
import android.support.v4.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public class SmartPoster implements ParsedNdefRecord {

    // a very simple smart poster model, made from a title and a link
    // storage of the two record
    private final TextRecord mTitleRecord;
    private final UriRecord mUriRecord;

    public SmartPoster(TextRecord title, UriRecord uri) {
        mUriRecord = Preconditions.checkNotNull(uri);
        mTitleRecord = title;
    }

    public UriRecord getUriRecord() {
        return mUriRecord;
    }

    public TextRecord getTitle() {
        return mTitleRecord;
    }

    public String getRecordData() {
        if (mTitleRecord != null) {
            return mTitleRecord.getRecordData() + "\n" + mUriRecord.getRecordData();
        } else {
            return mUriRecord.getRecordData();
        }
    }

    // generic parser
    public static SmartPoster parsePosterRecords(NdefRecord[] records) {
        if (records.length != 2)
            return null;

        if (!TextRecord.isText(records[0]) || !UriRecord.isUri(records[1]))
            return null;

        return new SmartPoster(TextRecord.parse(records[0]), UriRecord.parse(records[1]));
    }
}
