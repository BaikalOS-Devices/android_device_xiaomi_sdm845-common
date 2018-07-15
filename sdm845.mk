#
# Copyright (C) 2018 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Get non-open-source specific aspects
$(call inherit-product-if-exists, vendor/xiaomi/sdm845-common/sdm845-common-vendor.mk)

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    $(LOCAL_PATH)/overlay-custom

# Properties
-include $(LOCAL_PATH)/common-props.mk

# AID/fs configs
PRODUCT_PACKAGES += \
    fs_config_files

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    tinymix

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_output_policy.conf:system/etc/audio_output_policy.conf \
    $(LOCAL_PATH)/audio/audio/audio_policy_configuration.xml:system/etc/audio/audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/audio_policy_configuration.xml:system/etc/audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/audio_platform_info.xml:system/etc/audio_platform_info.xml \
    $(LOCAL_PATH)/audio/a2dp_audio_policy_configuration.xml:system/etc/a2dp_audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/usb_audio_policy_configuration.xml:system/etc/usb_audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/mixer_paths_overlay_static.xml:system/etc/mixer_paths_overlay_static.xml \
    $(LOCAL_PATH)/audio/mixer_paths_overlay_dynamic.xml:system/etc/mixer_paths_overlay_dynamic.xml \
    $(LOCAL_PATH)/audio/mixer_paths_tavil.xml:system/etc/mixer_paths_tavil.xml 

# Camera
PRODUCT_PACKAGES += \
    Snap

# Common init scripts
PRODUCT_PACKAGES += \
    init.qcom.rc

# Dex
PRODUCT_DEXPREOPT_SPEED_APPS += \
    SystemUI

# Display
PRODUCT_PACKAGES += \
    libvulkan

# Fingerprint
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/fingerprint/android.hardware.biometrics.fingerprint@2.1-service.xiaomi_sdm845.rc:system/etc/init/android.hardware.biometrics.fingerprint@2.1-service.xiaomi_sdm845.rc

# HIDL
PRODUCT_PACKAGES += \
    android.hidl.base@1.0 \
    android.hidl.base@1.0_system \
    android.hidl.manager@1.0 \
    android.hidl.manager@1.0_system

# HotwordEnrollement app permissions
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/permissions/privapp-permissions-hotword.xml:system/etc/permissions/privapp-permissions-hotword.xml

# IFAA manager
PRODUCT_PACKAGES += \
    org.ifaa.android.manager

PRODUCT_BOOT_JARS += \
    org.ifaa.android.manager

# IMS
PRODUCT_PACKAGES += \
    ims-ext-common

# Input
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/keylayout/sdm845-tavil-snd-card_Button_Jack.kl:system/usr/keylayout/sdm845-tavil-snd-card_Button_Jack.kl

# Lights
PRODUCT_PACKAGES += \
    android.hardware.light@2.0-service.xiaomi_sdm845

# LiveDisplay
PRODUCT_PACKAGES += \
    lineage.livedisplay@2.0-service-sdm

# Media
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/media_profiles_vendor.xml:system/etc/media_profiles_vendor.xml

# Net
PRODUCT_PACKAGES += \
    netutils-wrapper-1.0

# Power
PRODUCT_PACKAGES += \
    power.qcom:64

# QTI
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/qti_whitelist.xml:system/etc/sysconfig/qti_whitelist.xml \
    $(LOCAL_PATH)/permissions/privapp-permissions-qti.xml:system/etc/permissions/privapp-permissions-qti.xml

# RCS
PRODUCT_PACKAGES += \
    rcs_service_aidl \
    rcs_service_aidl.xml \
    rcs_service_api \
    rcs_service_api.xml

# Recovery
PRODUCT_PACKAGES += \
    librecovery_updater_xiaomi

# Telephony
PRODUCT_PACKAGES += \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext

# TextClassifier
PRODUCT_PACKAGES += \
    textclassifier.bundle1

# VNDK-SP
PRODUCT_PACKAGES += \
    vndk-sp

# Healthd
PRODUCT_PACKAGES += \
    android.hardware.health@2.0-service \

#PRODUCT_COPY_FILES += \
#    $(OUT_DIR)/$(TARGET_COPY_OUT_VENDOR)/bin/hw/android.hardware.health@2.0-service:system/bin/hw/android.hardware.health@2.0-service

# Device-specific settings
PRODUCT_PACKAGES += \
    BaikalParts

# Device-specific settings
PRODUCT_PACKAGES += \
    BaikalDoze

# WFD
PRODUCT_PACKAGES += \
    libnl \
    libwfdaac

PRODUCT_BOOT_JARS += \
    WfdCommon

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/factory.prop:root/factory/factory.prop

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/etc/init.qcom.rc:system/etc/init/init.qcom.rc \
    $(LOCAL_PATH)/rootdir/etc/init.performance.rc:system/etc/init/init.performance.rc \
    $(LOCAL_PATH)/rootdir/etc/init.profiles.rc:system/etc/init/init.profiles.rc \
    $(LOCAL_PATH)/rootdir/etc/init.thermal.rc:system/etc/init/init.thermal.rc
