package com.anachat.chatsdk.uimodule.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anachat.chatsdk.AnaCore;
import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.library.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by lookup on 29/09/17.
 */

public class MediaPreviewActivity extends AppCompatActivity {

    public static String FILE_PATH = "file_path";
    public static String FILE_TYPE = "file_type";
    String filePath;
    int type;
    //    int mandatory;
    private ImageView ivPreview;
    private ImageView ivSend;
    private ImageView ivClose;

    public static Intent startIntent(Context context, String filePath,
                                     int type) {
        Intent intent = new Intent(context, MediaPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FILE_PATH, filePath);
        bundle.putInt(FILE_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_preview);
        ivSend = findViewById(R.id.send_image);
        ivSend.setColorFilter(Color.parseColor
                (PreferencesManager.getsInstance(this).getThemeColor()));
        ivPreview = findViewById(R.id.iv_preview);
        ivClose = findViewById(R.id.close_page);
        type = getIntent().getExtras().getInt(FILE_TYPE);
        filePath = getIntent().getExtras().getString(FILE_PATH);
        if (filePath != null) {
            Uri uri = Uri.fromFile(new File(filePath));
            Glide.with(MediaPreviewActivity.this)
                    .load(uri)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_placeholder)
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform())
                    .into(ivPreview);
        } else finish();

        ivSend.setOnClickListener(view -> {
            MessageResponse.MessageResponseBuilder responseBuilder
                    = new MessageResponse.MessageResponseBuilder
                    (MediaPreviewActivity.this.getApplicationContext());
            responseBuilder.inputMedia(AnaCore.getLastMessage(MediaPreviewActivity.this))
                    .buildMediaInput(filePath, type)
                    .build().send();
            finish();
        });
        ivClose.setOnClickListener(view -> finish());
    }
}
