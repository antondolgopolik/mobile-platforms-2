package by.bsuir.mobileplatforms2.entity;

import java.sql.Timestamp;

public class CachedImage {
    private int id;
    private String uid;
    private Timestamp expires;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getExpires() {
        return expires;
    }

    public void setExpires(Timestamp expires) {
        this.expires = expires;
    }
}
