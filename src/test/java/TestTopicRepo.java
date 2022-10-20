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

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestTopicRepo {

    private static String topicName = "WEATHER";
    private List<Topic> topicList = new ArrayList<>();

    private TopicRepository topicRepository;

    @BeforeAll
    public void init() {
        topicRepository = new TopicRepository(topicList);
    }

    @BeforeEach
    public void initTopic() {
        topicList.clear();

        topicList.add(new Topic("weather/*/temperature"));
        topicList.add(new Topic("weather/montreal/humidity"));
        topicList.add(new Topic("weather/#"));
        topicList.add(new Topic("humidity/montreal"));
    }

    @Test
    public void testFindByName() {
        // should find topic with same name
        List<Topic> topicList1 = topicRepository.findByName("humidity/montreal");
        assertEquals(1, topicList1.size());

        // should be equivalent Topic object
        Topic newTopic = new Topic("humidity/montreal");
        assertTrue(newTopic.equals(topicList1.get(0)));


        // should be Empty when not found
        List<Topic> topicList2 = topicRepository.findByName("animation/fr");
        assertTrue(topicList2.isEmpty());
    }

    @Test
    public void testFindByCorrespondingName() {
        // should find topics with same name
        List<Topic> topicList1 = topicRepository.findByCorrespondingName("humidity/montreal");
        assertEquals(1, topicList1.size());

        // should find topics following name pattern
        List<Topic> topicList2 = topicRepository.findByCorrespondingName("weather/montreal/temperature");
        assertEquals(2, topicList2.size());

        // should be equivalent Topic object
        assertTrue("weather/*/temperature".equals(topicList2.get(0).getName()));
        assertTrue("weather/#".equals(topicList2.get(1).getName()));
    }

    @Test
    public void testAddTopic() {
        // should add topic to the list
        int originalSize =  topicList.size();
        Topic newTopic = new Topic("New Topic Test");
        assertTrue(topicRepository.add(newTopic));
        assertEquals(originalSize+1, topicList.size());

        // should be equivalent Topic object
        List<Topic> topicList = topicRepository.findByName("New Topic Test");
        Topic topicRetrieved = topicList.get(0);
        assertTrue(newTopic == topicRetrieved);
    }

    @Test
    public void testRemoveTopic() {
        // should not remove when topic is not in the list
        int originalSize =  topicList.size();
        Topic topicToRemove = new Topic("New Topic Test");
        assertFalse(topicRepository.remove(topicToRemove));
        assertEquals(originalSize, topicList.size());

        // should remove only when topic is in the list
        topicToRemove = new Topic("humidity/montreal");
        assertTrue(topicRepository.remove(topicToRemove));
        assertEquals(originalSize-1, topicList.size());
    }




}
