package nam.ece.upatras.gr.fileup.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;



public class FileUploadAction extends ActionSupport  implements ServletRequestAware, ServletResponseAware {
	 
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	private String fileFinalLocation;

    HttpServletResponse servletResponse;
    HttpServletRequest servletRequest;

 
	@Action(value = "/fileUpload",
            results = {
                    @Result(name = "success", location = "/fileUpload/fileupload.jsp")
            }
    )
    public String fileUpload() {

        return SUCCESS;
    }
	
	
	
	@Action(value = "/fileresult",
			interceptorRefs = {
			@InterceptorRef( "exception"),
			@InterceptorRef("i18n"),
			@InterceptorRef("basicStack"),
  		  	@InterceptorRef( value="fileUpload", params = {"allowedTypes", "*/*", "maximumSize", "10485760"} ),
  		  	@InterceptorRef( value="params", params = {"excludeParams", "dojo\\..*,^struts\\..*" } ),
  		  	@InterceptorRef( value="validation", params = {"excludeMethods", "input,back,cancel,browse" } ),
  		  	@InterceptorRef( value="workflow", params = {"excludeMethods", "input,back,cancel,browse" } ),

  		    //@InterceptorRef("basicStack")
			},
            results = {
                    @Result(name = "success", location = "/fileUpload/result.jsp"),
                    @Result(name = "error", location = "/fileUpload/uploaderror.jsp"),
                    @Result(name = "input", location = "/fileUpload/uploaderror.jsp")
            }
    )
    public String fileresult() {
		/* Copy file to a safe location */

		   String destPath = "/tmp/";

	      try{
	     	 System.out.println("Src File name: " + fileUpload);
	     	 System.out.println("Dst File name: " + fileUploadFileName);
	     	    	 
	     	 File destFile  = new File(destPath, fileUploadFileName);
	    	 FileUtils.copyFile(fileUpload, destFile);
	    	 fileFinalLocation = destFile.getCanonicalPath();
	  
	      }catch(IOException e){
	         e.printStackTrace();
	         return ERROR;
	      }
        return SUCCESS;
    }
	
	
	
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}
 
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
 
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}
 
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}
 
	public File getFileUpload() {
		return fileUpload;
	}
 
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}
 
//	public String execute() throws Exception{
// 
//		return SUCCESS;
// 
//	}
// 
//	public String display() {
//		return NONE;
//	}

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



	/**
	 * @return the fileFinalLocation
	 */
	public String getFileFinalLocation() {
		return fileFinalLocation;
	}



	/**
	 * @param fileFinalLocation the fileFinalLocation to set
	 */
	public void setFileFinalLocation(String fileFinalLocation) {
		this.fileFinalLocation = fileFinalLocation;
	}
 
}