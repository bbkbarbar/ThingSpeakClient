package hu.barbar;

import com.angryelectron.thingspeak.Channel;
import com.angryelectron.thingspeak.Entry;
import com.angryelectron.thingspeak.Feed;
import com.angryelectron.thingspeak.ThingSpeakException;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.management.ManagementFactory;
import java.util.Map;

import com.sun.management.OperatingSystemMXBean;

public class App {

    public static void main(String[] args) {
        String apiWriteKey = "TODO";
        int channelId = 000000;
        Channel channel = new Channel(channelId, apiWriteKey);

        Entry entry = new Entry();

        boolean wasProblem = false;
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//        System.out.println(osBean.getSystemCpuLoad());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(osBean.getSystemCpuLoad());

        /*
        try {
            Feed f = channel.getChannelFeed();
            Map<Integer, Entry> map = f.getEntryMap();
            for (Map.Entry<Integer, Entry> e : map.entrySet())
                System.out.println("Key = " + e.getKey() +
                        ", Value = " + e.getValue());
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (ThingSpeakException e) {
            e.printStackTrace();
        }
        /**/

        while(true) {

            wasProblem = false;

            Double cpuLoad = osBean.getSystemCpuLoad();
            Double freeMemory = (double)osBean.getFreePhysicalMemorySize() / osBean.getTotalPhysicalMemorySize();
            entry.setField(1, String.valueOf(cpuLoad));
            entry.setField(2, String.valueOf(freeMemory));
            try {
                channel.update(entry);
            } catch (UnirestException | ThingSpeakException e) {
                //System.out.println("Channel update exception");
                //System.out.println(e.toString());
                wasProblem = true;
            }

            if(wasProblem == false){
                System.out.println("Update done");
            }

            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                System.out.println("Thread exception");
            }
        }

    }

}
