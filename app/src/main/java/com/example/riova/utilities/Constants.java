package com.example.riova.utilities;

public class Constants {

    // SharedPreferences file and keys.
    public static final String KEY_PREFERENCE_NAME = "riova_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_VOLUME = "master_volume";
    private static final String KEY_REMINDERS = "reminders_enabled";
    private static final String KEY_KEEP_LOGGED_IN = "keep_logged_in";

    public static final String KEY_COLLECTION_USERS = "User";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_IMAGE = "image";


    // Tags to find and retrieve pages' fragments
    public static final String TAG_DASHBOARD = "page_dashboard";
    public static final String TAG_LOGIN = "page_login";
    public static final String TAG_RESETPASSWORD = "page_resetpassword";
    public static final String TAG_JOURNAL = "page_journal";
    public static final String TAG_GENWELL = "page_genwell";
    public static final String TAG_MEDITATION = "page_meditation";
    public static final String TAG_MINDFULNESS = "page_mindfulness";
    public static final String TAG_SETTINGS = "page_settings";
    public static final String TAG_REGISTER = "page_register";
    public static final String TAG_LOGOUT = "page_logout";

    public static final String[] page_tag_array = {TAG_DASHBOARD, TAG_LOGIN, TAG_RESETPASSWORD, TAG_JOURNAL,
    TAG_GENWELL, TAG_MEDITATION, TAG_MINDFULNESS, TAG_SETTINGS, TAG_REGISTER, TAG_LOGOUT};

}
