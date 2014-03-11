package com.mobilike.preroll;

import android.util.Log;

public abstract class BaseObject
{
	/** @return Log method will print to console? */
    abstract protected boolean isLogEnabled();
    /** @return Tag string, that will be printed with log */
    abstract protected String getLogTag();
    
    
    /**
     * Log given message as priority Log.DEBUG
     * 
     * @param message String to log
     */
    protected void log(String message)
    {
    	log(message, Log.DEBUG);
    }
    
    
    /**
     * Log given message with given priority
     * 
     * @param message String to log
     * @param priority Log priority {DEBUG, INFO, ERROR, VERBOSE, ASSERT, WARN}
     */
    protected void log(String message, int priority)
    {
        log(message, priority, null);
    }
    
    
    /**
     * Log given message and exception with given priority
     * 
     * @param message String to log
     * @param priority Log priority {DEBUG, INFO, ERROR, VERBOSE, ASSERT, WARN}
     * @param exception Throwable to print with message
     */
    protected void log(String message, int priority, Throwable exception)
    {
        if(isLogEnabled())
        {
        	LogUtilities.log(priority, getLogTag(), message, exception);
        }
    }
}
