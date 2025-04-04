package com.steiner.vblog;

// 这是设置一些不可变的常量，如果你想配置，去 `configure/ApplicationConfigure`
public class Constants {
    public static final String AuthenticationHeader = "Authentication";
    public static final String AuthorizationHeader = "Authorization";
    public static final int BufferSize = 1024 * 1024 * 10;
    public static final String CURRENT_USER_KEY = "current-user";

    public static final String USERNAME_KEY = "username";
    public static final String USERID_KEY = "userid";
}
