package com.sensifai.enhancement.SNPE;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.qualcomm.qti.snpe.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A logger that uses the standard Android Log class to log exceptions, and also logs them to a
 * file on the device. Requires permission WRITE_EXTERNAL_STORAGE in AndroidManifest.xml.
 *
 * @author Cindy Potvin
 */
public class Log {

    private static ContextGetter contextGetter;
    private static String logsDir = ".sensifai";
    private static String timestamp = System.currentTimeMillis() + "";

    public static void setContextGetter(ContextGetter contextGetter) {
        Log.contextGetter = contextGetter;
    }

    public static void setLogsDir(String logsDir) {
        Log.logsDir = logsDir;
    }

    /**
     * Sends an error message to LogCat and to a log file.
     *
     * @param logMessageTag A tag identifying a group of log messages. Should be a constant in the
     *                      class calling the logger.
     * @param logMessage    The message to add to the log.
     */
    public static void e(String logMessageTag, String logMessage) {
        log(android.util.Log.ERROR, logMessageTag, logMessage, null);
    }

    /**
     * Sends an error message and the exception to LogCat and to a log file.
     *
     * @param logMessageTag      A tag identifying a group of log messages. Should be a constant in the
     *                           class calling the logger.
     * @param logMessage         The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void e(String logMessageTag, String logMessage, Throwable throwableException) {
        log(android.util.Log.ERROR, logMessageTag, logMessage, throwableException);
    }

    /**
     * Sends an info message to LogCat and to a log file.
     *
     * @param logMessageTag A tag identifying a group of log messages. Should be a constant in the
     *                      class calling the logger.
     * @param logMessage    The message to add to the log.
     */
    public static void i(String logMessageTag, String logMessage) {
        log(android.util.Log.INFO, logMessageTag, logMessage, null);
    }

    /**
     * Sends an info message and the exception to LogCat and to a log file.
     *
     * @param logMessageTag      A tag identifying a group of log messages. Should be a constant in the
     *                           class calling the logger.
     * @param logMessage         The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void i(String logMessageTag, String logMessage, Throwable throwableException) {
        log(android.util.Log.INFO, logMessageTag, logMessage, throwableException);
    }

    /**
     * Sends an warn message to LogCat and to a log file.
     *
     * @param logMessageTag A tag identifying a group of log messages. Should be a constant in the
     *                      class calling the logger.
     * @param logMessage    The message to add to the log.
     */
    public static void w(String logMessageTag, String logMessage) {
        log(android.util.Log.WARN, logMessageTag, logMessage, null);
    }

    /**
     * Sends an warn message and the exception to LogCat and to a log file.
     *
     * @param logMessageTag      A tag identifying a group of log messages. Should be a constant in the
     *                           class calling the logger.
     * @param logMessage         The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void w(String logMessageTag, String logMessage, Throwable throwableException) {
        log(android.util.Log.WARN, logMessageTag, logMessage, throwableException);
    }

    /**
     * Sends an debug message to LogCat and to a log file.
     *
     * @param logMessageTag A tag identifying a group of log messages. Should be a constant in the
     *                      class calling the logger.
     * @param logMessage    The message to add to the log.
     */
    public static void d(String logMessageTag, String logMessage) {
        log(android.util.Log.DEBUG, logMessageTag, logMessage, null);
    }

    /**
     * Sends an debug message and the exception to LogCat and to a log file.
     *
     * @param logMessageTag      A tag identifying a group of log messages. Should be a constant in the
     *                           class calling the logger.
     * @param logMessage         The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void d(String logMessageTag, String logMessage, Throwable throwableException) {
        log(android.util.Log.DEBUG, logMessageTag, logMessage, throwableException);
    }

    /**
     * Sends a message to LogCat and to a log file.
     *
     * @param logMessageTag A tag identifying a group of log messages. Should be a constant in the
     *                      class calling the logger.
     * @param logMessage    The message to add to the log.
     */
    public static void v(String logMessageTag, String logMessage) {
        log(android.util.Log.VERBOSE, logMessageTag, logMessage, null);
    }

    /**
     * Sends a message and the exception to LogCat and to a log file.
     *
     * @param logMessageTag      A tag identifying a group of log messages. Should be a constant in the
     *                           class calling the logger.
     * @param logMessage         The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void v(String logMessageTag, String logMessage, Throwable throwableException) {
        log(android.util.Log.VERBOSE, logMessageTag, logMessage, throwableException);
    }

    private static void log(int logType, String logMessageTag, String logMessage, Throwable throwableException) {
        if (!android.util.Log.isLoggable(logMessageTag, logType) ||
                logType == android.util.Log.VERBOSE && !BuildConfig.DEBUG ||
                logType == android.util.Log.DEBUG && !BuildConfig.DEBUG)
            return;

        int logResult = 0;
        switch (logType) {
            case android.util.Log.ERROR:
                logResult = throwableException == null
                        ? android.util.Log.e(logMessageTag, logMessage)
                        : android.util.Log.e(logMessageTag, logMessage, throwableException);
                break;
            case android.util.Log.WARN:
                logResult = throwableException == null
                        ? android.util.Log.w(logMessageTag, logMessage)
                        : android.util.Log.w(logMessageTag, logMessage, throwableException);
                break;
            case android.util.Log.INFO:
                logResult = throwableException == null
                        ? android.util.Log.i(logMessageTag, logMessage)
                        : android.util.Log.i(logMessageTag, logMessage, throwableException);
                break;
            case android.util.Log.DEBUG:
                logResult = throwableException == null
                        ? android.util.Log.d(logMessageTag, logMessage)
                        : android.util.Log.d(logMessageTag, logMessage, throwableException);
                break;
            case android.util.Log.VERBOSE:
                logResult = throwableException == null
                        ? android.util.Log.v(logMessageTag, logMessage)
                        : android.util.Log.v(logMessageTag, logMessage, throwableException);
                break;
        }

        if (logResult > 0) {
            if (throwableException != null)
                logToFile(logType, logMessageTag, logMessage);
            else
                logToFile(logType, logMessageTag, logMessage + "\r\n" + android.util.Log.getStackTraceString(throwableException));
        }

    }

    private static void logToFile(int logType, String logMessageTag, String logMessage) {
        try {
            File logDirectory = new File(Environment.getExternalStorageDirectory() + "/" + logsDir);
            File logFile = new File(logDirectory, "log" + timestamp + ".txt");

            // create log folder
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            if (!logFile.exists())
                logFile.createNewFile();

            String logTypeTag = "";
            switch (logType) {
                case android.util.Log.ERROR:
                    logTypeTag = "E/";
                    break;
                case android.util.Log.WARN:
                    logTypeTag = "W/";
                    break;
                case android.util.Log.INFO:
                    logTypeTag = "I/";
                    break;
                case android.util.Log.DEBUG:
                    logTypeTag = "D/";
                    break;
                case android.util.Log.VERBOSE:
                    logTypeTag = "V/";
                    break;
            }

            // Write the message to the log with a timestamp
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(String.format("%1s %s%2s: %3s\r\n", getDateTimeStamp(), logTypeTag, logMessageTag, logMessage));
            writer.close();
            if (contextGetter != null) {
                // Refresh the data so it can seen when the device is plugged in a
                // computer. You may have to unplug and replug to see the latest
                // changes
                MediaScannerConnection.scanFile(contextGetter.getContext(),
                        new String[]{logFile.toString()},
                        null,
                        null);
            }
        } catch (IOException e) {
            android.util.Log.e(Log.class.getSimpleName(), "Unable to log exception to file.");
        }
    }

    /**
     * Gets a stamp containing the current date and time to write to the log.
     *
     * @return The stamp for the current date and time.
     */
    private static String getDateTimeStamp() {
        Date dateNow = Calendar.getInstance().getTime();
        return (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(dateNow));
    }

    public interface ContextGetter {
        Context getContext();
    }
}
