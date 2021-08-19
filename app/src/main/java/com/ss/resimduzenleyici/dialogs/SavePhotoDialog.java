package com.ss.resimduzenleyici.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.resimduzenleyici.AppConstants;
import com.ss.resimduzenleyici.AppSettings;
import com.ss.resimduzenleyici.R;
import com.ss.resimduzenleyici.asynctasks.OnExecuteCompleteListener;
import com.ss.resimduzenleyici.asynctasks.SaveBitmapAsyncTask;
import com.ss.resimduzenleyici.utils.AsyncTaskUtils;
import com.ss.resimduzenleyici.utils.BitmapUtils;
import com.ss.resimduzenleyici.utils.MediaScanUtils;
import com.ss.resimduzenleyici.utils.StorageUtils;
import com.ss.resimduzenleyici.widget.VibrateOnTouchListener;

import java.io.File;


public class SavePhotoDialog extends Dialog {

    private ImageView prv_Image;

    private TextView dialogShare;
    private TextView dialogSave;
    private String previewPath;
    private Context context;

    private EditText saveFileName;
    private TextView dialog_info_image_size;

    private File saveLocation;
    private SaveBitmapAsyncTask saveTask;
    private ProgressDialog saveProgress;

    public SavePhotoDialog(Context context, String cachePath, ProgressDialog saveProgress) {
        super(context);
        this.context = context;
        this.previewPath = cachePath;
        this.saveProgress = saveProgress;
    }

    public void setPreviewImage(String path) {
        this.previewPath = path;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_complete_dialog_layout);
        prv_Image = (ImageView) findViewById(R.id.dialog_image_preview);

        dialogShare = (TextView) findViewById(R.id.dialog_btnShare);
        dialogSave = (TextView) findViewById(R.id.dialog_btnSave);
        saveFileName = (EditText) findViewById(R.id.dialog_info_filename);
        dialog_info_image_size = (TextView) findViewById(R.id.dialog_info_image_size);
        //dialogPreview.setOnClickListener(click);
        dialogShare.setOnClickListener(click);
        dialogSave.setOnClickListener(click);

        saveLocation = StorageUtils.getNewAppsFile();
        saveFileName.setText(saveLocation.getName());

        if (previewPath != null && !previewPath.equals("")) {
            Options option = new Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(previewPath, option);
            dialog_info_image_size.setText(this.context.getResources()
                    .getString(R.string.savecompletedialog_imagesize)
                    + option.outWidth + "x" + option.outHeight);
            int w = 300;
            int h = (int) (300 * ((float) option.outHeight / option.outWidth));
            option.inSampleSize = BitmapUtils.calculateSampleSize(option, w, h);
            option.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(previewPath, option);
            prv_Image.setImageBitmap(bm);
        } else {
            dismiss();
        }
    }

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            VibrateOnTouchListener vEx = new VibrateOnTouchListener(context);
            vEx.onTouchVibrate(AppConstants.BUTTON_TOUCH_VIBRATE_TIME_MS);
            switch (v.getId()) {
                case R.id.dialog_btnShare:
                    ShareIntentsDialog shareDialog = new ShareIntentsDialog.Builder(
                            context)
                            .setImagePath(previewPath)
                            .setDialogTitle(
                                    SavePhotoDialog.this.context
                                            .getResources()
                                            .getString(
                                                    R.string.sharedialog_title))
                            .build();

                    shareDialog.show();
                    dismiss();
                    break;
                case R.id.dialog_btnSave:
                    String fileName = saveFileName.getText().toString();
                    if (!fileName.toLowerCase().endsWith(".jpeg")) {
                        fileName = fileName + ".jpeg";
                    }
                    File folder = StorageUtils.getPublicDirectiry();
                    saveLocation = new File(folder, fileName);

                    if (saveLocation.exists()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setMessage("Dosya Adı : " + fileName + " olarak mevcut, Üzerine yazılsınmı ?")
                                .setPositiveButton("Evet", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        saveToFile();
                                    }
                                })
                                .setNegativeButton("Hayır", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        alertDialog.show();
                    } else {
                        saveToFile();
                    }

                    break;
                default:
                    break;
            }

        }
    };

    private void saveToFile() {
        dismiss();
        saveTask = new SaveBitmapAsyncTask.Builder()
                .setSaveLocation(saveLocation)
                .setSourcePath(previewPath)
                .setOnExecuteCompleteListener(new OnExecuteCompleteListener() {
                    @Override
                    public void onExecuteComplete(Object... params) {
                        MediaScanUtils.scanAndAddFiles(context, saveLocation.getAbsolutePath());
                        if (AppSettings.getInstance().SHOW_TOAST_MESSAGE) {
                            Toast.makeText(context, "kaydedildi" + saveLocation.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setProgressDialog(saveProgress).build();
        AsyncTaskUtils.executeTask(saveTask);
    }
}
