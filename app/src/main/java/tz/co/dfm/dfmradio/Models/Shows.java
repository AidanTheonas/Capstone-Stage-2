package tz.co.dfm.dfmradio.Models;

public class Shows {
    private String showName,episodeTitle,episodeDate,episodeDescription,episodeComments,episodeThumbnail,episodeMediaFile;

    public Shows(){}

    public void setEpisodeComments(String episodeComments) {
        this.episodeComments = episodeComments;
    }

    public void setEpisodeDate(String episodeDate) {
        this.episodeDate = episodeDate;
    }

    public void setEpisodeDescription(String episodeDescription) {
        this.episodeDescription = episodeDescription;
    }

    public void setEpisodeMediaFile(String episodeMediaFile) {
        this.episodeMediaFile = episodeMediaFile;
    }

    public void setEpisodeThumbnail(String episodeThumbnail) {
        this.episodeThumbnail = episodeThumbnail;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getEpisodeThumbnail() {
        return episodeThumbnail;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public String getEpisodeComments() {
        return episodeComments;
    }

    public String getEpisodeDate() {
        return episodeDate;
    }

    public String getEpisodeDescription() {
        return episodeDescription;
    }

    public String getEpisodeMediaFile() {
        return episodeMediaFile;
    }

    public String getShowName() {
        return showName;
    }
}
