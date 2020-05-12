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
    public void wheneverUploadedVideo_EditedChannel(@Payload UploadedVideo uploadedVideo){
        if(uploadedVideo.isMe()){
            System.out.println("##### listener EditedChannel : " + uploadedVideo.toJson());
            channelSystemRepository.findById(uploadedVideo.getChannelId()).ifPresent(
                    channelSystem -> {
                        //channelSystem.addTotalView(uploadedVideo.addTotalView()); // 조회수 추가
                        channelSystem.addTotalView(uploadedVideo.getViewCount()); // 조회수 세팅
                        channelSystemRepository.save(channelSystem);
                    }
                );

            if(!channelSystemRepository.existsById(uploadedVideo.getChannelId())) {
                ChannelSystem ch = new ChannelSystem();
                ch.setChannelId(uploadedVideo.getChannelId());
                ch.setClientId(uploadedVideo.getClientId());
                ch.setTotalView(uploadedVideo.getViewCount());
                channelSystemRepository.save(ch);
                System.out.println("#####[NEW] listener EditedChannel : " + uploadedVideo.toJson());
            }
            System.out.println("======================");
        }
    }
}
