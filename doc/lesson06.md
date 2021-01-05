# Онлайн-проект <a href="https://github.com/JavaWebinar/topjava">Topjava</a>

## <a href="https://drive.google.com/drive/folders/1Urw8CidiVJDIXd9IwyadjBxPmpsL_MCr">Материалы занятия</a>

### ![correction](https://cloud.githubusercontent.com/assets/13649199/13672935/ef09ec1e-e6e7-11e5-9f79-d1641c05cbe6.png) Правки в проекте

#### Apply 6_0_fix.patch
- замена deprecated метода
- рефакторинг `Profiles`

## ![hw](https://cloud.githubusercontent.com/assets/13649199/13672719/09593080-e6e7-11e5-81d1-5cb629c438ca.png) Разбор домашнего задания HW5

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 1. [HW5: Spring Profiles. Spring Data JPA](https://drive.google.com/file/d/1dlhXeQr0fi0XymEFyBG-TXv5hpPgXtlT)

#### Apply 6_01_HW5_data_jpa.patch
Транзакция начинается, когда встречается первая `@Transactional`. С default propagation `REQUIRED` остальные `@Transactional` просто участвуют в первой. Поэтому ставим аннотацию сверху `DataJpaMealRepository.save()`, чтобы все обращения к базе внутри метода были в одной транзакции. Аналогично, если из сервиса собирается несколько запросов к репозиториям, `@Transactional` ставится над методом сервиса.

#### Apply 6_02_HW5_profile_test.patch
**Для IDEA в `spring-db.xml` не забудьте выставить Spring Profiles. Например `datajpa, postgres`**

> - Заменил `description.getMethodName()` на `getDisplayName()` в выводе результатов тестов. После `printResult()` буфер сбрасывается в 0, чтобы не накапливать изменения. 

#### Apply 6_03_extract_rules.patch
> Вынес измерение времени и сводку в отдельный класс `TimingRules`

[JUnit Rules External Resources](https://carlosbecker.com/posts/junit-rules/#external-resources)

## ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 2. [HW5: Optional](https://drive.google.com/file/d/1S6gOOzLV9ndSbPiSuyEmqhEy3xZoUpXZ)

#### Apply 6_04_HW5_optional_fix_jdbc_profiles.patch

- <a href="http://javarticles.com/2013/12/spring-profiles.html">Spring Profiles</a>. <a href="https://www.javacodegeeks.com/2013/10/spring-4-conditional.html">Spring 4 Conditional</a>.
- дополнительно:
  - зайдите в исходники `@Profile` и посмотрите (подебажьте) его  реализацию через `@Conditional(ProfileCondition.class)`.
  - [реализация через Java Config и Profiles на уровне методов](http://stackoverflow.com/a/43645463/548473)

#### Apply 6_05_update_hsqldb.patch
В реальном проекте часто проблему можно решить простым обновлением версии: <a href="http://hsqldb.org/">new HSQLDB version supports Java 8 time API</a>

#### Apply 6_06_HW5_optional_fetch_join.patch
> - Добавил проверки и тесты на `NotFound` для `MealService.getWithUser` и  `UserService.getWithMeals` 
> - Убрал `CascadeType.REMOVE`, в уроке далее будет про Cascade.

-  <a href="http://stackoverflow.com/questions/11938253/jpa-joincolumn-vs-mappedby">JPA JoinColumn vs mappedBy</a>
-  <a href="https://en.wikibooks.org/wiki/Java_Persistence/OneToMany#Unidirectional_OneToMany.2C_No_Inverse_ManyToOne.2C_No_Join_Table_.28JPA_2.x_ONLY.29">Unidirectional OneToMany</a>

#### Apply 6_07_HW5_graph_batch_size.patch
- **<a href="http://stackoverflow.com/questions/97197/what-is-the-n1-selects-issue">N+1 selects issue</a>**
- <a href="https://docs.oracle.com/javaee/7/tutorial/persistence-entitygraphs002.htm">Using Named Entity Graphs</a>
  - [JPA ENTITY GRAPHS](http://www.radcortez.com/jpa-entity-graphs/)
  - [`EntityGraphType.FETCH` vs `LOAD`](http://stackoverflow.com/questions/31978011/what-is-the-diffenece-between-fetch-and-load-for-entity-graph-of-jpa)
- <a href="https://dou.ua/lenta/articles/jpa-fetch-types/">Стратегии загрузки коллекций в JPA</a>
- <a href="https://dou.ua/lenta/articles/hibernate-fetch-types/">Стратегии загрузки коллекций в Hibernate</a>

> Когда мы достаем всех юзеров с ролями без `@BatchSize`, делается запрос юзеров (1), и на каждого юзера идет в базу запрос ролей (+N).
C `@BatchSize(size = 200)` делается запрос на юзеров (1), и затем роли достаются пачками для 200 юзеров (+ N/200).

## Занятие 6:

### Добавил тесты на валидацию
> - К сожалению, в JUnit <a href="https://github.com/junit-team/junit4/pull/778">нет `ExpectedException.expectRootCause`</a>. `AbstractServiceTest.validateRootCause()` сделал через [JUnit 4.13 assertThrows](https://stackoverflow.com/a/2935935/548473). 

 > ![](https://cloud.githubusercontent.com/assets/13649199/13672858/9cd58692-e6e7-11e5-905d-c295d2a456f1.png) Откуда у нас берется `ConstraintViolationException` в тестах на валидацию? Для каких наших исключений он является рутом?
 
 Прежде всего - пользуйтесь дебагом! Исключение легко увидеть в методе `getRootCause()`. Если подебажить выполение Hibernate валидации, то можно найти, где обрабатываются аннотации валидации и место в `org.hibernate.cfg.beanvalidation.BeanValidationEventListener.validate()`, где бросается `ConstraintViolationException`.

#### Apply 6_08_add_test_validation.patch
**Тесты валидации для Jdbc не работают, нужно будет починить в HW6 (в реализация Jdbc валидация отсутствует)**

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 3. <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFeTV0SUFfblk5NE0">Кэш Hibernate</a>
> Кэш мигрировал на 3.x

#### Apply 6_09_hibernate_cache.patch
**Теперь уже все Jdbc тесты поломались. Требуется починить в HW6**

-  <a href="http://habrahabr.ru/post/135176/">Уровни кэширования Hibernate</a>
-  <a href="http://habrahabr.ru/post/136375/">Hibernate Cache. Практика</a>
-  <a href="http://www.tutorialspoint.com/hibernate/hibernate_caching.htm">Hibernate - Caching</a>
-  Починка тестов: <a href="http://stackoverflow.com/questions/1603846/hibernate-2nd-level-cache-invalidation-when-another-process-modifies-the-databas">инвалидация кэша Hibernate</a>
-  [Hibernate User Guide: Caching](http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#caching)
-  [Hibernate 5, Ehcache 3.x](https://www.boraji.com/index.php/hibernate-5-jcache-ehcache-3-configuration-example)
-  Ресурсы:
   - **<a href="https://www.youtube.com/watch?list=PLYj3Bx1JM6Y7BKivc3eZwRUhWwBmbIFXg&v=V-ljsrVy6pE">Hibernate performance tuning (Mikalai Alimenkou /Igor Dmitriev)</a>**
   -  <a href="http://stackoverflow.com/questions/3663979/how-to-use-jpa2s-cacheable-instead-of-hibernates-cache">JPA2 @Cacheable vs Hibernate @Cache</a>
   -  <a href="http://vladmihalcea.com/2015/06/08/how-does-hibernate-query-cache-work/">How does Hibernate Query Cache work</a>
   -  <a href="https://www.javacodegeeks.com/2014/06/pitfalls-of-the-hibernate-second-level-query-caches.html">Pitfalls of the Hibernate Second-Level / Query Caches</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 4. <a href="https://drive.google.com/file/d/0B9Ye2auQ_NsFVmdpNDJSNXRTWUE">Cascade. Auto generate DDL.</a>
#### Apply 6_10_cascade_ddl.patch
#### Cascading
> Есть SQL ON .. CASCADE, которая выполняется в базе данных, и есть аннотация в Hibernate, исполняемая в приложении

- <a href="http://stackoverflow.com/questions/13027214">Do not use `CascadeType` for @ManyToOne</a>
- <a href="http://stackoverflow.com/questions/836569">CascadeType meaning</a>
- <a href="https://en.wikibooks.org/wiki/Java_Persistence/ElementCollection">No cascade option on an ElementCollection, the target objects are always persisted, merged, removed with their parent.</a>
- <a href="http://stackoverflow.com/questions/21149660">Create ON DELETE CASCADE: `@OnDelete`</a>
- <a href="http://stackoverflow.com/questions/3087040">Hibernate second level cache and ON DELETE CASCADE in database schema</a>
- [`orphanRemoval=true` vs `CascadeType.REMOVE`](http://stackoverflow.com/a/19645397/548473)
- [JPA `cascade/orphanRemoval` doesn't work with `NamedQuery`](http://stackoverflow.com/questions/7825484/jpa-delete-where-does-not-delete-children-and-throws-an-exception)

#### Auto schema generation
- <a href="http://www.radcortez.com/jpa-database-schema-generation/">JPA DATABASE SCHEMA GENERATION</a>
- <a href="http://stackoverflow.com/questions/7793395">hbm2ddl.auto and autoincrement</a>
- <a href="http://stackoverflow.com/questions/2585641">Hibernate/JPA DB Schema Generation Best Practices</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 5.  <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFVE1jWkRucm1UTjA">Spring Web</a>
#### Apply 6_11_spring_web.patch
> - Для сборки проекта в окне Maven отключите тесты (`Toggele 'Skip Tests' Mode`)
> - В `web.xml` задаются профили запуска по умолчанию: `<param-value>postgres,datajpa</param-value>`. **Если запускаетесь под HSQLDB, надо поменять на `hsqldb,datajpa`**.

-  <a href="http://www.mkyong.com/servlet/what-is-listener-servletcontextlistener-example/">ServletContextListener</a>.
-  <a href="https://docs.oracle.com/javaee/6/tutorial/doc/bnafi.html">Servlet Lifecycle</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 6.   <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFN3k0ZVk1MnF5TjQ">JPS, JSTL, internationalization</a>
#### Apply 6_12_jsp_jstl_i18n.patch
> - Поменял `users/meals` в ключах локализации на `user/meal`. Понадобится при локализации ошибок (сделаем позже)
> - [Since Java 9 default encoding in properties files is UTF-8](https://docs.oracle.com/javase/9/intl/internationalization-enhancements-jdk-9.htm). Галочка `Transparent` и ASCII кода уже не нужны.

**Убедитесь, что [в настройках IDEA](https://github.com/JavaOPs/topjava/wiki/IDEA#Поставить-кодировку-utf-8) кодировка везде UTF-8</a>**

-  <a href="http://docs.oracle.com/javaee/1.3/tutorial/doc/JSPIntro8.html">Including Content in a JSP Page</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 7.   <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFLTB3R3pKNFNEQmM">Динамическое изменение профиля при запуске.</a>

    -Dspring.profiles.active="datajpa,postgres"

- <a href="http://stackoverflow.com/questions/10041410/default-profile-in-spring-3-1#answer-10041835">Set profiles in Spring 3.1</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 8.   <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFdkFRRFdYa0NoWkU">Конфигурирование Tomcat через maven plugin. Jndi-lookup.</a>
С плагином мы можем сконфигурировать Tomcat прямо в `pom.xml` и запустить его с задеплоенным туда нашим приложением WAR из командной строки 
без IDEA и без инсталляции Tomcat. По умолчанию он скачивает его из центрального maven-репозитория (можно также указать свой в `<container><home>${container.home}</home></container>`).
При запуске Tomcat из IDEA запускается Tomcat, путь к которому мы прописали в конфигурации запуска (со своими настройками). 

#### Apply 6_13_tomcat_pool_jndi_cargo.patch
> - для запуска в Tomcat 9 поменял `tomcat7-maven-plugin` на `cargo-maven2-plugin`.
> - в `pom.xml` вместо `context.xml.default` можно делать [индивидуальный контекст приложения](https://stackoverflow.com/a/60797999/548473) 
> - плагин сконфигурирован под postgres. Для HSQLDB нужно скорректировать `driverClassName` + `validationQuery="SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS` в `context.xml` и `dependencies`.

> ![](https://cloud.githubusercontent.com/assets/13649199/13672858/9cd58692-e6e7-11e5-905d-c295d2a456f1.png) Томкат сам управляет пулом коннектов? На каждый запрос в браузере будет даваться свой коннект?
 
Да, в Томкате есть реализация пула коннектов `tomcat-jdbc` (мы его подключаем со `scope=provided`). Если запускаемся с профилем `tomcat`, приложение на каждую транзакцию (или операцию не в транзакции) берет коннект к базе из пула, сконфигурированного в подкладываемом Tomcat `context.xml`.

Плагин запускается после сборки проекта. Запуск из командной строки:

     mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run

Приложение деплоится в application context topjava: [http://localhost:8080/topjava](http://localhost:8080/topjava)

- <a href="https://codehaus-cargo.github.io/cargo/Maven2+plugin.html">Cargo Maven2 plugin</a>
- <a href="http://stackoverflow.com/questions/4305935/is-it-possible-to-supply-tomcat6s-context-xml-file-via-the-maven-cargo-plugin#4417945">Кастомизация context.xml в cargo-maven2-plugin</a>
- <a href="https://tomcat.apache.org/tomcat-8.0-doc/jndi-resources-howto.html"/>Tomcat JNDI Resources</a>
- <a href="https://commons.apache.org/proper/commons-dbcp/configuration.html">BasicDataSource Configuration</a>

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 9. <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFQThUX2VyQXNiTHM">Spring Web MVC</a>
#### Apply 6_14_spring_webmvc.patch
Обработка запросов переезжает в `RootController`, сервлеты уже не нужны
> - Починил [путь к корню](http://stackoverflow.com/questions/10327390/how-should-i-get-root-folder-path-in-jsp-page)
> - В Spring 4.3 ввели новые аннотации `@Get/Post/...Mapping` (сокращенный вариант `@RequestMapping`)

-  <a class="anchor" id="mvc"></a><a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc">Spring Web MVC</a>
- [ContextLoaderListener vs DispatcherServlet](https://howtodoinjava.com/spring-mvc/contextloaderlistener-vs-dispatcherservlet/)
-  <a href="http://design-pattern.ru/patterns/front-controller.html">Паттерн Front Controller</a>
-  <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-servlet-context-hierarchy">Иерархия контекстов в Spring Web MVC</a>
-  <a href="http://www.tutorialspoint.com/spring/spring_web_mvc_framework.htm">Сценарий обработки запроса</a>. <a href="http://www.studytrails.com/frameworks/spring/spring-mvc.jsp">HandlerMappings</a>
-  <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-viewresolver">View Resolution</a>: прячем jsp под WEB-INF.
-  HandlerMapping: <a href="http://www.mkyong.com/spring-mvc/spring-mvc-simpleurlhandlermapping-example/">SimpleUrlHandlerMapping</a>, <a href="http://www.mkyong.com/spring-mvc/spring-mvc-beannameurlhandlermapping-example/">BeanNameUrlHandlerMapping</a>
-  <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-caching-static-resources">Маппинг ресурсов.</a>
-  Ресурсы:
   -  <a href="http://www.mkyong.com/spring-mvc/spring-mvc-hello-world-example/">Spring MVC hello world</a>
   -  <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-servlet-special-bean-types">Special bean types in the WebApplicationContext</a>

> Настройки `Project Structure->Modules->Spring`:

![image](https://cloud.githubusercontent.com/assets/13649199/22221277/52c03cb4-e1c3-11e6-9039-08787e31a505.png)

> ![](https://cloud.githubusercontent.com/assets/13649199/13672858/9cd58692-e6e7-11e5-905d-c295d2a456f1.png) В `web.xml` мы инициализируем `DispatcherServlet`, передавая ему параметром `spring-mvc.xml`. Получается, что `DispatcherServlet` парсит `spring-mvc.xml` и находит в нем context?

Да, можно подебажить родителя (`FrameworkServlet.initWebApplicationContext()`). После инициализации сервлет `DispatcherServlet` раскидывает все запросы по контроллерам (бинам контекста Спринга). См. <a href="http://design-pattern.ru/patterns/front-controller.html">паттерн Front Controller</a>.

### ![video](https://cloud.githubusercontent.com/assets/13649199/13672715/06dbc6ce-e6e7-11e5-81a9-04fbddb9e488.png) 10. <a href="https://drive.google.com/open?id=0B9Ye2auQ_NsFUEctTkRSMWNvRjg">Spring Internationalization</a>
#### Apply 6_15_spring_i18n.patch
**Внимание: проверьте, что переменная окружения `TOPJAVA_ROOT` настроена!**
> - В локализации поменял <a href="https://stackoverflow.com/questions/4441682/what-to-use-for-localization-in-jsps-with-spring">`fmt:message` на `spring:message`</a>
> - Выбор языка зависит от языка операционной системы и заголовка `Accept-Language`. Добавил в `spring-mvc.xml` `messageSource` параметр [`fallbackToSystemLocale`](http://stackoverflow.com/questions/4281504/spring-local-sensitive-data).
Он управляет выбором, куда переключаться при выборе `en` и отсутствии `app_en.properties`: локаль операционной системы или `app.properties` (`fallbackToSystemLocale=false`). Переключение локалей будем реализовывать в конце проекта.  

#### Для тестирования локали [можно поменять `Accept-Language`](https://stackoverflow.com/questions/7769061/how-to-add-custom-accept-languages-to-chrome-for-pseudolocalization-testing). Для Хрома в `chrome://settings/languages` перетащить нужную локаль наверх или поставить [плагин Locale Switcher](https://chrome.google.com/webstore/detail/locale-switcher/kngfjpghaokedippaapkfihdlmmlafcc)

-  <a href="http://learningviacode.blogspot.ru/2012/07/reloadable-messagesources.html">Reloadable MessageSources</a>
-  <a href="http://nginx.com/resources/admin-guide/serving-static-content/">nginx: Serving Static Content</a>

## ![question](https://cloud.githubusercontent.com/assets/13649199/13672858/9cd58692-e6e7-11e5-905d-c295d2a456f1.png) Ваши вопросы
>  Кэш hibernate надстраивается над ehcache или живет самостоятельно?

- <a href="http://mrbool.com/understanding-hibernate-caching/28721">Understanding Hibernate Caching</a>:
Hibernate supports following open-source cache implementations out-of-the-box: EHCache (Easy Hibernate Cache), OSCache (Open Symphony Cache), Swarm Cache, and JBoss Tree Cache.

> Где конфигурируется интернализация для jstl (т.е. файл, где задаются app, app_ru.properties)? Достаточно указать в страницах bundle и путь в ресурсы?

`<fmt:setBundle basename="messages.app"/>` означает, что ресурсы будут искаться в `classpath:messages/app(_xx)/properties`:
<a href="http://docs.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/fmt/setBundle.html">Tag setBundle</a>: fully-qualified resource name, which has the same form as a fully-qualified class name.
После сборки проекта maven их можно найти в `target/classes` или `target/topjava/WEB-INF/classes`.

> Отлично, что она все пишет на том языке, который пришел в заголовке запроса. А если я хочу выбрать?

Выбор языка зависит от языка операционной системы и заголовка `Accept-Language`. Параметр `fallbackToSystemLocale`, который управляет выбором, когда с `Accept-Language: en,en-US;` не находится локализация `app_en.properties`. Для переключения локали используется <a href="http://www.codejava.net/java-ee/jstl/jstl-format-tag-setlocale">JSTL Format Tag fmt:setLocale</a>. Мы будем реализовывать переключение локалей в Spring i18n в конце проекта.

> Мы создаем бин, где получаем dataSource по имени `<jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/topjava"/>`.
Но там не указан класс, как в других dataSource. Получается, по имени jdbc/topjava нам уже отдается готовый объект dataSource, и мы как бы помещаем его в бин?

Здесь используется namespace `jee:jndi-lookup`, который прячет под собой классы реализации. JNDI объект DataSource конфигурируется в `src/main/resources/tomcat/context.xml`

> В плагине прописан профиль `<spring.profiles.active>tomcat,datajpa</spring.profiles.active>`, а в web.xml `<param-value>postgres,datajpa</param-value>`.
Какой же реально отрабатывает?

См. видео урока "Динамическое изменение профиля при запуске". В плагине мы задаем параметры JVM запуска Tomcat

> Почему мы не используем элемент `<context:annotation-config/>` в `spring-db.xml`?

В проекте у нас сейчас 2 Spring контекста: `spring-mvc.xml (см. web.xml, DispatcherServlet)` и родительский `spring-app.xml + spring-db.xml (web.xml, contextConfigLocation)`.
Грубо: 2 мапы, причем для mvc доступно все, что есть в родителе. Т.е. `spring-db.xml` не является отдельным самостоятельным контекстом, и достаточно того, что `<context:annotation-config/>` у нас есть в `spring-app.xml`.

> A `@NamedQuery` или `@Query` подвержены кэшу запросов? Т.е. если мы поставим _USE_QUERY_CACHE_value_="true", будет ли Hibernate их кэшировать?

Чтобы запрос кэшировался, кроме true в конфигурации [нужно еще явно выставить запросу _setCacheable_](http://vladmihalcea.com/2015/06/08/how-does-hibernate-query-cache-work/).    
По поводу кэширования `@NamedQuery` нашел [`@QueryHint`](https://docs.jboss.org/jbossas/docs/Clustering_Guide/5/html/ch04s02s03.html)

> Почему messages мы кладем в config и используем system environment? Разве так делают в реальном проекте? Не будешь же вписывать на сервере эти переменные каждый раз, если проект куда-то будет переезжать. Можно по-другому, кроме systemEnvironment['TOPJAVA_ROOT'], задать путь от корня проекта?

1. messages нам нужны в runtime (при работе приложения). Проект к собранному и задеплоенному в Tomcat war отношения никакого уже не имеет и на этом сервере он обычно не находится. Если ресурсы нужны только при сборке и тестировании, то путь к корню для одномодульного maven проекта можно задать как `${project.basedir}`, но для многомодульного проекта (а все реальные проекты многомодульные) это путь к корню своего модуля.
2. В "реальном приложении" делается совершенно по-разному:
   - нести с собой в classpath, но ресурсы нельзя будет динамически (без передеплоя) обновлять
   - класть в war (не в classpath) и обновлять в развернутом TOMCAT_HOME/webapps/[appname]/...
   - класть в зафиксированное определенное место (например, в home: `~` или в путь от корня `/app/config`). Можно задавать фиксированный пусть в пропертях профиля maven и фильтровать ресурсы (maven resources), чтобы они попали в проперти проекта.
   - делать через переменную окружения, как у нас
   - задавать в параметрах запуска JVM как системную переменную через -D..
   - располагать в преференсах (для unix это home, для windows - registry): <a href="http://java-course.ru/articles/preferences-api/">использование Preferences API</a>
   - держать настройки в DB

   Часто в одном приложении используют несколько способов для разных видов конфигураций.

> Не происходит ли дублирования при кэшировании пользователей чрез Hibernate и `@Cacheable`?

`@Cacheable` кэширует результат запроса `getAll()`, т.е. список юзеров. Hibernate кэширует юзеров по отдельности, т.е., грубо говоря, делает мапу id->User. Можно назвать это дублированием. Нужно ли будет такое в реальном приложении? Все смотрится из логики запросов и их частоты, вполне вероятно, что нет. Как-то мы писали приложение для Дойчебанка (аналог skype на GWT, т.е. на экране небольшое окошко) - там было 5(!!!) уровней кэширования, первый вообще в базе.

> У меня стоит Томкат 8-й версии, в помнике у нас 9-й прописан, но всё работает. Почему?

В `pom.xml` мы подключаем `tomcat-servlet-api` со `scope=provided`, что означает, что он используется только для компиляции и не идет в war. Т.к. мы не используем никаких фич Tomcat 9.x, то наш код совместим с Tomcat 8.x. При запуске через `cargo-maven2-plugin` Tomcat 9 загружается из maven-репозитория.

> Откуда `@Transactional` вытягивает класс для работы с транзакцией, в составе какого бина он идет?

1. Если в контексте Spring есть `<tx:annotation-driven/>`, то подключается `BeanPostProcessors`, который проксирует классы (и методы), помеченные `@Transactional`.
2. По умолчанию для TransactionManager используется бин с `id=transactionManager`

---------------------------

## ![hw](https://cloud.githubusercontent.com/assets/13649199/13672719/09593080-e6e7-11e5-81d1-5cb629c438ca.png) Домашнее задание HW06
- 1.1 Починить тесты `InMemoryAdminRestControllerSpringTest/InMemoryAdminRestControllerTest`  (добавлять `spring-mvc.xml` в контекст не стоит, т.к. в новой версии Spring для этого требуется `WebApplicationContext`. Можно просто поправить `inmemory.xml`)
- 1.2 Починить тесты Jdbc (исключить валидацию в тестах Jdbc)
  - <a href="http://iliachemodanov.ru/ru/blog-ru/12-tools/57-junit-ignore-test-by-condition-ru">org.junit.Assume</a>
  - <a href="http://www.ekiras.com/2015/09/spring-how-to-get-current-profiles-in-spring-application.html">How to get Current Profiles in Spring Application</a>
- 1.3 Перенести функциональность `MealServlet` в `JspMealController` контроллер (по аналогии с `RootController`).
`MealRestController` у нас останется, с ним будем работать позже. 
  - 1.3.1 разнести запросы на update/delete/.. по разным методам (попробуйте вообще без `action=`). Можно по аналогии с `RootController#setUser` принимать `HttpServletRequest request` (аннотации на параметры и адаптеры для `LocalDate/Time` мы введем позже). 
  - 1.3.2 в одном контроллере нельзя использовать другой. Чтобы не дублировать код, можно сделать наследование контроллеров от абстрактного класса.
  - 1.3.3 добавить локализацию и `jsp:include` в `mealForm.jsp / meals.jsp`

### Optional
- 2.1 Добавить транзакционность (`DataSourceTransactionManager`) в Jdbc-реализации  
- 2.2 Добавить еще одну роль к юзеру Admin (будет 2 роли: `USER, ADMIN`).
- 2.3 В `JdbcUserRepository` добавить реализацию ролей юзера (добавлять можно одним запросом с JOIN и `RowMapper`, либо двумя запросами (отдельно `users` и отдельно `roles`). [Объяснение SQL JOIN](http://www.skillz.ru/dev/php/article-Obyasnenie_SQL_obedinenii_JOIN_INNER_OUTER.html)
  - 2.3.1 В реализации `getAll` НЕ делать запрос ролей для каждого юзера (N+1 select)
  - 2.3.2 При save посмотрите на <a href="https://www.mkyong.com/spring/spring-jdbctemplate-batchupdate-example/">batchUpdate()</a>
- 2.4 Добавить проверку ролей в `UserTestData.USER_MATCHER.assertMatch` и починить ВСЕ тесты (тесты должны проходить для юзера с несколькими ролями)  
- 2.5 Добавить валидацию для `Jdbc..Repository` через Bean Validation API (для JPA это делается автоматически при сохранении в базу, для Jdbc мы должны это делать вручную).  Оптимизировать код.
   - Валидацию `@NotNull` для `Meal.user` пока можно закомментировать. На 10-м уроке решим проблему через [Jackson JSON Views](http://www.baeldung.com/jackson-json-view-annotation)
   - [Валидация данных при помощи Bean Validation API](https://alexkosarev.name/2018/07/30/bean-validation-api/) 

### Optional 2 (повышенной сложности)
- 3 Отключить кэш в тестах через `NoOpCacheManager` и для кэша Hibernate 2-го уровня `hibernate.cache.use_second_level_cache=false`. 
  - [JPA 2.0 disable session cache for unit tests](https://stackoverflow.com/a/58963737/548473)
  - [Example of PropertyOverrideConfigurer](https://www.concretepage.com/spring/example_propertyoverrideconfigurer_spring)
  - [Spring util schema](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#xsd-schemas-util)      

---------------------
## ![error](https://cloud.githubusercontent.com/assets/13649199/13672935/ef09ec1e-e6e7-11e5-9f79-d1641c05cbe6.png) Подсказки по HW06
- 1: Неверная кодировка UTF-8 с Spring обычно решается фильтром `CharacterEncodingFilter`:
```
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```
- 2: **Если не поднимается контекст Spring, смотрим причину вверху самого нижнего исключения.** Все ошибки на отсутствия бина в контексте или его нескольких реализациях относятся к пониманию основ: Spring application context. Если нет понимания этих основ, двигаться дальше нельзя, нужно вернуться к видео Спринг, где объясняется, что это такое. Также пересмотрите видео [Тестирование UserService через AssertJ](https://drive.google.com/file/d/1SPMkWMYPvpk9i0TA7ioa-9Sn1EGBtClD). Начиная с 11.30 как раз разбираются подобные ошибки.
- 3: Если неправильно формируется url относительно контекста приложения (например, `/topjava/meals/meals`), посмотрите на
  -  <a href="http://stackoverflow.com/questions/4764405/how-to-use-relative-paths-without-including-the-context-root-name">Relative paths in JSP</a>
  -  <a href="http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html#mvc-redirecting-redirect-prefix">Spring redirect: prefix</a>
- 4: При проблемах с запуском Томкат проверьте запущенные процессы `java`, нет ли в `TOMCAT_HOME\webapps` приложения каталога `topjava`, логи tomcat (нет ли проблем с доступом к каталогам или контекстом Spring)
- 5: Если создаете неизменяемые List или Map, пользуйтесь `List.of()/ Map.of()`
- 6: В MealController общую часть `@RequestMapping(value = "/meals")` лучше вынести на уровень класса
- 7: Проверьте `@Transactional(readOnly = true)` сверху `Jdbc..Repository`
- 8: Проверьте, что `config\messages\app_ru.properties` у вас в кодировке UTF-8 (в любом редакторе/вьюере или при отключенном [Transparent native-to-ascii conversion](https://github.com/JavaOPs/topjava/wiki/IDEA#%D0%9F%D0%BE%D1%81%D1%82%D0%B0%D0%B2%D0%B8%D1%82%D1%8C-%D0%BA%D0%BE%D0%B4%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D1%83-utf-8) в IDEA).
- 9: Учтите, что роли у юзеров можно менять/добавлять/удалять
- 10: Убедитесь, что все методы UserService корректно работают с юзерами, у которых несколько ролей (**запусти наши тесты для Admin с 2-мя ролями**)
