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


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.interceptor.security.AbstractAuthorizingInInterceptor;
import org.apache.cxf.security.SecurityContext;

public class MySimpleAuthorizingInterceptor  extends AbstractAuthorizingInInterceptor {

    protected Map<String, List<String>> methodRolesMap = new HashMap<String, List<String>>();
    protected Map<String, List<String>> userRolesMap = Collections.emptyMap();
    protected List<String> globalRoles = Collections.emptyList();
    private boolean checkConfiguredRolesOnly;
	private static final transient Log logger = LogFactory.getLog(MySimpleAuthorizingInterceptor.class.getName());
    
    @Override 
    protected boolean isUserInRole(SecurityContext sc, List<String> roles, boolean deny) {
    	
    	logger.info("===============   isUserInRole  sc.getUserPrincipal() "+sc.getUserPrincipal().getName());
        /*if (!checkConfiguredRolesOnly && !super.isUserInRole(sc, roles, deny)) {

        	logger.info("===============   isUserInRole false 1================");
        	logger.info("===============   isUserInRole false 1 sc.getUserPrincipal() "+sc.getUserPrincipal());
        	for (String role : roles) 
            	logger.info("===============   isUserInRole false 1 role =  "+ role);
        	
            return false;
        }*/
    	for (String role : roles) {
        	logger.info("===============   isUserInRole CHECKING role="+role);
            if ( role.equals( sc.getUserPrincipal().getName() )) {
            	logger.info("===============   isUserInRole return role="+role);
                return true;
            }
    		
    	}
    	
        // Additional check.
        if (!userRolesMap.isEmpty()) {
            List<String> userRoles = userRolesMap.get(sc.getUserPrincipal().getName());    
            if (userRoles == null) {
            	logger.info("===============   isUserInRole false 2================");
                return false;
            }
            for (String role : roles) {
                if (userRoles.contains(role)) {
                	logger.info("===============   isUserInRole return role = "+role);
                    return true;
                }
            }
        	logger.info("===============   isUserInRole false 3================");
            return false;
        } else {
        	logger.info("===============   isUserInRole return !checkConfiguredRolesOnly");
            return !checkConfiguredRolesOnly;
        }
    }
    
    protected String createMethodSig(Method method) {
    	logger.info("===============   createMethodSig ================");
        StringBuilder b = new StringBuilder(method.getReturnType().getName());
        b.append(' ').append(method.getName()).append('(');
        boolean first = true;
        for (Class<?> cls : method.getParameterTypes()) {
            if (!first) {
                b.append(", ");
                first = false;
            }
            b.append(cls.getName());
        }
        b.append(')');
    	logger.info("===============   createMethodSig b.toString()="+b.toString());
        return b.toString();
    }
    
    @Override
    protected List<String> getExpectedRoles(Method method) {
    	logger.info("===============   getExpectedRoles ================ method ="+ method.getName());
        List<String> roles = methodRolesMap.get(createMethodSig(method));
        if (roles == null) {
            roles = methodRolesMap.get(method.getName());
        }
        if (roles != null) {
        	logger.info("===============   getExpectedRoles return roles");
        	
            return roles;
        }
    	logger.info("===============   getExpectedRoles return globalRoles");
        return globalRoles;
    }


    public void setMethodRolesMap(Map<String, String> rolesMap) {
    	logger.info("===============   setMethodRolesMap ================");
        methodRolesMap.putAll(parseRolesMap(rolesMap)); 
    }
    
    public void setUserRolesMap(Map<String, String> rolesMap) {
    	logger.info("===============   setUserRolesMap ================ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        userRolesMap = parseRolesMap(rolesMap);
    }
    
    public void setGlobalRoles(String roles) {
        globalRoles = Arrays.asList(StringUtils.split(roles, " "));
    }
    
    public void setCheckConfiguredRolesOnly(boolean checkConfiguredRolesOnly) {
        this.checkConfiguredRolesOnly = checkConfiguredRolesOnly;
    }
    
    private static Map<String, List<String>> parseRolesMap(Map<String, String> rolesMap) {
    	logger.info("===============   parseRolesMap ================");
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Map.Entry<String, String> entry : rolesMap.entrySet()) {
            map.put(entry.getKey(), Arrays.asList(StringUtils.split(entry.getValue(), " ")));
        	logger.info("entry.getKey() = "+ entry.getKey()+", entry.getValue()="+entry.getValue() );
        }
        return map;
    }
}
