package co.kartoo.app.bank.detail;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePopUp extends AppCompatActivity {

    PhotoViewAttacher mAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_pop_up);

        ImageView imagePopUp = (ImageView) findViewById(R.id.imagePopUp);

        Intent intent = getIntent();
        String urlImage = intent.getStringExtra("url");

        Log.e("TAG", "urlImage: "+urlImage );

        Picasso.with(this).load(urlImage).into(imagePopUp);

        imagePopUp.setFocusable(true);
        imagePopUp.setAdjustViewBounds(true);

        mAttacher = new PhotoViewAttacher(imagePopUp);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager ().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }
}
