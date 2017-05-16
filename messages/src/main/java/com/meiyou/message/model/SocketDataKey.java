package com.meiyou.message.model;

public class SocketDataKey {

    public static final String TYPE = "type";
    public static final String PUSH_TYPE = "push_type";
    public static final String LEAP_TYPE = "leap_type";
    public static final String DATA_TYPE = "data_type";
    public static final String SN = "sn";
    public static final String STATUS = "status";
    public static final String TO = "to";
    public static final String FROM = "from";
    public static final String TIME = "time";
    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String ICON = "icon";
    public static final String UPDATES = "updates";
    public static final String VERIFY_CODE = "vercode";
    public static final String VERIFY_RET_CODE = "verify_retcode";
    public static final String LOGIN_STATUS = "login_status";
    public static final String CMD = "cmd";
    public static final String OFFLINE_SIZE = "offline_leftsize";

    public static final String BLACK_USER = "block_users";
    public static final String BLACK_VERSION = "block_version";

    public static final String VALID = "valid"; //有效次数
    public static final String EXPIRE = "expire"; //有效时间，以秒为单位
    public static final String VERCODE = "vercode";   //验证码

    public static final String PUBLIC_SERVICE = "public_service";
    public static final String PUBLIC_SERVICE_DATA = "public_service_data";


    //msg_offline_leftsize

	/*
    #define JKEY_MSG_TYPE "msg_type" 				 //消息类型 int MsgType
	#define JKEY_MSG_SN "msg_sn"      				//序列号 string
	#define JKEY_MSG_STATUS "msg_status"			//服务器返回码 int
	#define JKEY_MSG_TO   "msg_to"					//消息接收方 string
	#define JKEY_MSG_FROM "msg_from"				//消息发送方 string
	#define JKEY_MSG_TIME "msg_time"				//消息时间 string（解析后转成int64）
	#define JKEY_MSG_ID "msg_id"					//消息id string（解析后转成int64）
	#define JKEY_MSG_DATA "msg_data"				//消息内容 string
	#define JKEY_MSG_VERIFY_CODE "msg_vercode"		//服务器验证码 string
	#define JKEY_MSG_VERIFY_RETCODE	"msg_verify_retcode"	//验证结果 int 0=通过
	#define JKEY_MSG_LOGIN_STATUS "msg_login_status"		//登陆状态 int
	*/
}
