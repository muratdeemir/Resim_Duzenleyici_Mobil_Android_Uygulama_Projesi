package com.ss.resimduzenleyici.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ss.resimduzenleyici.AppConfigs;
import com.ss.resimduzenleyici.AppConstants;
import com.ss.resimduzenleyici.R;
import com.ss.resimduzenleyici.asynctasks.OpenBitmapAsyncTask;
import com.ss.resimduzenleyici.dialogs.SavePhotoDialog;
import com.ss.resimduzenleyici.dialogs.ShareIntentsDialog;
import com.ss.resimduzenleyici.utils.GLog;
import com.ss.resimduzenleyici.utils.StorageUtils;
import com.ss.resimduzenleyici.widget.TouchImageView;
import com.ss.resimduzenleyici.widget.VibrateOnTouchListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class EditPreviewActivity extends Activity {

    private String selected_photo; // đường dẫn đến bức ảnh gốc đã chọn để edit
    private Uri selected_uri; // uri đến bức ảnh gốc
    private String edited_photo; // đường dẫn đến ảnh lưu trong thư mục cache
    private Uri output_uri; // uri đến bức ảnh trong cache
    private File saveLocation; // Ảnh được lưu tại file này mỗi lần nhấn save

    private TouchImageView prv_Image; // preview
    private ProgressDialog progress;
    private OpenBitmapAsyncTask openTask;
    //private SaveBitmapAsyncTask saveTask;
    private ImageButton btnEdit;
    private ImageButton btnFrame;
    private ImageButton btnShare;
    private ImageButton btnSave;

    /**
     * lấy đường dẫn bức ảnh được chọn, load lên imageview, tạo file cache, ảnh
     * sau khi edit bằng editor được lưu vào cache.
     * <p/>
     * Có các trường hợp để mở một bức ảnh
     * 1. Mở trực tiếp chọn từ Gallery
     * 2.Chọn chụp từ Camera
     * 3. Nhận Action Filter Share, Send, Open, Edit, Pick từ các ứng dụng khác
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLog.v("Önceki Aktiviteyi Düzenle", "OnCreate");
        // Request Fullscreen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_preview_activity);
        setupViews();

        // Receive
        Intent receiveIntent = getIntent();
        String receiveAction = receiveIntent.getAction();
        String receiveType = receiveIntent.getType();
        if (receiveAction != null && receiveAction.equals(Intent.ACTION_SEND)) {
            assert receiveType != null;
            if (receiveType.startsWith("image/")
                    || receiveType.startsWith("*/*")) {
                Bundle receiveExtras = receiveIntent.getExtras();
                if (receiveExtras != null
                        && receiveExtras.containsKey(Intent.EXTRA_STREAM)) {
                    Uri receiveUri = (Uri) receiveExtras
                            .get(Intent.EXTRA_STREAM);
                    selected_photo = StorageUtils.getPathFromUri(receiveUri,
                            this);
                }
            }
        } else if (receiveAction != null
                && (receiveAction.equals(Intent.ACTION_VIEW)
                || receiveAction.equals(Intent.ACTION_EDIT) || receiveAction
                .equals(Intent.ACTION_PICK))) {

            Uri receiveUri = receiveIntent.getData();
            if (receiveUri != null) {
                selected_photo = StorageUtils.getPathFromUri(receiveUri, this);
            }
        } else {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            selected_photo = bundle.getString(AppConstants.SELECTED_PHOTO);
        }
        if (selected_photo != null && !selected_photo.equals("")) {
            File select_f = new File(selected_photo);
            selected_uri = Uri.fromFile(select_f);
            edited_photo = StorageUtils.getCacheDirectory().getPath() + "/"
                    + select_f.getName();
            File edited_f = new File(edited_photo);

            output_uri = Uri.fromFile(edited_f);
            openTask = new OpenBitmapAsyncTask.Builder(this, selected_photo)
                    .setImageView(prv_Image)
                    .setRequestSize(AppConfigs.getInstance().deviceWidth, AppConfigs.getInstance().deviceHeight)
                    .setProgress(progress)
                    .build();
            openTask.execute();
            GLog.v("Seçildi", selected_photo);
        }


    }

    private void setupViews() {
        prv_Image = (TouchImageView) findViewById(R.id.imgv_preview);
        //prv_Image.setDisplayType(DisplayType.FIT_TO_SCREEN);
        progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(
                R.string.loading_dialog_text));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(viewClick);

        btnFrame = (ImageButton) findViewById(R.id.btnFrame);
        btnFrame.setOnClickListener(viewClick);


        btnShare = (ImageButton) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(viewClick);


        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(viewClick);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            output_uri = UCrop.getOutput(data);
            prv_Image.setImageURI(output_uri);
            if (requestCode == AppConstants.REQUEST_CODE_EDIT_ACTIVITY) {
                openTask = new OpenBitmapAsyncTask.Builder(this, edited_photo)
                        .setImageView(prv_Image)
                        .setRequestSize(AppConfigs.getInstance().deviceWidth, AppConfigs.getInstance().deviceHeight)
                        .setProgress(progress)
                        .build();
                openTask.execute();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        GLog.v("EDIT PREVIEW", "ONDESTROY");
        Drawable drawable = prv_Image.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bmDr = (BitmapDrawable) drawable;
            bmDr.getBitmap().recycle();
        }

        // Cancel load image task
        if (openTask != null && !openTask.isCancelled()) {
            openTask.cancel(true);
        }

        super.onDestroy();
    }
    public OnClickListener viewClick = new OnClickListener() {


        @Override
        public void onClick(View v) {
            int id = v.getId();
            VibrateOnTouchListener vEx = new VibrateOnTouchListener(
                    EditPreviewActivity.this);
            vEx.onTouchVibrate(AppConstants.BUTTON_TOUCH_VIBRATE_TIME_MS);
            switch (id) {
                case R.id.btnEdit:
                    startEdit();
                    break;

                case R.id.btnFrame:
                    // frameEdit();

                    UCrop.of(selected_uri, output_uri)
                            .withAspectRatio(16, 9)
                            .withMaxResultSize(1280, 760)
                            .start(EditPreviewActivity.this);
                    break;

                case R.id.btnSave:
                    File editedFile = new File(edited_photo);
                    if (!editedFile.exists()) {
                        try {
                            StorageUtils.copyFile(new File(selected_photo), editedFile);
                        } catch (IOException e) {
                            Toast.makeText(EditPreviewActivity.this, "Bir Hata Oluştu..!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    progress.setMessage(getString(R.string.saving_dialog_text));
                    SavePhotoDialog savePhotoDialog = new SavePhotoDialog(EditPreviewActivity.this, edited_photo, progress);
                    savePhotoDialog.show();
                    break;

                case R.id.btnShare:
                    share();
                    break;
                default:
                    break;
            }

        }
    };

    private void share() {
        String sharePath = null;
        File edited_f = new File(edited_photo);
        if (edited_f.exists()) {
            sharePath = edited_photo;
        } else {
            sharePath = selected_photo;
        }

        ShareIntentsDialog share = new ShareIntentsDialog.Builder(this)
                .setDialogTitle(
                        getResources().getString(R.string.sharedialog_title))
                .setImagePath(sharePath).build();

        share.show();
    }

//    private void frameEdit() {
//        File edited_f = new File(edited_photo);
//        if (!edited_f.exists()) {
//
//            try {
//                StorageUtils.CopyFile(new File(selected_photo), edited_f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Intent intent = new Intent(EditPreviewActivity.this,
//                SelectFrameActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(AppConstants.SELECTED_PHOTO, edited_photo);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }


    private void startEdit() {
        Intent editIntent = new Intent(this, EditorActivity.class);
        editIntent.putExtra(AppConstants.SELECTED_PHOTO, selected_photo);
        editIntent.putExtra(EditorActivity.OUTPUT_PATH, edited_photo);
        startActivityForResult(editIntent, AppConstants.REQUEST_CODE_EDIT_ACTIVITY);
    }



}