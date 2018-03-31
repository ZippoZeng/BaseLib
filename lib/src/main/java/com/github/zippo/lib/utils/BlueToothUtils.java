package com.github.zippo.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by leo on 2018/3/21.
 * email:fanrunqi@qq.com
 */

public class BlueToothUtils {
    public static BluetoothAdapter mBluetoothAdapter;
    public Context ctx;

    public BlueToothUtils(Context c) {
        this.ctx = c;
        getBluetoothAdapter();
    }

    /**
     * step1----------------------------------------------------------------------------------------
     * Get the BluetoothAdapter
     */
    public BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    //判断蓝牙是否打开
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public Boolean blueIsEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * step2  打开蓝牙
     */
    Boolean mEBTReceiverRegisterFlag;

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void enableBluetooth(enableBluetoothListener listener) {
        //判断是否支持蓝牙4.0
        if (!ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.i("BTU", "该设备不支持BLE蓝牙!!!");
            return;
        }
        this.ebtListener = listener;
        if (!mBluetoothAdapter.isEnabled()) {
            mEBTReceiverRegisterFlag = true;
            //打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ctx.startActivity(enableBtIntent);

            //蓝牙状态广播
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            ctx.registerReceiver(mEBTReceiver, filter);
        } else {
            listener.enableSuccess();
        }
    }

    public final BroadcastReceiver mEBTReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothAdapter.ACTION_STATE_CHANGED) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.i("BTU", "STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i("BTU", "STATE_ON");
                        ebtListener.enableSuccess();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i("BTU", "STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.i("BTU", "STATE_OFF");
                        break;
                }
            }
        }
    };
    enableBluetoothListener ebtListener;

    public interface enableBluetoothListener {
        void enableSuccess();
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * step3
     *
     * @return 获取已经配对设备
     */
    public Set<BluetoothDevice> queryingPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * step4 获取发现的设备
     *
     * @param listener 回调接口
     * @param scanTime 扫描时间
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void discoveringDevices(long scanTime, discoveringDevicesListener listener) {
        Boolean isStartSuccess = mBluetoothAdapter.startDiscovery();
        if (!isStartSuccess) {
            return;
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        ctx.registerReceiver(mDDReceiver, filter);

        this.ddListener = listener;
        new Timer().schedule(new TimerTask() {
            @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
            @Override
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
            }
        }, scanTime);
    }

    public final BroadcastReceiver mDDReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                ddListener.getDiscouveredDevice(device);
            }
        }
    };
    discoveringDevicesListener ddListener;

    public interface discoveringDevicesListener {
        void getDiscouveredDevice(BluetoothDevice device);
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 使蓝牙变为可发现状态
     */
    Boolean mEDReceiverRegisterFlag;

    public void enablingDiscoverability(int duration) {
        mEDReceiverRegisterFlag = true;
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
        ctx.startActivity(discoverableIntent);
        //蓝牙可发现状态广播
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        ctx.registerReceiver(mEDReceiver, filter);
    }

    public final BroadcastReceiver mEDReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                switch (blueState) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.i("BTU", "SCAN_MODE_CONNECTABLE_DISCOVERABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.i("BTU", "SCAN_MODE_CONNECTABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.i("BTU", "SCAN_MODE_NONE");
                        break;
                }
            }
        }
    };

    /**
     * ----------------------------------------------------------------------------------------------
     * step5 连接ble设备，并监听读写操作回调
     */
    static BluetoothGatt mBluetoothGatt;
    static BluetoothGattCharacteristic tempBluetoothGattCharacteristic;
    String bluetoothGattCharacteristicUuid;

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void bleConnect(BluetoothDevice bd, String bluetoothGattCharacteristicUuid) {
        if (null == bd) {
            return;
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
        this.bluetoothGattCharacteristicUuid = bluetoothGattCharacteristicUuid;
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothGatt = bd.connectGatt(ctx, false, mGattCallback);
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        //连接状态改变的回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 连接成功后启动服务发现
                Log.i("BTU", "启动服务发现");
                mBluetoothGatt.discoverServices();
            }
        }

        ;

        //发现服务的回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("BTU", "成功发现服务");
                List<BluetoothGattService> serviceList = mBluetoothGatt.getServices();
                for (BluetoothGattService bs : serviceList) {
                    Log.i("BTU", "BluetoothGattService:" + bs.getUuid().toString());
                    for (BluetoothGattCharacteristic ch : bs.getCharacteristics()) {
                        Log.i("BTU", "BluetoothGattCharacteristic:" + ch.getUuid().toString());
                        if (ch.getUuid().toString().equals(bluetoothGattCharacteristicUuid)) {
                            tempBluetoothGattCharacteristic = ch;
                            Log.i("BTU", "连接成功");
                        }
                    }
                }
            } else {
                Log.i("BTU", "服务发现失败，错误码为:" + status);
            }
        }

        ;

        //写操作的回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("BTU", "写入成功" + characteristic.getValue());
                groupWrite();
            }
        }

        ;

        //读操作的回调
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("BTU", "读取成功" + characteristic.getValue());
                dataListener.readSuccess(characteristic.getValue());
            }
        }

        //数据返回的回调（此处接收BLE设备返回数据）
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i("BTU", "设备返回数据" + characteristic.getValue());
            dataListener.replyData(characteristic.getValue());
        }

        ;
    };
    /**
     * 读写成功回调
     */
    interactiveDataListener dataListener;

    public interface interactiveDataListener {
        void writeSuccess(String data);

        void readSuccess(byte[] data);

        void replyData(byte[] data);
    }

    public void addInteractiveDataListener(interactiveDataListener dataListener) {
        this.dataListener = dataListener;
    }

    /**
     * 写入字符串,不限长度
     */
    String info;
    List<byte[]> writeDataList = new ArrayList<>();

    public void blueStringWrite(String info, String encode) {
        this.info = info;
        if (null == tempBluetoothGattCharacteristic) {
            Log.i("BTU", "请先连接蓝牙设备");
            return;
        }
        byte[] data = string2bytes(info, encode);
        byte[] temp = new byte[20];
        for (int i = 0; i < data.length; i++) {
            if ((i + 1) % 20 == 0) {
                writeDataList.add(temp);
            }
            temp[i % 20] = data[i];
        }
        int remainder = data.length % 20;
        if (remainder > 0) {
            byte[] lastArray = new byte[remainder];
            for (int j = 0; j < remainder; j++) {
                lastArray[j] = temp[j];
            }
            writeDataList.add(lastArray);
        }
        //循环写数据
        groupWrite();
    }

    int groupId = 0;

    private void groupWrite() {
        if (groupId > writeDataList.size() - 1) {
            dataListener.writeSuccess(info);
            //初始化
            writeDataList.clear();
            info = null;
            groupId = 0;
            return;
        }
        bleBytesWrite(writeDataList.get(groupId));
        groupId++;
    }

    /**
     * ble写数据(ble传输过程每次最大只能传输20个字节)
     * 可以请求更改MTU方法为：mBluetoothGatt.requestMtu(100);
     */
    public void bleBytesWrite(byte[] data) {
        mBluetoothGatt.setCharacteristicNotification(tempBluetoothGattCharacteristic, true);//设置该特征具有Notification功能
        tempBluetoothGattCharacteristic.setValue(data);//将指令放置进特征中
        tempBluetoothGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//设置回复形式
        mBluetoothGatt.writeCharacteristic(tempBluetoothGattCharacteristic);//开始写数据
    }

    /**
     * ble读数据
     */
    public void bleBytesRead() {
        mBluetoothGatt.readCharacteristic(tempBluetoothGattCharacteristic);
    }

    /**
     * 字符串转化成byte[]数组
     *
     * @param source
     * @return
     */
    public byte[] string2bytes(String source, String encode) {
        byte[] bytes = null;
        try {
            bytes = source.getBytes(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return bytes;
    }

    /**
     * 判断是否已经连接设备
     *
     * @return
     */
    public Boolean blueIsConnected() {
        if (mBluetoothAdapter.isEnabled() && tempBluetoothGattCharacteristic != null) {
            return true;
        }
        return false;
    }

    /**
     * ----------------------------------------------------------------------------------------------
     * 关闭蓝牙
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void closeBlueTooth() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
    }

    /**
     * ----------------------------------------------------------------------------------------------
     * 释放资源和关闭广播
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void release() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
        if (mEBTReceiverRegisterFlag) {
            ctx.unregisterReceiver(mEBTReceiver);
        }
        if (mEDReceiverRegisterFlag) {
            ctx.unregisterReceiver(mEDReceiver);
        }
        ctx.unregisterReceiver(mDDReceiver);
    }

}