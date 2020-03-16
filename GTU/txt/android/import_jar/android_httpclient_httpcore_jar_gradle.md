看起來只適用sdk23
---

	implementation group: 'org.apache.httpcomponents', name: 'httpcore', version: '4.4.13'
	implementation files('libs/httpclient-4.5.12.jar')


app/build.gradle
	android {
	    useLibrary 'org.apache.http.legacy'
	}



另一解
---
	https://hc.apache.org/downloads.cgi
		下載 httpclient5-5.0
		開啟壓縮檔 /httpcomponents-client-5.0/lib/
		取得
			httpclient5-5.0.jar
			httpcore5-5.0.jar


=====
以下為最佳解 簡單快速
---
	implementation 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'