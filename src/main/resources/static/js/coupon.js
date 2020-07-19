$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $('#coupon-submit').click(function () {
        var inputCouponCode = { a: $('#inputCouponCode').val()}
        $.ajax({
            url: "/coupon/search",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            date: JSON.stringify(inputCouponCode),
        }).done(function (data) {
            console.log(data);
            if (data !== null) {
                var discountRate = 100 - data.discountRate;
                var totalPrice = $('#hidden-total-price').text();
                var discountedPrice = totalPrice * (discountRate / 100);


                $('#discounted-price').text('クーポン適用後：' + discountedPrice + '円');
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $("#p1").text(jqXHR.status); //例：404
        }).always(function () {
        });
    });
});