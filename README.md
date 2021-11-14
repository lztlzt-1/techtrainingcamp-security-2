



# 一. 项目要求：

### 作业名称
《抓到你了——具备安全防护能力的账号系统》

### 产品形态
具有安全风控防护能力的账号注册登录系统

### 产品功能介绍
设计并开发一个登录注册系统，可以支持注册、登录和登出或注销的基本功能，另外也需要对一些黑灰产的用户，例如薅羊毛的这种异常用户，进行识别和制定风控策略进行不同程度的拦截和处罚

### 要实现的接口包括：
- 获取验证码
- 注册
- 登录
- 登出或注销

### 恶意行为的用户：
- 例如频繁刷注册等
- 其他的恶意行为可以结合接口自行拓展，可以从恶意用户的角度来考虑进行检测和防护。

### 对恶意行为用户的处罚主要包括三种措施：
- 滑块验证：用户通过滑块验证后才能执行后续的动作，在这之前不能继续进行后续的动作
- 过一段时间重试：用户需要等待一段时间才能执行后续的动作，在这之前不能继续进行后续的动作
- 拦截用户的请求：对这个用户的请求直接拒绝后续动作的执行

# 二、使用说明：

​	需要在本地打开 mysql 和redis服务，并在C:\Users\M\Desktop\11\techtrainingcamp-security-2\src\main\resources配置环境，之后请重新load一遍pom.xml

​	注意：本小组的开发环境是jdk1.8，其余版本可能报错

# 三. 技术栈：
-  框架
	-  springboot
	- spring security
	- mybatis
- 语言
	- java
	- html
	- css
	- js 
- 数据库
	- mysql
- 缓存
	- redis
- 接口测试工具
	- swagger-ui 3 

# 三. 架构图
![请添加图片描述](https://img-blog.csdnimg.cn/5fc653ba44e4456fa08a8df3c27762da.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5Lmd5bm95a2k57-O,size_20,color_FFFFFF,t_70,g_se,x_16)

![请添加图片描述](https://img-blog.csdnimg.cn/bb4951d161cd4cb684307e511dcd28ed.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5Lmd5bm95a2k57-O,size_20,color_FFFFFF,t_70,g_se,x_16)
![请添加图片描述](https://img-blog.csdnimg.cn/6c6d7c2a22974889830b28b6019ff2ab.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5Lmd5bm95a2k57-O,size_20,color_FFFFFF,t_70,g_se,x_16)


# 四. 功能与实现
#### 注册
 1. 一个手机号只能注册一个账号；通过数据库层将手机与账号都设置为唯一索引保证
 2.  用户能通过注册页面注册账户
#### 登录
1. 设置token有效期为2分钟, 有效期内登陆过的用户可以不登陆直接访问；通过自定义的jwt鉴权过滤器实现
#### 验证码
1.  图形和手机验证码不会明文发送给前端；是以生成的uuid为key存储在redis中，将uuid发送给前端
2. 登录采用图形验证码功能，会将图形验证码打印在控制台
3. 注册采用手机验证码功能，会将手机验证码打印在控制台
#### 注销
1. 只有登录过的账户才可以注销；因为前端必须发给这个接口token，才可以注销
2.  通过点击按钮，可以注销当前账号；后端删除redis中的token以及从数据库中删除账号
#### 安全
1.  未登录用户禁止访问主页面，不允许访问非匿名接口；spring security自带功能
2. json web token鉴权；token 采用强散列哈希加密，缓存在redis中
3. 跨域防护；通过自定义的cors过滤器实现
4.  xss注入防护；spring security自带功能
5.  能过滤掉被加入黑名单和临时封禁的账号；通过自定义的ip过滤器以及redis实现
#### 风控
1.  用户在一分钟内访问接口次数超过100次，将被禁止访问1min，并且提升违规等级。禁止访问功能的实现是使用redis中的zset数据结构，score为时间戳，获取时按score从大到小排序，使得能更快获取的频繁访问的k-v对，并且可以定期清楚已经惩罚结束的用户。
2. 当用户违规等级不为0时，对用户的行为进行滑块验证码校验；通过redis获取用户的违规等级，如果不为0，给前端的request中增加字段，告诉前端需要启用滑块校验
3. 当用户违规次数在3~7次时，封禁用户7天；通过redis的zset实现
4. 当用户违规次数超过7次，加入永久黑名单；通过redis的set实现

# 五. 可以优化的地方
1. 黑名单set可以修改为布隆过滤器，通过牺牲极小的准确性，节省大量空间和时间。
2. 记录接口访问次数可以封装成一个切面，而不是一味地复制粘贴计数代码（这次要记录的接口比较少，所以就没弄）。
3. 可以考虑根据DDD领域驱动设计的思想，以领域为单位划分类。
4. 可以进阶架构为分布式结构，构建微服务，并升级缓存为分布式缓存。
5. 通过请教安全专家，指定更有效的安全策略。
