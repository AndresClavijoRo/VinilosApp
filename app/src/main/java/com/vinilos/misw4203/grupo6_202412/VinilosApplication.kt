package com.vinilos.misw4203.grupo6_202412

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.vinilos.misw4203.grupo6_202412.models.network.CacheManager
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.models.network.VinilosServiceAdapter

class VinilosApplication: Application(), ImageLoaderFactory {

    private val vinilosService by lazy { VinilosServiceAdapter.getInstance(BuildConfig.BASE_URL_API_SERVICE) }
    private val cacheManager by lazy { CacheManager.getInstance() }
    val vinilosRepository by lazy { VinilosRepository.create(vinilosService, cacheManager) }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}