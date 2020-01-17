# Scenery
🏜 Hand-painted scenery, let it fly~


## 绘制
### 1.onSizeChanged() 获取View的宽高
```java
@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentWidth = getWidth();
        mParentHeight = getHeight();
        Log.e("Scenery", "width = " + getWidth() + "， height = " + getHeight());
//        drawYun(w, h);
    }

```

### 2.onDraw() 绘制圆形，三角形，云朵
画圆：

```java
        Paint p = new Paint();
        // 自定义颜色 Color.WHITE
        p.setColor(Color.parseColor("#6ABDE8"));
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        // 画圆
        canvas.drawCircle(mParentWidth / 2, mParentHeight / 2, mParentWidth / 2, p);        
```

画三角形：

```java
        Paint lmp = new Paint();
        lmp.setAntiAlias(true);
        lmp.setColor(Color.parseColor("#E6E6E8"));
        //实例化路径
        Path path = new Path();
        path.moveTo(left - 80, right + 20);// 此点为多边形的起点
        path.lineTo(left - 80 + 110, right + 20 + 130);
        path.lineTo(left - 80 - 110, right + 20 + 130);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, lmp);
```

画云朵：

```java
		  mLeftCloudPath.reset();
        mRightCloudPath.reset();


        float centerX = w / 2; //view's center x coordinate
        float minSize = Math.min(w, h); //get the min size

        float leftCloudWidth = minSize / 2.5f; //the width of cloud
        float leftCloudBottomHeight = leftCloudWidth / 3f; //the bottom height of cloud
        float leftCloudBottomRoundRadius = leftCloudBottomHeight; //the bottom round radius of cloud

        float rightCloudTranslateX = leftCloudWidth * 2 / 3; //the distance of the cloud on the right
        float leftCloudEndX = (w - leftCloudWidth - leftCloudWidth * CLOUD_SCALE_RATIO / 2) / 2 + leftCloudWidth; //the left cloud end x coordinate
        float leftCloudEndY = h / 3; //clouds' end y coordinate

        //add the bottom round rect
        mLeftCloudPath.addRoundRect(new RectF(leftCloudEndX - leftCloudWidth, leftCloudEndY - leftCloudBottomHeight,
                leftCloudEndX, leftCloudEndY), leftCloudBottomHeight, leftCloudBottomRoundRadius, Path.Direction.CW);

        float leftCloudTopCenterY = leftCloudEndY - leftCloudBottomHeight;
        float leftCloudRightTopCenterX = leftCloudEndX - leftCloudBottomRoundRadius;
        float leftCloudLeftTopCenterX = leftCloudEndX - leftCloudWidth + leftCloudBottomRoundRadius;

        mLeftCloudPath.addCircle(leftCloudRightTopCenterX, leftCloudTopCenterY, leftCloudBottomRoundRadius * 3 / 4, Path.Direction.CW);
        mLeftCloudPath.addCircle(leftCloudLeftTopCenterX, leftCloudTopCenterY, leftCloudBottomRoundRadius / 2, Path.Direction.CW);

        //************************compute right cloud**********************
        //The cloud on the right is CLOUD_SCALE_RATIO size of the left
        float rightCloudCenterX = rightCloudTranslateX + centerX - leftCloudWidth / 2; //the right cloud center x

        RectF calculateRect = new RectF();
        mLeftCloudPath.computeBounds(calculateRect, false); //compute the left cloud's path bounds

        mComputeMatrix.reset();
        mComputeMatrix.preTranslate(rightCloudTranslateX, -calculateRect.height() * (1 - CLOUD_SCALE_RATIO) / 2);
        mComputeMatrix.postScale(CLOUD_SCALE_RATIO, CLOUD_SCALE_RATIO, rightCloudCenterX, leftCloudEndY);
        mLeftCloudPath.transform(mComputeMatrix, mRightCloudPath);

        float left = calculateRect.left + leftCloudBottomRoundRadius;
        mRightCloudPath.computeBounds(calculateRect, false); //compute the right cloud's path bounds

        float right = calculateRect.right;
        float top = calculateRect.bottom;
        //************************compute right cloud**********************
        mRainRect.set(left, top, right, h * 3 / 4f); //compute the rect of rain...
        mRainClipRect.set(0, mRainRect.top, w, mRainRect.bottom);

        mMaxTranslationX = leftCloudBottomRoundRadius / 2;
```


## 参考资料
 - 云朵及动画：https://www.jianshu.com/p/ff7c3d36b5ef
 - 烧杯滴水动画：https://github.com/Ajian-studio/GABottleLoading
 - 很多自定义View参考：https://github.com/samlss/FunnyViews
 - 自定义View学习：https://blog.csdn.net/carson_ho/article/details/62037696
 - 高级UI示例：https://github.com/zincPower/UI2018
 - 动画原网页：https://dribbble.com/shots/4761564




