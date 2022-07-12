## ArtPlanSoftwareApps

Разминочное задание, а также набор сервисов для SOA WEB приложения

## Настройка

Запустить разминочное задание можно при помощи - main.jar

Запустить сервис - demo-0.0.1-SNAPSHOT.jar

В spring-boot application использовалась субд Postgres, ее настройку можно найти в application.properties

Приложение доступно по http://localhost:9090

Главные эндпоинты:

http://localhost:9090/signup - регистрация

http://localhost:9090/login - авторизация

http://localhost:9090/createType - создание типа

http://localhost:9090/createPet/1 - создание животного

http://localhost:9090/getPet/1 - получить животное

http://localhost:9090/updatePet - обновить животное

http://localhost:9090/delete/1 - удалить животное

http://localhost:9090/getAllMyPets - получить список своих животных

## Важные замечания

При авторизации будет выслан bearer token в header, его необходимо скопировать и вставить в header в остальные запросы. Иначе доступ будет закрыт.

![image](https://user-images.githubusercontent.com/66429474/178431990-885a2443-26a2-4c6b-ade1-aceab70a6d72.png)

Также при создании своего животного и получении своих животных не нужно указывать свой id или username. Сервис получит из успешной авторизации id текущего юзера. Вот пример создания животного(в pathvariable указан id типа животного, котороый нужно создать заранее): 

![image](https://user-images.githubusercontent.com/66429474/178432798-d9ffd292-ec75-4b7f-b714-67ac4b56cbf9.png)

Все ошибки ловятся и отправляются в виде json

![image](https://user-images.githubusercontent.com/66429474/178433350-b1a9d45f-bedf-4c2b-bfa5-dece6cf2bbde.png)

Стоит также учесть, что если произойдет 10 неверных попыток авторизации, то аккаунт заблокируется на час и независимо оттого, будет ли в дальнейшем успешная авторизация, он разблокируется лишь по истечению часа.
