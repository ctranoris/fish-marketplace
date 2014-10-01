/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

public class AjaxUserFilter extends UserFilter {
	

	private static final transient Log logger = LogFactory.getLog(AjaxUserFilter.class.getName());
	
    @Override
    protected boolean onAccessDenied(ServletRequest request,
        ServletResponse response) throws Exception {
    
    	logger.info("=======> AjaxUserFilter:onAccessDenied <=============");
    	
        HttpServletRequest req = WebUtils.toHttp(request);
        String xmlHttpRequest = req.getHeader("X-Requested-With");
        if ( xmlHttpRequest != null )
            if ( xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest") )  {
                HttpServletResponse res = WebUtils.toHttp(response);
                res.sendError(401);
                return false;
        }
        
        
        HttpServletResponse res = WebUtils.toHttp(response);
        res.sendError(401);
        return false;
        
        //return false;
        //return super.onAccessDenied(request, response);
    }
}  