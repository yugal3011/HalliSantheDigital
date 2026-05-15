package com.hallisanthe.digital.utils

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import java.io.File

object ImageCompressor {
    suspend fun compressImage(context: Context, imageFile: File): File {
        return Compressor.compress(context, imageFile) {
            size(307200) // 300KB
        }
    }
}
