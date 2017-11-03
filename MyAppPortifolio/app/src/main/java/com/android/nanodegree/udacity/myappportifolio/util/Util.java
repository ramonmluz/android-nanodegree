package com.android.nanodegree.udacity.myappportifolio.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.android.nanodegree.udacity.myappportifolio.R;

/**
 * Created by ramon on 02/11/17.
 */

public class Util {

    /**
     * Mensagens enviada
     *
     * @param titleMessage
     * @param message
     * @param context
     */
    public static void sendMessageDilog(String titleMessage, String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(titleMessage);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (DialogInterface dialogInterface, int i) -> dialogInterface.dismiss());
        alertDialog.show();
    }

    /**
     * Retorna a classificação dos filmes a partir das preferências do usuário
     * @return
     */
    public static String getSharedPreferences(Activity activity) {
        //Obtem a forma de classifiação a partir das preferências do usuário.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        // Obtem a clissificação selecionada
        String sortType = prefs.getString(activity.getString(R.string.movie_list_key), activity.getString(R.string.popular_value));
        return sortType;
    }

}
