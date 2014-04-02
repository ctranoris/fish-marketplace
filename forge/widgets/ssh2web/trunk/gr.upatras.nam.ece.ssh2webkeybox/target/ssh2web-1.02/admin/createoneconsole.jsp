<%
    /**
     * Copyright 2013 Sean Kavanagh - sean.p.kavanagh6@gmail.com
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>

<style>
#dragandrophandler
{
border: 2px dashed #92AAB0;
width: 650px;
height: 300px;
color: #92AAB0;
text-align: center;
vertical-align: middle;
padding: 10px 0px 10px 10px;
font-size:200%;
display: table-cell;
}
.progressBar {
    width: 140px;
    height: 22px;
    border: 1px solid #ddd;
    border-radius: 5px;
    overflow: hidden;
    display:inline-block;
    margin:0px 10px 5px 5px;
    vertical-align:top;
}
 
.progressBar div {
    height: 100%;
    color: #fff;
    text-align: right;
    line-height: 22px; /* same as #progressBar height if we want text middle aligned */
    width: 0;
    background-color: #0ba1b5; border-radius: 3px;
}


.statusbaroverlay
{
    position: absolute;
	top: 5;
	left: 10;
	zIndex: 100;
}

.statusbar
{
    border-top:1px solid #A9CCD1;
    min-height:25px;
    width:485px;
    padding:5px 5px 0px 5px;
    vertical-align:top;
    background:#EBEFF0;
}
.statusbar:nth-child(odd){
    background:#FFFFFF;
}
.filename
{
display:inline-block;
vertical-align:top;
width:150px;
font-size:10px;
font-weight:normal;
}


.filesize
{
display:inline-block;
vertical-align:top;
color:#30693D;
width:100px;
margin-left:10px;
margin-right:5px;
}

.filenamestatustxt
{
	display:inline-block;
	vertical-align:top;
	color:#30693D;
	margin-left:5px;
	margin-top:-10px;
    font-family:arial;font-size:9px;font-weight:normal;
}


.abort{
    background-color:#A8352F;
    -moz-border-radius:4px;
    -webkit-border-radius:4px;
    border-radius:4px;display:inline-block;
    color:#fff;
    font-family:arial;font-size:13px;font-weight:normal;
    padding:4px 15px;
    cursor:pointer;
    vertical-align:top
    }
</style>

<jsp:include page="../_res/inc/header.jsp"/>

<script type="text/javascript">




$(document).ready(function () {


    $('#dummy').focus();

    $("#set_password_dialog").dialog({
        autoOpen: false,
        height: 200,
        minWidth: 550,
        modal: true
    });
    $("#set_passphrase_dialog").dialog({
        autoOpen: false,
        height: 200,
        minWidth: 550,
        modal: true
    });
    $("#error_dialog").dialog({
        autoOpen: false,
        height: 200,
        minWidth: 550,
        modal: true
    });

    $(".termwrapper").sortable({
        helper: 'clone'
    });

    //submit add or edit form
    $(".submit_btn").button().click(function () {
        <s:if test="pendingSystemStatus!=null">
        $(this).parents('form:first').submit();
        </s:if>
        $("#error_dialog").dialog("close");
    });
    //close all forms
    $(".cancel_btn").button().click(function () {
        $("#set_password_dialog").dialog("close");
        window.location = 'getNextPendingSystemForTerms.action?pendingSystemStatus.id=<s:property value="pendingSystemStatus.id"/>&script.id=<s:if test="script!=null"><s:property value="script.id"/></s:if>';

    });


    //if terminal window toggle active for commands
    $(".run_cmd").click(function () {

        //check for cmd-click / ctr-click
        if (!keys[17] && !keys[91] && !keys[93] && !keys[224]) {
            $(".run_cmd").removeClass('run_cmd_active');
        }

        if ($(this).hasClass('run_cmd_active')) {
            $(this).removeClass('run_cmd_active');
        } else {
            $(this).addClass('run_cmd_active')
        }

    });

    $('#select_all').click(function () {
        $(".run_cmd").addClass('run_cmd_active');
    });


    $('#upload_push').click(function () {


        //get id list from selected terminals
        var ids = [];
        $(".run_cmd_active").each(function (index) {
            var id = $(this).attr("id").replace("run_cmd_", "");
            ids.push(id);
        });
        var idListStr = '?action=upload';
        ids.forEach(function (entry) {
            idListStr = idListStr + '&idList=' + entry;
        });

        $("#upload_push_frame").attr("src", "setUpload.action" + idListStr);
        $("#upload_push_dialog").dialog("open");


    });


    <s:if test="currentSystemStatus!=null && currentSystemStatus.statusCd=='GENERICFAIL'">
    $("#error_dialog").dialog("open");
    </s:if>
    <s:elseif test="pendingSystemStatus!=null">
    <s:if test="pendingSystemStatus.statusCd=='AUTHFAIL'">
    $("#set_password_dialog").dialog("open");
    </s:if>
    <s:elseif test="pendingSystemStatus.statusCd=='KEYAUTHFAIL'">
    $("#set_passphrase_dialog").dialog("open");
    </s:elseif>
    <s:else>
    <s:if test="currentSystemStatus==null ||currentSystemStatus.statusCd!='GENERICFAIL'">
    $("#composite_terms_frm").submit();
    </s:if>
    </s:else>
    </s:elseif>






    <s:if test="pendingSystemStatus==null">

    var keys = {};

    $(document).keypress(function (e) {
        var keyCode = (e.keyCode) ? e.keyCode : e.charCode;

        var idList = [];
        $(".output").each(function (index) {
            var id = $(this).attr("id").replace("output_", "");
            idList.push(id);
        });

        if (String.fromCharCode(keyCode) && String.fromCharCode(keyCode) != ''
                && !keys[91] && !keys[93] && !keys[224] && !keys[27]
                && !keys[37] && !keys[38] && !keys[39] && !keys[40]
                && !keys[13] && !keys[8] && !keys[9] && !keys[17]) {
            var cmdStr = String.fromCharCode(keyCode);
            connection.send(JSON.stringify({id: idList, command: cmdStr}));
        }

    });
    //function for command keys (ie ESC, CTRL, etc..)
    $(document).keydown(function (e) {
        var keyCode = (e.keyCode) ? e.keyCode : e.charCode;
        keys[keyCode] = true;
        //27 - ESC
        //37 - LEFT
        //38 - UP
        //39 - RIGHT
        //40 - DOWN
        //13 - ENTER
        //8 - BS
        //9 - TAB
        //17 - CTRL
        if (keys[27] || keys[37] || keys[38] || keys[39] || keys[40] || keys[13] || keys[8] || keys[9] || keys[17]) {
            var idList = [];
            $(".output").each(function (index) {
                var id = $(this).attr("id").replace("output_", "");
                idList.push(id);
            });

            connection.send(JSON.stringify({id: idList, keyCode: keyCode}));
        }

    });

    $(document).keyup(function (e) {
        var keyCode = (e.keyCode) ? e.keyCode : e.charCode;
        delete keys[keyCode];
        $('#dummy').focus();
    });

    $(document).click(function (e) {
        $('#dummy').focus();
    });

    //get cmd text from paste
    $("#dummy").bind('paste', function (e) {
        $('#dummy').val('');
        setTimeout(function () {
            var idList = [];
            $(".output").each(function (index) {
                var id = $(this).attr("id").replace("output_", "");
                idList.push(id);
            });
            var cmdStr = $('#dummy').val();
            connection.send(JSON.stringify({id: idList, command: cmdStr}));
        }, 100);
    });


    var termMap = {};

    $(".output").each(function (index) {
        var id = $(this).attr("id").replace("output_", "");
        termMap[id] = new Terminal(80, 24);
        termMap[id].open($(this));
        termMap[id].write("\tDrag files over this window to upload to server...\n\r\tFiles will be pushed to your home ~ directory\n\r\tHave fun!The FORGE team!\n\r=====\n\r");
    });


    var loc = window.location, ws_uri;
    if (loc.protocol === "https:") {
        ws_uri = "wss:";
    } else {
        ws_uri = "ws:";
    }
   	
    ws_uri += "//" + loc.host + '/ssh2web/ssh2webterms.ws?t=' + new Date().getTime();
    //ws_uri += "//" + loc.host + '/terms.ws?t=' + new Date().getTime();

    var connection = new WebSocket(ws_uri);


    // Log errors
    connection.onerror = function (error) {
        console.log('WebSocket Error ' + error);
    };

    // Log messages from the server
    connection.onmessage = function (e) {
        var json = jQuery.parseJSON(e.data);
        $.each(json, function (key, val) {
            if (val.output != '') {
                termMap[val.hostSystemId].write(val.output);
            }
        });

    };

    setInterval(function () {
        connection.send('');
    }, 500);

    </s:if>
    
    
    var obj = $("#output_1"); //give default
    //we have ony one output so the obj will be only one    
    $(".output").each(function (index) {
        var id = $(this).attr("id").replace("output_", "");
        obj = $("#output_"+id);
    });
    
    obj.on('dragenter', function (e)
    {
        e.stopPropagation();
        e.preventDefault();
        $(this).css('border', '2px solid #0B85A1');
    });
    obj.on('dragover', function (e)
    {
         e.stopPropagation();
         e.preventDefault();
    });
    obj.on('drop', function (e)
    {
     
         $(this).css('border', '2px dotted #0B85A1');
         e.preventDefault();
         var files = e.originalEvent.dataTransfer.files;
     
         //We need to send dropped files to Server
         handleFileUpload(files,obj);
    });
    $(document).on('dragenter', function (e)
    {
        e.stopPropagation();
        e.preventDefault();
    });
    $(document).on('dragover', function (e)
    {
      e.stopPropagation();
      e.preventDefault();
      obj.css('border', '2px dotted #0B85A1');
    });
    $(document).on('drop', function (e)
    {
        e.stopPropagation();
        e.preventDefault();
    });
     

});





function sendFileToServer(formData,status)
{
    //var uploadURL ="http://localhost:8080/fileUpload/fileresultdrag.action"; //Upload URL
    var uploadURL ="upload.action"; //Upload URL
    var extraData ={}; //Extra Data.
    var jqXHR=$.ajax({
            xhr: function() {
            var xhrobj = $.ajaxSettings.xhr();
            if (xhrobj.upload) {
                    xhrobj.upload.addEventListener('progress', function(event) {
                        var percent = 0;
                        var position = event.loaded || event.position;
                        var total = event.total;
                        if (event.lengthComputable) {
                            percent = Math.ceil(position / total * 100);
                        }
                        //Set progress
                        status.setProgress(percent);
                    }, false);
                }
            return xhrobj;
        },
    url: uploadURL,
    type: "POST",
    contentType:false,
    processData: false,
        cache: false,
        data: formData,
        success: function(data){
            status.setProgress(100);
            status.setStatusText("File uploaded to server. Pushing to target machines. Please wait...");
            //$("#status1").append("File upload to server, Done<br>");        
        }
    });
 
    status.setAbort(jqXHR);
}

function pushFileToMachine(formData,status)
{
    //var uploadURL ="http://localhost:8080/fileUpload/fileresultdrag.action"; //Upload URL
    var uploadURL ="push.action"; //Upload URL
    var extraData ={}; //Extra Data.
    var jqXHR=$.ajax({
            xhr: function() {
            var xhrobj = $.ajaxSettings.xhr();
           
            return xhrobj;
        },
    url: uploadURL,
    type: "POST",
    contentType:false,
    processData: false,
        cache: false,
        data: formData,
        success: function(data){
            //status.setProgress(100);
            status.setStatusText("File pushed to target machine, Done");
     		status.fade();
            //$("#status1").append("File pushed to target machine, Done<br>");        
        }
    });
 
    status.setAbort(jqXHR);
}
 
var rowCount=0;
function createStatusbar(obj)
{
     rowCount++;
     var row="odd";
     if(rowCount %2 ==0) row ="even";
     this.statusbar = $("<div class='statusbar "+row+"'></div>");
     this.filename = $("<div class='filename'></div>").appendTo(this.statusbar);
     this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
     this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.statusbar);
     this.abort = $("<div class='abort'>Abort</div>").appendTo(this.statusbar);
     this.statustxt = $("<div class='filenamestatustxt'>- </div>").appendTo(this.statusbar);
     
     this.statusbar.appendTo(obj);
     //obj.after(this.statusbar);
     
 
    this.setFileNameSize = function(name,size)
    {
        var sizeStr="";
        var sizeKB = size/1024;
        if(parseInt(sizeKB) > 1024)
        {
            var sizeMB = sizeKB/1024;
            sizeStr = sizeMB.toFixed(2)+" MB";
        }
        else
        {
            sizeStr = sizeKB.toFixed(2)+" KB";
        }
 
        this.filename.html(name);
        this.size.html(sizeStr);
    }
    this.setStatusText = function(txt)
    {
    	this.statustxt.html(txt);	
    	
    }
    
    this.fade = function(){
    	this.statusbar.fadeTo( 2000, 0, function() {
    		this.remove();
    	}    			
    	);	
    }
    
    this.setProgress = function(progress)
    {      
        var progressBarWidth =progress*this.progressBar.width()/ 100; 
        this.progressBar.find('div').animate({ width: progressBarWidth }, 10).html(progress + "% ");
        if(parseInt(progress) >= 100)
        {
            this.abort.hide();
        }
    }
    this.setAbort = function(jqxhr)
    {
        var sb = this.statusbar;
        this.abort.click(function()
        {
            jqxhr.abort();
            sb.hide();
        });
    }
    
    
}
function handleFileUpload(files,obj)
{
   for (var i = 0; i < files.length; i++)
   {
        var fd = new FormData();
        fd.append('upload', files[i]);
 
        var status = new createStatusbar( $("#statusbaroverlaydiv") ); //Using this we can set progress.
        status.setFileNameSize(files[i].name,files[i].size);
        status.setStatusText("Uploading file...");
        sendFileToServer(fd,status);
 		pushFileToMachine(fd,status);
 		
 		 
 		
   }
}



</script>

<title>KeyBox - One Console Term!</title>

</head>
<body>

	
    <s:if test="(systemList!= null && !systemList.isEmpty()) || pendingSystemStatus!=null">
        <div class="content">

            <s:if test="pendingSystemStatus==null">


                

            </s:if>
            
                <s:iterator value="systemList">
                			<div id="statusbaroverlaydiv" class="statusbaroverlay"> </div>
                            <div id="output_<s:property value="id"/>" class="output"></div>
                </s:iterator>


            <div id="set_password_dialog" title="Enter Password">
                <p class="error"><s:property value="pendingSystemStatus.errorMsg"/></p>

                <p>Enter password for <s:property value="pendingSystemStatus.displayLabel"/>

                </p>
                <s:form id="password_frm" action="createTermsOneConsole">
                    <s:hidden name="pendingSystemStatus.id"/>
                    <s:password name="password" label="Password" size="15" value="" autocomplete="off"/>
                    <s:if test="script!=null">
                        <s:hidden name="script.id"/>
                    </s:if>
                    <tr>
                        <td colspan="2">
                            <div class="submit_btn">Submit</div>
                            <div class="cancel_btn">Cancel</div>
                        </td>
                    </tr>
                </s:form>
            </div>

            <div id="set_passphrase_dialog" title="Enter Passphrase">
                <p class="error"><s:property value="pendingSystemStatus.errorMsg"/></p>

                <p>Enter passphrase for <s:property value="pendingSystemStatus.displayLabel"/></p>
                <s:form id="passphrase_frm" action="createTermsOneConsole">
                    <s:hidden name="pendingSystemStatus.id"/>
                    <s:password name="passphrase" label="Passphrase" size="15" value="" autocomplete="off"/>
                    <s:if test="script!=null">
                        <s:hidden name="script.id"/>
                    </s:if>
                    <tr>
                        <td colspan="2">
                            <div class="submit_btn">Submit</div>
                            <div class="cancel_btn">Cancel</div>
                        </td>
                    </tr>
                </s:form>
            </div>


            <div id="error_dialog" title="Error">
                <p class="error">Error: <s:property value="currentSystemStatus.errorMsg"/></p>

                <p>System: <s:property value="currentSystemStatus.displayLabel"/>

                </p>

                <s:form id="error_frm" action="createTermsOneConsole">
                    <s:hidden name="pendingSystemStatus.id"/>
                    <s:if test="script!=null">
                        <s:hidden name="script.id"/>
                    </s:if>
                    <tr>
                        <td colspan="2">
                            <div class="submit_btn">OK</div>
                        </td>
                    </tr>
                </s:form>
            </div>

         

            <s:form id="composite_terms_frm" action="createTermsOneConsole">
                <s:hidden name="pendingSystemStatus.id"/>
                <s:if test="script!=null">
                    <s:hidden name="script.id"/>
                </s:if>
            </s:form>

        </div>
    </s:if>
    <s:else>
        <jsp:include page="../_res/inc/navigation.jsp"/>

        <div class="content" style="width: 70%">
            <p class="error">No sessions could be created</p>
        </div>
    </s:else>

<div style="float:right;"><textarea name="dummy" id="dummy" size="1"
                                    style="border:none;color:#FFFFFF;width:1px;height:1px"></textarea></div>
<div style="float:right;"><input type="text" name="dummy2" id="dummy2" size="1"
                                 style="border:none;color:#FFFFFF;width:1px;height:1px"/>
</div>
	
</body>
</html>
