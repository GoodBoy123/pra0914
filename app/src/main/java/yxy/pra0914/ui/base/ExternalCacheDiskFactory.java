package yxy.pra0914.ui.base;

import android.content.Context;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;


import java.io.File;

import yxy.pra0914.utils.FileUtils;

/**
 * Created by dasu on 2017/5/4.
 */

public class ExternalCacheDiskFactory extends DiskLruCacheFactory {

    public ExternalCacheDiskFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public ExternalCacheDiskFactory(Context context, int diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public ExternalCacheDiskFactory(final Context context, final String diskCacheName, int diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                File cacheDirectory;
                if (FileUtils.isSDcardAvailable()) {
                    // 如果SD卡可以用的话把图片缓存到SD卡上
                    cacheDirectory = new File(FileUtils.getAppRootDirectoryPath(), "cache");
                } else {
                    // 把图片缓存到应用data/data/包/...下
                    cacheDirectory = context.getCacheDir();
                }
                if (cacheDirectory == null) {
                    return null;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }
        }, diskCacheSize);
    }
}
