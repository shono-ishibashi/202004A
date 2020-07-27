$(function () {

    $('#zipcode').on('click',function () {
        alert('あああ');

        var ddd = $('#hiddenName').val();
        $('#name').val(ddd);

        var fff = $('#hiddenEmail').val();
        $('#mailAddress').val(fff);

        var sss = $('#hiddenZipcode').val();
        $('#zip').val(sss);

        var ggg = $('#hiddenTel').val();
        $('#tel').val(ggg);


    });


});