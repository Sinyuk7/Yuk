package com.sinyuk.yuk.api;

/**
 * Created by Sinyuk on 16.6.16.
 */
public class DribbleApi {
//    public static final String CLIENT_ID  = "c9a49d76fdded3a1d51a55396268081d12af093401bcc2730453d7a4c0f511b4";
//    public static final String CLIENT_SECRET  ="f31fa45f06cfdd3e447f5d7398e3e1693ae81ee3b2d48d4b3ce88f9134351b95";
    public static final String REDIRECT_URI  = "http://yuk.com/redirect";




    public static final String END_POINT = "https://api.dribbble.com/v1/";
    public static final String OAUTH_END_POINT ="https://dribbble.com/oauth/";
    public static final String NODE_AUTHORIZE ="authorize";
    public static final String PARAM_CLIENT_ID ="client_id";
    public static final String PARAM_REDIRECT_URI ="redirect_uri";
    public static final String PARAM_SCOPE ="scope";
    public static final String PARAM_STATE ="state";
    public static final String PER_PAGE = "per_page";

    public static final int PAGE_SIZE = 10;

    // Shots

    /**
     * List Types
     * Default: Results of any type.
     */
    public static final String ALL = "";
    public static final String ANIMATED = "animated";
    public static final String ATTACHMENTS = "attachments";
    public static final String DEBUTS = "debuts";
    public static final String PLAYOFFS = "playoffs";
    public static final String REBOUNDS = "rebounds";
    public static final String TEAMS = "teams";

    /**
     * timeframe
     * Default: Results from now.
     */
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String EVER = "ever";

    /**
     * data
     * Limit the timeframe to a specific date, week, month, or year. Must be in the format of YYYY-MM-DD.
     */

    /**
     * sort
     * Default: Results are sorted by popularity.
     */
    public static final String COMMENTS = "comments";
    public static final String RECENT = "recent";
    public static final String VIEWS = "views";

    public static final String DATE_FORMAT  = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ACCESS_TYPE = "Bearer";
}
