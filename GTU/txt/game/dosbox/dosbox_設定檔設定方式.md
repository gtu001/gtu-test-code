執行檔設定方式
---
"C:\GOG Games\Harvester\DOSBOX\dosbox.exe" -conf "..\dosbox_harvester.conf" -conf "..\dosbox_harvester_single.conf" -noconsole -c exit



設定檔加入以下
---

[sdl]
#       fullscreen: Start dosbox directly in fullscreen. (Press ALT-Enter to go back)
#       fulldouble: Use double buffering in fullscreen. It can reduce screen flickering, but it can also result in a slow DOSBox.
#   fullresolution: What resolution to use for fullscreen: original or fixed size (e.g. 1024x768).
#                     Using your monitor's native resolution with aspect=true might give the best results.
#                     If you end up with small window on a large screen, try an output different from surface.
# windowresolution: Scale the window to this size IF the output device supports hardware scaling.
#                   Good sizes:  800x600, 1024x768, 1280x960, 1536x1152 (prob too large for most)
#                     (output=surface does not!)
#           output: What video system to use for output.
#                   Possible values: surface, overlay, opengl, openglnb, ddraw.
#         autolock: Mouse will automatically lock, if you click on the screen. (Press CTRL-F10 to unlock)
#      sensitivity: Mouse sensitivity.
#      waitonerror: Wait before closing the console if dosbox has an error.
#         priority: Priority levels for dosbox. Second entry behind the comma is for when dosbox is not focused/minimized.
#                     pause is only valid for the second entry.
#                   Possible values: lowest, lower, normal, higher, highest, pause.
#       mapperfile: File used to load/save the key/event mappings from. Resetmapper only works with the defaul value.
#     usescancodes: Avoid usage of symkeys, might not work on all operating systems.

fullscreen=false
fulldouble=false
fullresolution=original
windowresolution=800x600 <<<< Change this to 1024x768 or 1280x960 
output=opengl
autolock=true
sensitivity=100
waitonerror=true
priority=higher,normal
mapperfile=mapper-0.74.map
usescancodes=true