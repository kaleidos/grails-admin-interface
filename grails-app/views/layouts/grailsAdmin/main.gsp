<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><g:layoutTitle/></title>

        <gap:layoutCss formType="${formType}" className="${className}"/>
    </head>
    <body>
        <g:include action="menu" params="[slug: params.slug]"/>

        <g:if test="${flash.success}">
            <div id="msg" class="container">
              <div class="alert alert-success">
                  ${flash.success}
              </div>
            </div>
        </g:if>

        <div id="msg-error" class="container" style="display:none;">
          <div class="alert alert-danger">
             <p>ERROR</p>
             <hr>
             <div class="alert-text"></div>
          </div>
        </div>

        <g:layoutBody/>

        <gap:layoutHandlebers formType="${formType}" className="${className}"/>
        <gap:layoutJs formType="${formType}" className="${className}"/>
        <script type="text/javascript">
            app.init();
        </script>
    </body>
</html>
