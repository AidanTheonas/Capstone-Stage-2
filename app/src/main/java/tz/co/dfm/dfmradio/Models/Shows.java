package tz.co.dfm.dfmradio.Models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Shows implements Parcelable {
  public static final Creator<Shows> CREATOR =
      new Creator<Shows>() {
        @Override
        public Shows createFromParcel(Parcel in) {
          return new Shows(in);
        }

        @Override
        public Shows[] newArray(int size) {
          return new Shows[size];
        }
      };
  private String showName,
      episodeTitle,
      episodeDate,
      episodeDescription,
      episodeThumbnail,
      episodeMediaFile,
      episodeHostName;
  private int episodeId, mediaType;

  public Shows() {}

  public Shows(
      int episodeId,
      String showName,
      String episodeTitle,
      String episodeDate,
      String episodeDescription,
      String episodeThumbnail,
      String episodeMediaFile,
      String episodeHostName,
      int mediaType) {
    this.episodeId = episodeId;
    this.showName = showName;
    this.episodeTitle = episodeTitle;
    this.episodeDate = episodeDate;
    this.episodeDescription = episodeDescription;
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
    episodeThumbnail = in.readString();
    episodeMediaFile = in.readString();
    episodeHostName = in.readString();
    mediaType = in.readInt();
    episodeId = in.readInt();
  }

  public int getEpisodeId() {
    return episodeId;
  }

  public void setEpisodeId(int episodeId) {
    this.episodeId = episodeId;
  }

  public String getEpisodeHostName() {
    return episodeHostName;
  }

  public void setEpisodeHostName(String episodeHostName) {
    this.episodeHostName = episodeHostName;
  }

  public String getEpisodeThumbnail() {
    return episodeThumbnail;
  }

  public void setEpisodeThumbnail(String episodeThumbnail) {
    this.episodeThumbnail = episodeThumbnail;
  }

  public String getEpisodeTitle() {
    return episodeTitle;
  }

  public void setEpisodeTitle(String episodeTitle) {
    this.episodeTitle = episodeTitle;
  }

  public String getEpisodeDate() {
    return episodeDate;
  }

  public void setEpisodeDate(String episodeDate) {
    this.episodeDate = episodeDate;
  }

  public String getEpisodeDescription() {
    return episodeDescription;
  }

  public void setEpisodeDescription(String episodeDescription) {
    this.episodeDescription = episodeDescription;
  }

  public String getEpisodeMediaFile() {
    return episodeMediaFile;
  }

  public void setEpisodeMediaFile(String episodeMediaFile) {
    this.episodeMediaFile = episodeMediaFile;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public int getMediaType() {
    return mediaType;
  }

  public void setMediaType(int mediaType) {
    this.mediaType = mediaType;
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
    dest.writeString(episodeThumbnail);
    dest.writeString(episodeMediaFile);
    dest.writeString(episodeHostName);
    dest.writeInt(mediaType);
    dest.writeInt(episodeId);
  }
}
