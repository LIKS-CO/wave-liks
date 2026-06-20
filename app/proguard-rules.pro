# LiteRT-LM on-device LLM — native/JNI bridge classes used via com.google.ai.edge.litertlm.
# The library loads native code and may reflect on its own classes; keep them all.
-keep class com.google.ai.edge.litertlm.** { *; }
-keepclassmembers class com.google.ai.edge.litertlm.** { *; }

# Keep runtime annotations, generic signatures, and inner-class metadata
# so reflection-based libraries (Compose, security-crypto, LiteRT-LM) keep working.
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# Preserve line numbers for crash reporting.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# androidx.security-crypto depends on Tink, which references JSR-305 concurrency/nullable
# annotations that are not on the Android runtime classpath.
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.concurrent.GuardedBy
