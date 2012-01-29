<html>
  <head>
    <title>Grails Runtime Exception</title>
    <style type="text/css">
      body {
        font-family: verdana, arial;
      }
      .message {
        border: 1px solid black;
        padding: 5px;
        background-color:#E9E9E9;
      }
      .stack {
        border: 1px solid black;
        padding: 5px;
        overflow:auto;
        height: 300px;
      }
      .snippet {
        padding: 5px;
        background-color:white;
        border:1px solid black;
        margin:3px;
        font-family:courier;
      }
      .head1 {
        padding-top:10px;
        padding-left:25px;
        padding-right:25px;
        padding-bottom:0px;
        margin: 5px;
        border:1px solid black;
        /*position:fixed;*/
        background: url(../images/styleimages/toolbar.png) #6d84a2 repeat-x;
        /*top:0px;*/
        /*position:fixed;*/
        /*left:0px;*/
        min-height:65px;
        /*	width:320px;*/
        /*width:100%;*/
        /*z-index:+1;*/
        /*border-bottom: 1px solid #E0E0E0;*/
        overflow:hidden;
        display:block;
        font-family: verdana, arial;
        font-size:28px;
        font-weight:bold;
        /*text-shadow:rgba(0,0,0,0.4) 0 1px 0;*/
        color:black;
      }
    </style>
  </head>

  <body>
    <div class="head1">Items Runtime Exception</div>
    <h2>Error Details</h2>

    <div class="message">
      <strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
      <strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br/>
      <strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/>
      <g:if test="${exception}">
        <strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br />
        <strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br />
        <strong>Class:</strong> ${exception.className} <br />
        <strong>At Line:</strong> [${exception.lineNumber}] <br />
        <strong>Code Snippet:</strong><br />
        <div class="snippet">
          <g:each var="cs" in="${exception.codeSnippet}">
${cs?.encodeAsHTML()}<br />
          </g:each>
        </div>
      </g:if>
    </div>
  <g:if test="${exception}">
    <h2>Stack Trace</h2>
    <div class="stack">
      <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
    </div>
  </g:if>
</body>
</html>