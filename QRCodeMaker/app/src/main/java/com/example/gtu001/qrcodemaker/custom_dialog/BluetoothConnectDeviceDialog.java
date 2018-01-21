package com.example.gtu001.qrcodemaker.custom_dialog;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.R;

import java.io.OutputStream;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class BluetoothConnectDeviceDialog {

    private static final String TAG = BluetoothConnectDeviceDialog.class.getSimpleName();

    private Context context;
    private BluetoothDevice device;

    public BluetoothConnectDeviceDialog(Context context) {
        this.context = context;
    }

    public BluetoothConnectDeviceDialog setDevice(BluetoothDevice device) {
        this.device = device;
        return this;
    }

    public Dialog build() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.subview_dialog_conn_device);

        final TextView text_title = (TextView) dialog.findViewById(R.id.text_title);
        final TextView text_close = (TextView) dialog.findViewById(R.id.text_close);
        final TextView text_content = (TextView) dialog.findViewById(R.id.text_content);
        final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
        final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        text_title.setText("是否要連線設備?");
        text_content.setText(device.getName());

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connectDevice(device);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "連線失敗 : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.v(TAG, "# Listen onDismiss");
                btn_cancel.performClick();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(TAG, "# Listen onCancel");
                btn_cancel.performClick();
            }
        });

        return dialog;
    }

    private void connectDevice(BluetoothDevice device) {
        // 判斷那個裝置是不是你要連結的裝置，根據藍芽裝置名稱判斷
        Log.v(TAG, "# connectDevice : " + device.getName());
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // 一進來一定要停止搜尋
            mBluetoothAdapter.cancelDiscovery();

            // 連結到該裝置
            BluetoothSocket mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            mBluetoothSocket.connect();

            // 取得outputstream
            OutputStream mOutputStream = mBluetoothSocket.getOutputStream();

            // 送出訊息
            String message = "hello";
            mOutputStream.write(message.getBytes());
            mOutputStream.flush();
        } catch (Exception e) {
            Log.e(TAG, "# mReceiver ERROR : " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
