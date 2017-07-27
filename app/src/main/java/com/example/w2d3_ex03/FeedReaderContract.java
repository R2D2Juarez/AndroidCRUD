package com.example.w2d3_ex03;

import android.provider.BaseColumns;

/**
 * Created by pharr on 26/07/17.
 */

public final class FeedReaderContract {
    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
