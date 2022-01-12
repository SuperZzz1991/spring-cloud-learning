# spring-cloud-learning

[参考文档](http://www.macrozheng.com/#/cloud/springcloud)

## Eureka:服务注册/发现

##### 模块说明

* eureka-server --eureka注册中心
* eureka-security-server -- 带登录认证的eureka注册中心
* eureka-client -- eureka客户端

##### 常用配置

```yml
eureka:
  client: #eureka客户端配置
    register-with-eureka: true #是否将自己注册到eureka服务端上去
    fetch-registry: true #是否获取eureka服务端上注册的服务列表
    service-url:
      defaultZone: http://localhost:8001/eureka/ # 指定注册中心地址
    enabled: true # 启用eureka客户端
    registry-fetch-interval-seconds: 30 #定义去eureka服务端获取服务列表的时间间隔
  instance: #eureka客户端实例配置
    lease-renewal-interval-in-seconds: 30 #定义服务多久去注册中心续约
    lease-expiration-duration-in-seconds: 90 #定义服务多久不去续约认为服务失效
    metadata-map:
      zone: jiangsu #所在区域
    hostname: localhost #服务主机名称
    prefer-ip-address: false #是否优先使用ip来作为主机名
  server: #eureka服务端配置
    enable-self-preservation: false #关闭eureka服务端的保护机制
```

##### pom.xml依赖

```xml
<dependencies>
    <!--服务端依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <!--客户端依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--Security登录中心依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

##### 使用

* @EnableEurekaServer
* @EnableDiscoveryClient
* Eureka注册中心添加认证
    * 1.服务端添加Java配置WebSecurityConfig
    * 2.客户端通过用户名密码进行注册(http://${username}:${password}@${hostname}:${port}/eureka/)


## Ribbon：负载均衡

##### 模块说明

* eureka-server --eureka注册中心
* euser-service -- 提供User对象CRUD接口的服务（多实例）
* ribbon-service -- ribbon服务调用测试服务

##### 全局配置

```yml
ribbon:
  ConnectTimeout: 1000 #服务请求连接超时时间（毫秒）
  ReadTimeout: 3000 #服务请求处理超时时间（毫秒）
  OkToRetryOnAllOperations: true #对超时请求启用重试机制
  MaxAutoRetriesNextServer: 1 #切换重试实例的最大个数
  MaxAutoRetries: 1 # 切换实例后重试最大次数
  NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #修改负载均衡算法
```

##### 指定服务进行配置

```yml
user-service:
  ribbon:
    ConnectTimeout: 1000 #服务请求连接超时时间（毫秒）
    ReadTimeout: 3000 #服务请求处理超时时间（毫秒）
    OkToRetryOnAllOperations: true #对超时请求启用重试机制
    MaxAutoRetriesNextServer: 1 #切换重试实例的最大个数
    MaxAutoRetries: 1 # 切换实例后重试最大次数
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #修改负载均衡算法
```

##### Ribbon的负载均衡策略

>所谓的负载均衡策略，就是当A服务调用B服务时，此时B服务有多个实例，这时A服务以何种方式来选择调用的B实例，ribbon可以选择以下几种负载均衡策略。

* com.netflix.loadbalancer.RandomRule：从提供服务的实例中以随机的方式；
* com.netflix.loadbalancer.RoundRobinRule：以线性轮询的方式，就是维护一个计数器，从提供服务的实例中按顺序选取，第一次选第一个，第二次选第二个，以此类推，到最后一个以后再从头来过；
* com.netflix.loadbalancer.RetryRule：在RoundRobinRule的基础上添加重试机制，即在指定的重试时间内，反复使用线性轮询策略来选择可用实例；
* com.netflix.loadbalancer.WeightedResponseTimeRule：对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择；
* com.netflix.loadbalancer.BestAvailableRule：选择并发较小的实例；
* com.netflix.loadbalancer.AvailabilityFilteringRule：先过滤掉故障实例，再选择并发较小的实例；
* com.netflix.loadbalancer.ZoneAwareLoadBalancer：采用双重过滤，同时过滤不是同一区域的实例和故障实例，选择并发较小的实例。

##### pom.xml依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
</dependencies>
```

##### 使用

* RestTemplate的使用(GET,POST,PUT,DELETE)
    * GET
        ```
        <T> T getForObject(String url, Class<T> responseType, Object... uriVariables);
        <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables);
        <T> T getForObject(URI url, Class<T> responseType);
        <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);
        <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables);
        <T> ResponseEntity<T> getForEntity(URI var1, Class<T> responseType);
        ```
    * POST
        ```
        <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables);
        <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables);
        <T> T postForObject(URI url, @Nullable Object request, Class<T> responseType);
        <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables);
        <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables);
        <T> ResponseEntity<T> postForEntity(URI url, @Nullable Object request, Class<T> responseType);
        ```
    * PUT
        ```
        void put(String url, @Nullable Object request, Object... uriVariables);
        void put(String url, @Nullable Object request, Map<String, ?> uriVariables);
        void put(URI url, @Nullable Object request);
        ```
    * DELETE
        ```
        void delete(String url, Object... uriVariables);
        void delete(String url, Map<String, ?> uriVariables);
        void delete(URI url);
        ```
* RibbonConfig中配置@LoadBalanced
```java
@Configuration
public class RibbonConfig {
   @Bean
   @LoadBalanced
   public RestTemplate restTemplate(){
       return new RestTemplate();
   }
}
```

## Hystrix：服务容错保护

##### 模块说明

* eureka-server --eureka注册中心
* user-service -- 提供User对象CRUD接口的服务
* hystrix-service -- hystrix服务调用测试服务

##### 全局配置

```yml
hystrix:
  command: #用于控制HystrixCommand的行为
    default:
      execution:
        isolation:
          strategy: THREAD #控制HystrixCommand的隔离策略，THREAD->线程池隔离策略(默认)，SEMAPHORE->信号量隔离策略
          thread:
            timeoutInMilliseconds: 1000 #配置HystrixCommand执行的超时时间，执行超过该时间会进行服务降级处理
            interruptOnTimeout: true #配置HystrixCommand执行超时的时候是否要中断
            interruptOnCancel: true #配置HystrixCommand执行被取消的时候是否要中断
          timeout:
            enabled: true #配置HystrixCommand的执行是否启用超时时间
          semaphore:
            maxConcurrentRequests: 10 #当使用信号量隔离策略时，用来控制并发量的大小，超过该并发量的请求会被拒绝
      fallback:
        enabled: true #用于控制是否启用服务降级
      circuitBreaker: #用于控制HystrixCircuitBreaker的行为
        enabled: true #用于控制断路器是否跟踪健康状况以及熔断请求
        requestVolumeThreshold: 20 #超过该请求数的请求会被拒绝
        forceOpen: false #强制打开断路器，拒绝所有请求
        forceClosed: false #强制关闭断路器，接收所有请求
      requestCache:
        enabled: true #用于控制是否开启请求缓存
  collapser: #用于控制HystrixCollapser的执行行为
    default:
      maxRequestsInBatch: 100 #控制一次合并请求合并的最大请求数
      timerDelayinMilliseconds: 10 #控制多少毫秒内的请求会被合并成一个
      requestCache:
        enabled: true #控制合并请求是否开启缓存
  threadpool: #用于控制HystrixCommand执行所在线程池的行为
    default:
      coreSize: 10 #线程池的核心线程数
      maximumSize: 10 #线程池的最大线程数，超过该线程数的请求会被拒绝
      maxQueueSize: -1 #用于设置线程池的最大队列大小，-1采用SynchronousQueue，其他正数采用LinkedBlockingQueue
      queueSizeRejectionThreshold: 5 #用于设置线程池队列的拒绝阀值，由于LinkedBlockingQueue不能动态改版大小，使用时需要用该参数来控制线程数
```

##### 实例配置

```yml
hystrix:
  command:
    HystrixComandKey: #将default换成HystrixComrnandKey
      execution:
        isolation:
          strategy: THREAD
  collapser:
    HystrixCollapserKey: #将default换成HystrixCollapserKey
      maxRequestsInBatch: 100
  threadpool:
    HystrixThreadPoolKey: #将default换成HystrixThreadPoolKey
      coreSize: 10
```

>配置文件中相关key的说明

* HystrixComandKey对应@HystrixCommand中的commandKey属性；
* HystrixCollapserKey对应@HystrixCollapser注解中的collapserKey属性；
* HystrixThreadPoolKey对应@HystrixCommand中的threadPoolKey属性

##### pom.xml依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
</dependencies>
```

##### 使用

* @EnableCHystrix~~@EnableCircuitBreaker~~：开启断路器
* @HystrixCommand
    * fallbackMethod：指定服务降级处理方法；
    * ignoreExceptions：忽略某些异常，不发生服务降级；
    * commandKey：命令名称，用于区分不同的命令；
    * groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
    * threadPoolKey：线程池名称，用于划分线程池。
* Hystrix请求缓存
    * @CacheResult：开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key
    * @CacheKey：指定缓存的key，可以指定参数或指定参数中的属性值为缓存key，cacheKeyMethod还可以通过返回String类型的方法指定；
    * @CacheRemove：移除缓存，需要指定commandKey。
* @HystrixCollapser：请求合并，减少通信消耗及线程数量
    * batchMethod：用于设置请求合并的方法；
    * collapserProperties：请求合并属性，用于控制实例属性，有很多；
    * timerDelayInMilliseconds：collapserProperties中的属性，用于控制每隔多少时间合并一次请求；

##### 问题

1. HystrixRequestContext未初始化。通过使用过滤器，在每个请求前后初始化和关闭HystrixRequestContext来解决
```
java.lang.IllegalStateException: Request caching is not available. Maybe you need to initialize the HystrixRequestContext?
      at com.netflix.hystrix.HystrixRequestCache.get(HystrixRequestCache.java:104) ~[hystrix-core-1.5.18.jar:1.5.18]
      at com.netflix.hystrix.AbstractCommand$7.call(AbstractCommand.java:478) ~[hystrix-core-1.5.18.jar:1.5.18]
      at com.netflix.hystrix.AbstractCommand$7.call(AbstractCommand.java:454) ~[hystrix-core-1.5.18.jar:1.5.18]
```
```java
@Component
@WebFilter(urlPatterns = "/*",asyncSupported = true)
public class HystrixRequestContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            context.close();
        }
    }
}
```

## 标题

##### 模块说明

* eureka-server --eureka注册中心
* user-service -- 提供User对象CRUD接口的服务
* hystrix-service -- hystrix服务调用测试服务

##### 常用配置

```yml
```

##### pom.xml依赖

```xml
```

##### 使用