/**
 * Created by thomasmunoz on 18/04/15.
 */
var result = [
    {
        conf: 1,
        lift: 20,
        x: "#SW",
        y: ["Harrison", "Ford", "Chewie"]
    },
    {
        conf: 0.8,
        lift: 22,
        x: "#SW",
        y: ["BB8", "R2D2", "C3PO", "Ford"]
    },
    {
        conf: 0.8,
        lift: 22,
        x: "#SW",
        y: ["BB8", "Luke", "Skywalker"]
    },
    {
        conf: 1,
        lift: 22,
        x: "#SW",
        y: ["StarWars", "Skywalker", "Ford", "BB8"]
    }
];
var scenarios = [
    {
        id: 1,
        entry: 'Recherche Twitter (#SW)',
        processing: 'MinConf = 0.8',
        state: 'Terminé',
        doing: false
    },
    {
        id: 2,
        entry: 'Fichier CSV',
        processing: 'MinConf = 1',
        state: 'En cours (18%)',
        doing: true
    }
];

$(document).ready(function(){
    var file;
    getScenarios();
    $('#entry').on('change', function(){
        $('#csvfile').remove();
        $('#twitterkeyword').remove();

        if($(this).val() == 'csv'){
            if($('#csvfile').length == 0){
                $(this).parent().append(
                    $('<input />')
                        .attr('type', 'file')
                        .attr('id', 'csvfile')
                );
            }
        } else if ($(this).val() == 'twitter'){
            if($('#twitterkeyword').length == 0){
                $('#entryform').append(
                    $('<input>')
                        .attr('type', 'text')
                        .attr('placeholder', 'Élement de recherche')
                        .attr('name', 'twitterkeyword')
                        .attr('id', 'twitterkeyword')
                );
            }
        }
    });

    $('.jumbotron').on('change', '#csvfile', function(e){
        file = e.target.files[0];
        console.log(file);
    });

    $('#sendbutton').on('click', function(){
        var value = $('#entry').val();
        var minconf = $('#minconf').val();

        if(value == 'blackbox'){
            location.href = 'http://unicorn.ovh';
        } else if (value == 'twitter'){
            getTwitter();
        } else if(value == 'csv'){
            display();
        }
    });


    $('table a').on('click', function(){

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
                        display();
                    } else {
                        $('.jumbotron').append(
                            $('<div />')
                                .addClass('alert alert-danger')
                                .attr('role', 'alert')
                                .text('Une erreur est survenue')
                        );
                    }
                }
            }
        }
    });
}
function getScenarios(){
    /*
        AJAX Call
    */
    scenarios.forEach(function(scenar){
        $('.jumbotron table tbody').append(
            $('<tr />')
                .append(
                $('<td />')
                    .text(scenar.entry),
                $('<td />')
                    .text(scenar.processing),
                $('<td />')
                    .html((scenar.doing) ? scenar.state : '<a href="/scenario/' + scenar.id + '">Terminé</a>')
            )
        );
    });
}

function display(response){
    response = result;

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
                .attr('value', 'table')
                .attr('id', 'table')
                .text('Tableau'),
            $('<option />')
                .attr('id', 'tagcloud')
                .attr('value', 'tagcloud')
                .text('Nuage de mot')
        )
    )

    $('.jumbotron').on('change', '#outtype', function(){
       if($('#outtype').val() == 'table'){

       } else if($('#outtype').val() == 'tagcloud'){
           console.log('coucou');
           $('.jumbotron').append('<h2>Mot clé ' + response[0].x + '</h2>');
           createCloud(response);
       }
    });
}

function createCloud(data){

}

function getTwitter(){
    var keyword = $('.jumbotron #twitterkeyword').val();

    if(keyword == undefined || keyword.length <= 1){
        if($('#twitteralert').length == 0){
            $('.jumbotron').prepend(
                $('<div />')
                    .addClass('alert alert-danger')
                    .attr('role', 'alert')
                    .attr('id', 'twitteralert')
                    .text('Veuillez saisir un mot clé plus long')
            );
        }
        return false;
    }
    var minconf = $('#minconf').val();

    // AJAX call

    $('#sendbutton').remove();
    $('.jumbotron').append(
        $('<h2 />')
            .text('Veuillez patienter pendant la capture de vos tweets'),
        $('<img>')
            .attr('src', 'img/loading.gif')
            .attr('width', 100)
            .attr('height', '100')
    );
    setTimeout(function(){
        display();
    }, 5000);
}