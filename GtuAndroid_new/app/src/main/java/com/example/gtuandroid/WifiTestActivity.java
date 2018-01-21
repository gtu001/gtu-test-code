package com.example.gtuandroid;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 未完成 TODO
 */
public class WifiTestActivity extends Activity {

    ArrayAdapter<String> aryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_wifitest);
        back();
        
        setWifiOn();
        readEapConfig();

        ListView listview = (ListView) findViewById(R.id.listView1);
        final TextView textview = (TextView) findViewById(R.id.textView1);

        final String[] dayArray = new String[] { "" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dayArray);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                textview.setText("你選的是" + arg0.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                textview.setText("@你選的是" + dayArray[arg2]);
            }
        });
    }

    void readEapConfig() {
        /* Get the WifiService */
        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        /* Get All WIfi configurations */
        List<WifiConfiguration> configList = wifi.getConfiguredNetworks();
        /*
         * Now we need to search appropriate configuration i.e. with name
         * SSID_Name
         */
        
        if(configList == null){
            return;
        }
        
        for (int i = 0; i < configList.size(); i++) {
//            if (configList.get(i).SSID.contentEquals("\"SSID_NAME\"")) {
            if (true) {
                /* We found the appropriate config now read all config details */
                Iterator<WifiConfiguration> iter = configList.iterator();
                WifiConfiguration config = configList.get(i);

                /*
                 * I dont think these fields have anything to do with EAP config
                 * but still will print these to be on safe side
                 */
                try {
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[SSID]" + config.SSID);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[BSSID]" + config.BSSID);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[HIDDEN SSID]" + config.hiddenSSID);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PASSWORD]" + config.preSharedKey);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[ALLOWED ALGORITHMS]===");
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[LEAP]" + config.allowedAuthAlgorithms.get(AuthAlgorithm.LEAP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[OPEN]" + config.allowedAuthAlgorithms.get(AuthAlgorithm.OPEN));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[SHARED]" + config.allowedAuthAlgorithms.get(AuthAlgorithm.SHARED));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[GROUP CIPHERS]===");
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[CCMP]" + config.allowedGroupCiphers.get(GroupCipher.CCMP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[TKIP]" + config.allowedGroupCiphers.get(GroupCipher.TKIP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP104]" + config.allowedGroupCiphers.get(GroupCipher.WEP104));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP40]" + config.allowedGroupCiphers.get(GroupCipher.WEP40));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[KEYMGMT]===");
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[IEEE8021X]" + config.allowedKeyManagement.get(KeyMgmt.IEEE8021X));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[NONE]" + config.allowedKeyManagement.get(KeyMgmt.NONE));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA_EAP]" + config.allowedKeyManagement.get(KeyMgmt.WPA_EAP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA_PSK]" + config.allowedKeyManagement.get(KeyMgmt.WPA_PSK));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PairWiseCipher]===");
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[CCMP]" + config.allowedPairwiseCiphers.get(PairwiseCipher.CCMP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[NONE]" + config.allowedPairwiseCiphers.get(PairwiseCipher.NONE));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[TKIP]" + config.allowedPairwiseCiphers.get(PairwiseCipher.TKIP));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[Protocols]===");
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[RSN]" + config.allowedProtocols.get(Protocol.RSN));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA]" + config.allowedProtocols.get(Protocol.WPA));
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PRE_SHARED_KEY]" + config.preSharedKey);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP Key Strings]===");
                    String[] wepKeys = config.wepKeys;
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 0]" + wepKeys[0]);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 1]" + wepKeys[1]);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 2]" + wepKeys[2]);
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 3]" + wepKeys[3]);

                } catch (Exception e) {
                    Toast toast1 = Toast.makeText(this, "Failed to write Logs to ReadConfigLog.txt", 3000);
                    Toast toast2 = Toast.makeText(this, "Please take logs using Logcat", 5000);
                    Log.e("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "Could not write to ReadConfigLog.txt" + e.getMessage());
                }
                //測試 by gtu001 ↓↓↓↓↓↓
//                try{
//                    Class[] wcClasses = WifiConfiguration.class.getClasses();
//                    ToStringUtil.Setting.FULL.apply();
//                    for(Field f : config.getClass().getDeclaredFields()){
//                        for(Class<?> clz : wcClasses){
//                            if(clz == f.getType()){
//                                f.setAccessible(true);
//                                Object value = f.get(config);
//                                if(value!=null){
//                                    Log.v(value.getClass().getSimpleName(), ToStringUtil.toString(value));
//                                }
//                            }
//                        }
//                    }
//                }catch(Exception ex){
//                    ex.printStackTrace();
//                }
                //測試 by gtu001 ↑↑↑↑↑↑

                /* reflection magic */
                /* These are the fields we are really interested in */
                try {
                    final String INT_PRIVATE_KEY = "private_key";
                    final String INT_PHASE2 = "phase2";
                    final String INT_PASSWORD = "password";
                    final String INT_IDENTITY = "identity";
                    String INT_EAP = "eap";
                    String INT_CLIENT_CERT = "client_cert";
                    String INT_CA_CERT = "ca_cert";
                    String INT_ANONYMOUS_IDENTITY = "anonymous_identity";
                    final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";

                    // Let the magic start
                    Class[] wcClasses = WifiConfiguration.class.getClasses();
                    // null for overzealous java compiler
                    Class wcEnterpriseField = null;

                    for (Class wcClass : wcClasses){
                        if (wcClass.getName().equals(INT_ENTERPRISEFIELD_NAME)) {
                            wcEnterpriseField = wcClass;
                            break;
                        }
                    }
                       
                    boolean noEnterpriseFieldType = false;
                    if (wcEnterpriseField == null)
                        noEnterpriseFieldType = true; // Cupcake/Donut access
                                                      // enterprise settings
                                                      // directly

                    Field wcefAnonymousId = null, wcefCaCert = null, wcefClientCert = null, wcefEap = null, wcefIdentity = null, wcefPassword = null, wcefPhase2 = null, wcefPrivateKey = null;
                    Field[] wcefFields = WifiConfiguration.class.getFields();
                    // Dispatching Field vars
                    for (Field wcefField : wcefFields) {
                        if (wcefField.getName().trim().equals(INT_ANONYMOUS_IDENTITY))
                            wcefAnonymousId = wcefField;
                        else if (wcefField.getName().trim().equals(INT_CA_CERT))
                            wcefCaCert = wcefField;
                        else if (wcefField.getName().trim().equals(INT_CLIENT_CERT))
                            wcefClientCert = wcefField;
                        else if (wcefField.getName().trim().equals(INT_EAP))
                            wcefEap = wcefField;
                        else if (wcefField.getName().trim().equals(INT_IDENTITY))
                            wcefIdentity = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PASSWORD))
                            wcefPassword = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PHASE2))
                            wcefPhase2 = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PRIVATE_KEY))
                            wcefPrivateKey = wcefField;
                    }
                    Method wcefSetValue = null;
                    if (!noEnterpriseFieldType) {
                        for (Method m : wcEnterpriseField.getMethods())
                            // System.out.println(m.getName());
                            if (m.getName().trim().equals("value")) {
                                Log.d("!noEnterpriseFieldType", "特別找:" + m);
                                wcefSetValue = m;
                                break;
                            }
                    }

                    /* EAP Method */
                    String result = null;
                    Object obj = null;
                    if (!noEnterpriseFieldType) {
                        obj = wcefSetValue.invoke(wcefEap.get(config), null);
                        String retval = (String) obj;
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP METHOD]" + retval);
                    }

                    /* phase 2 */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefPhase2.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PHASE 2 AUTHENTICATION]" + result);
                    }

                    /* Anonymous Identity */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefAnonymousId.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP ANONYMOUS IDENTITY]" + result);
                    }

                    /* CA certificate */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefCaCert.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP CA CERTIFICATE]" + result);
                    }

                    /* private key */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefPrivateKey.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PRIVATE KEY]" + result);
                    }

                    /* Identity */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefIdentity.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP IDENTITY]" + result);
                    }

                    /* Password */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefPassword.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PASSWORD]" + result);
                    }

                    /* client certificate */
                    if (!noEnterpriseFieldType) {
                        result = (String) wcefSetValue.invoke(wcefClientCert.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP CLIENT CERT]" + result);
                        Toast toast1 = Toast.makeText(this, "All config data logged to ReadConfigLog.txt", 3000);
                        Toast toast2 = Toast.makeText(this, "Extract ReadConfigLog.txt from SD CARD", 5000);
                    }

                } catch (Exception e) {
                    Toast toast1 = Toast.makeText(this, "Failed to write Logs to ReadConfigLog.txt", 3000);
                    Toast toast2 = Toast.makeText(this, "Please take logs using Logcat", 5000);
                    Log.e("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "Could not write to ReadConfigLog.txt" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void setWifiOn(){
        Log.v("WIFI", "setWifiOn...");
//        android.permission.CHANGE_WIFI_STATE
        WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                WifiTestActivity.this.setResult(RESULT_CANCELED, WifiTestActivity.this.getIntent());
                WifiTestActivity.this.finish();
            }
        });
    }
}
