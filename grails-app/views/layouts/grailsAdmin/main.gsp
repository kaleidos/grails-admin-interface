<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Grails admin example</title>
        <link href="${resource(file: 'libs/bootstrap/dist/css/bootstrap.css', plugin: 'admin')}" rel="stylesheet" />
        <link href="${resource(file: 'libs/bootstrap/dist/css/bootstrap-theme.css', plugin: 'admin')}" rel="stylesheet" />
        <link href="${resource(file: 'libs/select2/select2.css', plugin: 'admin')}" rel="stylesheet" />
        <link href="${resource(file: 'libs/select2/select2-bootstrap.css', plugin: 'admin')}" rel="stylesheet" />
        <link href="${resource(file: 'css/main.css', plugin: 'admin')}" rel="stylesheet" />
    </head>
    <body>
        <g:include action="menu" />
        <g:layoutBody/>
        <script src="${resource(file: 'libs/jquery/dist/jquery.js', plugin: 'admin')}"></script>
        <script src="${resource(file: 'libs/bootstrap/dist/js/bootstrap.js', plugin: 'admin')}"></script>
        <script src="${resource(file: 'libs/select2/select2.js', plugin: 'admin')}"></script>
        <script src="${resource(file: 'js/main.js', plugin: 'admin')}"></script>
    </body>
</html>
