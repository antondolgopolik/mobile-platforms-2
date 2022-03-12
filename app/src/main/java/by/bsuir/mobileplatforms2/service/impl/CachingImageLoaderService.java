package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsuir.mobileplatforms2.datasource.dao.CachedImageDao;
import by.bsuir.mobileplatforms2.datasource.dao.impl.CachedImageDaoImpl;
import by.bsuir.mobileplatforms2.entity.CachedImage;
import by.bsuir.mobileplatforms2.service.ImageLoaderService;

public class CachingImageLoaderService implements ImageLoaderService {
    private static final Pattern UID_PATTERN = Pattern.compile("/([^/]*)$");
    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("max-age=(\\d*)");

    private final Context context;
    private final CachedImageDao cachedImageDao;

    public CachingImageLoaderService(Context context) {
        this.context = context;
        cachedImageDao = new CachedImageDaoImpl(context);
    }

    @Override
    public Future<Bitmap> loadImage(String url) {
        return Executors.newCachedThreadPool().submit(() -> {
            String uid = extractUid(url);
            CachedImage cachedImage = cachedImageDao.read(uid);
            // Проверка наличия картинки
            if (cachedImage != null) {
                // Проверка срока годности
                if (cachedImage.getExpires().compareTo(new java.util.Date()) > 0) {
                    return loadFromFiles(uid);
                } else {
                    return refresh(cachedImage, url, uid);
                }
            } else {
                return firstLoad(url, uid);
            }
        });
    }

    private String extractUid(String url) {
        Matcher matcher = UID_PATTERN.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("Invalid URL passed");
        }
        return matcher.group(1);
    }

    private Bitmap loadFromFiles(String uid) {
        File file = new File(context.getFilesDir(), uid + ".png");
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    private Bitmap refresh(CachedImage cachedImage, String url, String uid) {
        // Загрузить свежую версию
        NetworkBitmap networkBitmap = loadFromNetwork(url);
        if (networkBitmap != null) {
            cachedImage.setExpires(networkBitmap.expires);
            // Обновить срок годности
            cachedImageDao.update(cachedImage);
            // Кэшировать свежую версию
            cacheImage(uid, networkBitmap.bitmap);
            return networkBitmap.bitmap;
        } else {
            return null;
        }
    }

    private NetworkBitmap loadFromNetwork(String strUrl) {
        try {
            URL url = new URL(strUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            NetworkBitmap networkBitmap = new NetworkBitmap();
            networkBitmap.bitmap = BitmapFactory.decodeStream(inputStream);
            networkBitmap.expires = extractExpires(urlConnection);
            return networkBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    private Timestamp extractExpires(URLConnection urlConnection) {
        String headerField = urlConnection.getHeaderField("cache-control");
        Matcher matcher = MAX_AGE_PATTERN.matcher(headerField);
        if (matcher.find()) {
            String maxAge = matcher.group(1);
            if (maxAge != null) {
                long offset = Long.parseLong(maxAge) * 1000;
                return new Timestamp(System.currentTimeMillis() + offset);
            }
        }
        return null;
    }

    private void cacheImage(String uid, Bitmap bitmap) {
        File file = new File(context.getFilesDir(), uid + ".png");
        // Создать файл
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Запись
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap firstLoad(String url, String uid) {
        // Загрузить картинку
        NetworkBitmap networkBitmap = loadFromNetwork(url);
        if (networkBitmap != null) {
            // Создать
            cachedImageDao.create(uid, networkBitmap.expires);
            // Кэшировать
            cacheImage(uid, networkBitmap.bitmap);
            return networkBitmap.bitmap;
        } else {
            return null;
        }
    }

    private static class NetworkBitmap {
        Bitmap bitmap;
        Timestamp expires;
    }
}
