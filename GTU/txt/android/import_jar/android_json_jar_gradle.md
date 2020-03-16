dependency設定如下

    implementation group: 'net.sf.json-lib', name: 'json-lib', version: '2.4', classifier: 'jdk15', {
        exclude group: 'commons-beanutils'
        exclude group: 'commons-collections'
    }
    implementation 'commons-beanutils:commons-beanutils:1.9.3'
    implementation 'commons-collections:commons-collections:3.2.1'


加入以下設定
    android {
	    lintOptions {
	        checkReleaseBuilds false
	        // Or, if you prefer, you can continue to check for errors in release builds,
	        // but continue the build even when errors are found:
	        abortOnError false
	    }
	}