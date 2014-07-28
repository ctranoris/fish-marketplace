package gr.upatras.ece.nam.baker.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HeadersOutInterceptor extends AbstractOutDatabindingInterceptor {
	
	
	private static final transient Log logger = LogFactory.getLog(HeadersOutInterceptor.class.getName());

	public HeadersOutInterceptor() {
		 super(Phase.MARSHAL);
	}

	@Override
	public void handleMessage(Message outMessage) throws Fault {		
		
		Map<String, List<String>> headers = (Map<String, List<String>>) outMessage.get(Message.PROTOCOL_HEADERS);
		
		if (headers == null) {
			headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
			
			List<String> vl = new ArrayList<String>();
			vl.add("1.0.0");
			headers.put("X-Baker-API-Version", vl );			
			outMessage.put(Message.PROTOCOL_HEADERS, headers);
		}
		
		
		
		// modify headers

	}

}
