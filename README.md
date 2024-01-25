<h1 align="center">Clevertec course<h1>
<h2 align="center">Hibernate</h2>
<h3>Описание задачи</h3>

- Создать Web приложение учёта домов и жильцов
- Оформить MR (PR), заполнить и отправить форму

 <h4>Описание:</h4>

- 2 сущности: House, Person
- Система должна предоставлять REST API для выполнения следующих операций:
- CRUD для House
- В GET запросах не выводить информацию о Person
- CRUD для Person
- В GET запросах не выводить информацию о House
- Для GET операций использовать pagination (default size: 15)

<h4>House:</h4>

- У House обязаны быть поля id, uuid, area, country, city, street, number, create_date
- House может иметь множество жильцов (0-n)
- У House может быть множество владельцев (0-n)
- create_date устанавливается один раз при создании

<h4>Person:</h4>

- У Person обязаны быть id, uuid, name, surname, gender, passport_series, passport_number, create_date, update_date
- Person обязан жить только в одном доме и не может быть бездомным
- Person не обязан владеть хоть одним домом и может владеть множеством домов
- Сочетание passport_series и passport_number уникально
- gender должен быть [Male, Female]
- Все связи обеспечить через id
- Не возвращать id пользователям сервисов, для этого предназначено поле uuid
- create_date устанавливается один раз при создании
- update_date устанавливается при создании и изменяется каждый раз, когда меняется информация о Person. При этом, если запрос не изменяет информации, поле не должно обновиться

<h4>Примечание:</h4>

-Ограничения и нормализацию сделать на своё усмотрение
-Поля представлены для хранения в базе данных. В коде могут отличаться

<h4>Обязательно:</h4>

- GET для всех Person проживающих в House
- GET для всех House, владельцем которых является Person
- Конфигурационный файл: application.yml
- Скрипты для создания таблиц должны лежать в classpath:db/
- create_date, update_date - возвращать в формате ISO-8601 (https://en.wikipedia.org/wiki/ISO_8601). Пример: 2018-08-29T06:12:15.156.
- Добавить 5 домов и 10 жильцов. Один дом без жильцов и как минимум в 1 доме больше 1 владельца
- Использовать репозиторий с JDBC Template для одного метода.

<h4>Дополнительно:</h4>

- *Добавить миграцию
- *Полнотекстовый поиск (любое текстовое поле) для House
- *Полнотекстовый поиск (любое текстовое поле) для Person
- **PATCH для Person и House

<h4>Самостоятельно:</h4>

-  Изучить отношения: OneToOne, OneToMany, ManyToOne, ManyToMany;
- !!! Изучить Hibernate Persistence Context;
- !!! Изучить кеширование в hibernate;
- Application requirements
- JDK version: 17 – use Streams, java.time.*, etc. where it is possible.
- Application packages root: ru.clevertec.house.
- Any widely-used connection pool could be used.
- Spring JDBC Template should be used for data access.
- Use transactions where it’s necessary.
- Java Code Convention is mandatory (exception: margin size – 120 chars).
- Build tool: Gradle, latest version.
- Web server: Apache Tomcat.
- Application container: Spring IoC. Spring Framework, the latest version.
- Database: PostgreSQL, latest version.
- Testing: JUnit 5.+, Mockito.
- Service layer should be covered with unit tests not less than 80%.
- Repository layer should be tested using integration tests with an in-memory embedded database or testcontainers.
- As a mapper use Mapstruct.
- Use lombok.
- General requirements
- Code should be clean and should not contain any “developer-purpose” constructions.
- App should be designed and written with respect to OOD and SOLID principles.
- Code should contain valuable comments where appropriate.
- Public APIs should be documented (Javadoc).
- Clear layered structure should be used with responsibilities of each application layer defined.
- JSON should be used as a format of client-server communication messages.
- Convenient error/exception handling mechanism should be implemented: all errors should be meaningful on backend side. Example: handle 404 error:
- HTTP Status: 404
- response body    
- {
“errorMessage”: “Requested resource not found (uuid = f4fe3df1-22cd-49ce-a54d-86f55a7f372e)”,
“errorCode”: 40401
}

- where *errorCode” is your custom code (it can be based on http status and requested resource - person or house)
- Abstraction should be used everywhere to avoid code duplication.
- Several configurations should be implemented (at least two - dev and prod).

<h4>Application restrictions</h4>

- It is forbidden to use:
- Spring Boot.
- Spring Data Repositories.
- Spring Data JPA.

<h3>Использование</h3>

1. Для запуска соберите проект через терминал коммандой ./gradlew assemble
2. Подкиньте собранный war-файл в tomcat build/libs/servlet-1.0-SNAPSHOT.war (уверено запускается на версии 10.1.7)
3. Запустите контейнер tomcat
4. Для удобной проверки работоспособности сервлетов импортируйте коллекцию запросов в postman из пакета postman/Clevertec Servlet.postman_collection.json.

<h3>Контрольные точки</h3>

- Развертывание базы данных происходит при установке ключа enabled: true в секции Flyway: файла конфигурации application.yaml (src/main/resources/application.yaml) при подъеме приложения

<h3> Task Spring </h3>
<h4>Берём за основу существующее приложение и переезжаем на Spring boot 3.2.* в ветке feature/boot</h4>

Добавляем сущность HouseHistory (id, house_id, person_id, date, type)
1.	type [OWNER, TENANT]
      a.	Создать свой тип данных в БД
      b.	Хранить как enum в коде
2.	При смене места жительства добавляем запись в HouseHistory [type = TENANT], с текущей датой
3.	При смене владельца, добавляем запись в HouseHistory [type = OWNER], с текущей датой
4.	* Реализовать через триггер в БД
5.	* Если используется миграция, дописать новый changeset, а не исправлять существующие.

Добавляем методы:
1.	GET для получения всех Person когда-либо проживавших в доме
2.	GET для получения всех Person когда-либо владевших домом
3.	GET для получения всех House где проживал Person
4.	GET для получения всех House которыми когда-либо владел Person

Добавляем кэш из задания по рефлексии на сервисный слой House и Person.
1.	Добавляем Integration тесты, чтобы кэш работал в многопоточной среде.
2.	Делаем экзекутор на 6 потоков и параллельно вызываем сервисный слой (GET\POST\PUT\DELETE) и проверяем, что результат соответствует ожиданиям.
3.	Используем H2 или *testcontainers

* Добавляем swagger (OPEN API)
  ** Добавляем starter:
1.	**Реализовываем мультипроект
2.	**Реализовываем свой cache-starter (из задания по рефлексии)
3.	**Добавляем таску с build в mavenLocal
4.	**Добавляем стартер в основное приложение, через mavelLocal
5.	**Удаляем все классы из основного приложения

Следуем Application requirements из задания по Hibernate

<h3>Использование</h3>

1. Swagger документация доступна по http://localhost:8080/documentation
2. Обновление таблицы HouseHistory реализовано с помощью триггеров БД
