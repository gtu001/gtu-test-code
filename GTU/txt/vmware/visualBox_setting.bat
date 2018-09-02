@Echo off
REM This variable is for testing, this should be commented out before committing.
REM SET WORKSPACE=%~dp0
path = %PATH%;"C:\Program Files\Oracle\VirtualBox";"D:\VirtualMachines";

REM :: Some general path information.
SET VBoxPath=C:\Program Files\Oracle\VirtualBox
SET VBoxManage="%VBoxPath%\VBoxManage.exe"

REM ::This is the ID of the Virtual Machine we wish to control.
SET VBoxUID="6456b630-ff1a-41e4-bca6-294767fd3058"

REM :: The snapshot that contains the desired pre-product-install configuration