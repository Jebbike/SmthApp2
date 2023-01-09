package melbet.malbet.hispone.plug;

public class Info {
    String header;
    String details;

    public Info(String header, String details) {
        this.header = header;
        this.details = details;
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
}
