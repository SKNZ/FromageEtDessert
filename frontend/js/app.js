/**
 * Created by thomasmunoz on 18/04/15.
 */
var result = [
    {
        conf: 1,
        lift: 20,
        x: ["#SW"],
        y: ["Harrison", "Ford", "Chewie"]
    },
    {
        conf: 0.8,
        lift: 22,
        x: ["#SW"],
        y: ["BB8", "R2D2", "C3PO"]
    },
    {
        conf: 1,
        lift: 21,
        x: ["BB"],
        y: ["Bryan", "Cranston", "Walter", "White"]
    }
]

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
            //upload(file);
            display();
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

function display(){
    $('.jumbotron').empty();
    $('.jumbotron').append(
        $('<h1 />')
            .text("Rendu"),
        $('<select />')
            .attr('name', 'outtype')
            .attr('id', 'outtype')
            .append(
            $('<option />')
                .prop('disabled', true)
                .prop('selected', true)
                .text('Selectionner le type d\'export'),
            $('<option />')
                .attr('name', 'table')
                .text('Tableau'),
            $('<option />')
                .attr('name', 'tagcloud')
                .text('Nuage de mot')
        )
    )
}
