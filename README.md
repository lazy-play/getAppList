# getAppList
adb启动透明activity来保存手机内应用

包括应用名，应用包名，应用版本号，应用图标

intent参数
    
    showSystem (Boolean)是否显示系统应用
    
    zipName    (String) 自定义压缩包文件名，不带.zip  默认为AppInfo

adb shell am start -n com.pudding.getapplist/.MainActivity --ez "showSystem" true --es "zipName" "zipImage"
