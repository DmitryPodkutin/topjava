# Онлайн-проекта <a href="https://github.com/JavaWebinar/topjava">TopJava</a>

### ![correction](https://cloud.githubusercontent.com/assets/13649199/13672935/ef09ec1e-e6e7-11e5-9f79-d1641c05cbe6.png) Правки в проекте

#### Apply [12_0_fix.patch](https://drive.google.com/file/d/1MJeUN22gRwsBLQ298KIw5l4Gn3Mo3H7l)

> - Fix `@OnDelete`. Требуется только для автогенерации (как делаем на Spring Boot), но т.к мы делаем максимально правильно, сюда тоже внес
>   - [@ElementCollection + @OnDelete](https://stackoverflow.com/a/62848296/548473)
>   - [@ManyToOne + @OnDelete](https://stackoverflow.com/a/44988100/548473)
> - Refactor `ValidationUtil.getRootCause`
> - Log before check
> - Do not delegate `AbstractUserController.create(UserTo userTo)`
> - Fix sticky-footer and button margin and when login screen has low height/width
> - Change `AbstractServiceTest.validateRootCause` signature similar to `Assertions.assertThrows`
> - Small fix

## Spring Boot + Миграция
### 1. Пройдите основы Spring Boot: [BootJava](https://javaops.ru/view/bootjava)
### 2. Миграция REST части проекта `Calories Management` на Spring Boot

Вычекайте в отдельную папку (как отдельный проект) ветку `spring-boot`:  
```
git clone --branch spring-boot --single-branch https://github.com/JavaWebinar/topjava.git topjava_boot
```  
Если будете его менять, [настройте `git remote`](https://javaops.ru/view/bootjava/lesson01#project)

- Проект сделал с минимальным количеством кода (как тестовое задание или ТЗ на выпускной проект): убрал слой сервисов, профили, группы, локализацию, весь UI.  
- База создается автогенераций по модели (для тестового задания и базы в памяти - лучший вариант). 
- При запросе сущности с неверным `id` вместо `NotFoundException` возвращаю `notFound()`, см.
реализацию `ResponseEntity.of`
- Сделал `BaseRepository` - сюда можно размещать [общие методы репозиториев](https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa)  
- Вместо своих конверторов использую `@DateTimeFormat`  
- В `AbstractControllerTest` Spring Boot
сам [конфигурирует `MockMvc` через `@AutoConfigureMockMvc`](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment)  
- Мигрировал все тесты контроллеров. `MealRestControllerTest.updateDuplicate` и `createDuplicate` не получилось сделать красиво, пока в *TODO*.
 В тестовом проекте столько тестов **НЕ ТРЕБУЕТСЯ**, достаточно нескольких на основные юзкейсы.  
- **Сделал красивый [REST API documentation](http://localhost:8080/swagger-ui/). Добавьте в выпускной проект** - это будет большим плюсом и избавит от необходимости писать документацию.
Не забудьте ссылку на нее в `readme.md`!  

### 3. Кеширование (коммит `caffine_cache`)
- [Spring Boot With Caffeine Cache](https://www.javadevjournal.com/spring-boot/spring-boot-with-caffeine-cache/)
- [Profile Specific Files - отключение кэша при тестировании](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-files-profile-specific)

Кэширование желательно в выпускном. 7 раз подумайте, что будете кэшировать! (самые частые запросы, которые редко изменяются).