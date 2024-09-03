package com.github.sanjayrawat1.security;

/**
 * Constants for Spring Security authorities.
 *
 * @author sanjayrawat1
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {}

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String SYSTEM = "ROLE_SYSTEM";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String CLIENT_PRODUCT_SERVICE = "ROLE_CLIENT_PRODUCT_SERVICE";
    public static final String CLIENT_ORDER_SERVICE = "ROLE_CLIENT_ORDER_SERVICE";
    public static final String CLIENT_SHIPPING_SERVICE = "ROLE_CLIENT_SHIPPING_SERVICE";
    public static final String CLIENT_FULFILMENT_SERVICE = "ROLE_CLIENT_FULFILMENT_SERVICE";

    // hasAuthority('authority-name') constants
    public static final String HAS_AUTHORITY_ADMIN = "hasAuthority('" + ADMIN + "')";
    public static final String HAS_AUTHORITY_USER = "hasAuthority('" + USER + "')";
    public static final String HAS_AUTHORITY_SYSTEM = "hasAuthority('" + SYSTEM + "')";
    public static final String HAS_AUTHORITY_ANONYMOUS = "hasAuthority('" + ANONYMOUS + "')";
    public static final String HAS_AUTHORITY_CLIENT_PRODUCT_SERVICE = "hasAuthority('" + CLIENT_PRODUCT_SERVICE + "')";
    public static final String HAS_AUTHORITY_CLIENT_ORDER_SERVICE = "hasAuthority('" + CLIENT_ORDER_SERVICE + "')";
    public static final String HAS_AUTHORITY_CLIENT_SHIPPING_SERVICE = "hasAuthority('" + CLIENT_SHIPPING_SERVICE + "')";
    public static final String HAS_AUTHORITY_CLIENT_FULFILMENT_SERVICE = "hasAuthority('" + CLIENT_FULFILMENT_SERVICE + "')";
}
