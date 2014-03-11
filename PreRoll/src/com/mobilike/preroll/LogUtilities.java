package com.mobilike.preroll;

public class LogUtilities
{
	/**************************************
     * LOG
     */
    /**
     * Library wise static boolean flag
     */
    private static final boolean LOG = true;
    
    
    public static void log(int priority, String tag, String message, Throwable exception)
    {
            if(LogUtilities.LOG)
            {
                    // If message is null, convert it to empty string
                    // Useful for later string concatinations
                    if(message == null) message = "";
                    // If there exist an exception, attach exception to message's tail
                    if(exception != null) message += "\n" + android.util.Log.getStackTraceString(exception);
                    // Log with given parameters
                    android.util.Log.println(priority, tag, message);
            }
    }
}
