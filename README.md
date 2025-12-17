# 脚手架模板工程

## 包结构
### common 通用类模块 
* dto包: 对外提供接口需要的bean对象
* enums: 枚举
* constants: 静态变量
* builder: dto对象的创建类
* utils: 公用工具类

### inner 对内接口类
#### aspect包: aop切面类
#### config包: 配置类
* cache包:
  - CacheManagerConfiguration: 多cache manager配置
  - CacheConfiguration: 默认cache manager配置
  - MyCacheConfig: 自定义cache配置
  - RedissonConfiguration:  redisson配置类（@Bean若放开注释，须提前配置redis）
* mq包:
  - RocketMQConfig: rocketmq配置
  - ConsumerClient: rabbitmq consumer client配置
* SwaggerConfig: swagger配置
#### controller: 对外提供http服务
#### dto包: 对外提供接口需要的bean对象
#### exception: 异常
* GlobalExceptionHandlerAdvice： 全局异常处理
#### health: health健康检查接口
#### listener: 监听包
#### schedule: 定时任务包
#### service包: 业务层

### web 对外服务
#### aspect包: aop切面类
#### config包: 配置类
* cache包:
  - CacheManagerConfiguration: 多cache manager配置
  - CacheConfiguration: 默认cache manager配置
  - MyCacheConfig: 自定义cache配置
  - RedissonConfiguration:  redisson配置类（@Bean若放开注释，须提前配置redis）
* mq包:
  - RocketMQConfig: rocketmq配置
  - ProducerClient: rabbitmq producer client配置
* WebAppConfig: web mvc配置
#### controller: 对外提供http服务
#### dto包: 对外提供接口需要的bean对象
#### exception: 异常
* GlobalExceptionHandlerAdvice： 全局异常处理
#### health: health接口
#### interceptor: 拦截器包
#### service包: 业务层  

### manager 通用业务包
#### dao: 数据库查询
#### dto包: 对外提供接口需要的bean对象
* po: 数据库po
* bo: 业务处理对象
#### feign包: 
* client包: 相关feign client
* config包: 错误处理及拦截器
* request包: feign请求request
* reponse包: feign请求response
#### manager: 通用业务类


## 引用
### 数据库:
* druid 
* mybatis-plus
### 缓存:
* redisson
* jedis
* caffeine
* spring-boot-starter-data-redis
### 测试
* spring-boot-starter-test
* jupiter
### RPC
* springcloud
   - okhttp3
   - openfeign
* openservices  mq消息
### 配置
* nacos
### 工具包
* simply-common-core 核心公用common包
* simply-common-cache 缓存公用common包