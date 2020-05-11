ant_自訂_jdk.md
---
	<property name="jdk_javac" value="/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/javac" />

	** 在 javac 加入 executable="${jdk_javac}" fork="yes"

	<antcall target="compiler">
		<param name="enable" value="true" />
	</antcall>

	<target name="compiler" if="${enable}">
		<echo># compiler ...</echo>
		<javac executable="${jdk_javac}" compiler="javac1.6" fork="yes" source="1.6" target="1.6" includeantruntime="true" srcdir="${src_dir}" destdir="${build_dir}" deprecation="no" nowarn="true" encoding="UTF-8" debug="on"><!-- -->
			<include name="**/${main_class}.java" />
			<classpath>
				<path location="${build_dir}" />
			</classpath>
			<classpath refid="javafx_class_path" /> 
		</javac>
	</target>