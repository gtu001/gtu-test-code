echo off
set ServiceName=PiServiceLauncher
set ProcessName=PiService.exe
tasklist /fi “imagename eq %ProcessName%” |find /v “:” > nul&&(
sc query “%ServiceName%”|find “STATE”|find /v “RUNNING”>nul&&(
sc start %ServiceName%
timeout 10
)
sc stop %ServiceName%
) || (
sc start %ServiceName%
)