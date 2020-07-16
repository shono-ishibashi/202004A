$(function () {
    $('html').change(function () {

        var checkedItemSize = $('input[name=size]:checked').val();

        var checkedItemPrice = 0;

        if(checkedItemSize == 'M'){
            checkedItemPrice = $('#itemPriceM').text();
        }else if (checkedItemSize == 'L'){
            checkedItemPrice = $('#itemPriceL').text();
        }

        var toppingPrice;

        if (checkedItemSize == 'M') {
            toppingPrice = 200;
        } else if (checkedItemSize == 'L') {
            toppingPrice = 300;
        }
        console.log(toppingPrice);

        var toppingCount = $('#topping input:checkbox:checked').length;

        var itemCount = Number($("#quantity").val());
        var sumPrice = toppingPrice * toppingCount + Number(checkedItemPrice) ;
        var totalPrice = sumPrice * itemCount;
        $("#total-price").text(totalPrice);
    })
});
