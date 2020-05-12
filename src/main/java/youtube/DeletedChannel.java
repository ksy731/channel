package youtube;


import org.springframework.beans.factory.annotation.Autowired;

public class DeletedChannel extends AbstractEvent {

    @Autowired
    private ChannelSystemRepository channelSystemRepository;

    private String channelName;
    private String channelId;
    private String clientId;
    private String videoId;

    public DeletedChannel(){
        super();
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
