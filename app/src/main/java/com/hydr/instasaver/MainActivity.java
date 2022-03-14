package com.hydr.instasaver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.hydr.saverinsta.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;

public class MainActivity extends AppCompatActivity implements OnUserEarnedRewardListener, View.OnClickListener {

    public ImageView image;
    Button downldImg;
    public static final int PERMISSION_WRITE = 0;
    EditText edt;
    String username;
    TextView follow, followin, username_, full_;
    String url, verified;
    AdView mAdView;
    public String usrnf;
    public ProgressBar progressBar;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private final String TAG = "Activity_Main";
    ImageView verifeid;
    String interli = "ca-app-pub-8750718022354184/4454442025";
    String rewarded = "ca-app-pub-8750718022354184/3141360355";
    FloatingActionButton fabMain, fabOne, fabTwo, fabThree, fabFoor;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
    private InterstitialAd mInterstitialAd;
    public boolean connected = false;
    ConnectivityManager connectivityManager;
    String usefinal;
    ReviewInfo reviewInfo;
    Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        define();
        initFabMenu();
        Interstitial();
        type = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf");
        final Typeface type1 = Typeface.createFromAsset(getAssets(), "fonts/ubuntubold.ttf");


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            openMenu();

            new GuideView.Builder(this)
                    .setTitle("Type the Username")
                    .setContentText("Type here the exact username you want !")
                    .setGravity(Gravity.auto) //optional
                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                    .setTargetView(edt)
                    .setContentTextSize(16)//optional
                    .setTitleTextSize(14)//optional
                    .setTitleTypeFace(type1)
                    .setContentTypeFace(type)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {

                            new GuideView.Builder(MainActivity.this)
                                    .setTitle("View Picture")
                                    .setContentText("Click here to see the picture !")
                                    .setGravity(Gravity.auto) //optional
                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                    .setTargetView(image)
                                    .setContentTextSize(13)//optional
                                    .setTitleTextSize(14)//optional
                                    .setTitleTypeFace(type1)
                                    .setContentTypeFace(type)
                                    .setGuideListener(new GuideListener() {
                                        @Override
                                        public void onDismiss(View view) {

                                            new GuideView.Builder(MainActivity.this)
                                                    .setTitle("Save Picture")
                                                    .setContentText("Click here to save the picture in your device ")
                                                    .setGravity(Gravity.auto) //optional
                                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                    .setTargetView(fabOne)
                                                    .setTitleTypeFace(type1)
                                                    .setContentTypeFace(type)
                                                    .setContentTextSize(13)//optional
                                                    .setTitleTextSize(14)//optional
                                                    .setGuideListener(new GuideListener() {
                                                        @Override
                                                        public void onDismiss(View view) {

                                                            new GuideView.Builder(MainActivity.this)
                                                                    .setTitle("Share Picture")
                                                                    .setContentText("you can share the picture to anywhere !")
                                                                    .setGravity(Gravity.auto) //optional
                                                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                                    .setTargetView(fabThree)
                                                                    .setContentTextSize(13)//optional
                                                                    .setTitleTextSize(14)//optional
                                                                    .setTitleTypeFace(type1)
                                                                    .setContentTypeFace(type)
                                                                    .setGuideListener(new GuideListener() {
                                                                        @Override
                                                                        public void onDismiss(View view) {

                                                                            new GuideView.Builder(MainActivity.this)
                                                                                    .setTitle("Watch ads for support us")
                                                                                    .setContentText("If you benefited from the application, click here and watch the advertising video to encourage us!")
                                                                                    .setGravity(Gravity.auto) //optional
                                                                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                                                    .setTargetView(downldImg)
                                                                                    .setContentTextSize(13)//optional
                                                                                    .setTitleTypeFace(type1)
                                                                                    .setContentTypeFace(type)
                                                                                    .setTitleTextSize(14)//optional
                                                                                    .build()
                                                                                    .show();
                                                                            checkPermission();


                                                                        }
                                                                    })
                                                                    .build()
                                                                    .show();


                                                        }
                                                    })
                                                    .build()
                                                    .show();

                                        }
                                    })
                                    .build()
                                    .show();
                        }
                    })
                    .build()
                    .show();

        }
       connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
           ;
       else
           FancyToast.makeText(this, "No Internet Connection !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        downldImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    rewardedInterstitialAd.show(/* Activity */ MainActivity.this, (OnUserEarnedRewardListener) MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SingleViewActivity.class);
                if (url != null) {
                    intent.putExtra("ID_IMAGE", url);
                }
                startActivity(intent);
                //startActivity(intent,ActivityOptions.makeCustomAnimation(Activity_Main.this,R.anim.fade_in,R.anim.fade_out).toBundle());
            }
        });

    }


    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        Log.i(TAG, "onUserEarnedReward");
        // TODO: Reward the user!
    }

    public void Interstitial() {

        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAd();
            }
        });
        InterstitialAd.load(this, interli, adRequest1, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


    }

    public void define() {
        full_ = findViewById(R.id.tv_address);
        follow = findViewById(R.id.follow);
        followin = findViewById(R.id.followin);
        image = findViewById(R.id.profile_image);
        username_ = findViewById(R.id.tv_name);
        downldImg = findViewById(R.id.rightim);
        verifeid = findViewById(R.id.img_verified);
        edt = findViewById(R.id.edt);
        edt.setOnEditorActionListener(editorListener);
        progressBar = findViewById(R.id.progres);
        verifeid.setVisibility(View.GONE);

        full_.setTypeface(type);
        username_.setTypeface(type);
        followin.setTypeface(type);
        follow.setTypeface(type);
        edt.setTypeface(type);
    }

    public void loadAd() {
        RewardedInterstitialAd.load(MainActivity.this, rewarded,
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.i(TAG, "onAdFailedToShowFullScreenContent");
                            }

                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.i(TAG, "onAdShowedFullScreenContent");
                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.i(TAG, "onAdDismissedFullScreenContent");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e(TAG, "onAdFailedToLoad");
                    }
                });
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                username = edt.getText().toString(); //gets you the contents of edit text

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    try {

                        if (getHDProfilePicFromUsername(username) == null) {
                            FancyToast.makeText(getApplicationContext(), "incorrect username !", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                        } else {
                            Data data = getHDProfilePicFromUsername(username);
                            url = data.image;
                            verified = data.verified;
                            usrnf = data.user_name;
                            usefinal = data.user_name;
                            full_.setText("@ " + data.user_name);
                            username_.setText(data.full_name);
                            follow.setText(data.followers);
                            followin.setText(data.following);
                            if (verified.equals("true"))
                                verifeid.setVisibility(View.VISIBLE);
                            else verifeid.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);


                            Glide.with(getApplicationContext())
                                    .load(url)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(image);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    FancyToast.makeText(getApplicationContext(), "No Internet Connection !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }


            return false;
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

    private class getDataFromUrl extends AsyncTask<String, Void, String> {
        Context mContext;

        public getDataFromUrl(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(arg0[0]);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                return jsonStr;
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null)
                FancyToast.makeText(getApplicationContext(), "incorrect username !", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            else {
                String url = s.replace('|', ',').split(",")[1];
                Log.d(TAG, "Link: " + url);
            }
        }
    }


    private Data getHDProfilePicFromUsername(String username) throws ExecutionException, InterruptedException, JSONException {
        JSONObject jObject;
        String profileInfo = new getDataFromUrl(getApplicationContext()).execute("https://www.instagram.com/" + username + "/?__a=1").get();

        if (profileInfo == null)
            FancyToast.makeText(this, "incorrect username !", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();

        else {
            jObject = new JSONObject(profileInfo);
            jObject = jObject.getJSONObject("graphql");
            jObject = jObject.getJSONObject("user");
            String user_name = jObject.getString("username");
            String full_name = jObject.getString("full_name");
            String image = jObject.getString("profile_pic_url_hd");
            String verified = jObject.getString("is_verified");
            String followers = jObject.getJSONObject("edge_followed_by").getString("count");
            String following = jObject.getJSONObject("edge_follow").getString("count");

            return new Data(user_name, full_name, image, followers, following, verified);
        }
        return null;
    }

    static final class Data {
        public String image;
        public String followers;
        public String following;
        public String user_name;
        public String full_name;
        public String verified;

        public Data(String user_name, String full_name, String image, String followers, String following, String verified) {
            this.user_name = user_name;
            this.full_name = full_name;
            this.image = image;
            this.verified = verified;
            this.followers = followers;
            this.following = following;
        }
    }

    public static class HttpHandler {

        private static final String TAG = HttpHandler.class.getSimpleName();

        public HttpHandler() {
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }


    public class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... url) {
            File f = new File(Environment.DIRECTORY_PICTURES + "/picinsta");
            if (!f.isFile()) {
                if (!(f.isDirectory())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.createDirectory(Paths.get(f.getAbsolutePath()));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        f.mkdir();
                    }
                }
            }

            DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ss");
            String date = dateFormat.format(new Date());


            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(true)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES + "/picinsta", usefinal + ".jpg");
            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"parag.jpeg")


            manager.enqueue(request);
            return f.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            FancyToast.makeText(getApplicationContext(), "Image Saved !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        }
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);

            return false;
        }
        if ((WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);

            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initFabMenu() {
        fabMain = findViewById(R.id.fabMain);
        fabOne = findViewById(R.id.fabOne);
        fabTwo = findViewById(R.id.fabTwo);
        fabThree = findViewById(R.id.fabThree);
        fabFoor = findViewById(R.id.fabFoor);

        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabFoor.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabFoor.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
        fabFoor.setOnClickListener(this);

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabFoor.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabFoor.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void handleFabOne() {
        Log.i(TAG, "handleFabOne: ");
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.fabMain:
                Log.i(TAG, "onClick: fab main");
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabOne:
                Log.i(TAG, "onClick: fab one");
                handleFabOne();

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    if (checkPermission()) {

                        if (url == null)
                            FancyToast.makeText(this, "No image to Download !", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();


                        else new Downloading().execute(url);

                    }
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                    connected = true;
                } else {
                    connected = false;
                    FancyToast.makeText(this, "No Internet Connection !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

                break;
            case R.id.fabTwo:
                Log.i(TAG, "onClick: fab two");

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (usrnf == null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/" + "prhydar" + "/"));
                        startActivity(browserIntent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/" + usrnf + "/"));
                        startActivity(browserIntent);
                    }
                } else {
                    FancyToast.makeText(this, "No Internet Connection !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

                break;
            case R.id.fabThree:
                Log.i(TAG, "onClick: fab three");


                try {
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        Uri bmpUri = getLocalBitmapUri(image);
                        if (bmpUri != null) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "This picture is the instagram profile of *" + usrnf + "*\n\n" + "You can download this app to view any profile picture and download it.\n\nFrom here : " + "https://play.google.com/store/apps/details?id=com.hydr.saverinsta");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Instagram Profile Saver App");
                            shareIntent.setType("image/*");
                            startActivity(Intent.createChooser(shareIntent, "Share Image"));
                        }
                    } else
                        FancyToast.makeText(this, "No Internet Connection !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.fabFoor:

                try {
                    ReviewManager manager = null;

                    manager = ReviewManagerFactory.create(this);

                    Task<ReviewInfo> request = manager.requestReviewFlow();
                    ReviewManager finalManager = manager;
                    request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                        @Override
                        public void onComplete(@NonNull Task<ReviewInfo> task) {

                            if (task.isSuccessful()) {

                                reviewInfo = task.getResult();
                                Task<Void> flow = finalManager.launchReviewFlow(MainActivity.this, reviewInfo);

                                flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void result) {

                                    }
                                });
                            } else {
                                FancyToast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}

