package org.narses.narsion.dev.world;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldDownloader {

    private static final String remoteHost = "panel.tempusrift.net";
    private static final int port = 2022;
    private static final String username = "public.68f85fd7";
    private static final String downloadDir = "Narses6k";
    private static final String saveDir = "world";
    private static final String timekeeper = "level.dat";
    private static final long updateInterval = 86400; // In seconds

    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final String pass;
    private final AtomicInteger numFiles = new AtomicInteger(0);
    private CountDownLatch numFilesDownloaded;

    private final @NotNull DevServer server;

    public WorldDownloader(@NotNull DevServer server) {
        this.server = server;
        this.pass = "Fp63XC!ezq4P8b";

        System.out.println("Checking for new world version");

        if (!shouldDownload()) {
            System.out.println("Finished world updating");
            return;
        }

        System.out.println("Connecting to world storage...");

        ChannelSftp channel = setupJsch();

        if (channel == null) {
            System.out.println("Error updating world.");
            return;
        }

        new File(saveDir).mkdir();
        try {
            downloadFolder(downloadDir, channel);
        } catch (SftpException e) {
            e.printStackTrace();
        }

        new File(downloadDir).delete();
        this.numFilesDownloaded = new CountDownLatch(numFiles.intValue());

        try {
            numFilesDownloaded.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished world updating");

        channel.exit();
    }


    private static final String[] ignoredDirs = {
            downloadDir + "/advancements",
            downloadDir + "/datapacks",
            downloadDir + "/data",
            downloadDir + "/playerdata",
            downloadDir + "/poi",
            downloadDir + "/stats"
    };


    public void ensureLatestWorld() throws Throwable {

    }

    private boolean shouldDownload() {

        Duration time = Duration.ofMillis(System.currentTimeMillis());

        File file = new File(System.getProperty("user.dir") + "/" + saveDir + "/" + timekeeper);

        if (!file.exists()) {
            System.out.println("No local download detected, downloading from database...");
            return true;
        }

        long compareTime = file.lastModified();

        Duration timePassed = Duration.ofSeconds(time.getSeconds() - (compareTime / 1000));

        System.out.println(
                "Time since last world update: " +
                timePassed.toDaysPart() + " Days " +
                timePassed.toHoursPart() + " Hours " +
                timePassed.toMinutesPart() + " Minutes and " +
                timePassed.toSecondsPart() + " Seconds."
        );

        if (timePassed.getSeconds() > updateInterval) {
            System.out.println("New world version is larger then a day old, commencing download...");
            return true;
        };

        System.out.println("New world version is not larger then a day old, skipping download...");
        return false;
    }

    private void downloadFolder(String path, ChannelSftp channel) throws SftpException {

        for (String dir : ignoredDirs) {
            if (dir.equals(path)) {
                System.out.println("Ignoring folder: " + path);
                return;
            }
        }

        String downloadPath = path.replaceFirst(downloadDir + "[/]", saveDir + "/");

        new File(System.getProperty("user.dir") + "/" + downloadPath).mkdir();

        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> list = channel.ls(path);

        for (ChannelSftp.LsEntry entry : list) {
            if (entry.getAttrs().isDir()) {
                downloadFolder(path + "/" + entry.getFilename(), channel);
            } else {
                numFiles.incrementAndGet();
                executor.execute(() -> {
                    try {
                        downloadFile(path + "/" + entry.getFilename());
                    } catch (SftpException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void downloadFile(String path) throws SftpException, IOException {

        ChannelSftp channel = getNextChannel();

        BufferedInputStream bis = new BufferedInputStream(channel.get(path));

        String downloadPath = path.replaceFirst(downloadDir + "[/]", saveDir + "/");

        File file = new File(System.getProperty("user.dir") + "/" + downloadPath);

        file.createNewFile();

        OutputStream outStream = new FileOutputStream(file);

        outStream.write(bis.readAllBytes());

        outStream.close();
        bis.close();
        numFilesDownloaded.countDown();
        System.out.println("Downloaded: " + path + " (" + (numFiles.intValue() - numFilesDownloaded.getCount()) + "/" + numFiles.intValue() + ")");
    }

    private final Map<Thread, ChannelSftp> channels = new ConcurrentHashMap<>();

    private ChannelSftp getNextChannel() {

        Thread thread = Thread.currentThread();

        channels.computeIfAbsent(thread, (_thread) -> setupJsch());

        return channels.get(thread);
    }

    private ChannelSftp setupJsch() {
        try {
            JSch jsch = new JSch();
            Session jschSession = jsch.getSession(username, remoteHost);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            jschSession.setPassword(pass);
            jschSession.setPort(port);
            jschSession.connect();
            ChannelSftp channel = (ChannelSftp) jschSession.openChannel("sftp");

            channel.connect();
            return channel;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}