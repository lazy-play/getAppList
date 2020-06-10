package com.pudding.getapplist

import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * This created by Error on 2020/05/29,10:00.
 */
object SaveUtil{
    fun saveBitmapToFile(bitmap: Bitmap, _file: String) {
            var os: BufferedOutputStream? = null
            try {
                val file = File(_file)
                val end = _file.lastIndexOf(File.separator)
                val _filePath = _file.substring(0, end)
                val filePath = File(_filePath)
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }
                file.createNewFile()
                os = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            } finally {
                if (os != null) {
                    try {
                        os.close()
                    } catch (e: IOException) {
                    }
                }
            }
    }
}