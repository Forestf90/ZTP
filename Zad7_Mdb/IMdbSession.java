package pl.jrj.mdb;

import javax.ejb.Remote;

@Remote
public interface IMdbSession {
    public String sessionId(String album);
}