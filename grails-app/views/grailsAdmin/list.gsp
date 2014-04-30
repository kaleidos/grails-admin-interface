<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Dashboard</title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container">
            <table class="table table-condensed dashboard">
                <tr>
                    <td>Attendee</td>
                    <td>
                        <div class="btn-group">
                            <g:link class="btn btn-link" mapping="list">
                                <span class="glyphicon glyphicon-list"></span>
                                List
                            </g:link>
                            <g:link class="btn btn-link" mapping="add">
                                <span class="glyphicon glyphicon-plus"></span>
                                Add
                            </g:link>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Talk</td>
                    <td>
                        <div class="btn-group">
                            <g:link class="btn btn-link" mapping="list">
                                <span class="glyphicon glyphicon-list"></span>
                                List
                            </g:link>
                            <g:link class="btn btn-link" mapping="add">
                                <span class="glyphicon glyphicon-plus"></span>
                                Add
                            </g:link>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
