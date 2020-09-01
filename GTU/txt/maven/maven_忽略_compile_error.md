maven_忽略_compile_error.md
---

	<plugin>
	    <groupId>org.apache.maven.plugins</groupId>
	    <artifactId>maven-compiler-plugin</artifactId>
	    <configuration>
	        <failOnError>false</failOnError>
	        <skipTests>true</skipTests>
	    </configuration>
	</plugin>
	

	mvn package  -Dmaven.compiler.failOnError=false  -DskipTests