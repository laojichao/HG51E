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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.hjq.shape.** {*;}

# Gson model — reflection-based deserialization needs field names preserved
-keep class com.aotem.hg51e.data.model.User { *; }
-keep class com.aotem.hg51e.data.model.User$DataBean { *; }

# Retrofit API interface — keep methods + annotations for dynamic proxy
-keep interface com.aotem.hg51e.data.remote.ApiService { *; }