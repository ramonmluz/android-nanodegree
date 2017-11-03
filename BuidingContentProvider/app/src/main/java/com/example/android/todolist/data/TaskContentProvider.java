/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class TaskContentProvider extends ContentProvider {

    private TaskDbHelper mTaskDbHelper;

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.

    private static final int TASKS = 100;
    private static final int TASKS_WITH_ID = 101;

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //Define a static buildUriMatcher method that associates URI's with their int match
    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Uri para ser acessada pelas tasks
        uriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PAHT_TASK, TASKS);

        // Uri para ser acessada por um item especifico
        uriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PAHT_TASK + "/#", TASKS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // [Hint] Declare the DbHelper as a global variable

        // Instancia a classe que criará a tebela, que por sua vez é crida usando a classe de contrato
        mTaskDbHelper = new TaskDbHelper(getContext());

        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        // TODO (1) Get access to the task database (to write new data to)

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        // TODO (2) Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        switch (match) {
            case TASKS:
                long id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

                // TODO (3) Insert new values into the database
                if (id > 0) {
                    // TODO (4) Set the value for the returnedUri and write the default case for unknown URI's
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to inset row into" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknow  uri" + uri);
        }
        // TODO (5) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // TODO (1) Get access to underlying database (read-only for query)

        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        // TODO (2) Write URI match code and set a variable to return a Cursor

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // TODO (3) Query for the tasks directory and write a default case
            case TASKS:
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknow  uri" + uri);
        }

        // TODO (4) Set a notification URI on the Cursor and return that Cursor
        getContext().getContentResolver().notifyChange(uri, null);
        return retCursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // TODO (1) Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        int mtch = sUriMatcher.match(uri);
        int tasksDelete = 0;

        switch (mtch) {
            case TASKS_WITH_ID:
                // TODO (2) Write the code to delete a single row of data
                // [Hint] Use selections to delete an item by its row ID
                String id = uri.getPathSegments().get(1);

                tasksDelete = db.delete(TaskContract.TaskEntry.TABLE_NAME,
                        TaskContract.TaskEntry._ID + "=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("");

        }

        if (tasksDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDelete;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int tasksUpdated;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksUpdated = mTaskDbHelper.getWritableDatabase().update(TaskContract.TaskEntry.TABLE_NAME,
                        values,"_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri" + uri);
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
