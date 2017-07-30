package udacity.nanodegree.android.com.waitlistexercise.data;

import android.provider.BaseColumns;

/**
 * Created by ramon on 15/07/17.
 */

public class WaitlistContract {



   public static class WaitlistEntry implements BaseColumns{

        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
