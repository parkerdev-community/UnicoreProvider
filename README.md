<img src="https://github.com/UnicoreProject/UnicoreProvider/blob/main/unicoreprovider.png?raw=true" />

# UnicoreProvider ![Kotlin](https://img.shields.io/badge/-Kotlin-05122A?style=flat&logo=Kotlin&logoColor=FFA518)&nbsp;
[![Build Status](https://github.com/UnicoreProject/UnicoreProvider/actions/workflows/gradle.yml/badge.svg)](https://github.com/UnicoreProject/UnicoreProvider/actions)

> Модуль для интеграции серсисов авторизации [UnicoreCMS](https://unicore.uno) и [GravitLauncher](https://github.com/GravitLauncher/Launcher)

## Установка и настройка
1. Создайте API-ключ с правом `unicore.kernel.provider`.
2. Поместите Jar-файл в **modules/**.
3. Произведите настройку **LaunchServer.json**.

```json
// ...
"core": {
  "type": "unicore",
  "apiUrl": "Адрес UnicoreCMS-сервера",
  "apiKey": "API-ключ"
},
// ...
```

## Сборка
UnicoreProvider использует Gradle для обработки зависимостей и сборки.

#### Зависимости
* Java 17 JDK или более поздней версии
* Git

#### Компиляция
```sh
git clone https://github.com/UnicoreProject/UnicoreProvider.git
cd UnicoreProvider/
./gradlew build
```

Собранный Jar-файл будет лежать в папке build/libs
