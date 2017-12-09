$(function(){
    setInterval(function() {
        $.ajax({
            url: '/getBroadcastsList?' + crsf,
            type: 'POST',
            dataType: 'JSON',
            processData: false,
            contentType: false,
            success: function(data){

            },
            error: function (error) {
            }
        });

        }, 10000);

    $('button').click(function () {
        btnConnect(this);
    });

});
