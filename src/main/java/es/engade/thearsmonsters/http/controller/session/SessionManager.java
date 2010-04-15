package es.engade.thearsmonsters.http.controller.session;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.configuration.AppContext;
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
//    private final static String LAIR_SESSION_ATTRIBUTE = "myLair";
    private final static String LOGIN_SESSION_ATTRIBUTE = "loginName";

    public static final String LOGIN_NAME_COOKIE = "login";
    public static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";

    private static final int COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD =
        30 * 24 * 3600; // 30 days
    private static final int COOKIES_TIME_TO_LIVE_REMOVE = 0;

    @Autowired
    private static UserFacade userFacade;
    @Autowired
    private static LairFacade lairFacade;
    
    private SessionManager() {}
    
    public static UserFacade getUserFacade() {
        return userFacade;
    }

    public static void setUserFacade(UserFacade userFacade) {
        SessionManager.userFacade = userFacade;
    }

    public static LairFacade getLairFacade() {
        return lairFacade;
    }

    public static void setLairFacade(LairFacade lairFacade) {
        SessionManager.lairFacade = lairFacade;
    }

    static {
        userFacade = (UserFacade) AppContext.getInstance().getAppContext().getBean("userFacade");
        lairFacade = (LairFacade) AppContext.getInstance().getAppContext().getBean("lairFacade");
    }
    
    public final static void login(HttpServletRequest request,
        HttpServletResponse response, String login,
        String clearPassword, boolean rememberMyPassword, boolean loginAsAdmin) 
        throws IncorrectPasswordException, InstanceNotFoundException,
            InternalErrorException {

        /* 
         * Try to login, and if successful, update session with the necessary 
         * objects for an authenticated user.
         */ 
        LoginResult loginResult = doLogin(request, login, 
            clearPassword, false, loginAsAdmin);

        /* Save login into session */
        setSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, loginResult);
//        saveAttribute(request, LAIR_SESSION_ATTRIBUTE, loginResult.getLair());
        
        /* Add cookies if requested. */
        if (rememberMyPassword) {
            leaveCookies(response, login, 
                loginResult.getEncryptedPassword(),
                COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD);
        }
        
    }
    
    public final static void registerUser(HttpServletRequest request,
        String login, String clearPassword, 
        UserDetails UserDetails) 
        throws DuplicateInstanceException, InternalErrorException, FullPlacesException {
        
        /* Register user. */
            
       LoginResult loginResult = userFacade.registerUser(login, clearPassword, 
            UserDetails);
            
        /* Save login into session */
        setSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, loginResult);
        
        /* 
         * Update session with the necessary objects for an authenticated
         * user. 
         */
        Locale locale = new Locale(UserDetails.getLanguage());
        updateSesssionForAuthenticatedUser(request, locale);

    }
    
    public final static User findUserProfile(HttpServletRequest request) 
    throws InternalErrorException {
        
        String login = getMyLogin(request);
        
        try {
            return userFacade.findUserProfile(login);
        } catch (InstanceNotFoundException e) {
            return null;
        }
    }
    
    public final static void updateUserProfileDetails(
        HttpServletRequest request, UserDetails UserDetails) 
        throws InternalErrorException  {
            
        String login = getMyLogin(request);
        
        userFacade.updateUserProfileDetails(login, UserDetails);
        
        /* Update user's session objects.*/
        Locale locale = new Locale(UserDetails.getLanguage());
        
        updateSesssionForAuthenticatedUser(request, locale);
    }

    public final static void changePassword(HttpServletRequest request, 
        HttpServletResponse response, String oldClearPassword,
        String newClearPassword) throws IncorrectPasswordException,
        InternalErrorException  {
        
        String login = getMyLogin(request);
                   
        /* Change user's password. */
            
        userFacade.changePassword(login, oldClearPassword, newClearPassword);
            
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

//    public final static UserFacade getUserFacade(
//        HttpServletRequest request) throws InternalErrorException {
//
//        HttpSession session = request.getSession(true);
//        UserFacade UserFacade = 
//            (UserFacade) session.getAttribute(
//                USER_FACADE_SESSION_ATTRIBUTE);
//                
//        if (UserFacade == null) {
//            UserFacade = new UserFacadeMock();
//            session.setAttribute(USER_FACADE_SESSION_ATTRIBUTE,
//                UserFacade);
//        }
//
//        return UserFacade;
//        
//    }
    
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
    
         try {
             return (getMyLogin(request) != null);
         } catch (RuntimeException e) {
             return false;
         }
         
    }
    
    public final static boolean isUserAuthenticatedAsAdmin(
    		HttpServletRequest request) {
        
            return	isUserAuthenticated(request) &&
    				getSessionAttribute(request, IS_ADMIN_SESSION_ATTRIBUTE, false) != null;
        
    }
    
    private final static void updateSesssionForAuthenticatedUser(
        HttpServletRequest request, Locale locale) 
    	throws InternalErrorException {
        
        /* Insert objects in session. */
        setSessionAttribute(request, Globals.LOCALE_KEY, locale);

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
        String login = null;
        String encryptedPassword = null;
        int foundCookies = 0;

        for (int i=0; (i<cookies.length) && (foundCookies < 2); i++) {
            if (cookies[i].getName().equals(LOGIN_NAME_COOKIE)) {
                login = cookies[i].getValue();
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
            doLogin(request, login, encryptedPassword, true, false);
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) { // Incorrect login or encryptedPassword
            return;
        }

    }
    
    private final static void leaveCookies(HttpServletResponse response,
        String login, String encryptedPassword, int timeToLive) {
        
         /* Create cookies. */
        Cookie loginCookie = new Cookie(LOGIN_NAME_COOKIE, login);
        Cookie encryptedPasswordCookie = new Cookie(ENCRYPTED_PASSWORD_COOKIE, 
            encryptedPassword);

        /* Set maximum age to cookies. */
        loginCookie.setMaxAge(timeToLive);
        encryptedPasswordCookie.setMaxAge(timeToLive);

        /* Add cookies to response. */
        response.addCookie(loginCookie);
        response.addCookie(encryptedPasswordCookie);

    }
    
    /**
     * Tries to log in with the corresponding method of 
     * <code>UserFacade</code>, and if successful, inserts in the
     * session the necessary objects for an authenticated user.
     */
    private final static LoginResult doLogin(HttpServletRequest request,
         String login, String password, boolean passwordIsEncrypted,
         boolean loginAsAdmin) 
         throws IncorrectPasswordException, InstanceNotFoundException, 
                InternalErrorException {
         
        /* Check "login" and "clearPassword". */
//        UserFacade userFacade = getUserFacade(request);
        LoginResult loginResult = userFacade.login(
            login, password, passwordIsEncrypted, loginAsAdmin);
 

        /* Insert necessary objects in the session. */
        Locale locale = new Locale(loginResult.getLanguage());
        updateSesssionForAuthenticatedUser(request, locale);
        setSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, loginResult);
        
    	
    	if(loginAsAdmin) setSessionAttribute(request, IS_ADMIN_SESSION_ATTRIBUTE, true); // Esto no es demasiado peligroso ???s
        
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
     * @throws InternalErrorException 
     */
    public static Lair getMyLair(HttpServletRequest request) throws RuntimeException, InternalErrorException {
    	LoginResult loginResult = (LoginResult) getSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, true);
    	if (loginResult == null || loginResult.getLogin() == null) {
            throw new RuntimeException("Error: There is no user in the session. User must doLogin first.");
        }

    	Lair lair;
    	if (loginResult.isPersistentLair()) {
    	    lair = loginResult.getLair();
    	} else {
    	    // If lair is not in the session or it is not persistent,
    	    // load it from the database and set it in the session
            String login = loginResult.getLogin();

            try {
                lair = lairFacade.findLairByLogin(login); 
//                lair = userFacade.findUserProfile(login).getLair();
                loginResult.setLair(lair);
                setSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, loginResult);
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException("Error: Can't load user lair");
            }
    	}

		return lair;
    }
    
    public static String getMyLogin(HttpServletRequest request) throws RuntimeException {
        boolean throwException = false;

        String login = null;

        LoginResult loginResult = (LoginResult) getSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, false);
        if (loginResult != null) {
            login = loginResult.getLogin();
            throwException = (login == null);
        } else {
            throwException = true;
        }

        if (throwException) {
            throw new RuntimeException("Error: There is no user in the session. User must doLogin first.");
        }
        return login;
    }
    
    /**
     * Removes the session object bound wiht LAIR_SESSION_ATTRIBUTE name,
     * the lair. It is usefull for remove unstable data from the session.
     * If the myLair attribute is not in the session, this method do nothing.
     */
    public static void removeMyLair(HttpServletRequest request) {

        LoginResult loginResult = (LoginResult) getSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, true);
    	if (loginResult != null) {
        	loginResult.detachLair();
        	setSessionAttribute(request, LOGIN_SESSION_ATTRIBUTE, loginResult);
    	}
    }

    private static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        request.getSession(true).setAttribute(name, value);
    }
    
    private static Object getSessionAttribute(HttpServletRequest request, String name, boolean create) {
        return request.getSession(create).getAttribute(name);
    }
}
