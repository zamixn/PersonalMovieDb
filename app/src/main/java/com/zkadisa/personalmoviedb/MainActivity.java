package com.zkadisa.personalmoviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.zkadisa.personalmoviedb.DataHandling.CSVReader;
import com.zkadisa.personalmoviedb.DataHandling.CSVWriter;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.Misc.ScrollListView;
import com.zkadisa.personalmoviedb.Misc.Utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivityClass {

    private Context context = this;
    private static MainActivity instance;

    private EditText searchQueryEditText;

    private Button searchButton;

    private ScrollListView searchList;
    private SearchResultListAdapter adapter;

    public static CustomIndicator customIndicator;

    private long previousBottomScrollTime = -500;

    private static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivitydesign);
        instance = this;

        searchQueryEditText = findViewById(R.id.searchPhraseEditText);
        searchButton = findViewById(R.id.search_button);

        searchList = findViewById(R.id.searchEntryListView);
        adapter = new SearchResultListAdapter(this, new ArrayList<SearchEntryListItem>());
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchEntryListItem item = (SearchEntryListItem) searchList.getItemAtPosition(i);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("data", (Serializable) item);
                context.startActivity(intent);
            }
        });
        searchList.setOnBottomReachedListener(new ScrollListView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if(OMDbReader.GetPageNumber() < 100 && (System.currentTimeMillis() - previousBottomScrollTime > 500)) {
                    OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), true, adapter);
                    previousBottomScrollTime = System.currentTimeMillis();
                }
            }
        });

        customIndicator = findViewById(R.id.downloadProgressBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), false, adapter);
                hideSoftKeyboard(MainActivity.this, view);
            }
        });
        searchQueryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchButton.performClick();
                    return true;
                }
                return false;
            }
        });

//        findViewById(R.id.testLoadButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoadDatabase();
//            }
//        });
//        findViewById(R.id.testSaveButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SaveDatabase();
//            }
//        });
//        findViewById(R.id.testListButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ListDriveFiles();
//            }
//        });

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getResources().getString(R.string.databaseName))
                .allowMainThreadQueries().build();
//                context.deleteDatabase(getResources().getString(R.string.databaseName));

        if(!DriveServiceHelper.isInitialized()){
            Intent intent = new Intent(context, AccountActivity.class);
            intent.putExtra("connectionType", AccountActivity.CONNECTION_TYPE_AUTOLOGIN);
            startActivityForResult (intent, 0);
        }
    }

//    @Override
//    protected void onStop() {
//        SaveDatabase();
//        super.onStop();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
//            Utilities.ShowCustomToast(context, "on result " + DriveServiceHelper.isInitialized());
            if (DriveServiceHelper.isInitialized()) {

            }
        }
    }

    public static void SaveDatabase(){
        if(!DriveServiceHelper.isInitialized()){
            Log.e("Drive", "Saving failed. DriveServiceHelper not initialized");
//            Utilities.ShowCustomToast(instance.context, "Not signed in");
            return;
        }
//        Utilities.ShowCustomToast(instance.context, "Saving file");

        String databaseFileName = instance.getResources().getString(R.string.databaseName);
        java.io.File databaseFile = instance.getDatabasePath(instance.getResources().getString(R.string.databaseName));
        final String databaseString;

        try {
            StringWriter writer = new StringWriter();
            CSVWriter csvWrite = new CSVWriter(writer);
            Cursor curCSV = getDatabase().userListDao().getStringToExport();
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount() - 1; i++)
                    arrStr[i] = curCSV.getString(i);
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            writer.close();
            databaseString = writer.toString().replace("\n", "\\n\n");
            Log.i("Drive", "Saving database:\n" + databaseString);
        } catch (Exception sqlEx) {
            Log.e("Drive", sqlEx.getMessage(), sqlEx);
//            Utilities.ShowCustomToast(instance.context, "Saving Failed");
            return;
        }

        DriveServiceHelper.instance.GetFileByName(databaseFileName).addOnSuccessListener(new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File file) {
                if(file != null) {
                    Log.i("Drive", file.getName() + " exists. Updating...");
                    DriveServiceHelper.instance.saveFile(file.getId(), file.getName(), databaseString).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            Utilities.ShowCustomToast(instance.context, "Saving complete");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Utilities.ShowCustomToast(instance.context, "Saving Failed");
                        }
                    });
                }
                else {
                    Log.i("Drive", "Database file does not exist. Creating one");
                    DriveServiceHelper.instance.createFile(databaseFileName).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.i("Drive", "Database file created. ID: " + s);
                            DriveServiceHelper.instance.saveFile(s, databaseFileName, databaseString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Utilities.ShowCustomToast(instance.context, "Saving complete");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Utilities.ShowCustomToast(instance.context, "Saving Failed");
                                    Log.e("Drive", "Failed to save database file. Exception: " + e);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Drive", "Failed to create database file. Exception: " + e);
//                            Utilities.ShowCustomToast(instance.context, "Saving Failed");
                        }
                    });
                }
            }
        });
    }
    public static void LoadDatabase() {
        if(!DriveServiceHelper.isInitialized()){
            Log.e("Drive", "Failed to load database file. DriveServiceHelper not initialized");
            Utilities.ShowCustomToast(instance.context, "Not signed in");
            return;
        }
        Utilities.ShowCustomToast(instance.context, "Loading");

        String databaseFileName = instance.getResources().getString(R.string.databaseName);
        DriveServiceHelper.instance.GetFileByName(databaseFileName).addOnSuccessListener(new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File file) {
                if(file != null) {
                    Log.i("File", file.getName());
                    DriveServiceHelper.instance.readFile(file.getId()).addOnSuccessListener(new OnSuccessListener<Pair<String, String>>() {
                        @Override
                        public void onSuccess(Pair<String, String> stringStringPair) {
                            String fileName = stringStringPair.first;
                            String fileContent = stringStringPair.second;
                            fileContent = fileContent.replace("\\n", "\n");
                            Log.i("Drive", "Loading complete. File:\n" + fileContent);


                            CSVReader csvReader = new CSVReader(new StringReader(fileContent));
                            String[] nextLine;
                            int count = 0;
                            StringBuilder columns = new StringBuilder();
                            StringBuilder value = new StringBuilder();
                            try {
                                while ((nextLine = csvReader.readNext()) != null) {
                                    // nextLine[] is an array of values from the line
                                    for (int i = 0; i < nextLine.length; i++) {
//                                        Log.i("CSV", nextLine[i]);
                                        if (count == 0) {
                                            if (i == nextLine.length - 1)
                                                columns.append(nextLine[i]);
                                            else
                                                columns.append(nextLine[i]).append(",");
                                        } else {
                                            if(i == 0)
                                                value.append("(");
                                            if (i == nextLine.length - 1)
                                                value.append("'").append(nextLine[i]).append("'").append("),");
                                            else
                                                value.append("'").append(nextLine[i]).append("',");
                                        }
                                    }
                                    count++;
                                }

                                String tableName = "UserList";
                                String queryString = "Insert INTO " + tableName + " (" + columns + ") " + "values " + value.subSequence(0, value.length() - 1);
//                                queryString = "Insert INTO UserList (title,movie_ids,date) values('hsba','[\"tt0094712\"]','2019-11-10'),('bat','[\"tt0372784\",\"tt2975590\"]','2019-11-10')";
                                SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString, new Object[]{});
//                                Log.i("Query", queryString + "\n " + query.getSql());
                                getDatabase().userListDao().deleteAll();
                                getDatabase().userListDao().insertDataRawFormat(query);
                                Utilities.ShowCustomToast(instance.context, "Loading complete");
                            }
                            catch (Exception e){
                                Log.e("Drive", "Failed to load database file. Exception: " + e);
                                Utilities.ShowCustomToast(instance.context, "Loading failed");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Drive", "Failed to load database file. File was null");
                            Utilities.ShowCustomToast(instance.context, "Loading failed");
                        }
                    });

                } else{
                    Log.e("Drive", "Failed to load database file. File was null");
                    Utilities.ShowCustomToast(instance.context, "Loading failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Drive", "Failed to load database file. Exception: " + e);
                Utilities.ShowCustomToast(instance.context, "Loading failed");
            }
        });
    }
    public static void ListDriveFiles(){
        if(!DriveServiceHelper.isInitialized()){
//            Utilities.ShowCustomToast(instance.context, "Not signed in");
            return;
        }
//        Utilities.ShowCustomToast(instance.context, "Listing");

        DriveServiceHelper.instance.queryFiles().addOnCompleteListener(new OnCompleteListener<FileList>() {
            @Override
            public void onComplete(@NonNull Task<FileList> task) {
                List<com.google.api.services.drive.model.File> files = task.getResult().getFiles();
                for (com.google.api.services.drive.model.File f :
                        files) {
                    Log.i("FileList", f.getName());

                }
            }
        });
    }


    public static AppDatabase getDatabase(){
        return database;
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}