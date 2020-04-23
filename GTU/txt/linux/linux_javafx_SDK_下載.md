javafx SDK 下載
---
	https://gluonhq.com/products/javafx/
	https://openjfx.io/

	
	export PATH_TO_FX_MODS=/media/gtu001/OLD_D/apps/javafx-jmods-11.0.2/

	jlink --module-path $PATH_TO_FX_MODS \
    --add-modules java.se,javafx.fxml,javafx.web,javafx.media,javafx.swing \
    --bind-services --output /usr/lib/jvm/java-11-openjdk-amd64 \
    --ignore-signing-information 


		jlink  --module-path   /media/gtu001/OLD_D/apps/javafx-jmods-11.0.2/lib   --add-modules=javafx.base  --add-modules=javafx.controls  --add-modules=javafx.fxml  --add-modules=javafx.graphics  --add-modules=javafx.web  --add-modules=javafx.media  --add-modules=javafx.swing  --bind-services  --output  /usr/lib/jvm/java-11-openjdk-amd64 --ignore-signing-information 


	https://zhuanlan.zhihu.com/p/82540739