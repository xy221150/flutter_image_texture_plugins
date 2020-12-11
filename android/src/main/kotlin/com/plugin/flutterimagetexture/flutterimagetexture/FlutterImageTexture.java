package com.plugin.flutterimagetexture.flutterimagetexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    int width;
    int height;
    TextureRegistry.SurfaceTextureEntry mEntry;
    Surface surface;
    public FlutterImageTexture(Context context, String url, int width, int height, TextureRegistry.SurfaceTextureEntry entry) {
        this.context = context;
        this.url = url;
        this.width = width;
        this.height = height;
        this.mEntry = entry;
        this.surface = new Surface(entry.surfaceTexture());
        loadImage( context,url,width,height);
    }


    private void draw(Bitmap bitmap){
        if(surface!=null&&surface.isValid()){
            mEntry.surfaceTexture().setDefaultBufferSize(width,height);
            Canvas canvas = surface.lockCanvas(null);
            canvas.drawBitmap( bitmap,0.0f, 0.0f, new Paint(3));
            surface.unlockCanvasAndPost(canvas);
            if (bitmap != null && !bitmap.isRecycled())
            {
                bitmap=null;
            }
        }
    }
    public void dispose(){
        surface.release();
        surface = null;
        mEntry.release();
    }

    public void loadImage(Context context, String url, final int width, int height) {

        Glide.with(context).asBitmap().load(url).override(width,height).into(new CustomTarget<Bitmap>() {
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


}