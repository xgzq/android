package com.xgzq.fullscene;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HiResAnim mHiResAnim;
    private List<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHiResAnim = findViewById(R.id.main_hi_res_anim);

        mImageList = new ArrayList<>();
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589905&di=bb6311650cc5eb7facd75085ece94017&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn20%2F560%2Fw1080h1080%2F20180710%2F98c7-hfefkqp7625250.jpg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607021621876&di=5bcc70d502f57c6c2f869cb2aeacb6ee&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F18%2F20190118190003_kopap.jpeg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607021621874&di=9f712c358f0c2817a29ce0dfa49d8cea&imgtype=0&src=http%3A%2F%2Fp4.itc.cn%2Fimages01%2F20201110%2F687316f736ca4418b989a1362ee02306.jpeg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589904&di=81b3443a64744344ce7045400026c9d9&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp101503309.jpg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589904&di=c4cce973f66886e85c0729305a4edad7&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201804%2F10%2F20180410161008_mpwam.jpeg");

//        RoundedCorners roundedCorners = new RoundedCorners((int) (8 * getResources().getDisplayMetrics().density));
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.mipmap.placeholder);

        mHiResAnim.addImageUrl(mImageList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
