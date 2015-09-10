import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import ru.voskhod.smev.message_exchange_service_client.InAttachment;
import ru.voskhod.smev.message_exchange_service_client.MessageExchangeEndpoint;
import ru.voskhod.smev.message_exchange_service_client.PersonalSigner;
import ru.voskhod.smev.message_exchange_service_client.SMEVCertificateStore;
import ru.voskhod.smev.message_exchange_service_client.impl.FileSystemSMEVCertificateStore;
import ru.voskhod.smev.message_exchange_service_client.impl.KeyPersonalSignerImpl;
import ru.voskhod.smev.message_exchange_service_client.intercept.InterceptorStorage;
import ru.voskhod.crypto.DigitalSignatureFactory;
import ru.voskhod.crypto.KeyStoreWrapper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class Example {

    // "JCP" или "DIGT"
    private static final String CRYPTO_PROVIDER = "JCP";
	// адрес СМЭВ
	private static final String SMEV_URL = "http://10.77.102.150:7500/ws?wsdl";
    // адрес FTP
    private static final String FTP_ADDRESS = "10.77.102.144";
	// имя хранилища
	private static final String STORE_NAME = "HDImageStore";
	// имя контейнера ЭП-ОВ
	private static final String CONTAINER_ALIAS = "LOSKUTOV";
	// пароль для контейнера ЭП-ОВ
	private static final String CONTAINER_PASSWORD = "123456";
	// имя контейнера ЭП-СП
	private static final String PERSONAL_CONTAINER_ALIAS = "LOSKUTOV";
	// пароль для контейнера ЭП-СП
	private static final String PERSONAL_CONTAINER_PASSWORD = "123456";

    public static void main(String[] args) throws Exception {

		// подготавливаем временные папки
		initFileStore();

		// инициализация JCP
        DigitalSignatureFactory.init(CRYPTO_PROVIDER);
        KeyStoreWrapper keyStore = DigitalSignatureFactory.getKeyStoreWrapper();

        // подготовка подписи ЭП-СП
        PrivateKey spPrivateKey;
        X509Certificate spCertificate;
        {
            spPrivateKey = keyStore.getPrivateKey(PERSONAL_CONTAINER_ALIAS, PERSONAL_CONTAINER_PASSWORD.toCharArray());
            spCertificate = keyStore.getX509Certificate(PERSONAL_CONTAINER_ALIAS);
        }

        // инициализация точки доступа к веб-сервису и подготовка ЭП-ОВ
        PrivateKey ovPrivateKey;
        X509Certificate ovCertificate;
        {
            ovPrivateKey = keyStore.getPrivateKey(CONTAINER_ALIAS, CONTAINER_PASSWORD.toCharArray());
            ovCertificate = keyStore.getX509Certificate(CONTAINER_ALIAS);

        }
        MessageExchangeEndpoint messageExchange = MessageExchangeEndpoint.create(
            SMEV_URL, FTP_ADDRESS, ovPrivateKey, ovCertificate
        );

		// генерация ID сообщения
		String messageID = messageExchange.generateMessageID();

		// подготовка сообщения
        Element content;
        {
            /*
            String contentString =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns1:EchoPlease xmlns:ns1=\"urn://x-artefacts-smev-gov-ru/services/echo/1.0\">\n" +
                "	<ns1:MyYell>Это - русский текст</ns1:MyYell>\n" +
                "</ns1:EchoPlease>";
            */
            String contentString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><EchoPlease xmlns=\"urn://x-artefacts-smev-gov-ru/services/echo/1.0\"><MyYell>Это - русский текст</MyYell></EchoPlease>";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(contentString)));
            content = document.getDocumentElement();
        }

        // подготовка вложений
		List<InAttachment> attachmentList = new ArrayList<InAttachment>();
		// TODO тут формируем список вложений, если они есть

        // можно включить трассировку запросов и ответов (по умолчанию выключена)
        InterceptorStorage.getRequest().setIntercept(true);
        InterceptorStorage.getResponse().setIntercept(true);

		// отправляем сообщение
		try {
            // используем ЭП-СП:
            PersonalSigner signPersonal = new KeyPersonalSignerImpl(spPrivateKey, spCertificate);
            messageExchange.sendRequest(messageID, content, signPersonal, null, attachmentList);
		} catch (Exception exception) {
			// тут могут быть разные exception в зависимости от ответа сервиса
			exception.printStackTrace(System.err);
		}

		// через InterceptorStorage можно получить конверты запроса и ответа
		System.out.println(InterceptorStorage.getRequest().getString());
		System.out.println(InterceptorStorage.getResponse().getString());
	}

	private static void initFileStore() {
		// Хранилище сертификатов СМЭВ.
		File smevCertificateStoreDir = new File("smev-certificates");
		SMEVCertificateStore smevCertificateStore = new FileSystemSMEVCertificateStore(smevCertificateStoreDir);
		SMEVCertificateStore.setInstance(smevCertificateStore);
	}
}
