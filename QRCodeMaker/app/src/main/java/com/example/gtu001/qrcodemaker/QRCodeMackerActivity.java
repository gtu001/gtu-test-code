package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.EmailUtil;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gtu001 on 2017/12/19.
 */

public class QRCodeMackerActivity extends Activity {

    private static final String TAG = QRCodeMackerActivity.class.getSimpleName();

    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout layout = LayoutViewHelper.createContentView(this);

        editText = new EditText(this);
        editText.setHint("請輸入網址或內容");
        layout.addView(editText);

        Button makeBtn = new Button(this);
        makeBtn.setText("產生QR Code");
        layout.addView(makeBtn);
        makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String qrCodeData = editText.getText().toString();
                    if (qrCodeData.isEmpty()) {
                        Toast.makeText(QRCodeMackerActivity.this, "請輸入內容", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    String subname = "png";
//                    int widthAndHeight = 1200;
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    QRCodeUtil.getInstance().createQRCode(qrCodeData, subname, baos, widthAndHeight);
//                    byte[] arry = baos.toByteArray();
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(arry, 0 , arry.length);

                    int fixedWidth = (int) (getWitdh() * 0.8);

                    Bitmap bitmap = QRCode.from(qrCodeData)//
                            .to(ImageType.PNG)//
                            .withSize(fixedWidth, fixedWidth)//
                            .withCharset("UTF-8")//
                            .withErrorCorrection(ErrorCorrectionLevel.L)//
                            .withHint(EncodeHintType.CHARACTER_SET, "UTF-8")//
//                            .stream();//
                            .bitmap();//

                    imageView.setImageBitmap(bitmap);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(QRCodeMackerActivity.this, "錯誤 : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button makeBtn2 = new Button(this);
        makeBtn2.setText("寄送QR Code");
        layout.addView(makeBtn2);
        makeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String qrCodeData = editText.getText().toString();
                    if (qrCodeData.isEmpty()) {
                        Toast.makeText(QRCodeMackerActivity.this, "請輸入內容", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int fixedWidth = (int) (getWitdh() * 0.8);

                    File file = QRCode.from(qrCodeData)//
                            .to(ImageType.PNG)//
                            .withSize(fixedWidth, fixedWidth)//
                            .withCharset("UTF-8")//
                            .withErrorCorrection(ErrorCorrectionLevel.L)//
                            .withHint(EncodeHintType.CHARACTER_SET, "UTF-8")//
//                            .stream();//
                            .file();

                    Context context = QRCodeMackerActivity.this.getApplicationContext();
                    String emailTo = "gtu001@gmail.com";
                    String emailCC = "gtu001@gmail.com";
                    String subject = "QRCode產生器";
                    String emailText = qrCodeData;
                    List<String> filePaths = new ArrayList<String>();
                    filePaths.add(file.getAbsolutePath());

                    EmailUtil.sendEmail(context, emailTo, emailCC, subject, emailText, filePaths);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(QRCodeMackerActivity.this, "錯誤 : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView = new ImageView(this);
        layout.addView(imageView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
    }

    private int getWitdh(){
        Context context = QRCodeMackerActivity.this.getApplicationContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }
}
