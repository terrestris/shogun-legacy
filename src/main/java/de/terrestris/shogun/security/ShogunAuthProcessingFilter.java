package de.terrestris.shogun.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * based upon Blogs for Development
 * @see http://javajeedevelopment.blogspot.com/2011/02/integrating-spring-security-3-with.html
 * 
 * @author mbenrhouma
 */

/**
 * Class overwrites the standard Spring Filter for 
 * User/Password authentication.
 * Steering our authentication process.
 * 
 * @author terrestris GmbH & Co. KG
 */
public class ShogunAuthProcessingFilter extends UsernamePasswordAuthenticationFilter {

	/**
	 * On successful authentication by an Authentication Manager of Spring Security
	 * we intercept with this method  and change the respone to include the ROLES of 
	 * the logged in user.
	 * This way we can react on the ROLES and redirect accordingly within the requesting login form (here login.js)
	 * 
	 * @see WebContent/client/login.js
	 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
    	SecurityContextHolder.getContext().setAuthentication(authResult);
    	// manually adding the spring security context into the session to 
    	// get the currentuser on session timeout in our custom httpsessionlistener
    	request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        SavedRequestAwareAuthenticationSuccessHandler srh = new SavedRequestAwareAuthenticationSuccessHandler();
        this.setAuthenticationSuccessHandler(srh);
        srh.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse, String s) throws IOException {
                    //do nothing, no redirect
            }
        });
        super.successfulAuthentication(request, response, authResult);
        
        // build a comma separated string of the ROLES
        String authorityText = StringUtils.join(authResult.getAuthorities(), ",");
        
        // write the servlet return object
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        Writer out = responseWrapper.getWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBooleanField("success", true);
        jsonGenerator.writeStringField("name", authResult.getName());
        jsonGenerator.writeStringField("role", authorityText);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }

    /**
     * React on unsuccessful authentication.
     * We again intercept the response and return a JSON object with a flag indicating unsuccessful login.
     * 
     * @see WebContent/client/login.js
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    	super.unsuccessfulAuthentication(request, response, failed);
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        Writer out = responseWrapper.getWriter();
        out.write("{success:false}");
        out.close();
    }
}
