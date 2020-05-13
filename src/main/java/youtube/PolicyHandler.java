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
            if(!channelSystemRepository.existsById(uploadedVideo.getChannelId())) { // 동영상에서 등록하려는 채널이 존재하지 않을 경우, 채널을 신규 생성하여 동영상 등록
                ChannelSystem ch = new ChannelSystem();
                ch.setChannelId(uploadedVideo.getChannelId());
                ch.setClientId(uploadedVideo.getClientId());
                ch.setVideoId(uploadedVideo.getVideoId());
                ch.setTotalView(uploadedVideo.getViewCount());
                channelSystemRepository.save(ch);
                System.out.println("#####[NEW] listener EditedChannel : " + uploadedVideo.toJson());
            }

            channelSystemRepository.findById(uploadedVideo.getChannelId()).ifPresent(
                    channelSystem -> { // 동영상을 등록하려는 채널이 존재할 경우
                        channelSystem.setChannelId(uploadedVideo.getChannelId());
                        channelSystem.setClientId(uploadedVideo.getClientId());
                        channelSystem.setVideoId(uploadedVideo.getVideoId());
                        channelSystem.addTotalView(uploadedVideo.getViewCount()); // 같은 채널에 등록된 동영상 조회수 합산
                        channelSystemRepository.save(channelSystem);
                        System.out.println("#####listener EditedChannel : Add uploadVideo for channelID: " + uploadedVideo.getChannelId());
                    }
                );


            System.out.println("======================");
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEditedVideo_EditedChannel(@Payload EditedVideo editedVideo){
        if(editedVideo.isMe()){
            System.out.println("##### listener EditedChannel : " + editedVideo.toJson());
            channelSystemRepository.findById(editedVideo.getChannelId()).ifPresent(
                    channelSystem -> {
                        channelSystem.setChannelId(editedVideo.getChannelId());
                        channelSystem.setClientId(editedVideo.getClientId());
                        channelSystem.setVideoId(editedVideo.getVideoId());
                        channelSystem.addTotalView(editedVideo.getViewCount()); // 같은 채널에 등록된 동영상 조회수 합산
                        channelSystemRepository.save(channelSystem);
                        System.out.println("#####listener EditedChannel : Add uploadVideo for channelID: " + editedVideo.getChannelId());
                    }
            );

            System.out.println("======================");
        }
    }
}
