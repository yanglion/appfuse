package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


public class SignupControllerTest extends BaseControllerTestCase {
    private SignupController c;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (SignupController) ctx.getBean("signupController");
    }

    protected void tearDown() {
        c = null;
    }
    
    public void testDisplayForm() throws Exception {
        MockHttpServletRequest request =
            new MockHttpServletRequest(null, "GET", "/signup.xml");
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = c.handleRequest(request, response);
        assertTrue("returned correct view name",
                   mv.getViewName().equals("signup"));
    }

    public void testSignupUser() throws Exception {
        MockHttpServletRequest request =
            new MockHttpServletRequest(null, "POST", "/signup.xml");
        request.addParameter("username", "self-registered");
        request.addParameter("password", "Password1");
        request.addParameter("confirmPassword", "Password1");
        request.addParameter("firstName", "First");
        request.addParameter("lastName", "Last");
        request.addParameter("city", "Denver");
        request.addParameter("province", "Colorado");
        request.addParameter("country", "USA");
        request.addParameter("postalCode", "80210");
        request.addParameter("email", "self-registered@raibledesigns.com");
        request.addParameter("website", "http://raibledesigns.com");
        request.addParameter("passwordHint", "Password is one with you.");

        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = c.handleRequest(request, response);
        Errors errors =
            (Errors) mv.getModel().get(BindException.ERROR_KEY_PREFIX + "user");
        assertTrue("no errors returned in model", errors == null);
        
        // verify that success messages are in the request
        assertNotNull(request.getSession().getAttribute("messages"));
        assertTrue(request.getSession().getAttribute(Constants.REGISTERED) != null);
    }
}
