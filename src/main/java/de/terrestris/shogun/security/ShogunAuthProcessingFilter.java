/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 */
package de.terrestris.shogun.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

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
