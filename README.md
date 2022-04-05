# 日志读取器

![log-reader](./src/main/resources/log-reader.jpeg)

***说明：此项目为练习TDD而使用！***

在实际的开发中，应用程序的日志扮演着举足轻重的地位，日志可以帮助开发人员分析问题，排查问题，监控应用程序的状态，为提高应用程序的稳定性起着至关重要的作用。

大部分应用程序的日志以`log.txt`的文件格式存储，面对日益增长的文件数量和大规模的数据，这种文件格式并不利于开发人员排查问题，比如，你想分析某年某月某日的所有错误的日志，
你就需要找到这个时间点的日志，下载下来，然后再用文本编辑器打开日志文件，检索为`ERRPR`格式的日志行，进而分析定位问题。

上面的这一系列过程既耗时又耗力，为了解决这一问题，我们想开发一款辅助开发人员定位Log日志问题的"阅读器"，使得开发人员能够通过API接口，输入特定的查询条件，就可以基于特定的Log文件检索到对应的日志。

## 业务逻辑

下面是此`log-reader`的所有业务逻辑：
### 前置条件

1. 此日志阅读器能够处理如下格式的日志文件：
    ```log
    2022-04-02 18:01:28.855 [Test worker] WARNING c.log.resourse.LogReaderControllerTest   : Starting LogReaderControllerTest using Java 1.8.0_282 on Jinghuas-MacBook-Pro-2.local with PID 49751 (started by jinhli in /Users/jinhli/workspace/MyProject/TDD/log-reader)
    2022-04-02 18:01:28.856 [Test worker] INFO c.log.resourse.LogReaderControllerTest   : No active profile set, falling back to 1 default profile: "default"
    2022-04-02 18:01:30.251 [Test worker] WARNING o.s.b.t.m.w.SpringBootMockServletContext : Initializing Spring TestDispatcherServlet ''
    2022-04-02 18:01:30.251 [Test worker] INFO o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''
    2022-04-02 18:01:30.253 [Test worker] INFO o.s.t.web.servlet.TestDispatcherServlet  : Completed initialization in 1 ms
    2022-04-02 18:01:30.281 [Test worker] WARNING c.log.resourse.LogReaderControllerTest   : Started LogReaderControllerTest in 1.846 seconds (JVM running for 2.79)
    2022-04-02 04:15:31.006 [llEventLoop-5-1] ERROR reactor.core.publisher.Operators         : Operator called default onErrorDropped
    org.springframework.dao.DataAccessResourceFailureException: Unable to acquire JDBC Connection; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection
    at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:277) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]
    at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:255) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]
    at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:528) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]
    at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61) ~[spring-tx-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]
    at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:242) ~[spring-tx-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]
    2022-03-28 04:16:01.013 [llEventLoop-5-1] INFO o.h.engine.jdbc.spi.SqlExceptionHelper   : HikariPool-1 - Connection is not available, request timed out after 30000ms.
    2022-03-28 04:16:01.013 [llEventLoop-5-1] ERROR o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure
    ```
3. 需要分析的日志文件暂时位于项目的`resource`目录下面，可以在`application.yml`里面进行配置。

### AC1：查询所有日志

* **Given:** log-reader已经配置了如前置条件里面声明的log文件
* **When:** 软件开发人员能够使用Log-Reader的如下接口来查询所有的日志文件：
    ```curl --location --request GET 'http://localhost:8088/log-reader/search'```
* **Then:** 上面的接口将返回到目前为止所有的日志，返回结果格式如下：
    ```json
    [
        {
            "type": "ERROR",
            "message": "reactor.core.publisher.Operators         : Operator called default onErrorDropped",
            "dateTime": "2022-04-03 04:16:01",
            "threadId": "llEventLoop-5-1"
        },
        {
            "type": "WARNING",
            "message": "o.s.b.t.m.w.SpringBootMockServletContext : Initializing Spring TestDispatcherServlet ''",
            "dateTime": "2022-04-02 18:01:30",
            "threadId": "Test worker"
        },
        {
            "type": "INFO",
            "message": "o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''",
            "dateTime": "2022-04-02 18:01:30",
            "threadId": "Test worker"
        }
    ]
    ```

### AC2: 根据特定条件查询日志
* **Given:** log-reader已经配置了如前置条件里面声明的log文件
* **When:** 软件开发人员能够使用Log-Reader的如下接口来查询所有的日志文件：
  * 查询特定类型的日志(ERROR/WARNING/INFO/DEBUG)：`curl --location --request GET 'http://localhost:8088/log-reader/search?type=ERROR'`
  * 查询特定时间段内的日志：`curl --location --request GET 'http://localhost:8088/log-reader/search?startTime=2022-04-02 04:16:01&endTime=2022-04-03 04:13:05'`
  * 查询特定时间段内特定类型的日志：`curl --location --request GET 'http://localhost:8088/log-reader/search?type=INFO&startTime=2022-04-02 04:16:01&endTime=2022-04-03 04:13:05'`
* **Then:** 上面的接口返回日志如下：
  * 仅返回到目前为止的特定类型的日志，如：
      ```json
      [
          {
              "type": "ERROR",
              "message": "reactor.core.publisher.Operators         : Operator called default onErrorDropped",
              "dateTime": "2022-04-03 04:16:01",
              "threadId": "llEventLoop-5-1"
          }
      ]
      ```
  * 仅返回特定时间段内的日志：
    ```json
      [
          {
            "type": "WARNING",
            "message": "o.s.b.t.m.w.SpringBootMockServletContext : Initializing Spring TestDispatcherServlet ''",
            "dateTime": "2022-04-02 18:01:30",
            "threadId": "Test worker"
          },
          {
            "type": "INFO",
            "message": "o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''",
            "dateTime": "2022-04-02 18:01:30",
            "threadId": "Test worker"
          }
      ]
    ```
  * 仅返回特定时间段内特定格式的日志：
    ```json
    [
        {
            "type": "INFO",
            "message": "o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''",
            "dateTime": "2022-04-02 18:01:30",
            "threadId": "Test worker"
        }
    ]
    ```
## 你需要具备什么？
由于此次课程属于TDD的中级课程，所以你需要具备以下技能才能完整的学习：

* TDD基础知识
* Java基本技能
* Spring Boot/Redis/Mockito等基础知识

## 你能收获什么？
学习完成本课程，你讲获得如下的收获：

* Tasking的基本思路与实现方案
* 可用于生产环境的基于三层架构的测试策略
* Mock、Stub、Spy的使用方法

## 你需要做什么？  
**1.Clone此仓库到你的本地**
```bash
git clone https://github.com/Jinghua-Li/otp.git
```
**2. 进入到此项目的文件夹下，执行Build命令：**
```bash
./gradlew clean build
```

**3. 启动该项目，在本地尝试着访问：**

```bash
./gradlew bootRun
```

可以访问如下API：`curl --location --request GET 'http://localhost:8088/log-reader/search'`

**4. 使用IntellJ Idea打开此项目，等待相关依赖导入成功，之后切换到tdd分支进行开发。**


## 分支管理说明
此项目总共三个分支：

* **main分支：** 此分支已经完整实现了此应用程序，如果您自己实现的时候有问题，可以适当参考。
* **tdd分支：** 此分支用于让大家练习TDD的分支，包括基本的依赖和相关的脚手架代码。
* **test分支：** 此分支用于对测试不熟悉的小伙伴，想在有测试辅助的情况下练习TDD，新手可以依次去掉测试的注释逐步来实现。