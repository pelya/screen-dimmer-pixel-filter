#!/bin/sh

./gradlew assembleRelease || exit 1

[ -z "$ANDROID_KEYSTORE_FILE" ] && ANDROID_KEYSTORE_FILE=~/.android/debug.keystore
[ -z "$ANDROID_KEYSTORE_ALIAS" ] && ANDROID_KEYSTORE_ALIAS=androiddebugkey

APPNAME=PixelFilter

rm -f $APPNAME.apk

zipalign 4 app/build/outputs/apk/release/app-release-unsigned.apk $APPNAME.apk

stty -echo
#jarsigner -verbose -keystore $ANDROID_KEYSTORE_FILE -sigalg MD5withRSA -digestalg SHA1 app/build/outputs/apk/app-release-unsigned.apk $ANDROID_KEYSTORE_ALIAS || exit 1
apksigner sign --ks $ANDROID_KEYSTORE_FILE --ks-key-alias $ANDROID_KEYSTORE_ALIAS $APPNAME.apk
stty echo
echo
