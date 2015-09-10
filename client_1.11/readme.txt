1. Для сборки модуля необходимо, чтобы были установлены:
 - JDK1.7_05;
 - Maven 3.*;
 - CryptoPro JCP 1.0.5*.

2.	Сборка модуля клиента СМЭВ

Сборка библиотек для работы с криптографией:
  a) Перейти в папку crypto 
  b) Выполнить mvn install

Сборка клиента:
  a) Перейти в папку client
  b) Выполнить mvn install


Сборка осуществляется выполнением команды “mvn install” в папке code_new
В результате в папке code_new\modules\smev-client\target\ и локальном maven-репозитории появится файл smev-client-*.jar

3.	Подключение модуля клиента в maven-проект

Для подключения модуля клиента добавляем в pom.xml зависимость
<dependency>
	<groupId>ru.it.smev</groupId>
	<artifactId>smev-client</artifactId>
	<version>${project.version}</version>
</dependency>

4.	Пример использования
Для отправки/получения сообщений необходимо вызывать методы класса MessageExchangeEndpoint, например:
MessageExchangeEndpoint.getInstance().sendRequest(messageID, content, personalSignature, null, attachmentList);

Метод sendRequest() отправляет сообщение в СМЭВ, где
messageID – ID сообщения, 
content – ссылка на тело сообщения
personalSignature – подпись сообщения ЭП-СП
attachmentList – вложения

В папке example можно найти приложение, демонстрирующее вызов данного метода. Весь код находится в файле Example.java. По коду даны поясняющие комментарии.
Перед запуском необходимо подправить константы в начале кода – адрес СМЭВ, имя хранилища, параметры ключей.
Указанные ключи должны быть установлены в CryptoPro JCP.

