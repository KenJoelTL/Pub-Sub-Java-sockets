import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;
import interfaces.IPublication;
import main.App;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestBroker {

  private static String topicName = "WEATHER";
  private List<Topic> topicList = new ArrayList<>();
  private BrokerThread brokerThread;
  private Advertisement advertisement;
  private App app;
  private Subscriber subscriber;
  private Subscription subscription;

  @BeforeAll
  public void init() {
    app = new App();
    Publisher p = new Publisher(1);
    TopicRepository topicRepository = new TopicRepository(topicList);
    brokerThread = new BrokerThread(null, topicRepository, app);
    advertisement = new Advertisement(p, IPublication.Format.valueOf("XML"));
    subscriber = new Subscriber(1);
    subscription = new Subscription(subscriber, IPublication.Format.valueOf("XML"));
  }

  @BeforeEach
  public void initTopic() {
    topicList.clear();
  }

  @Test
  public void testAdvertise() {
    brokerThread.onAdvertise(topicName, advertisement);
    assertEquals(1, topicList.get(0).getPub().size());
  }

  @Test
  public void testUnAdvertise() {
    brokerThread.onAdvertise(topicName, advertisement);
    brokerThread.onUnadvertise(topicName, advertisement);
    assertEquals(0, topicList.size());
  }

  @Test
  public void testSubscribe() {
    brokerThread.onAdvertise(topicName, advertisement);
    brokerThread.onSubscribe(topicName, subscription);
    assertEquals(1, topicList.get(0).getSub().size());
  }

  @Test
  public void testUnSubscribe() {
    brokerThread.onAdvertise(topicName, advertisement);
    brokerThread.onSubscribe(topicName, subscription);
    brokerThread.onUnsubscribe(topicName, subscription);
    assertEquals(1, topicList.size());
    assertEquals(0, topicList.get(0).getSub().size());
  }

  @Test
  public void testFromCanonicalToXML(){
    Publication publication = new Publication(new Topic(topicName), "JSON", " { hello: world}");
    String xmlString = publication.fromCanonicalToXML();
    assertEquals("<hello>world</hello>", xmlString);
  }

  @Test
  public void testFromCanonicalToJSON(){
    Publication publication = new Publication(new Topic(topicName), "XML", "<note>\n" +
            "<to>Tove</to>\n" +
            "<from>Jani</from>\n" +
            "<heading>Reminder</heading>\n" +
            "<body>Don't forget me this weekend!</body>\n" +
            "</note>");

    String jsonString = publication.fromCanonicalToJSON();
    assertEquals("{\"note\": {\n" +
                         "    \"heading\": \"Reminder\",\n" +
                         "    \"from\": \"Jani\",\n" +
                         "    \"to\": \"Tove\",\n" +
                         "    \"body\": \"Don't forget me this weekend!\"\n" +
                         "}}", jsonString);
  }

}
