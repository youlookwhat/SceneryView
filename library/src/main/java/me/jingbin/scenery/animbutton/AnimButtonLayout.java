package me.jingbin.scenery.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.jingbin.library.scenery.R;


/**
 * 底部按钮的展开收起布局
 *
 * @author jingbin
 * @data 2019-05-24
 */
public class AnimButtonLayout extends RelativeLayout {

    private RelativeLayout rlExpand;
    private RelativeLayout relativeLayout;
    private ImageView imageBg;
    private ImageView imageAdd;
    /**
     * 是否已经开始，防止连续点击
     */
    private boolean isStart = false;
    /**
     * 是否第一次点击，用来区分是展开还是收起
     */
    private boolean isFirst = true;
    /**
     * 0：没有点击
     * 1：点击 产品评论
     * 2：点击 发布文章
     */
    private int isClick = 0;
    private AnimatorSet animatorSet;
    private Context mContext;

    public AnimButtonLayout(Context context) {
        super(context);
        initView(context);
    }

    public AnimButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnimButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.button_layout_expand, this);
        relativeLayout = inflate.findViewById(R.id.relativeLayout);
        rlExpand = inflate.findViewById(R.id.rl_expand);
        imageBg = inflate.findViewById(R.id.image_bg);
        imageAdd = inflate.findViewById(R.id.image_add);
        TextView tvWriteComment = inflate.findViewById(R.id.tv_write_comment);
        TextView tvWriteArticle = inflate.findViewById(R.id.tv_write_article);

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.addListener(listenerAdapter);

        rlExpand.setOnClickListener(listener);
        imageBg.setOnClickListener(listener);
        tvWriteComment.setOnClickListener(listener);
        tvWriteArticle.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.image_bg) {
                if (clickListener != null) {
                    clickListener.onExpand();
                } else {
                    expandView();
                }
            } else if (id == R.id.tv_write_comment) {
                isClick = 1;
                expandView();
            } else if (id == R.id.tv_write_article) {
                isClick = 2;
                expandView();
            } else if (id == R.id.rl_expand) {
                hideView();
            }
        }
    };

    public void expandView() {
        if (!isStart) {
            isStart = true;
            if (isFirst) {
                play();
            } else {
                AnimationHelper.hide(rlExpand, dip2px(mContext, 54) / 2f, new AnimationHelper.AnimatorEndListener() {
                    @Override
                    public void onEnd() {
                        play();
                    }
                });
            }
        }
    }

    public void hideView() {
        if (!isFirst) {
            if (!isStart) {
                isStart = true;
                AnimationHelper.hide(rlExpand, dip2px(mContext, 54) / 2f, new AnimationHelper.AnimatorEndListener() {
                    @Override
                    public void onEnd() {
                        play();
                    }
                });
            }
        }
    }

    public boolean isStart() {
        return animatorSet.isRunning();
    }

    private void play() {
        float startF = 1.0f;
        float endF = 5 / 4f;

        float startRF = 0.0f;
        float endRF = 45.0f;
        if (!isFirst) {
            startF = 5 / 4f;
            endF = 1.0f;

            startRF = 45.0f;
            endRF = 90.0f;
        }
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageBg, "scaleX", startF, endF);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageBg, "scaleY", startF, endF);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(imageAdd, "rotation", startRF, endRF);

        if (animatorSet != null) {
            if (!isFirst) {
                // 收起的时候，显示button的背景色
                imageBg.setVisibility(VISIBLE);
                rlExpand.setOnClickListener(null);
            }
            animatorSet.play(objectAnimator1).with(objectAnimator2).with(objectAnimator3);
            animatorSet.start();
        }
    }

    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (isFirst) {
                // 显示展开的时候，隐藏button的背景色
                imageBg.setVisibility(INVISIBLE);
                rlExpand.setOnClickListener(listener);
                AnimationHelper.show(rlExpand, relativeLayout, dip2px(mContext, 54) / 2f, new AnimationHelper.AnimatorEndListener() {
                    @Override
                    public void onEnd() {
                        isFirst = false;
                        isStart = false;
                    }
                });
            } else {
                isFirst = true;
                isStart = false;
                if (isClick == 1) {
                    isClick = 0;
                    if (clickListener != null) {
                        clickListener.onAsk();
                    }
                } else if (isClick == 2) {
                    isClick = 0;
                    if (clickListener != null) {
                        clickListener.onPublish();
                    }
                }
            }
        }
    };

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * xml文件里的dp --->  手机像素里的px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private AnimButtonClickListener clickListener;

    public void setClickListener(AnimButtonClickListener listener) {
        clickListener = listener;
    }

}
