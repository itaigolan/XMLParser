package Threads;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class WatchThread extends Thread{
    Path directory;
    
    //Watches the folder
    WatchService watcher;
    WatchKey watchKey;
    List<WatchEvent<?>> events;
    
    public WatchThread(String directory){
        try {
            //Creates path to directory
            this.directory = Paths.get(directory);
            //Creates watcher for directory
            watcher = this.directory.getFileSystem().newWatchService();
            //Monitors for new files added to the directory
            this.directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            try {
            //Listens to events
            watchKey = watcher.take();
            } catch (InterruptedException ex) {
                System.out.println("The operation was interrupted.");
            }
        } catch (IOException ex) {
            System.out.println("The folder was not found.");
        }
    }
    
    @Override
    public void start() {
        // Get list of events as they occur
        events = watchKey.pollEvents();
    }

    public List<WatchEvent<?>> getEventsList() {
        return events;
    }
    
    public WatchEvent getEvent() {
        WatchEvent event = events.get(0);
        events.remove(0);
        return event;
    }

    //Returns true if there are events in events
    public boolean hasEvents() {
        return events.size()>0;
    }
}

