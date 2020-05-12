package youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import youtube.config.kafka.KafkaProcessor;


@Service
public class PolicyHandler{

    @Autowired
    private ChannelSystemRepository channelSystemRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onEventByString(@Payload String event){
        System.out.println("====== start event message ================");
        System.out.println(event);
        System.out.println("====== end event message ================");
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverUploadedVideo_EditedChannel(@Payload UploadedVideo uploadedVideo){

        if(uploadedVideo.isMe()){
            System.out.println("##### listener EditedChannel : " + uploadedVideo.toJson());
//            ChannelSystem ch = channelSystemRepository.findById(uploadedVideo.getChannelId());

//            p.setName(uploadedVideo.get());
//            p.setStock(orderPlaced.getQty());
//            productRepository.save(p);
            System.out.println("======================");
        }
    }
}
