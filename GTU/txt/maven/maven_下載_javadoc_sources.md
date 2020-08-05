修改 ~/.m2/settings.xml

<settings>

   <!-- ... other settings here ... -->

    <profiles>
        <profile>
            <id>downloadSources</id>
            <properties>
                <downloadSources>true</downloadSources>
                <downloadJavadocs>true</downloadJavadocs>
            </properties>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>downloadSources</activeProfile>
    </activeProfiles>
</settings>