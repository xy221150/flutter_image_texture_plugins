package com.plugin.flutterimagetexture.flutterimagetexture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import io.flutter.view.TextureRegistry;

public class FlutterImageTexture {
    Context context;
    String url;
    float width;
    float height;
    TextureRegistry.SurfaceTextureEntry mEntry;
    Surface surface;
    public FlutterImageTexture(Context context, String url, float width, float height, TextureRegistry.SurfaceTextureEntry entry) {
        this.context = context;
        this.url = url;
        this.width = width;
        this.height = height;
        this.mEntry = entry;
        this.surface = new Surface(entry.surfaceTexture());
        loadImage(context,url,width,height);
    }


    private void draw(Bitmap bitmap){
        if(surface!=null&&surface.isValid()){
            mEntry.surfaceTexture().setDefaultBufferSize(dip2px(context,width),dip2px(context,height));
            Canvas canvas = surface.lockCanvas(null);
            canvas.drawBitmap(bitmap,0,0,new Paint());
            surface.unlockCanvasAndPost(canvas);
        }
    }
    public void dispose(){
        surface.release();
        surface = null;
        mEntry.release();
    }

    private void loadImage(Context context, String url, final float width,final float height) {
        Glide.with(context).asBitmap().load(url).override(dip2px(context,width),dip2px(context,height)).into(new CustomTarget<Bitmap>() {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
            }

            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                draw(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }


    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}