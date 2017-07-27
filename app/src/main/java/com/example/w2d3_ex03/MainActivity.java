package com.example.w2d3_ex03;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w2d3_ex03.FeedReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private DBHelper helper;
    private SQLiteDatabase database;

    EditText titleET;
    EditText subtitleET;
    EditText searchET;
    TextView resultListTV;
//__________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DBHelper(this);
        database = helper.getWritableDatabase();

        titleET = (EditText) findViewById(R.id.ed_title);
        subtitleET = (EditText) findViewById(R.id.ed_subtitle);
        resultListTV = (TextView) findViewById(R.id.tv_resultList);
        searchET = (EditText) findViewById(R.id.et_searcRecord);
    }
//__________________________________________________________________________________________________

    @Override
    protected void onResume() {
        super.onResume();
        //saveRecord();
        //readRecord();
        //readRecord();
        //updateRecord();
        //deleteRecord();
    }

//__________________________________________________________________________________________________

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
//__________________________________________________________________________________________________

    private void saveRecord(){
        //String title = "RecordTitle";
        //String subtitle = "Record subtitle";

        String newTitle = titleET.getText().toString();
        String newSubtitle = subtitleET.getText().toString();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, newTitle);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, newSubtitle);

        long recordId = database.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
        if (recordId > 0){
            Log.d(TAG, "saveRecord: Record saved");
        }else {
            Log.d(TAG, "saveRecord: Record not saved");
        }
        titleET.setText("");
        subtitleET.setText("");
    }
//__________________________________________________________________________________________________

    private void readRecord(){
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };
        String selection = FeedEntry.COLUMN_NAME_TITLE + "= ?";
        String[] selectionArr = {
          "Record title"
        };
        String sortOrder = FeedEntry.COLUMN_NAME_SUBTITLE + "DESC";
        Cursor cursor = database.query(
                FeedEntry.TABLE_NAME,       // TABLE
                projection,                 //Projection
                null,                       //Selection Where
                null,                       //Values for selection
                null,                       //Group by
                null,                       //Filters
                null                        //Sort order
        );
        while (cursor.moveToNext()){

            long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
            String entryTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
            String entrySubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
            Log.d(TAG, "readRecord: id: " + String.valueOf(entryId) + " Title: " + entryTitle + " Subtitle: " + entrySubtitle);

            resultListTV.append(entryTitle + " " + entrySubtitle );

        }
    }
 //__________________________________________________________________________________________________

    private void deleteRecord(){
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ? ";
        String[] selectionArgs = {
                //"Record title"
                "tio"
        };
        int deleted = database.delete(
                FeedEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
        if (deleted > 0){
            Log.d(TAG, "deleteRecord:  Record deleted");
        }else {
            Log.d(TAG, "deleteRecord:  Record not deleted");
        }
    }

 //__________________________________________________________________________________________________

    private void updateRecord(){
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, "Record new title");

        String selection = FeedEntry.COLUMN_NAME_TITLE  + " LIKE ? ";
        String[] selectionArgs = {
                //"Record title"
                "rtttd"
        };

        int count = database.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        if (count > 0){
            Log.d(TAG, "updateRecord: Update record" + count);
        }else {
            Log.d(TAG, "updateRecord: Record not updated");
        }
    }

//__________________________________TEST____________________________________________________________

    public void saveRecord2(View view) {
        String newTitle = titleET.getText().toString();
        String newSubtitle = subtitleET.getText().toString();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, newTitle);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, newSubtitle);

        long recordId = database.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
        if (recordId > 0){
            Log.d(TAG, "saveRecord: Record saved");
        }else {
            Log.d(TAG, "saveRecord: Record not saved");
        }
        titleET.setText("");
        subtitleET.setText("");
    }

//__________________________________TEST____________________________________________________________

    public void readRecord2(View view) {

        resultListTV.setText("");

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };
        String selection = FeedEntry.COLUMN_NAME_TITLE + "= ?";
        String[] selectionArr = {
                "Record title"
        };
        String sortOrder = FeedEntry.COLUMN_NAME_SUBTITLE + "DESC";
        Cursor cursor = database.query(
                FeedEntry.TABLE_NAME,       // TABLE
                projection,                 //Projection
                null,                       //Selection Where
                null,                       //Values for selection
                null,                       //Group by
                null,                       //Filters
                null                        //Sort order
        );
        while (cursor.moveToNext()){

            String entryId = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID)));
            String entryTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
            String entrySubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
            Log.d(TAG, "readRecord: id: " + entryId + " Title: " + entryTitle + " Subtitle: " + entrySubtitle);

            resultListTV.append("ID -->  " + entryId +" TITLE --> " + entryTitle + "  SUB -->  " + entrySubtitle + "\n");

        }
    }

//__________________________________TEST____________________________________________________________

    public void searchRecord(View view) {

        String recordToSearch = searchET.getText().toString();

        Cursor cursor = null;
        cursor = this.database.rawQuery(
                "select * from " + FeedEntry.TABLE_NAME +
                " where " + FeedEntry._ID + "=" + recordToSearch  , null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                String entryId = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID)));
                String entryTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
                String entrySubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));

                titleET.setText(entryTitle);
                subtitleET.setText(entrySubtitle);
            }
        }
    }
//__________________________________TEST____________________________________________________________

    public void deleterecord2(View view) {
        String recordToSearch = searchET.getText().toString();
        String selection = FeedEntry._ID + " LIKE ? ";
        String[] selectionArgs = {
                //"Record title"
                recordToSearch
        };
        int deleted = database.delete(
                FeedEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
        if (deleted > 0){
            Toast.makeText(this, "Record DELETED", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "deleteRecord:  Record deleted");
        }else {
            Log.d(TAG, "deleteRecord:  Record not deleted");
        }
    }
//__________________________________TEST____________________________________________________________

    public void updateRecord2(View view) {
        ContentValues values = new ContentValues();

        String newTitle = titleET.getText().toString();
        String newSubtitle = subtitleET.getText().toString();
        String recordToSearch = searchET.getText().toString();

        values.put(FeedEntry.COLUMN_NAME_TITLE, newTitle);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, newSubtitle);

        String selection = FeedEntry._ID  + " LIKE ? ";
        String[] selectionArgs = {
                //"Record title"
                recordToSearch
        };

        int count = database.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        if (count > 0){
            Toast.makeText(this, "Record UPDATED", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "updateRecord: Update record" + count);
        }else {
            Log.d(TAG, "updateRecord: Record not updated");
        }
    }
}
