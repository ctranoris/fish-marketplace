package nam.ece.upatras.gr.mgmt.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class ShowUsersAction extends ActionSupport  implements ServletRequestAware, ServletResponseAware {


    HttpServletResponse servletResponse;
    HttpServletRequest servletRequest;

    @Action(value = "/showusers",
            results = {
                    @Result(name = "success", location = "/mgmg/showusers.jsp")
            }
    )
    public String showusers() {

        return SUCCESS;
    }

	public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }
	
	

}
