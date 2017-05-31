package com.codiansoft.foodtruck;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.ImageProcessor;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codiansoft.foodtruck.Utils.AppConstants.UPDATE_PROFILE;

public class EditTruckOwner extends Fragment implements View.OnClickListener {
    View view;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private EditText mUser_name, mPhone, mPassword, mDescription;
    private ImageButton mSaveBtn;
    private ImageProcessor imageProcessor;
    private Uri mSelectedImageUri;
    private static Uri mOutputFileUri;
    private Bitmap bitmap;
    private File output;
    ImageView profilePic;
    UserModel userModel;
    ProgressBar progressBar;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.owner_profile, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.optoin_icon);
        // searchfield = (EditText) view.findViewById(searchfield);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) getActivity()).opendrawel();
            }
        });

        ((DrawerActivity) getActivity()).hidetoolbar();


        init(v);
        ((DrawerActivity) getActivity()).hidetoolbar();
        loadData();
        view = v;
        return v;
    }


    public void init(View view) {

        progressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        userModel = MyApplication.getInstance().getPrefManger().getUserProfile();
        profilePic = (ImageView) view.findViewById(R.id.profile_image);
        imageProcessor = new ImageProcessor(getContext());
        mUser_name = (EditText) view.findViewById(R.id.fullname);
        mPhone = (EditText) view.findViewById(R.id.mobile);
        mPassword = (EditText) view.findViewById(R.id.pass);
        mDescription = (EditText) view.findViewById(R.id.description);
        mSaveBtn = (ImageButton) view.findViewById(R.id.savebtn);

        mSaveBtn.setOnClickListener(this);
        profilePic.setOnClickListener(this);

    }

    void loadData() {
        if (!userModel.getProfilepic().equals("")) {
            Picasso
                    .with(getActivity())
                    .load(AppConstants.IMAGE_URL + userModel.getProfilepic())
                    .fit() // will explain later
                    .into(profilePic);
        }
        mUser_name.setText(userModel.getName());
        mPhone.setText(userModel.getPhonenumber());
        mDescription.setText(userModel.getDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_image:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    openImageIntent();
                }

                break;

            case R.id.savebtn:

                if (validationChecker(mUser_name, mPhone, mPassword)) {
                    onSaveEvent(mUser_name.getText().toString(), mPassword.getText().toString(), mPhone.getText().toString(), mDescription.getText().toString(), ImageProcess(output));
                }
                break;

        }
    }


    private void openImageIntent() {
        //final File root = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), "Vehicle Log Book Images");
        final File root = new File(Environment.getExternalStorageDirectory(), AppConstants.APP_MAIN_DIR + "/Profile Images");
        root.mkdirs();


        final String filename = "image" + new Date().getTime() + ".jpg";
        output = new File(root, filename);
        Log.d("output", output + "");
        mOutputFileUri = Uri.fromFile(output);
        final List<Intent> cameraIntents = new ArrayList<>();

        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            cameraIntents.add(intent);
        }
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        final Intent chooserIntent = Intent.createChooser(pickIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, 100);
    }

    /*
    * Result for add photo event
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 100) {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }
                    if (isCamera) {
                        mSelectedImageUri = mOutputFileUri;
                        profilePic.setImageBitmap(imageProcessor.LoadAndResizeBitmap(output.getAbsolutePath(), profilePic));
                        profilePic.setTag(output.getAbsolutePath());


                    } else {
                        mSelectedImageUri = data == null ? null : data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(mSelectedImageUri,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                        bitmap = imageProcessor.LoadAndResizeBitmap(picturePath, profilePic);
                        profilePic.setImageBitmap(bitmap);
                        profilePic.setTag(picturePath);

                    }


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void onSaveEvent(final String name, final String password, final String phone, final String desctipn, final String image) {

        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, UPDATE_PROFILE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {


                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data1 = Jobject.getJSONObject("data");
                                JSONObject Data = Data1.getJSONObject("user_data");
                                UserModel userModel = new UserModel();
                                userModel.setApi_secret(Data1.getString("api_secret"));
                                userModel.setUsername(Data.getString("name"));
                                userModel.setId(Data.getString("user_id"));
                                userModel.setEmail(Data.getString("email"));
                                userModel.setName(Data.getString("name"));
                                userModel.setProfilepic(Data.getString("image"));
                                userModel.setDescription(Data.getString("food_title"));
                                userModel.setPhonenumber(Data.getString("contact_no"));
                                userModel.setUsertype(Data.getString("user_type"));
                                userModel.setIstrackon(Data.getString("track_location"));
                                MyApplication.getInstance().getPrefManger().setUserProfile(userModel);
                                //             Toast.makeText(getActivity(),"Data : "+MyApplication.getInstance().getPrefManger().getUserProfile().getName(),Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception ee) {
                        }


                        if (progressBar.isShown()) progressBar.setVisibility(View.GONE);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 409:
                            utilities.dialog("Already Exist ", getActivity());
                            break;
                        case 400:
                            utilities.dialog("Connection Problemx", getActivity());
                            break;
                        default:
                            utilities.dialog("Connection Problem", getActivity());
                            break;
                    }
                }
                if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

                //Toast.makeText(getActivity(),"Error : "+MyApplication.getInstance().getPrefManger().getUserProfile().getName(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", userModel.getApi_secret());
                params.put("name", name);
                params.put("password", password);
                params.put("contact_no", phone);
                params.put("image", image);
                params.put("food_title", desctipn);
                params.put("user_type", userModel.getUsertype());

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);
    }


    public void onBackpress() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                openImageIntent();
                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.

            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot take picture", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String ImageProcess(File filename) {


        try {

            Log.d("1234", filename.getAbsolutePath());
            //    Bitmap bitmap = new ImageProcessor(getContext()).decodeFile(filename.toString());
            Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
            //Bitmap bitmap = BitmapFactory.decodeFile(filename.getAbsolutePath());


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            String img_str = Base64.encodeToString(image, Base64.DEFAULT);
            Log.d("image", img_str);
            return img_str;

        } catch (Exception e) {
            Log.d("sss", "ImageProcess: " + e.getMessage());
            e.getMessage();
            return "";
        }


    }

    public static Boolean validationChecker(EditText name, EditText Phone, EditText Password) {
        boolean valid = true;
        if (name.getText().toString().isEmpty()) {
            name.setError("enter  Name");
            valid = false;
        } else {
            name.setError(null);
        }
        if (Phone.getText().toString().isEmpty()) {
            Phone.setError("enter  Phone");
            valid = false;
        } else {
            Phone.setError(null);
        }

        if (!Password.getText().toString().isEmpty() && !(Password.getText().toString().length() >= 6)) {
            Password.setError("enter Password");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }

}
