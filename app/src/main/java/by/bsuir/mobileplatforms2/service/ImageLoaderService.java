package by.bsuir.mobileplatforms2.service;

import android.graphics.Bitmap;

import java.util.concurrent.Future;

public interface ImageLoaderService {

    Future<Bitmap> loadImage(String url);
}
