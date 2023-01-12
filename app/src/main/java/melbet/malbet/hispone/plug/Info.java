package melbet.malbet.hispone.plug;

import androidx.annotation.Nullable;

public class Info {
    String header;
    String details;

    @Nullable
    String imgSrc;

    public Info(String header, String details) {
        this.header = header;
        this.details = details;
    }

    public Info(String header, String details, @Nullable String imgSrc) {
        this.header = header;
        this.details = details;
        this.imgSrc = imgSrc;
    }

    @Nullable
    public String getImgSrc() {
        return imgSrc;
    }

    public String getHeader() {
        return header;
    }

    public String getDetails() {
        return details;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Info{" +
                "header='" + header + '\'' +
                ", details='" + details + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }
}
