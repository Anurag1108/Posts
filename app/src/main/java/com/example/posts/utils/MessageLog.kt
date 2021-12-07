package com.example.posts.utils

import android.util.Log

class MessageLog {
    companion object{

        /**
         * Set Log
         *
         * @param tag TAG of logcat
         * @param msg Message of Logcat
         */
        fun setLogCat(tag: String, msg: String?) {
            Log.i("$tag :", msg!!)
        }
    }
}