package clients.clients_service.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    public NewTopic createTopic(){
        return new NewTopic("clients_topic", 5, (short) 1);
    }

    public NewTopic createTopic2(){
        return new NewTopic("employeeTopic", 5, (short) 1);
    }

    public NewTopic createTopic3(){
        return new NewTopic("clPost", 5, (short) 1);
    }//cldelete



    public NewTopic createTopic4(){
        return new NewTopic("TOPICj", 5, (short) 1);
    }
    public NewTopic createTopic6(){return new NewTopic("topicupdate", 5, (short) 1);}

}
