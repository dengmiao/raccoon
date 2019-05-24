package com.cure.common.toolkit;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * @title: MessageAccessor
 * @description: classpath messages accessor
 * @author: dengmiao
 * @create: 2019-05-24 09:22
 **/
public class MessageAccessor {

    private static final MessageSourceAccessor messageAccessor;

    static {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * Retrieve the messages for the given code and the default Locale.
     *
     * @param code code of the messages
     * @param defaultMessage String to return if the lookup fails
     * @return the messages
     */
    public static String getMessage(String code, String defaultMessage) {
        return messageAccessor.getMessage(code, defaultMessage);
    }

    /**
     * Retrieve the messages for the given code and the given Locale.
     *
     * @param code code of the messages
     * @param defaultMessage String to return if the lookup fails
     * @param locale Locale in which to do lookup
     * @return the messages
     */
    public static String getMessage(String code, String defaultMessage, Locale locale) {
        return messageAccessor.getMessage(code, defaultMessage, locale);
    }

    /**
     * Retrieve the messages for the given code and the default Locale.
     *
     * @param code code of the messages
     * @param args arguments for the messages, or {@code null} if none
     * @param defaultMessage String to return if the lookup fails
     * @return the messages
     */
    public static String getMessage(String code, @Nullable Object[] args, String defaultMessage) {
        return messageAccessor.getMessage(code, args, defaultMessage);
    }

    /**
     * Retrieve the messages for the given code and the given Locale.
     *
     * @param code code of the messages
     * @param args arguments for the messages, or {@code null} if none
     * @param defaultMessage String to return if the lookup fails
     * @param locale Locale in which to do lookup
     * @return the messages
     */
    public static String getMessage(String code, @Nullable Object[] args, String defaultMessage, Locale locale) {
        return messageAccessor.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * Retrieve the messages for the given code and the default Locale.
     *
     * @param code code of the messages
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(String code) throws NoSuchMessageException {
        return messageAccessor.getMessage(code);
    }

    /**
     * Retrieve the messages for the given code and the given Locale.
     *
     * @param code code of the messages
     * @param locale Locale in which to do lookup
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(String code, Locale locale) throws NoSuchMessageException {
        return messageAccessor.getMessage(code, locale);
    }

    /**
     * Retrieve the messages for the given code and the default Locale.
     *
     * @param code code of the messages
     * @param args arguments for the messages, or {@code null} if none
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
        return messageAccessor.getMessage(code, args);
    }

    /**
     * Retrieve the messages for the given code and the given Locale.
     *
     * @param code code of the messages
     * @param args arguments for the messages, or {@code null} if none
     * @param locale Locale in which to do lookup
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
        return messageAccessor.getMessage(code, args, locale);
    }

    /**
     * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance) in the default Locale.
     *
     * @param resolvable the MessageSourceResolvable
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return messageAccessor.getMessage(resolvable);
    }

    /**
     * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance) in the given Locale.
     *
     * @param resolvable the MessageSourceResolvable
     * @param locale Locale in which to do lookup
     * @return the messages
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return messageAccessor.getMessage(resolvable, locale);
    }
}
