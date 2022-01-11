# spring-cloud-learning

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