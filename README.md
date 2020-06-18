# EnhancedView


![enhancedview](https://user-images.githubusercontent.com/5418274/74895205-691e7f80-53d4-11ea-94c8-da23602859ea.gif)
![segmentview1](https://user-images.githubusercontent.com/5418274/74515360-c4262180-4f51-11ea-90f2-23faf181f624.gif)

Edit root/app/build.gradle like below.
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

and:
```
dependencies {
    implementation 'com.github.AndroidJustSeries:EnhancedView:1.0.0'
}
```
## How to use
#XML
```
<com.kds.just.enhancedview.view.EnhancedTextView
        android:id="@+id/enhanced_textview_01"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="EnhancedTextView Test"
        android:textSize="16sp"
        app:BgColorNormal="#ffffff"
        app:BgColorSelected="#727af2"
        app:BgRadius="25dp"
        app:BgStrokeColorNormal="#d8d8d8"
        app:BgStrokeColorSelected="#727af2"
        app:StrokeWidth="5dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:textColorNormal="#d8d8d8"
        app:textColorSelected="#ffffff" />
```
