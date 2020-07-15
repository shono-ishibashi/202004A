$(function () {

    var toppingCount=0;
    var toppingPrice=0;

    var itemCount=1;
    var firstCheckedItemPrice=0;
    var checkedItemPrice=0;
    var total=0;


     firstCheckedItemPrice = $('input[name=responsibleCompany]:checked').val();
     console.log(firstCheckedItemPrice);

    if (checkedItemPrice ==0 ){
        total =firstCheckedItemPrice * itemCount + toppingPrice * toppingCount ;
    } else {
        total =checkedItemPrice * itemCount + toppingPrice * toppingCount ;
    }

    $("#total-price").text(total);

    $('input:radio').change(function() {
        checkedItemPrice = $('input[name=responsibleCompany]:checked').val();
        console.log(checkedItemPrice);

        if( checkedItemPrice == firstCheckedItemPrice ){
            toppingPrice = 200;
        } else {
            toppingPrice = 300;
        }
        console.log(toppingPrice);

        if (checkedItemPrice ==0 ){
            total =firstCheckedItemPrice * itemCount + toppingPrice * toppingCount ;
        } else {
            total =checkedItemPrice * itemCount + toppingPrice * toppingCount ;
        }

        $("#total-price").text(total);

    });

    $('input:checkbox').change(function() {
        toppingCount = $('#topping input:checkbox:checked').length;
        console.log(toppingCount);

        if (checkedItemPrice ==0 ){
            total =firstCheckedItemPrice * itemCount + toppingPrice * toppingCount ;
        } else {
            total =checkedItemPrice * itemCount + toppingPrice * toppingCount ;
        }

        $("#total-price").text(total);
    });

    $("select, #auto").change(function () {
        alert("iiiii");

        itemCount = $("#auto").val();
        console.log(itemCount);

        if (checkedItemPrice ==0 ){
            total =firstCheckedItemPrice * itemCount + toppingPrice * toppingCount ;
        } else {
            total =checkedItemPrice * itemCount + toppingPrice * toppingCount ;
        }

        $("#total-price").text(total);
    });

});
