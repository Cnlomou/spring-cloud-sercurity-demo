# spring-cloud-security初体验

###  什么是spring-cloud-security

spring-cloud-security 是 一个基于oauth2.0实现的安全框架,提供第三方应用授权登陆，是第三方应用没有用户密码也能请求用户资源

### oauth2.0

oauth2.0是一套授权协议，内容设计资源拥有者、客户端、服务提供者（认证/授权服务器、资源服务器），授权流程如下

![image-20200515204744933](.\image-20200515204744933.png)

### 搭建项目
- 导入依赖

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-security</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-oauth2</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```
 - 配置WebSecurityConfig

  ```java
  @Configuration
  @EnableWebSecurity
  @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled =  true,jsr250Enabled = true)
  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
      @Bean
      public PasswordEncoder passwordEncoder(){
          return new BCryptPasswordEncoder();
      }
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          auth.inMemoryAuthentication() .withUser(User.builder().username("jsh").password(passwordEncoder().encode("123456")).roles("USER").build()) .withUser(User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("ADMIN","USER").build());
      }
      
  }
  ```
    - 配置AuthorizationServerConfig
    ```java
    @Configuration
    @EnableAuthorizationServer
    public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    
        @Autowired
        private PasswordEncoder passwordEncoder;
    
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("acme")
                    .secret(passwordEncoder.encode("acmesecret"))
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes("openid")
                    .redirectUris("https://github.com/");   //回调地址
        }
    
    }
    ```


- 获取授权码

  ```html
  GEt http://localhost:8080/oauth/authorize?client_id=acme&response_type=code
  ```


- 获取token

  ​		携带code、clientId、secret请求授权服务器
  
  ```html
  POST http://acme:acmesecret@localhost:8080/oauth/token
  <!--
   grant_type=authorization_code
   code=获取授权码返回的code
-->
  ```
  
  ​		成功如下，获取到access_token、后面我们就能用token访问资源服务器
  
  ![image-20200515205029080](C:\Users\ASUS\Desktop\spring-cloud-security\image-20200515205029080.png)

# 基于jdbc的spring-cloud-security

前面我们初体验了spring-cloud-security，当时我们把数据都是存放在内存当中，

在实际开发中，我们不应该这么做，这节我们介绍基于jdbc的使用。

首先我们要在数据库中建立表结构，spring项目案例也给我们提供了

```java
https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
```

### 建表

由于mysql不支持LONGBINARY所以我们把它换成blob

``` sql
-- used in tests that use HSQL
start TRANSACTION;
create table oauth_client_details (
  client_id VARCHAR(128) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(512),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token blob,
  authentication_id VARCHAR(128) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token blob,
  authentication_id VARCHAR(125) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication blob,
  refresh_token blob
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token blob,
  authentication blob
);

create table oauth_code (
  code VARCHAR(256), 
	authentication blob
);

create table oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);


-- customized oauth_client_details table
create table ClientDetails (
  appId VARCHAR(128) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);
COMMIT;
```

向数据库中oauth_client_details添加数据,secret使用的是Bcry加密

![image-20200516000056281](.\image-20200516000056281.png)

###  导入mysql相关依赖

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.15</version>
</dependency>
```

### yml配置

```yml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/oauth2?serverTimezone=UTC&characterEncoding=utf8
    driver-class-name: com.mysql.cj.jdbc.Driver
  main:
    allow-bean-definition-overriding: true
  application:
    name: author-server
```
### 配置类AuthoricationServerConfig
由于基于mysql的存储，所以需要增加以下配置
```java
@Bean
public ClientDetailsService clientDetailsService() throws Exception {
        return new JdbcClientDetailsServiceBuilder().dataSource(dataSource).build();
    }

@Bean
public TokenStore jdbcTokenstore() {
        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
        return jdbcTokenStore;
    }

@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jdbcTokenstore());
    }
```
运行流程还是和上述一样,授权成功结果如下
![image-20200515235614920](.\image-20200515235614920.png)

# 基于RBAC的访问控制

### 什么是RBAC?  

基于角色的访问控制 

```txt
其基本思想是，对系统操作的各种权限不是直接授予具体的用户，而是在用户集合与权限集合之间建立一个角色集合。每一种角色对应一组相应的权限。一旦用户被分配了适当的角色后，该用户就拥有此角色的所有操作权限。这样做的好处是，不必在每次创建用户时都进行分配权限的操作，只要分配用户相应的角色即可，而且角色的权限变更比用户的权限变更要少得多，这样将简化用户的权限管理，减少系统的开销。
```

### 那么角色与用户之间有什么关系？

在创建用户的时候会给用户赋予角色，一个角色可以对应多个用户，

一个用户可以拥有多个角色，像管理员用户即拥有ADMIN角色也拥有USER角色，

他们是多对多的关系，下面是表结构

```sql
CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `enname` varchar(64) NOT NULL COMMENT '权限英文名称',
  `url` varchar(255) NOT NULL COMMENT '授权路径',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='权限表';

CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `enname` varchar(64) NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `tb_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `tb_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户角色表';
```

# 搭建资源服务器

### 依赖

导入依赖，就不说了，和上面一样

### 配置类

```java
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

}

```

### yml

```yml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/resource?serverTimezone=UTC&characterEncoding=utf8
    driver-class-name: com.mysql.cj.jdbc.Driver
  main:
    allow-bean-definition-overriding: true
  application:
    name: author-resource

security:
  oauth2:
    client:
      client-id: acme
      client-secret: acmesecret
      access-token-uri: http://localhost:8080/oauth/token
      user-authorization-uri: http://localhost:8080/oauth/authorize
    resource:
      token-info-uri: http://localhost:8080/oauth/check_token
logging:
  level:
    org:
      springframework:
        security: debug
server:
  port: 9090
mybatis:
  type-aliases-package: com.example.pojo
  mapper-locations: classpath:mapper/*.xml
```

> 这里需要注意的是，要在授权服务器开放/oauth/check_token 

在WebSecurityConfig类下添加

```java
 @Override
    public void init(WebSecurity web) throws Exception {
        //提供给客户端检查token
        web.ignoring().mvcMatchers("/oauth/check_token");
        super.init(web);
    }
```

这样资源服务器就搭建好了，若我们没有授权就不能访问资源服务器

以上学习通过[李卫民老师的博客]( https://www.funtl.com/ )

