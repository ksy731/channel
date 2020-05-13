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
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long channelId;
    private String channelName;
    private Long clientId;
    private Long videoId;
    private int totalView = 0; // 조회수

    @PrePersist
    public void onPrePersist(){
        CreatedChannel createdChannel = new CreatedChannel();
        BeanUtils.copyProperties(this, createdChannel);
        createdChannel.publishAfterCommit();

    }

    @PreUpdate
    public void onPostEdited(){
        EditedChannel editedChannel = new EditedChannel();
        BeanUtils.copyProperties(this, editedChannel);
        editedChannel.publishAfterCommit();
    }


    @PreRemove
    public void onPostRemove(){
        DeletedChannel deletedChannel = new DeletedChannel();
        BeanUtils.copyProperties(this, deletedChannel);
        deletedChannel.publishAfterCommit();
    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public void addTotalView(int totalView) {
        this.totalView += totalView;
    }
}
