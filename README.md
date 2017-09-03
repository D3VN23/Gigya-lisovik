# Gigya-lisovik

Please note that if you run "MainActivityUnitTests" on OSX device you have to do following steps:

1. Choose "Edit Configuration" item under "Run" section;

![alt text](https://preview.ibb.co/nsttya/Screen_Shot_2017_09_03_at_11_51_43_AM.png)

2. On the left side choose "MainActivityUnitTests" item and fill "VM Options" and "Working directory" fields according to the next screenshot
: -ea, $MODULE_DIR$

![alt text](https://preview.ibb.co/eP8Xrv/Screen_Shot_2017_09_03_at_11_52_19_AM.png)

It looks like it's an Android Studio issue for OSX devices when you use "Robolectric".

