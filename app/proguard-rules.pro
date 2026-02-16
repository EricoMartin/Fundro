# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# ========================================
# RETROFIT
# ========================================
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# ========================================
# OKHTTP
# ========================================
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ========================================
# MOSHI
# ========================================
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keepclassmembers class ** {
    @com.squareup.moshi.Json <fields>;
}

# Keep all data classes
-keep @com.squareup.moshi.JsonClass class * extends java.lang.Object
-keepclassmembers class ** {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

# ========================================
# KOTLIN SERIALIZATION
# ========================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ========================================
# DATA MODELS (KEEP ALL FIELDS)
# ========================================
-keep class com.fundro.data.remote.dto.** { *; }
-keep class com.fundro.domain.model.** { *; }
-keep class com.fundro.data.local.entity.** { *; }

# ========================================
# ROOM DATABASE
# ========================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ========================================
# HILT
# ========================================
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keepclassmembers class * extends dagger.hilt.android.lifecycle.HiltViewModel {
    <init>(...);
}

# ========================================
# COMPOSE
# ========================================
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ========================================
# PAYSTACK SDK
# ========================================
-keep class co.paystack.android.** { *; }
-keep interface co.paystack.android.** { *; }

# ========================================
# FIREBASE
# ========================================
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ========================================
# ENCRYPTED SHARED PREFERENCES
# ========================================
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# ========================================
# BIOMETRIC
# ========================================
-keep class androidx.biometric.** { *; }

# ========================================
# GENERAL ANDROID
# ========================================
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}