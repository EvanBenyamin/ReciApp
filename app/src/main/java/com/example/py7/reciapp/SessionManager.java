package com.example.py7.reciapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "session";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveSession(String Email){
        editor.putString("key_email", Email); // Storing string
        editor.commit();

    }

    //    public void getSession(){
//        String sesiemail = pref.getString("key_email", null);
//        return sesiemail;
//    }
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLogout(boolean islogout) {
        editor.putBoolean(KEY_IS_LOGGEDIN, islogout);
        // commit changes
        editor.remove(KEY_IS_LOGGEDIN);
        editor.clear();
        editor.commit();

        Log.d(TAG, "User login session destroyed!");
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
