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

 <com.kds.just.segmentview.SegmentedView
        android:id="@+id/segment"
        android:layout_width="200dp"
        android:layout_height="80dp"
        android:layout_marginLeft="27dp"
        android:layout_marginTop="30dp"
        app:BgColorNormal="#ffffff"
        app:BgColorSelected="#727af2"
        app:BgRadius="3dp"
        app:BgStrokeColorNormal="#d8d8d8"
        app:BgStrokeColorSelected="#ff0000"
        app:textColorNormal="#d8d8d8"
        app:textColorSelected="#ffffff"
        app:textSize="13sp"
        app:StrokeWidth="3dp"/>
```
#JAVA
```
        SegmentedView segment = findViewById(R.id.segment);
        segment.addItem("item 0");
        segment.addItem("item 1");
        segment.addItem("item 2");
        segment.addItem("item 3");
```


License
--------

    Copyright 2018 Chris Banes

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

