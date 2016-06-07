package com.example.beyongha.softwareexperimentteam15;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.provider.MediaStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {
    final static int ACT_EDIT = 0;
    final static int PICK_FROM_CAMERA = 0;
    final static int PICK_FROM_ALBUM = 1;
    final static int CROP_FROM_IMAGE = 2;

    public EditText ask_etext;
    public Button ask_btn;
    public Button image_btn;
    public ImageView image_view;
    private Uri mImageCaptureUri;
    private String absoultePath;

    JSONObject jsonObject;
    final String TAG = "(InfoActivity)DEBUG:";
    String responseString;
    String urlString = "http://172.30.1.49:8080/test/SaveArticle.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ask_etext = (EditText) findViewById(R.id.ask_etext);
        ask_btn = (Button) findViewById(R.id.ask_btn);
        image_btn = (Button) findViewById(R.id.image_btn);
        image_view = (ImageView)findViewById(R.id.image_view);



        ask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToServer();

                Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
                intent.putExtra("textin", ask_etext.getText().toString());
                startActivityForResult(intent, ACT_EDIT);
                finish();
            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] noticeboard_list = new String[]{"사진 촬영", "앨범 선택", "취소"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(QuestionActivity.this);
                dlg.setTitle("MENU");

                dlg.setItems(noticeboard_list, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (noticeboard_list[which] == "사진 촬영") {
                            doTakePohotoAction();
                        } else if (noticeboard_list[which] == "앨범 선택") {
                            doTakeAlbumAction();
                        } else if (noticeboard_list[which] == "취소") {
                            dialog.dismiss();
                        }
                    }
                });
                dlg.show();
            }
        });

    }


    public void sendTextToServer() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    URI url = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(url);

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("userId", "ID4"));
                    nameValuePairs.add(new BasicNameValuePair("text", ask_etext.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("title", "you"));

                    Log.d(TAG, nameValuePairs.toString());

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    httpClient.execute(httpPost);
                }   catch (URISyntaxException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }   catch (ClientProtocolException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }   catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void doTakePohotoAction() { // 카메라 촬영 후 이미지 가져오기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성성
        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        /// "tmp_" +
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }
    public void doTakeAlbumAction() { // 앨범에서 이미지 가져오기
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 300); // crop한 이미지  x,y 축 크기
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 3); // crop 박스 x,y 축 비율
                intent.putExtra("aspectY", 2);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();

                //crop된 이미지를 저장하기 위한 file 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/makecropimage/" + System.currentTimeMillis() + ".jpg";
                Log.d(TAG, filePath);

                if(extras != null) {
                    Bitmap photo = extras.getParcelable("data"); // crop 된 bitmap
                    image_view.setImageBitmap(photo);

                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }

        }
    }
    ///storage/emulated/0/makecropimage/1465303680237.jpg
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // 폴더를 생성하여 이미지를 저장
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/makecropimage";
        File dirctory_makecropimage = new File(dirPath);
        if (!dirctory_makecropimage.exists()) { // 폴더가 없으면 생성
            dirctory_makecropimage.mkdir();
        }

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            //sendBroadcast 를 통해서 crop된 사진을 앨범에 보이도록 갱신
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
            Uri.fromFile(copyFile);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
