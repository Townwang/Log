# Log

> 定制个Log,自带的Json不会格式化 ,而且不能超过4000 很尴尬,很难受!

## 使用方法

第一步：build.gradle

```java

```

第二步: 设置配置(最好在Application中)
```java
       //初始化设置过滤关键词以及是否打印
        Log.setConfig("Demo",true);
```
开始使用吧：
```java
    Log.v ("皮一下,很开心");
    Log.d ("这很代码");
    Log.i ("重要的在下面");
    Log.e ("出了个错,别介意");
    Log.json ("打印JSON","这里的Json字符串");
```
## 看看效果??
![微信截图_20180705173344.png](https://i.loli.net/2018/07/05/5b3de8221f686.png)
