package jaangari.opensoft.iitkgp.jaankari;

import jaangari.opensoft.iitkgp.jaankari.util.SystemUiHider;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;


import jaangari.opensoft.iitkgp.jaangari.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class HomeScreen extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    public void videoIntent(View view){
        Intent intent = new Intent(getApplicationContext(),VideosActivity.class);
        startActivity(intent);
    }

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

//    protected void listClicked(String item){
//        if(item.equals("Update Profile Picture")){
//
//        }
//        else{
//            Intent intent = new Intent(getApplicationContext(),PasswordChangeActivity.class);
//            startActivity(intent);
//        }
//    }


    public void profilePreferences(View view){
        PopupMenu mPopupMenu = new PopupMenu(this, (ImageView)findViewById(R.id.profile_pic_home_screen));
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.home_screen, mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(HomeScreen.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                if(item.getTitle().equals("Update Password")){
                    Intent intent = new Intent(getApplicationContext(),PasswordChangeActivity.class);
                    startActivityForResult(intent, 0);
                }
                else if(item.getTitle().equals("Update Profile Picture")){
                    Intent intent = new Intent(getApplicationContext(),UpdateProfilePicActivity.class);
                    startActivityForResult(intent, 0);
                }
                return true;
            }
        });
        mPopupMenu.show();

//        Spinner mSpinner = (Spinner) findViewById(R.id.spinner_homeScreen_layout);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.profile_updates, android.R.layout.simple_selectable_list_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpinner.setAdapter(adapter);


//        List<String> mList = new ArrayList<String>();
//        mList.add("Update Profile Picture");
//        mList.add("Update Password");
//        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String> (getBaseContext(),android.R.layout.simple_dropdown_item_1line,mList);
//        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
//        ListView mListView = new ListView(this);
//        mListView.setAdapter(mArrayAdapter);
//        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.home_screen_layout);
//        mLinearLayout.addView(mListView);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//                String item = ((TextView)view).getText().toString();
//                Log.w("item", item);
//                listClicked(item);
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        String path = sp1.getString("proPic",null);


        Intent intent = new Intent(getApplicationContext(),VideoDownload.class);
        startService(intent);

        setContentView(R.layout.activity_home_screen);
        SearchManager mSearchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });
        mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(getComponentName()));
        ImageView mImageView = (ImageView)findViewById(R.id.profile_pic_home_screen);
        if(path!=null){
            try {
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(path);
//                yourSelectedImage.setDensity(Bitmap.DENSITY_NONE);
//                yourSelectedImage = Bitmap.createBitmap(yourSelectedImage,0,0,50,48);
                mImageView.setImageBitmap(yourSelectedImage);
            }catch(Exception e){
                Log.e("Profile Pic path","Error Could not find file at " + path);
                e.printStackTrace();
            }
        }


//        final View controlsView = findViewById(R.id.fullscreen_content_controls);
//        final View contentView = findViewById(R.id.fullscreen_content);


        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
//        mSystemUiHider = SystemUiHider.getInstance(this, controlsView, HIDER_FLAGS);
//        mSystemUiHider.setup();
//        mSystemUiHider
//                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//                    // Cached values.
//                    int mControlsHeight;
//                    int mShortAnimTime;
//
//                    @Override
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//                    public void onVisibilityChange(boolean visible) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                            // If the ViewPropertyAnimator API is available
//                            // (Honeycomb MR2 and later), use it to animate the
//                            // in-layout UI controls at the bottom of the
//                            // screen.
//                            if (mControlsHeight == 0) {
//                                mControlsHeight = controlsView.getHeight();
//                            }
//                            if (mShortAnimTime == 0) {
//                                mShortAnimTime = getResources().getInteger(
//                                        android.R.integer.config_shortAnimTime);
//                            }
//                            controlsView.animate()
//                                    .translationY(visible ? 0 : mControlsHeight)
//                                    .setDuration(mShortAnimTime);
//                        } else {
//                            // If the ViewPropertyAnimator APIs aren't
//                            // available, simply show or hide the in-layout UI
//                            // controls.
//                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
//                        }
//
//                        if (visible && AUTO_HIDE) {
//                            // Schedule a hide().
//                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
//                        }
//                    }
//                });

        // Set up the user interaction to manually show or hide the system UI.
//        controlsView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TOGGLE_ON_CLICK) {
//                    mSystemUiHider.toggle();
//                } else {
//                    mSystemUiHider.show();
//                }
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.logout_button).setOnTouchListener(mDelayHideTouchListener);
        Button mLogoutButton = (Button)findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(PRINT_SERVICE, "Attempting Login");
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("sLogin",null);
                Ed.putString("emailId",null);
                Ed.putString("proPic",null);
                Ed.commit();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
//        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


//    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };
//
//    Handler mHideHandler = new Handler();
//    Runnable mHideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mSystemUiHider.hide();
//        }
//    };
//
//    /**
//     * Schedules a call to hide() in [delay] milliseconds, canceling any
//     * previously scheduled calls.
//     */
//    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, delayMillis);
//    }
}
