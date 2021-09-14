package org.narses.narsion.dev.world;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.moandjiezana.toml.Toml;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.util.CryptoUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.HashMap;
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
    private static final String username = "krystilize.68f85fd7";

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private static String pass = "";

    private static final String downloadDir = "Narses6k";
    private static final String saveDir = "world";
    private static final String timekeeper = "level.dat";
    private static final long updateInterval = 86400; // In seconds

    private static final AtomicInteger numFiles = new AtomicInteger(0);
    private static CountDownLatch numFilesDownloaded;


    private static final String[] ignoredDirs = {
            downloadDir + "/advancements",
            downloadDir + "/datapacks",
            downloadDir + "/data",
            downloadDir + "/playerdata",
            downloadDir + "/poi",
            downloadDir + "/stats"
    };


    public static void ensureLatestWorld(@NotNull Toml config) throws Throwable {

        // TODO: Fix security by using a public read only account
        String secretKey = "35c02ad3b60c4d12a758f5c4fabbf865";
        pass = CryptoUtil.decrypt(secretKey, config.getString("World.PasswordInput"));

        System.out.println("Checking for new world version");

        if (!shouldDownload(downloadDir)) {
            System.out.println("Finished world updating");
            return;
        }

        System.out.println("Connecting to world storage...");

        ChannelSftp channel = setupJsch();

        new File(saveDir).mkdir();
        downloadFolder(downloadDir, channel);

        new File(downloadDir).delete();
        numFilesDownloaded = new CountDownLatch(numFiles.intValue());

        numFilesDownloaded.await();

        System.out.println("Finished world updating");

        channel.exit();
    }

    private static boolean shouldDownload(String path) {

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

    private static void downloadFolder(String path, ChannelSftp channel) throws SftpException {

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

    private static void downloadFile(String path) throws SftpException, IOException {

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

    private static final Map<Thread, ChannelSftp> channels = new ConcurrentHashMap<>();

    private static ChannelSftp getNextChannel() {

        Thread thread = Thread.currentThread();

        channels.computeIfAbsent(thread, (_thread) -> setupJsch());

        return channels.get(thread);
    }

    private static ChannelSftp setupJsch() {
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