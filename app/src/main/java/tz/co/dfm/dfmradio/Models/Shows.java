package tz.co.dfm.dfmradio.Models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Shows implements Parcelable {
    private String showName,episodeTitle,episodeDate,episodeDescription,episodeComments,episodeThumbnail,episodeMediaFile,episodeHostName,mediaType;

    public Shows(){}

    public Shows(String showName,String episodeTitle,String episodeDate,String episodeDescription,String episodeComments,String episodeThumbnail,String episodeMediaFile,String episodeHostName,String mediaType){
        this.showName = showName;
        this.episodeTitle = episodeTitle;
        this.episodeDate = episodeDate;
        this.episodeDescription = episodeDescription;
        this.episodeComments = episodeComments;
        this.episodeThumbnail = episodeThumbnail;
        this.episodeMediaFile = episodeMediaFile;
        this.episodeHostName = episodeHostName;
        this.mediaType = mediaType;
    }

    private Shows(Parcel in) {
        showName = in.readString();
        episodeTitle = in.readString();
        episodeDate = in.readString();
        episodeDescription = in.readString();
        episodeComments = in.readString();
        episodeThumbnail = in.readString();
        episodeMediaFile = in.readString();
        episodeHostName = in.readString();
        mediaType = in.readString();
    }

    public static final Creator<Shows> CREATOR = new Creator<Shows>() {
        @Override
        public Shows createFromParcel(Parcel in) {
            return new Shows(in);
        }

        @Override
        public Shows[] newArray(int size) {
            return new Shows[size];
        }
    };

    public void setEpisodeHostName(String episodeHostName) {
        this.episodeHostName = episodeHostName;
    }

    public String getEpisodeHostName() {
        return episodeHostName;
    }

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

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(showName);
        dest.writeString(episodeTitle);
        dest.writeString(episodeDate);
        dest.writeString(episodeDescription);
        dest.writeString(episodeComments);
        dest.writeString(episodeThumbnail);
        dest.writeString(episodeMediaFile);
        dest.writeString(episodeHostName);
        dest.writeString(mediaType);
    }
}
