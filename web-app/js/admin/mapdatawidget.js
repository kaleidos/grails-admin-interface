$('.js-add').on('click',function(event){
    event.preventDefault();
    $('.js-table tr:nth-child(2)').clone().appendTo($('.js-table')).removeClass('hidden');
});

$('.js-table').on('click','.js-delete',function(event){
    event.preventDefault();
    var table = $(this).parents("table");
    var result = $(this).parents(".js-mapdata").find('.js-input-map');
    $(this).parents('tr').remove();
    
    var trs = table.find("tr");
    var txt = mapa(trs);
    result.text(txt); 
});

$('.js-table').on('blur','.js-input-value, .js-input-key',function(){
    var trs = $(this).parents("table").find("tr");
    var txt = mapa(trs);
    $(this).parents(".js-mapdata").find('.js-input-map').text(txt); 
});

function mapa(trs){
     var txt ="{";
  
    $.each(trs, function(k, v){
        if(k!=0){
            var tr = $(v);
                       
            var key = tr.find(".js-input-key").val();
            var value = tr.find(".js-input-value").val();
            
            if(k<trs.length-1){
                txt += "'"+key+"':'"+value+"',";
            }else{
                txt += "'"+key+"':'"+value;
            }
        }
    });
    txt += "}";
    return txt;
}

