LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := \
		sciflydatacache \
		android-support-v4 \
		google-play-services
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) $(call all-renderscript-files-under, src) \
 		src/com/cqsmiletv/game/RemoteOneUpJoyGameAccount.aidl \
		
LOCAL_JAVA_LIBRARIES := \
		scifly.android \
		com.mstar.android
			
LOCAL_PACKAGE_NAME := BLLSciflyLauncher
LOCAL_CERTIFICATE := platform
LOCAL_DEX_PREOPT := false
#LOCAL_PRIVILEGED_MODULE := true
LOCAL_OVERRIDES_PACKAGES := Launcher3 Gallery Gallery2 Galaxy4 SoundRecorder Email DeskClock Calendar Calculator \
    HTMLViewer MLocalMM2 MTvTest Music MWidiDemo LatinIME MLatinIME PinyinIME DMR DMP PhotoTable BasicDreams Exchange2 VideoEditor QuickSearchBox \
    HoloSpiralWallpaper LiveWallpapers LiveWallpapersPicker MagicSmokeWallpapers NoiseField \
    PacProcessor PhaseBeam PhotoTable TelephonyProvider VisualizationWallpapers Camera2 MTvPlayer MTvHotkey MTvMisc  Settings\
    WAPPushManager BackupRestoreConfirmation CalendarProvider Contacts ContactsProvider TeleService WallpaperCropper
    
#LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

include $(BUILD_MULTI_PREBUILT)

include $(call all-makefiles-under,$(LOCAL_PATH))
