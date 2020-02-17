AndroidManifest.xml 兩種style

        <activity
            android:name=".LoginActivity"
            android:configChanges="orientation|keyboardHidden"
            android:theme="@style/Theme.MyDialog" <---- 這裡
            android:screenOrientation="portrait" />

(1)自訂 
            android:theme="@style/Theme.MyDialog"

       要mapping /res/values/styles.xml
       		    <!-- 無標題 for AppCompat -->
			    <style name="Theme.MyDialog" parent="@style/Theme.AppCompat.Light.Dialog">
			        <item name="windowActionBar">false</item>
			        <item name="windowNoTitle">true</item>
			    </style>

(2)內定
            android:theme="@android:style/Theme.NoTitleBar"