package com.zkadisa.personalmoviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.DataHandling.CSVReader;
import com.zkadisa.personalmoviedb.DataHandling.CSVWriter;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.DataHandling.TMDbExternalIDs;
import com.zkadisa.personalmoviedb.DataHandling.TMDbReader;
import com.zkadisa.personalmoviedb.DataHandling.TMDbSearchResult;
import com.zkadisa.personalmoviedb.Misc.Utilities;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class MainActivity extends BaseActivityClass{

    private Context context = this;
    private static MainActivity instance;
    private static AppDatabase database;

    private static Gson gson = new Gson();

    private LinearLayout upcomingMovieContainer;
    private LinearLayout popularMovieContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivitydesign);
        instance = this;

        upcomingMovieContainer = findViewById(R.id.upcomingMovieContainer);
        popularMovieContainer = findViewById(R.id.popularMovieContainer);
        DownloadFromTMDbToHorizontalScrollView(TMDbReader.GetTopUpcomingMoviesURL(), 10, upcomingMovieContainer);
        DownloadFromTMDbToHorizontalScrollView(TMDbReader.GetPopularMoviesURL(), 10, popularMovieContainer);

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

    private void DownloadFromTMDbToHorizontalScrollView(String url, int count, LinearLayout linearLayout){
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e != null){
                            Log.e("MainActivity_159", e.getMessage());
                            return;
                        }
                        TMDbSearchResult searchResult = gson.fromJson(result, TMDbSearchResult.class);

                        for (int i = 0; i < searchResult.total_results && i < count; i++){
                            Ion.with(context)
                                    .load(TMDbReader.GetExternalIDsURL(searchResult.results[i].id))
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if(e != null){
                                                Log.e("MainActivity_172", e.getMessage());
                                                return;
                                            }
                                            TMDbExternalIDs searchResult = gson.fromJson(result, TMDbExternalIDs.class);
                                            Ion.with(context)
                                                    .load(OMDbReader.GetSearchByIDURL(searchResult.imdb_id))
                                                    .asJsonObject()
                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                        @Override
                                                        public void onCompleted(Exception e, JsonObject result) {
                                                            if(e != null){
                                                                Log.e("MainActivity_183", e.getMessage());
                                                                return;
                                                            }
                                                            Entry entry  = gson.fromJson(result, Entry.class);
                                                            LinearLayout layout = (LinearLayout)View.inflate(MainActivity.this, R.layout.horizontalentrylayoutitem, null);
                                                            ((TextView)layout.findViewById(R.id.textView)).setText(entry.Title);
                                                            ((TextView)layout.findViewById(R.id.subTextView)).setText(entry.Released);
                                                            ImageButton imageButton = layout.findViewById(R.id.posterImageView);
                                                            Ion.with((ImageView)imageButton).load(entry.Poster);
                                                            imageButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    SearchEntryListItem item = new SearchEntryListItem(entry.Title, entry.Year, entry.imdbID, entry.Type, entry.Poster);
                                                                    Intent intent = new Intent(context, DetailsActivity.class);
                                                                    intent.putExtra("data", (Serializable) item);
                                                                    context.startActivity(intent);
                                                                }
                                                            });
                                                            linearLayout.addView(layout);

                                                        }
                                                    });

                                        }
                                    });
                        }

                    }
                });
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
                List<File> files = task.getResult().getFiles();
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

    public static void showSoftKeyboard(Activity activity, View view){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
