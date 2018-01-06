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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

import java.util.List;

// This activity will display a custom Android image composed of three body parts: head, body, and legs
public class AndroidMeActivity extends AppCompatActivity {

    public static final String HEAD_INDEX = "headIndex";
    public static final String BODY_INDEX = "bodyIndex";
    public static final String LEG_INDEX = "legIndex";
    public static final String LIST_INDEX = "listIndex_3";
    // Variables to store the values for the list index of the selected images
    // The default value will be index = 0
    private int headIndex;
    private int bodyIndex;
    private int legIndex;

    private BodyPartFragment headFragment;
    private BodyPartFragment bodyFragment;
    private BodyPartFragment legFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
            // Use setListindex(int index) to set the list index for all BodyPartFragments.

            Bundle bundle = getIntent().getExtras();
            headIndex = bundle.getInt(HEAD_INDEX);
            bodyIndex = bundle.getInt(BODY_INDEX);
            legIndex = bundle.getInt(LEG_INDEX);
            fillBodyFragment();
        } else {
            // Create a new head BodyPartFragment
            headFragment = new BodyPartFragment();
            onRestoreInstanceState(savedInstanceState);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fillBodyFragment(headIndex, headFragment, AndroidImageAssets.getHeads());
            startFragmentTransaction(headFragment, fragmentManager, R.id.head_container, true);
        }
    }

    private void fillBodyFragment() {
        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Create a new head BodyPartFragment
        headFragment = new BodyPartFragment();
        // Set the list of image id's for the head fragment and set the position to the second image in the list
        fillBodyFragment(headIndex, headFragment, AndroidImageAssets.getHeads());
        startFragmentTransaction(headFragment, fragmentManager, R.id.head_container, false);

        bodyFragment = new BodyPartFragment();
        fillBodyFragment(bodyIndex, bodyFragment, AndroidImageAssets.getBodies());
        startFragmentTransaction(bodyFragment, fragmentManager, R.id.body_container, false);

        legFragment = new BodyPartFragment();
        fillBodyFragment(legIndex, legFragment, AndroidImageAssets.getLegs());
        startFragmentTransaction(legFragment, fragmentManager, R.id.leg_container, false);
    }

    private void fillBodyFragment(int headIndex, BodyPartFragment bodyPartFragment, List<Integer> listImageAsserts) {
        bodyPartFragment.setImageIds(listImageAsserts);
        bodyPartFragment.setListIndex(headIndex);
    }

    private void startFragmentTransaction(BodyPartFragment bodyPartFragment, FragmentManager fragmentManager, int idContainer, boolean replace) {
        if (replace) {
            fragmentManager.beginTransaction()
                    .replace(idContainer, bodyPartFragment);
        } else {
            fragmentManager.beginTransaction()
                    .add(idContainer, bodyPartFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LIST_INDEX, headIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        headIndex = savedInstanceState.getInt(LIST_INDEX);
    }

}
