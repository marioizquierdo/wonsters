package es.engade.thearsmonsters.http.controller.session;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacadeMock;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * A facade utility class to transparently manage session objects and cookies.
 * The following objects are mantained in the session:
 *
 * <ul>
 *
 * <li>The user's first name, under the key 
 * <code>FIRST_NAME_SESSION_ATTRIBUTE</code>. This attribute is
 * only present for authenticated users, and is only of interest to the view
 * (JSP pages). </li>
 *
 * <li>An instance of <code>Locale</code>. If the user has been authenticated, 
 * his/her value is consistent with his/her profile information. This attribute
 * must only be accessed by using 
 * <code>org.apache.struts.action.Action.getLocale</code> or
 * <code>es.udc.mizquierdo.j2ee.util.struts.action.DefaultActionForm.getLocale</code>.
 * </li>
 *
 * <li>An instance of <code>UserFacadeDelegate</code>. This attribute
 * must only be accessed by using <code>getUserFacadeDelegate</code>.</li>
 *
 * </ul>
 *
 * <p>
 *
 * For users that select "remember my password" in the login wizard, the
 * following cookies are used:
 *
 * <ul>
 * <li><code>LOGIN_NAME_COOKIE</code>: to store the login name.</li>
 * <li><code>ENCRYPTED_PASSWORD_COOKIE</code>: to store the encrypted 
 * password.</li>
 * </ul>
 *
 * <p>
 *
 * In order to make transparent the management of session objects and cookies
 * to the implementation of controller actions, this class provides a number
 * of methods equivalent to some of the ones provided by 
 * <code>UserFacadeDelegate</code>, which manage session objects and cookies,
 * and call upon the corresponding <code>UserFacadeDelegate's</code> method.
 * These methods are as follows:
 *
 * <ul>
 * <li><code>login</code>.</li>
 * <li><code>registerUser</code>.</li>
 * <li><code>updateUserProfileDetails</code>.</li>
 * <li><code>changePassword</code>.</li>
 * </ul>
 *
 * It is important to remember that when needing to do some of the above
 * actions from the controller, the corresponding method of this class
 * (one of the previous list) must be called, and <b>not</b> the one in
 * <code>UserFacadeDelegate</code>. The rest of methods of
 * <code>UserFacadeDelegate</code> must be called directly. Currently, there
 * exists only one, <code>findUserProfileDetails</code>, but in a real portal
 * there would be more. For example, in a personalizable portal,
 * <code>UserFacadeDelegate</code> could include:
 * <code>findServicePreferences</code>, <code>updateServicePreferences</code>,
 * <code>findLayout</code>, <code>updateLayout</code>, etc. In a electronic
 * commerce shop, <code>UserFacadeDelegate</code> could include:
 * <code>getShoppingCart</code>, <code>addToShoppingCart</code>,
 * <code>removeFromShoppingCart</code>, <code>makeOrder</code>,
 * <code>findPendingOrders</code>, etc. When needing to invoke directly a
 * method of <code>UserFacadeDelegate</code>,
 * <code>SessionManager.getUserFacadeDelegate</code> must be invoked in order
 * to get the personal delegate (each user has his/her own delegate).
 *
 * <p>
 *
 * Same as <code>UserFacadeDelegate</code>, there exist some logical 
 * restrictions with regard to the order of method calling. In particular, 
 * <code>updateUserProfileDetails</code> and <code>changePassword</code>
 * can not be called if <code>login</code> or <code>registerUser</code> 
 * have not been previously called. After the user calls one of these two 
 * methods, the user is said to be authenticated.
 *
 */
public final class SessionManager {

    public final static String IS_ADMIN_SESSION_ATTRIBUTE = "isAdmin";
    private final static String USER_FACADE_SESSION_ATTRIBUTE = "userFacade";
    private final static String LAIR_SESSION_ATTRIBUTE = "myLair";

    public static final String LOGIN_NAME_COOKIE = "loginName";
    public static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";

    private static final int COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD =
        30 * 24 * 3600; // 30 days
    private static final int COOKIES_TIME_TO_LIVE_REMOVE = 0;

    private SessionManager() {}
    
    public final static void login(HttpServletRequest request,
        HttpServletResponse response, String loginName,
        String clearPassword, boolean rememberMyPassword, boolean loginAsAdmin) 
        throws IncorrectPasswordException, InstanceNotFoundException,
            InternalErrorException {

        /* 
         * Try to login, and if successful, update session with the necessary 
         * objects for an authenticated user.
         */ 
        LoginResult loginResult = doLogin(request, loginName, 
            clearPassword, false, loginAsAdmin);
            
        /* Add cookies if requested. */
        if (rememberMyPassword) {
            leaveCookies(response, loginName, 
                loginResult.getEncryptedPassword(),
                COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD);
        }
        
    }
    
    public final static void registerUser(HttpServletRequest request,
        String loginName, String clearPassword, 
        UserDetails UserDetails) 
        throws DuplicateInstanceException, InternalErrorException, FullPlacesException {
        
        /* Register user. */
        UserFacade userFacade  = new UserFacadeMock();
            
        userFacade.registerUser(loginName, clearPassword, 
            UserDetails);
            
        /* 
         * Update session with the necessary objects for an authenticated
         * user. 
         */
        Locale locale = new Locale(UserDetails.getLanguage());
        updateSesssionForAuthenticatedUser(request, locale);

    }
    
    public final static void updateUserProfileDetails(
        HttpServletRequest request, UserDetails UserDetails) 
        throws InternalErrorException  {
        
        /* Update user's profile details. */
        UserFacade userFacade = new UserFacadeMock();
            
        userFacade.updateUserProfileDetails(UserDetails);
        
        /* Update user's session objects.*/
        Locale locale = new Locale(UserDetails.getLanguage());
        
        updateSesssionForAuthenticatedUser(request, locale);
    }

    public final static void changePassword(HttpServletRequest request, 
        HttpServletResponse response, String oldClearPassword,
        String newClearPassword) throws IncorrectPasswordException,
        InternalErrorException  {
        
        /* Change user's password. */
        UserFacade userFacade = new UserFacadeMock();
            
        userFacade.changePassword(oldClearPassword, newClearPassword);
            
        /* Remove cookies. */
        leaveCookies(response, "", "", COOKIES_TIME_TO_LIVE_REMOVE);
        
    }

    /**
     * Destroyes session, and removes cookies if the user had selected
     * "remember my password.
     */
    public final static void logout(HttpServletRequest request,
        HttpServletResponse response) throws InternalErrorException {
    
        /* Remove cookies. */
        leaveCookies(response, "", "", COOKIES_TIME_TO_LIVE_REMOVE);
    
        /* Invalidate session. */
        HttpSession session = request.getSession(true);
        session.invalidate();
    
    }

    public final static UserFacade getUserFacade(
        HttpServletRequest request) throws InternalErrorException {

        HttpSession session = request.getSession(true);
        UserFacade UserFacade = 
            (UserFacade) session.getAttribute(
                USER_FACADE_SESSION_ATTRIBUTE);
                
        if (UserFacade == null) {
            UserFacade = new UserFacadeMock();
            session.setAttribute(USER_FACADE_SESSION_ATTRIBUTE,
                UserFacade);
        }

        return UserFacade;
        
    }
    
    /**
     * Guarantees that the session will have the necessary objects if the user
     * has been authenticated or had selected "remember my password" in the 
     * past.
     */
    public final static void touchSession(HttpServletRequest request) 
        throws InternalErrorException {
        
        /* 
         * The user had not been authenticated or his/her session has expired. 
         * We need to check if the user has selected "remember my password" in 
         * the last login (login name and password will come as cookies). If 
         * so, we reconstruct user's session objects.
         */
        if(!isUserAuthenticated(request)) {
        	updateSessionFromCookies(request);
        }
    
    }

    public final static boolean isUserAuthenticated(
    		HttpServletRequest request) {
    
        HttpSession session = request.getSession(false);
        
        return	session != null && 
        		session.getAttribute(LAIR_SESSION_ATTRIBUTE) != null;
    
    }
    
    public final static boolean isUserAuthenticatedAsAdmin(
    		HttpServletRequest request) {
        
            HttpSession session = request.getSession(false);
            
            return	session != null && 
    				session.getAttribute(LAIR_SESSION_ATTRIBUTE) != null &&
    				session.getAttribute(IS_ADMIN_SESSION_ATTRIBUTE) != null;
        
    }
    
    private final static void updateSesssionForAuthenticatedUser(
        HttpServletRequest request, Locale locale) 
    	throws InternalErrorException {
        
        /* Insert objects in session. */
        HttpSession session = request.getSession(true);
        session.setAttribute(Globals.LOCALE_KEY, locale);

    }
    
    /**
     * Tries to login (inserting necessary objects in the session) by using 
     * cookies (if present).
     */
    private final static void updateSessionFromCookies(
        HttpServletRequest request) throws InternalErrorException {
        
        /* Are there cookies in the request ? */
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        /* 
         * Check if the login name and the encrypted password come as 
         * cookies. 
         */
        String loginName = null;
        String encryptedPassword = null;
        int foundCookies = 0;

        for (int i=0; (i<cookies.length) && (foundCookies < 2); i++) {
            if (cookies[i].getName().equals(LOGIN_NAME_COOKIE)) {
                loginName = cookies[i].getValue();
                foundCookies++;
            }
            if (cookies[i].getName().equals(ENCRYPTED_PASSWORD_COOKIE)) {
                encryptedPassword = cookies[i].getValue();
                foundCookies++;
            }
        }

        if (foundCookies != 2) {
            return;
        }
        
        /* Try to login, and if successful, update session with the necessary 
         * objects for an authenticated user.
         */
        try {
            doLogin(request, loginName, encryptedPassword, true, false);
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) { // Incorrect loginName or encryptedPassword
            return;
        }

    }
    
    private final static void leaveCookies(HttpServletResponse response,
        String loginName, String encryptedPassword, int timeToLive) {
        
         /* Create cookies. */
        Cookie loginNameCookie = new Cookie(LOGIN_NAME_COOKIE, loginName);
        Cookie encryptedPasswordCookie = new Cookie(ENCRYPTED_PASSWORD_COOKIE, 
            encryptedPassword);

        /* Set maximum age to cookies. */
        loginNameCookie.setMaxAge(timeToLive);
        encryptedPasswordCookie.setMaxAge(timeToLive);

        /* Add cookies to response. */
        response.addCookie(loginNameCookie);
        response.addCookie(encryptedPasswordCookie);

    }
    
    /**
     * Tries to log in with the corresponding method of 
     * <code>UserFacade</code>, and if successful, inserts in the
     * session the necessary objects for an authenticated user.
     */
    private final static LoginResult doLogin(HttpServletRequest request,
         String loginName, String password, boolean passwordIsEncrypted,
         boolean loginAsAdmin) 
         throws IncorrectPasswordException, InstanceNotFoundException, 
                InternalErrorException {
         
        /* Check "loginName" and "clearPassword". */
        UserFacade userFacade = getUserFacade(request);
        LoginResult loginResult = userFacade.login(
            loginName, password, passwordIsEncrypted, loginAsAdmin);
 

        /* Insert necessary objects in the session. */
        HttpSession session = request.getSession(true);
        Locale locale = new Locale(loginResult.getLanguage());
        updateSesssionForAuthenticatedUser(request, locale);
        session.setAttribute(LAIR_SESSION_ATTRIBUTE, loginResult.getLair());
        
    	
    	if(loginAsAdmin) session.setAttribute(IS_ADMIN_SESSION_ATTRIBUTE, true); // Esto no es demasiado peligroso ???s
        
        /* Return "loginResult". */
        return loginResult;
         
    }
    
    /**
     * There is a Lair instance with all its Rooms cached in the
     * session (if not, load it from the database). 
     * Using this, Model Actions don't need to 
     * load the lair each time from the database, but they need to store the 
     * results back if the 
     * Lair instance or any of its Rooms was changed. The database info and 
     * the Lair stored in session synchronization is responsibility of Model Actions.
     * @return the user's lair stored in the session, or a new one loaded from the database.
     * @throws RuntimeException if lair is not in the session (may be the user has not logged in).
     */
    public static Lair getMyLair(HttpServletRequest request) throws RuntimeException {
    	HttpSession session = request.getSession(true);
		Lair lair = (Lair) session.getAttribute(LAIR_SESSION_ATTRIBUTE);
		
		// If lair is not in the session, load it from the database and set it in the session
		if(lair == null) { 
	        throw new RuntimeException("Error: There is no lair in the session. User must doLogin first.");
		}
		
		return lair;
    }
    
    /**
     * Removes the session object bound wiht LAIR_SESSION_ATTRIBUTE name,
     * the lair. It is usefull for remove unstable data from the session.
     * If the myLair attribute is not in the session, this method do nothing.
     */
    public static void removeMyLair(HttpServletRequest request) {
    	HttpSession session = request.getSession(true);
    	session.removeAttribute(LAIR_SESSION_ATTRIBUTE);
    }

}
