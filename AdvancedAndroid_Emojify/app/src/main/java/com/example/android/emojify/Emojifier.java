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

package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import timber.log.Timber;

class Emojifier {

    private static final float EMOJI_SCALE_FACTOR = .9f;
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

    /**
     * Method for detecting faces in a bitmap, and drawing emoji depending on the facial
     * expression.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static Bitmap detectFacesAndOverlayEmoji(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Timber.d( "detectFaces: number of faces = " + faces.size() );

        Bitmap resultBitmap = picture;

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {

            // Iterate through the faces
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);
                // Get the appropriate emoji for each face
                 Bitmap emojiBitmap = null;

                 switch (whichEmoji(face)){
                     case SMILE:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.smile);
                         break;
                     case FROWN:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frown);
                         break;
                     case LEFT_WINK:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwink);
                         break;
                     case RIGHT_WINK:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwink);
                         break;
                     case LEFT_WINK_FROWN:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwinkfrown);
                         break;
                     case  RIGHT_WINK_FROWN:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwinkfrown);
                         break;
                     case CLOSED_EYE_SMILE:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_smile );
                         break;
                     case CLOSED_EYE_FROWN:
                         emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_frown );
                         break;
                 }

                resultBitmap  = addBitmapToFace(resultBitmap, emojiBitmap, face);
            }
        }
        // Release the detector
        detector.release();
       return resultBitmap;
    }

    /**
     * Combina a imagem original aos bitmaps de emoji
     *
     * @param backgroundBitmap A imagem original
     * @param emojiBitmap      O emoji escolhido
     * @param face             O rosto detectado
     * @return O bitmap final, incluindo os emojis sobre os rostos
     */
    private static Bitmap addBitmapToFace(Bitmap  backgroundBitmap,  Bitmap emojiBitmap, Face face) {

           // Inicializa os resultados bitmap para serem cópias mutáveis da imagem original
            Bitmap resultBitmap = Bitmap.createBitmap(Bitmap.createBitmap(backgroundBitmap.getWidth(),
                    backgroundBitmap.getHeight(), backgroundBitmap.getConfig()));

           // Dimensiona o emoji para ele ficar melhor no rosto
           float scaleFactor  = EMOJI_SCALE_FACTOR;

           // Determina o tamanho do emoji para corresponder com a largura do rosto e manter a proporção
           int newEmojiWith = (int) (face.getWidth() * scaleFactor );
           int newEmojiHeght = (int) (emojiBitmap.getHeight() *
                   newEmojiWith / emojiBitmap.getWidth() * scaleFactor );

            // Dimensiona emoj
            emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWith, newEmojiHeght, false);

            float emojiPsitionX = (face.getPosition().x + face.getWidth() / 2)
                    - emojiBitmap.getWidth() / 2;

            float emojiPsitionY = (face.getPosition().x + face.getHeight() / 3)
                    - emojiBitmap.getWidth() / 3;

           // Cria o canvas e desenha os bitmaps nele
          Canvas canvas  = new Canvas( resultBitmap );
          canvas.drawBitmap(backgroundBitmap, 0, 0, null);
          canvas.drawBitmap(emojiBitmap, emojiPsitionX, emojiPsitionY, null);

        return  resultBitmap;
    }



    /**
     * Determines the closest emoji to the expression on the face, based on the
     * odds that the person is smiling and has each eye open.
     *
     * @param face The face for which you pick an emoji.
     */

    private static Emoji whichEmoji(Face face) {

        // TODO (1): Change the return type of the whichEmoji() method from void to Emoji.
        // Log all the probabilities
        Timber.d("whichEmoji: smilingProb = " + face.getIsSmilingProbability() );
        Timber.d( "whichEmoji: leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability()) ;
        Timber.d( "whichEmoji: rightEyeOpenProb = " + face.getIsRightEyeOpenProbability() );


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;

        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;


        // Determine and log the appropriate emoji
        Emoji emoji;
        if (smiling) {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK;
            } else if (rightEyeClosed && !leftEyeClosed) {
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed) {
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK_FROWN;
            } else if (rightEyeClosed && !leftEyeClosed) {
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed) {
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }

        // Log the chosen Emoji
        Timber.d( "whichEmoji: " + emoji.name() );

        return emoji;
    }


    // Enum for all possible Emojis
    private enum Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }

}
