/**
 * Created by thomasmunoz on 18/04/15.
 */
$(document).ready(function(){
    var file;

    $('#entry').on('change', function(){
        if($(this).val() == 'csv'){
            $(this).parent().append(
                $('<input />')
                    .attr('type', 'file')
                    .attr('id', 'csvfile')
            )
        }
    });

    $('.jumbotron').on('change', '#csvfile', function(e){
        file = e.target.files[0];
        console.log(file);
    });

    $('#sendbutton').on('click', function(){
        var value = $('#entry').val();
        var minconf = $('#minconf').val();
        console.log(minconf);

        if(value == 'blackbox'){
            location.href = 'http://unicorn.ovh';
        } else if (value == 'twitter'){
            console.log('Twitter Feed');
        } else if(value == 'csv'){
            upload(file);
        }
    });
});

function upload(file){
    var formData = new FormData();
    formData.append('file', file);
    $.ajax({
        url: '/upload.php',
        type: 'POST',
        processData: false,
        contentType: false,
        data: formData,
        xhr: function() {
            var xhr = new window.XMLHttpRequest();
            xhr.upload.addEventListener("progress", function(e) {
                if (e.lengthComputable) {
                    var percentCompleted = e.loaded / e.total;
                    percentCompleted = (percentCompleted * 100).toFixed(2);
                    console.log(percentCompleted);
                }
            }, false);
            return xhr;
        },
        success: function(data){
            if(data !== undefined){
                if(data.success !== undefined){
                    if(data.success) {
                        console.log('success');
                    } else {
                        console.log('erreur');
                    }
                }
            }
        }
    });
}