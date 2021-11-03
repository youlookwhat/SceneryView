## View

[![](https://jitpack.io/v/youlookwhat/SceneryView.svg)](https://jitpack.io/#youlookwhat/SceneryView) [![minSdk for Scenery](https://img.shields.io/badge/minSdk-15-green.svg)](#)

🏜 Hand-painted scenery, let it fly~


### 1.SceneryView
![](https://github.com/youlookwhat/SceneryView/blob/master/art/SceneryView.gif?raw=true)

 - 原型来自dribbble：[Gallery App Icon](https://dribbble.com/shots/4761564)
 - 对应文章：[《自定义View实现Dribbble上动感的Gallery App Icon》](https://juejin.cn/post/7024883320269832205)



### 2.HorizontalWaveProgressView
![](https://github.com/youlookwhat/SceneryView/blob/master/art/HorizontalWaveProgressView.GIF?raw=true)

 - 对应文章：[《自定义View实现横向的双水波纹进度条》](https://juejin.cn/post/7025249776069246984)

--

### Usage
#### Gradle
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```java
dependencies {
	implementation 'com.github.youlookwhat:SceneryView:1.0.1'
}
```

#### In layout.xml
```xml
<me.jingbin.scenery.SceneryView
    android:id="@+id/scenery"
    android:layout_width="100dp"
    android:layout_height="100dp"
    app:background_color="#2483D9"
    app:cloud_color="#0000FF"
    app:left_mountain_color="#e0e0e0"
    app:mid_mountain_color="#ff0000"
    app:right_mountain_color="#e0e0e0"
    app:sun_color="#ffff00" />
```

#### In code
```java
	SceneryView sceneryIcon = findViewById(R.id.scenery_icon);
	sceneryIcon.setCloudColor(Color.parseColor("#0000FF"));
	sceneryIcon.setColorBackground(Color.parseColor("#2483D9"));
	sceneryIcon.setMidMouColor(Color.parseColor("#ff0000"));
	sceneryIcon.setLeftMouColor(Color.parseColor("#e0e0e0"));
	sceneryIcon.setRightMouColor(Color.parseColor("#e0e0e0"));
	sceneryIcon.setSunColor(Color.parseColor("#ffff00"));
	
	sceneryIcon.playAnimator();
	sceneryIcon.setOnAnimationListener(new SceneryView.AnimationListener() {
	    @Override
	    public void onAnimationEnd() {
	
	    }
	});
```

### License
```
Copyright 2020 jingbin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


<!--
## link
 - 云朵及动画：https://www.jianshu.com/p/ff7c3d36b5ef
 - 烧杯滴水动画：https://github.com/Ajian-studio/GABottleLoading
 - 很多自定义View参考：https://github.com/samlss/FunnyViews
 - 自定义View学习：https://blog.csdn.net/carson_ho/article/details/62037696
 - 高级UI示例：https://github.com/zincPower/UI2018
-->


