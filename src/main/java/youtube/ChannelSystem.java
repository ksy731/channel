package youtube;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import java.util.List;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

@Entity
@Table(name="ChannelSystem_table")
public class ChannelSystem {

    private String channelName;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String channelId;
    private String clientId;
    private String videoId;

    @PrePersist
    public void onPrePersist(){
        CreatedChannel createdChannel = new CreatedChannel();
        BeanUtils.copyProperties(this, createdChannel);
        createdChannel.publishAfterCommit();


        EditedChannel editedChannel = new EditedChannel();
        BeanUtils.copyProperties(this, editedChannel);
        editedChannel.publishAfterCommit();


        DeletedChannel deletedChannel = new DeletedChannel();
        BeanUtils.copyProperties(this, deletedChannel);
        deletedChannel.publishAfterCommit();


    }

    @PostPersist
    public void eventPublish(){
        CreatedChannel createdChannel = new CreatedChannel();
        createdChannel.setChannelId(this.getClientId());
        createdChannel.setChannelName(this.getChannelName());
        createdChannel.setClientId(this.getClientId());
        createdChannel.setVideoId(this.getVideoId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(createdChannel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor processor = Application.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }




}
