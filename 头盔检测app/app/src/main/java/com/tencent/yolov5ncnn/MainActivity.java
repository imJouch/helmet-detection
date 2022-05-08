package com.tencent.yolov5ncnn;//// Tencent is pleased to support the open source community by making ncnn available.
////
//// Copyright (C) 2020 THL A29 Limited, a Tencent company. All rights reserved.
////
//// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
//// in compliance with the License. You may obtain a copy of the License at
////
//// https://opensource.org/licenses/BSD-3-Clause
////
//// Unless required by applicable law or agreed to in writing, software distributed
//// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
//// CONDITIONS OF ANY KIND, either express or implied. See the License for the
//// specific language governing permissions and limitations under the License.
//
//package com.tencent.yolov5ncnn;
//
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;



import android.media.ExifInterface;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Consumer;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import java.io.File;

import static android.Manifest.*;
import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {

    public static final int TAKE_PHOTO = 2;
    private static final int SELECT_IMAGE = 1;
    //获取实例
    private ImageView imageView;
    private Uri imageUri;
    private Bitmap temp = null;
    private Bitmap bitmap = null;
    private Bitmap yourSelectedImage = null;
    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        boolean ret_init = yolov5ncnn.Init(getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

        Button openCamera = (Button) findViewById(R.id.buttonPhoto);
        imageView = (ImageView) findViewById(R.id.imageView);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//创建File对象存放相机拍摄的照片，第一个参数为获取存放目录方法，第二个参数为输出图片名
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    } else {
                        outputImage.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//7.0以上系统，11必须调用FileProvider的getUriForFile方法将File对象转换为封装过的Urii对象，第一个参数为Context，第二个参数为为任意字符串，第三个参数为File对象
                imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.cameratest.fileprovider", outputImage);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    openCamera();
                }
            }
        });
        Button buttonImage = (Button) findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_IMAGE);
//                  Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//                  albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                  albumIntent.putExtra(Intent.EXTRA_INTENT, albumIntent);
//                  startActivityForResult(albumIntent, SELECT_IMAGE);
            }
        });

        Button buttonDetect = (Button) findViewById(R.id.buttonDetect);
        buttonDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (yourSelectedImage == null)
                    return;

                YoloV5Ncnn.Obj[] objects = yolov5ncnn.Detect(yourSelectedImage, false);

                showObjects(objects);
            }
        });

        Button buttonDetectGPU = (Button) findViewById(R.id.save);
        buttonDetectGPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, "保存成功请到相册查看", Toast.LENGTH_LONG).show();
                //saveBitmapFile(temp);
                //Toast.makeText(MainActivity.this, "已保存", Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void saveBitmapFile(Bitmap bmp){

//
//        String sd = "/storage/emulated/0/";
//        String name = String.valueOf(System.currentTimeMillis());
//        String filename = sd + name + ".jpeg" ;
//        File file=new File(filename);//将要保存图片的路径和图片名称
//        //    File file =  new File("/sdcard/1delete/1.png");/延时较长
//        try {
//            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            intent.setData(Uri.fromFile(file));
//            sendBroadcast(intent);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 首先保存图片
        File appDir = new File(MainActivity.this.getExternalFilesDir(null).getAbsolutePath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库

        try {
            String saveImg=  MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
//            Toast toast;
//            if(saveImg != null)
//            {
//                toast = Toast.makeText(MainActivity.this, "截图成功请到图库查看", Toast.LENGTH_LONG);
//
//            }
//            else {
//                toast = Toast.makeText(MainActivity.this, "未能截取到图片信息", Toast.LENGTH_LONG);
//
//            }
//            toast.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新

        MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,    Uri.fromFile(new File(file.getPath()))));

    }
/*
 * 保存文件，文件名为当前日期
 */
//public void saveBitmap(Bitmap bitmap, String bitName) {
//    Toast.makeText(MainActivity.this, "有空输入！\n请重新输入！", Toast.LENGTH_SHORT).show();
//    Log.d(TAG,"Build.BRAND============"+Build.BRAND);
//    String fileName;
//    File file;
//    if (Build.BRAND.equals("xiaomi")) { // 小米手机
//        fileName = MainActivity.this.getExternalFilesDir(null).getAbsolutePath()+ "/DCIM/Camera/" + bitName;
//    }else if (Build.BRAND.equals("Huawei")){
//        fileName = MainActivity.this.getExternalFilesDir(null).getAbsolutePath() + "/DCIM/Camera/" + bitName;
//    } else {  // Meizu 、Oppo
//        fileName = MainActivity.this.getExternalFilesDir(null).getAbsolutePath() + "/DCIM/" + bitName;
//    }
//    file = new File(fileName);
//
//    if (file.exists()) {
//        file.delete();
//    }
//    FileOutputStream out;
//    try {
//        out = new FileOutputStream(file);
//        // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
//        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
//            out.flush();
//            out.close();
//// 插入图库
//            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);
//
//        }
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//
//    }
//    // 发送广播，通知刷新图库的显示
//    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
//
//}



    private void openCamera() {
        //用Intent打开相机action
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //用putExtra方法指定输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //启动活动
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    private void showObjects(YoloV5Ncnn.Obj[] objects)
    {
        if (objects == null)
        {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Bitmap rgba;
        // draw objects on bitmap
        rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        final int[] colors = new int[] {
                Color.rgb( 54,  67, 244),
                Color.rgb( 99,  30, 233),
                Color.rgb(176,  39, 156),
                Color.rgb(183,  58, 103),
                Color.rgb(181,  81,  63),
                Color.rgb(243, 150,  33),
                Color.rgb(244, 169,   3),
                Color.rgb(212, 188,   0),
                Color.rgb(136, 150,   0),
                Color.rgb( 80, 175,  76),
                Color.rgb( 74, 195, 139),
                Color.rgb( 57, 220, 205),
                Color.rgb( 59, 235, 255),
                Color.rgb(  7, 193, 255),
                Color.rgb(  0, 152, 255),
                Color.rgb( 34,  87, 255),
                Color.rgb( 72,  85, 121),
                Color.rgb(158, 158, 158),
                Color.rgb(139, 125,  96)
        };

        Canvas canvas = new Canvas(rgba);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        Paint textbgpaint = new Paint();
        textbgpaint.setColor(Color.WHITE);
        textbgpaint.setStyle(Paint.Style.FILL);

        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(70);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++)
        {
            paint.setColor(colors[i % 19]);

            canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);

            // draw filled text inside image
            {
                String text = objects[i].label + " = " + String.format("%.1f", objects[i].prob * 100) + "%";

                float text_width = textpaint.measureText(text)+40;
                float text_height = - textpaint.ascent() + textpaint.descent()+20;

                float x = objects[i].x;
                float y = objects[i].y - text_height;
                if (y < 0)
                    y = 0;
                if (x + text_width > rgba.getWidth())
                    x = rgba.getWidth() - text_width;

                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);

                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
            }
        }

        imageView.setImageBitmap(rgba);
        saveBitmapFile(rgba);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        yourSelectedImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        imageView.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    try {
                        bitmap = decodeUri(selectedImage);
                        yourSelectedImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }

            default:
                break;
        }
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
    {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 640;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

        // Rotate according to EXIF
        int rotate = 0;
        try
        {
            ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(selectedImage));
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        }
        catch (IOException e)
        {
            Log.e("MainActivity", "ExifInterface IOException");
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}

