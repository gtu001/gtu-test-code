android_build_jar

---
	在build.gradle里加入

	task androidSourcesJar(type: Jar) {
	    classifier = "sources"
	    baseName = archivesBaseName
	    from android.sourceSets.main.java.srcDirs
	}

	artifacts {
	    archives androidSourcesJar
	}

	---

	執行 androidSourcesJar 

	可得到 source jar
		<project>/build/libs/<project>.jar
		Ex : lib-badge/build/libs/lib-badge-sources.jar

	可得到 build jar
		<project>/build/intermediates/full_jar/debug/createFullJarDebug/full.jar


===


以下可以 build jar
---
	sourceSets {
	    main {
	        java {
	            srcDir 'src/main/java'
	        }
	        resources {
	            srcDir 'src/../lib'
	        }
	    }
	}

	task clearJar(type: Delete) {
	    delete 'build/outputs/ProjectName.jar'
	}

	task makeJar(type: Copy) {
	    from('build/intermediates/bundles/release/')
	    into('build/outputs/')
	    include('classes.jar')
	    rename ('classes.jar', 'ProjectName.jar')
	}

	makeJar.dependsOn(clearJar, build)

	---


	執行 makeJar 
	但是沒看到 jar ...

	---
	PS: src 定義可用底下

		android {
		    compileSdkVersion 28
		    defaultConfig {
		        minSdkVersion 11
		        targetSdkVersion 28
		    }
		    lintOptions {
		        abortOnError false
		    }

		    sourceSets {
		        main {
		            java {
		                //points to an empty manifest, needed just to get the build to work
		                manifest.srcFile 'src/main/AndroidManifest.xml'
		                //defined our src dir as it's not the default dir gradle looks for
		                java.srcDirs = ['src']

		            }
		        }
		    }

		    sourceSets {
		        main {
		            java {
		                srcDir 'src/main/java'
		            }
		            resources {
		                srcDir 'src/../lib'
		            }
		        }
		    }
		}



更多參考
	https://github.com/digidotcom/xbee-android/issues/11