## Log
[![Download](https://api.bintray.com/packages/townwang/Log/logutils/images/download.svg)](https://bintray.com/townwang/Log/logutils/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Breaking android log word limits and automatically formatting json.

> 定制个Log,自带的Json不会格式化 ,而且不能超过4000 很尴尬,很难受!

## 使用方法
build.gradle(Module.app)

```java
	dependencies {
    implementation 'com.townwang:Log:v1.0.0'
	}
```

## 设置配置(最好在Application中)
```java
       //初始化设置过滤关键词以及是否打印
        Log.setConfig("Demo",true);
```
## 开始使用吧：
```java
    Log.v ("皮一下,很开心");
    Log.d ("这很代码");
    Log.i ("重要的在下面");
    Log.e ("出了个错,别介意");
    Log.json ("打印JSON","这里的Json字符串");
```
## 看看Json效果
![微信截图_20180705173344.png](https://i.loli.net/2018/07/05/5b3de8221f686.png)

