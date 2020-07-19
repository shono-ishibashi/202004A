$(function () {
    $('input[name=size]:checked').closest('.priceDiv').css('background-color','rgba(255,200,0,0.3)').css('border','2px solid orange');
    $('input[name=size]:not(:checked)').closest('.priceDiv').css('background-color','rgba(200,200,200,0.3)').css('border', '0px');

    $('input[name=size]').change(function(){
        $('input[name=size]:checked').closest('.priceDiv').css('background-color','rgba(255,200,0,0.3)').css('border','2px solid orange');
        $('input[name=size]:not(:checked)').closest('.priceDiv').css('background-color','rgba(200,200,200,0.3)').css('border', '0px');
    });
});
