package com.pudding.getapplist

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOCUMENTS
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream


/**
 * This created by Error on 2020/05/28,16:30.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list: ArrayList<AppInfo> = ArrayList()
        val showSystem = intent?.getBooleanExtra("showSystem", false) ?: false
        val zipName=intent?.getStringExtra("zipName")?:"imagePath"
        val packages =
            packageManager.getInstalledPackages(0)


        lifecycleScope.launch (Dispatchers.IO){
            for (i in packages.indices) {
                val packageInfo = packages[i]
                val tmpInfo = AppInfo()
                tmpInfo.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                tmpInfo.packageName = packageInfo.packageName
                tmpInfo.versionName = packageInfo.versionName
                tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(packageManager)

                if (showSystem || packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    list.add(tmpInfo)
                }
            }
            val imageDir=File(filesDir.absolutePath+ File.separator+"AppInfo")
            delete(imageDir)
            for (info in list){
                val fileName=imageDir.absolutePath+ File.separator+info.packageName+".png"
                val bitmap=getBitmapFromDrawable(info.appIcon)?:continue
                SaveUtil.saveBitmapToFile(bitmap,fileName)
                info.appIconName=info.packageName+".png"
            }
            val path=getExternalFilesDir(DIRECTORY_DOCUMENTS)?.absolutePath+ File.separator+zipName+".zip"
            val json="{list:${GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(list)}}"
            val jsonFile=File(filesDir.absolutePath+ File.separator+"listJson.json")
            if(!jsonFile.exists())
                jsonFile.createNewFile()
            try {
                val fos = FileOutputStream(jsonFile)
                fos.write(json.toByteArray())
                fos.flush()
                fos.close()
            } catch ( e:Exception) {
                e.printStackTrace()
            }
            ZipUtils.ZipFolder(filesDir.absolutePath,path)
            withContext(Dispatchers.Main){
                toast("成功")
                finish()
                overridePendingTransition(0,0)
            }
        }
    }
    @SuppressLint("NewApi")
    private fun getBitmapFromDrawable(drawable: Drawable?):Bitmap?{
        return when(drawable){
            is BitmapDrawable->drawable.bitmap
            is AdaptiveIconDrawable ->{
                val bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                return bmp
            }
            else->null
        }

    }
    private fun delete(file:File) {
        if(!file.exists())return
        if(file.isDirectory){
            for (newFile in file.listFiles()){
                if(newFile==null)continue
                delete(newFile)
            }
        }
        file.delete()
    }
}