package net.kaleidos.plugins.admin.widget

class MapDataWidget extends Widget {

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<div class='js-mapdata mapdata'>")
        html.append('<a href="" class="js-add btn btn-default add"><span class="glyphicon glyphicon-plus"></span>Add</a>')
        html.append("<table class='js-table table table-bordered elements-table'>")
        html.append("<tr class='headers'>")
        html.append("<th>key</th>")
        html.append("<th>value</th>")
        html.append("<th>&nbsp;</th>")
        html.append("</tr>")
        html.append("<tr class='js-tr styleInputs hidden' >")
        html.append('<td class="js-key key"><input type="text" name="key" class="js-input-key form-control " /></td>')
        html.append('<td class="js-value value"><input type="text" name="key" class="js-input-value form-control" /></td>')
        html.append('<td class="js-action delete"><a href="" class="js-delete btn btn-default"><span class="glyphicon glyphicon-trash">Delete</a></td>')
        html.append('</tr>')
        value.each{key, value->
            _createLine(key, value)
        }
        html.append("</table>")
        html.append('<input type="hide" name="map" class="js-input-map" style="display:none" />')
        html.append("</div>")
        return html
    }

    public void updateValue() {
        //Do nothing
    }

    private _createLine(String key,String value){
        StringBuilder html = new StringBuilder()
        html.append('<tr class="js-tr styleInputs" style="display: block;">')
        html.append("<td class=\"js-key\"><input type=\"text\" name=\"key\" class=\"js-input-key form-control\" value=\"$key\" /></td>")
        html.append("<td class=\"js-value\"><input type=\"text\" name=\"value\" class=\"js-input-value form-control\" value=\"$value\" /></td>")
        html.append('<td class="js-action"><a href="" class="js-delete btn btn-default"><span class="glyphicon glyphicon-trash">Delete</a></td>')
        html.append('</tr>')
        return html
    }
}
