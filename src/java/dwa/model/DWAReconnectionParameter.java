package dwa.model;

import java.util.Calendar;

import dwa.Dwa;

public class DWAReconnectionParameter {
    public String host;
    public long lastReconnect;
    public int count;
    private static final double backOff = 0.8f;
    public int delay = 0;
    public int delayMax = 0;
    public int scheduleToConnect = 0;
    public DWAReconnectionParameter(String host,int reconnectionDelay,int reconnectionDelayMax){
        this.host = host;
        this.delay = reconnectionDelay;
        this.delayMax = reconnectionDelayMax;
        this.scheduleToConnect = 0;
        this.resetCounter();
    }

    public long getTimer(){
        scheduleToConnect = this.count;
        long timer = Math.round(this.delay * Math.pow(( 1 + backOff) , count));
        if(timer>this.delayMax) return this.delayMax;
        else {
            if(timer<=10000) return 10000;
            else return timer;
        }
    }

    public void resetCounter() {
        this.lastReconnect = Calendar.getInstance().getTimeInMillis();
        this.count = 0;
    }
    public void increaseCounter(){
        this.lastReconnect = Calendar.getInstance().getTimeInMillis();
        this.count++;
    }
}
