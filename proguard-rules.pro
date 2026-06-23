# KG Rule Studio ProGuard Rules
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

# Keep model classes
-keep class com.github.kgstudio.model.** { *; }

# Keep Gson serialization
-keep class com.google.gson.** { *; }
