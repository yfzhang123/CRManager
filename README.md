# Crm
## 工程搭建
### pom.xml
*工程中引入的坐标资源环境

··· xml
<dependencies>
<!--Servlet-->
<dependency>
<groupId>javax.servlet</groupId>
<artifactId>javax.servlet-api</artifactId>
<version>4.0.1</version>
<scope>provided</scope>
</dependency>

        <!--JSTL-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!--springmvc依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>4.3.16.RELEASE</version>
        </dependency>

        <!--Spring的事务-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>4.3.16.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.16.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.16.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>4.3.16.RELEASE</version>
        </dependency>

        <!--jackson-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.9.0</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.0</version>
        </dependency>
        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.1</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.5</version>
        </dependency>
        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.9</version>
        </dependency>
        <!--连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.12</version>
        </dependency>

        <!--poi-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.15</version>
        </dependency>

        <!-- 文件上传 -->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>
        <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.12</version>
      <scope>provided</scope>
    </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory><!--所在的目录-->
                <includes><!--包括目录下的.properties,.xml文件都会扫描到-->
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
        <plugins>
            <!-- 编码和编译和JDK版本 -->
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
···
### web.xml
* 加载SpringMVC前端控制器
  * applicationContext-web.xml
* 加载Spring容器的监听器
  * applicationContext-service
* 中文乱码过滤器
```
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>crm</display-name>
  <!--注册spring的监听器-->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/applicationContext-service.xml</param-value>
  </context-param>
  <!--注册字符集过滤器-->
  <filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceRequestEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
    <init-param>
      <param-name>forceResponseEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

<!--  <listener>-->
<!--    <listener-class>com.bjpowernode.crm.web.listener.SysInitListener</listener-class>-->
<!--  </listener>-->

  <!--注册springmvc的中央调度器-->
  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring/applicationContext-web.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>
  <!--
    ServletContextListener是web组件，实例化它的是tomcat
    ContextLoader是spring容器的组件，用来初始化容器

  -->

  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
  </welcome-file-list>

</web-app>

```

###applicationContext-web.xml
* 注解扫描，加载controller包下的注解
* 视图解析器
  * 前缀  ‘/WEB-INF/jsp'
  * 后缀  ’.jsp‘
  * 通过控制器方法，返回值为String,即可根据视图解析器的前缀和后缀名称,找到对应的视图界面
* 注解驱动
  * 加载SpringMVC的组件,处理器映射器和处理器适配器
* SpringMVC拦截器
  * 权限控制,除了登录和跳转到登录页面之外,全部请求必须进行权限校验
  * 权限校验,就是必须登录后,才可以访问的
* 文件上传解析器
  * 定义了文件上传的最大内容
···
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:mvc="http://www.springframework.org/schema/mvc"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/mvc
http://www.springframework.org/schema/mvc/spring-mvc.xsd">


    <!--SpringMVC配置文件-->
    <!--声明组件扫描器-->
    <context:component-scan base-package="com.bjpowernode.crm.settings.web.controller"/>
    <context:component-scan base-package="com.bjpowernode.crm.workbench.web.controller"/>
    <context:component-scan base-package="com.bjpowernode.crm.exception"/>

    <!--<context:component-scan base-package="com.bjpowernode.crm"/>-->
    <!--声明视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp" />
        <property name="suffix" value=".jsp" />
    </bean>

    <!--声明注解驱动-->
    <mvc:annotation-driven/>

    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/settings/user/login.do"/>
            <bean class="com.bjpowernode.crm.interceptor.LoginInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <!-- 配置文件上传解析器 id:必须是multipartResolver-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="#{1024*1024*80}"/>
        <property name="defaultEncoding" value="utf-8"/>
    </bean>

  </beans>

···
### applicationContext-service.xml
* 注解扫描，加载service包下的注解
* 声明式事务控制
  * 声明Spring提供的事务管理器
  * 声明切面和切入点
  * 加载service方法，根据方法名称控制事务的开启方式
    * save/update/delete 命名的方法，均默认开启事务 
* 引入`applicationContext-dao.xml`持久层的配置文件
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
">

<!--引入持久层的配置文件-->
<import resource="applicationContext-dao.xml"/>
    <!--声明组件扫描器-->
    <context:component-scan base-package="com.bjpowernode.crm.settings.service" />
    <context:component-scan base-package="com.bjpowernode.crm.workbench.service" />





    <!--事务配置  声明式事务：在源代码之外，控制事务  声明事务管理器，指定完成事务的具体实体类 commit, rollback-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--指定数据源DataSource-->
        <property name="dataSource" ref="dataSource" />
    </bean>
    <!--配置通知（切面）：指定方法使用的事务属性  id:自定义的通知名称  transaction-manager：事务管理器 -->
    <tx:advice id="myAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--指定方法的事务属性    name:方法名称  isolation：隔离级别  propagation：传播行为
                rollback-for：回滚的异常类， 对于自定义的异常要使用全限定名称，系统的异常类可以名称
            -->
            <tx:method name="save*" isolation="DEFAULT" propagation="REQUIRED"
                       rollback-for="com.bjpowernode.crm.exception.AjaxRequestException,
                       com.bjpowernode.crm.exception.TraditionRequestException" />
            <tx:method name="update*" isolation="DEFAULT" propagation="REQUIRED"
                       rollback-for="com.bjpowernode.crm.exception.AjaxRequestException,
                       com.bjpowernode.crm.exception.TraditionRequestException" />
            <tx:method name="delete*" isolation="DEFAULT" propagation="REQUIRED"
                       rollback-for="com.bjpowernode.crm.exception.AjaxRequestException,
                       com.bjpowernode.crm.exception.TraditionRequestException" />
            <!--除了上面的方法以外的，其他方法-->
            <tx:method name="*" propagation="SUPPORTS" read-only="true" />
        </tx:attributes>
    </tx:advice>
    <!--配置aop-->
    <aop:config>
        <!--指定切入点-->
        <aop:pointcut id="servicePt" expression="execution(* *..service..*.*(..))" />
        <!--配置增强器对象（通知+切入点）-->
        <aop:advisor advice-ref="myAdvice" pointcut-ref="servicePt" />
    </aop:config>

</beans>
```
###applicationContext-dao.xml
* 加载数据库连接池信息
    * driver/url/username/password
* 加载`SqlSessionFactoryBran`用于Mybatis的SqlSession的创建
* 加载`MapperScannerConfigurer`用于批量加载dao接口
  *创建dao接口的代理对象，交给Spring容器进行管理
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
">
    <!--Spring配置文件-->
    <!--指定数据库属性配置文件-->
    <context:property-placeholder location="classpath:properties/jdbc.properties" />
    <!--声明DataSource-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="url" value="${jdbc.url}" />
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
    </bean>
    <!--声明SqlSessionFactoryBean-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:mybatis/mybatis-config.xml" />
    </bean>

    <!--声明MyBatis的扫描器-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
        <property name="basePackage" value="com.bjpowernode.crm.settings.dao,com.bjpowernode.crm.workbench.dao" />
    </bean>
</beans>
```
### mybatis-config.xml
* 声明了别名配置
  *无需使用权限定的实体类名称，可以直接使用类名进行操作返回值
* Sql输出日志信息配置
* 用于批量加载dao接口
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!--Sql日志信息输出-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
    <!--配置别名-->
    <typeAliases>
        <package name="com.bjpowernode.crm.settings.domain"/>
        <package name="com.bjpowernode.crm.workbench.domain"/>
    </typeAliases>

    <!--配置sql映射文件-->
    <mappers>
        <package name="com.bjpowernode.crm.settings.dao"/>
        <package name="com.bjpowernode.crm.workbench.dao"/>
    </mappers>


</configuration>
```
### jdbc.properties
```
jdbc.url=jdbc:mysql://localhost:3306/crm?characterEncoding=UTF-8
jdbc.username=root
jdbc.password=123456
```
### log4j.properties
```
log4j.rootLogger=DEBUG,Console
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n
log4j.logger.org.apache=INFO```
