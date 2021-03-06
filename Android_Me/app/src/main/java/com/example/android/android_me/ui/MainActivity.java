/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.android_me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

// This activity is responsible for displaying the master list of all images
// Implement the MasterListFragment callback, OnImageClickListener
public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    public static final String LIST_INDEX = "listIndex";
    public static final String HEAD_INDEX = "headIndex";
    public static final String BODY_INDEX = "bodyIndex";
    public static final String LEG_INDEX = "legIndex";
    // Variables to store the values for the list index of the selected images
    // The default value will be index = 0
    private int headIndex;
    private int bodyIndex;
    private int legIndex;
    private int bodyPartNumber;
    private int listIndex;

    // TODO (3) Create a variable to track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO (4) If you are making a two-pane display, add new BodyPartFragments to create an initial Android-Me image
        // Also, for the two-pane display, get rid of the "Next" button in the master list fragment
        if (findViewById(R.id.android_me_linear_layout) != null) {
            mTwoPane = true;

            // The "Next" button launches a new AndroidMeActivity
            Button nextButton = (Button) findViewById(R.id.next_button);
            nextButton.setVisibility(View.GONE);

            // Get a reference to the GridView in the fragment_master_list xml layout file
            GridView gridView = (GridView) findViewById(R.id.images_grid_view);
            gridView.setNumColumns(2);

            // Only create new fragments when there is no previously saved state
            if (savedInstanceState == null) {

                // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
                // Use setListindex(int index) to set the list index for all BodyPartFragments

                // Create a new head BodyPartFragment
                BodyPartFragment headFragment = new BodyPartFragment();

                // Set the list of image id's for the head fragment and set the position to the second image in the list
                headFragment.setImageIds(AndroidImageAssets.getHeads());

                // Get the correct index to access in the array of head images from the intent
                // Set the default value to 0
                int headIndex = getIntent().getIntExtra("headIndex", 0);
                headFragment.setListIndex(headIndex);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit();

                BodyPartFragment bodyFragment = new BodyPartFragment();
                bodyFragment.setImageIds(AndroidImageAssets.getBodies());
                int bodyIndex = getIntent().getIntExtra("bodyIndex", 0);
                bodyFragment.setListIndex(bodyIndex);

                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

                BodyPartFragment legFragment = new BodyPartFragment();
                legFragment.setImageIds(AndroidImageAssets.getLegs());
                int legIndex = getIntent().getIntExtra("legIndex", 0);
                legFragment.setListIndex(legIndex);

                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit();
            } else {
                setFragmentTablet(headIndex, R.id.head_container);
                setFragmentTablet(headIndex, R.id.body_container);
                setFragmentTablet(headIndex, R.id.leg_container);
            }

        } else {
            mTwoPane = false;
        }

    }


    // Define the behavior for onImageSelected
    public void onImageSelected(int position) {
        bodyPartNumber = position / 12;

        // Store the correct list index no matter where in the image list has been clicked
        // This ensures that the index will always be a value between 0-11
        listIndex = position - 12 * bodyPartNumber;

        fillFragmentTabletImageSelected();

        if (mTwoPane == false) {
            Bundle b = fillBundleListIndex();


            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, AndroidMeActivity.class);
            intent.putExtras(b);

            // The "Next" button launches a new AndroidMeActivity
            Button nextButton = (Button) findViewById(R.id.next_button);
            nextButton.setOnClickListener(v -> startActivity(intent));
        }

    }

    private void fillFragmentTabletImageSelected() {
        if (mTwoPane) {
            switch (bodyPartNumber) {
                case 0:
                    setFragmentTablet(listIndex, R.id.head_container);
                    break;
                case 1:
                    setFragmentTablet(listIndex, R.id.body_container);
                    break;
                case 2:
                    setFragmentTablet(listIndex, R.id.leg_container);
                    break;
            }
        }
    }

    private void setFragmentTablet(int listIndex, int bodyPartyContainer) {
        BodyPartFragment bodyPartFragment;// Create a new head BodyPartFragment
        bodyPartFragment = new BodyPartFragment();
        bodyPartFragment.setListIndex(listIndex);
        bodyPartFragment.setImageIds(AndroidImageAssets.getHeads());
        // Add the fragment to its container using a FragmentManager and a Transaction
        beginFragmentManage(bodyPartFragment, bodyPartyContainer);
    }

    @NonNull
    private Bundle fillBundleListIndex() {
        // Set the currently displayed item for the correct body part fragment
        switch (bodyPartNumber) {
            case 0:
                headIndex = listIndex;
                break;
            case 1:
                bodyIndex = listIndex;
                break;
            case 2:
                legIndex = listIndex;
                break;
            default:
                break;
        }

        return setBundle();
    }

    @NonNull
    private Bundle setBundle() {
        // Put this information in a Bundle and attach it to an Intent that will launch an AndroidMeActivity
        Bundle b = new Bundle();
        b.putInt(HEAD_INDEX, headIndex);
        b.putInt(BODY_INDEX, bodyIndex);
        b.putInt(LEG_INDEX, legIndex);
        return b;
    }

    private void beginFragmentManage(BodyPartFragment bodyPartFragment, int container) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(container, bodyPartFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(setBundle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        headIndex = savedInstanceState.getInt(HEAD_INDEX);
        bodyIndex = savedInstanceState.getInt(LIST_INDEX);
        legIndex = savedInstanceState.getInt(LEG_INDEX);
    }
}
