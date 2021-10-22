package dwa.model;

import java.util.Calendar;

public class DWABlacklistParameter {
    public String host;
    public long lastBlocked;
    public int count;
    public DWABlacklistParameter(String host){
        this.host = host;
        this.resetCounter();
    }

    public void resetCounter() {
        this.lastBlocked = Calendar.getInstance().getTimeInMillis();
        this.count = 0;
    }
    public void increaseCounter(){
        this.lastBlocked = Calendar.getInstance().getTimeInMillis();
        this.count++;
    }
}
