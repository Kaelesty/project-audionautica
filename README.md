# Audionautica

## Описание

![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/5fcb4f2f-c923-42a0-a8fd-88941051715c)

![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/949d94d6-03d2-4b0b-b364-ef79b465c00d)

![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/ae067fd5-7d84-42b0-9f78-03a2fcb79864)

## Сборка и запуск

0. Скачать код проекта из Github

## Запуск веб-приложения

1. Установить Python 3.11
2. Установить Django 4.2.5 с помощью pip/poetry
3. В директории проекта выполнить "cd backend\audionautica\audionautica"
4. В файле "audionautica\settings.py" добавить адрес, по которому будет выполняться подключение к веб-приложению
![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/60f1ffc2-4364-4b93-bc99-1b045bdeaa77)
6. Выполнить "python manage.py runserver"

### Сборка Android-приложения

1. В директории проекта открыть папку "android" как проект Android Studio
2. В файле "app\src\main\java\com\kaelesty\audionautica\data\remote\api\ApiServiceFactory.kt" в константу SERVER_URL внести адрес, по которому доступно веб-приложение (этот шаг можно пропустить, но тогда приложение будет доступно только в оффлайн режиме)
3. Выполнить сборку проекта с помощью Gradle
![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/a55bac97-a971-4d4e-97ac-b101d4ec1d65)
4. Выполнить сборку APK-файла
![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/70807fa9-b0cf-4d53-b41b-58f25fc7be41)

ИЛИ

Запустить debug-сборку на устройстве в режиме отладке или эмуляторе
![изображение](https://github.com/Kaelesty/project-fullstack-audionautica/assets/74826130/3bbe5b5e-5f3b-4ed9-acd7-04d74b559cf4)




