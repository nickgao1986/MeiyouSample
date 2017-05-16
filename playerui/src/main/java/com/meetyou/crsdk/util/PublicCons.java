package com.meetyou.crsdk.util;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class PublicCons {

    public PublicCons() {
    }

    public static final class NetType {
        public static final int INVALID = 0;
        public static final int WAP = 1;
        public static final int G2 = 2;
        public static final int G3 = 3;
        public static final int WIFI = 4;
        public static final int NO_WIFI = 5;

        public NetType() {
        }
    }

    public static final class DBCons {
        public static final String TB_TASK = "task_info";
        public static final String TB_TASK_URL_BASE = "base_url";
        public static final String TB_TASK_URL_REAL = "real_url";
        public static final String TB_TASK_FILE_PATH = "file_path";
        public static final String TB_TASK_PROGRESS = "onThreadProgress";
        public static final String TB_TASK_FILE_LENGTH = "file_length";
        public static final String TB_THREAD = "thread_info";
        public static final String TB_THREAD_URL_BASE = "base_url";
        public static final String TB_THREAD_URL_REAL = "real_url";
        public static final String TB_THREAD_FILE_PATH = "file_path";
        public static final String TB_THREAD_START = "start";
        public static final String TB_THREAD_END = "end";
        public static final String TB_THREAD_ID = "id";
        public static final String TB_TASK_SQL_CREATE = "CREATE TABLE task_info(_id INTEGER PRIMARY KEY AUTOINCREMENT, base_url CHAR, real_url CHAR, file_path CHAR, onThreadProgress INTEGER, file_length INTEGER)";
        public static final String TB_THREAD_SQL_CREATE = "CREATE TABLE thread_info(_id INTEGER PRIMARY KEY AUTOINCREMENT, base_url CHAR, real_url CHAR, file_path CHAR, start INTEGER, end INTEGER, id CHAR)";
        public static final String TB_TASK_SQL_UPGRADE = "DROP TABLE IF EXISTS task_info";
        public static final String TB_THREAD_SQL_UPGRADE = "DROP TABLE IF EXISTS thread_info";

        public DBCons() {
        }
    }

    public static final class AccessModes {
        public static final String ACCESS_MODE_R = "r";
        public static final String ACCESS_MODE_RW = "rw";
        public static final String ACCESS_MODE_RWS = "rws";
        public static final String ACCESS_MODE_RWD = "rwd";

        public AccessModes() {
        }
    }
}
