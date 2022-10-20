package classes;

import java.util.ArrayList;
import java.util.List;

public class TopicRepository {

    private List<Topic> topics;


    public TopicRepository(List<Topic> topics) {
        this.topics = topics;
    }

    public boolean add(Topic t){
        return this.topics.add(t);
    }

    public boolean remove(Topic t){
        return this.topics.remove(t);
    }

    public List<Topic> findByName(String topicName){
        return this.topics.stream().filter( t-> t.getName().equals(topicName) ).toList();
    }

    public List<Topic> findByCorrespondingName(String topicNameToFind){

        // Stockage comme candidats, les topics qui ont un même terme de départ
        String[] topicToFindSplit = topicNameToFind.split("/",0);
        List<Topic> topicToNotifyList = new ArrayList<>();
        List<Topic> candidateTopicList = this.topics.stream().filter( t-> t.getName().split("/")[0].equals(topicToFindSplit[0]) ).toList();

        // Filtrage de la liste des candidats -> topicToNotify
        while(!candidateTopicList.isEmpty()) {
            Topic currentTopic = candidateTopicList.get(0);
            String[] cTopicNameSplit = currentTopic.getName().split("/",0);
            boolean checkNextTerm = true; // s'il est nécessaire de regarder le prochain terme

            // Correspondance parfaite ou
            if (topicNameToFind.equals(currentTopic.getName()) || currentTopic.getName().equals("#")) {
//                candidateTopicList.remove(currentTopic);
                candidateTopicList = candidateTopicList.stream().filter( t-> !t.equals(currentTopic) ).toList();
                topicToNotifyList.add(currentTopic);
                checkNextTerm = false;
            }
            int indexNextTerm = 1;
            while(checkNextTerm) {
                checkNextTerm = false;
                // Correspondance partielle avec #
                if (cTopicNameSplit.length - 1 >= indexNextTerm && (cTopicNameSplit[indexNextTerm].equals("#"))) {
//                    candidateTopicList.remove(topicNameToFind);
                    candidateTopicList = candidateTopicList.stream().filter( t-> !t.equals(currentTopic) ).toList();
                    topicToNotifyList.add(currentTopic);
                }
                // Correspondance partielle -> on regarde si le prochain caractère est * ou le même
                else if (cTopicNameSplit.length == topicToFindSplit.length
                        && (cTopicNameSplit[indexNextTerm].equals("*")
                        || (cTopicNameSplit[indexNextTerm].equals(topicToFindSplit[indexNextTerm])))
                ) {
                    // si le prochain terme est le dernier
                    if (indexNextTerm == cTopicNameSplit.length-1) {
//                        candidateTopicList.remove(topicNameToFind);
                        candidateTopicList = candidateTopicList.stream().filter( t-> !t.equals(currentTopic) ).toList();
                        topicToNotifyList.add(currentTopic);
                    }
                    // sinon on regarde le caractère suivant
                    else {
                        indexNextTerm++;
                        checkNextTerm = true;
                    }
                }
                // Aucune correspondance
                else {
                    candidateTopicList = candidateTopicList.stream().filter( t-> !t.equals(currentTopic) ).toList();
//                    candidateTopicList.remove(currentTopic);
                }
            }
        }

        return topicToNotifyList;
    }
}
