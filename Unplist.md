# unplist

unplist解图集，书写方式只针对固定格式，可以使用map，然后对参数通过key进行读取，可以实现灵活。

## 使用方式

执行unplist类

## 核心代码

```java

得到开始切割位置    结束位置  长宽高，还有原始宽和高   是否有旋转
String[] points=text1.replace("{","").replace("}","").split(",");
int left=Integer.parseInt(points[0]);
int top=Integer.parseInt(points[1]);
int width=Integer.parseInt(points[2]);
int height=Integer.parseInt(points[3]);

String[] offsets=text2.replace("{","").replace("}","").split(",");
int offleft=0;
int offdown=0;

boolean rotate=Boolean.parseBoolean(text3);

String[] sizes=text4.replace("{","").replace("}","").split(",");
int oriwidth=Integer.parseInt(sizes[0]);
int oriheight=Integer.parseInt(sizes[1]);
```












## 增加unTexturePacker 

······









