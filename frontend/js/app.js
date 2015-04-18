/**
 * Created by thomasmunoz on 18/04/15.
 */
$(document).ready(function(){

    $('#entry').on('change', function(){
        if($(this).val() == 'csv'){
            $(this).parent().append(
                $('<input />')
                    .attr('type', 'file')
                    .attr('id', 'csvfile')
            )
        }
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

            var fileReader = new FileReader();
            fileReader.onload = function (e){
                app.generateEncryptForm(file.name, e.target.result);
            };
            
            fileReader.readAsDataURL(file);
        }
    });
});

function upload(file){
    var formData = new FormData();
    formData.append('filename', filename);
    formData.append('file', file);
    $.ajax({
        url: '/upload',
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
                    app.changeProgressBar($('#content .progress-bar'),
                        percentCompleted);
                }
            }, false);
            return xhr;
        },
        success: function(data){
            if(data !== undefined){
                if(data.success !== undefined){
                    if(data.success) {
                        app.onFileUploadSuccess(data.message, key);
                    } else {
                        app.displayError(data.message);
                    }
                }
            }
        }
    });
}