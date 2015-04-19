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
            y: ["BB8", "R2D2", "C3PO", "Ford"]
        },
        {
            conf: 0.8,
            lift: 22,
            x: ["#SW"],
            y: ["BB8", "Luke", "Skywalker"]
        },
        {
            conf: 1,
            lift: 22,
            x: ["#SW"],
            y: ["StarWars", "Skywalker", "Ford", "BB8"]
        }
    ];
$(document).ready(function(){
    var file;

    getScenarios();
    setTimeout(function(){
        getScenarios();
    }, 5000);

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
            var form = getForm();
            upload(file, form);
            display();
        }
    });


    $('table').on('click','a', function(e){
        e.preventDefault();

        console.log($(this).attr('href'));
        //TODO Ajax Call
        display();
    });
});

function getForm(){
    var form = {
        "scenario" : {
        }
    };

    var entry = $('#entry').val();
    if(entry == 'twitter'){
        entry = entry + " " + $('#twitterkeyword').val();
    }

    form.scenario.entry = entry;
    form.scenario.process = "apriori " + $('#minfreq').val() + " " + $('#minconf').val() + " " + $('#minlift').val();

    return form;
}

function upload(file, form){
    var formData = new FormData();
    formData.append('file', file);

    $.post('/backend/scenario/new', JSON.stringify(form), function(data){
       formData.append('id', data.id);
        $.ajax({
            url: '/backend/scenario/upload',
            type: 'POST',
            processData: false,
            contentType: false,
            data: formData,
            success: function(data){
                if(data !== undefined){
                    if(data.success !== undefined){
                        if(data.success) {
                            $.get('/backend/scenario/' + formData.id + '/run');
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
    });
}
function getScenarios(){
    var scenarios;

    $.get('/backend/scenario', function(data){
        scenarios = data;
        $('.jumbotron table tbody').empty();
        scenarios.scenarios.forEach(function(scenar){
            $('.jumbotron table tbody').append(
                $('<tr />')
                    .append(
                    $('<td />')
                        .text(scenar.entry),
                    $('<td />')
                        .text(scenar.process),
                    $('<td />')
                        .html((scenar.doing) ? scenar.state : '<a href="/scenario/' + scenar.id + '">Terminé</a>')
                )
            );
        });
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
           if($('.jumbotron table').length != 0){
               $('.jumbotron table').remove();
           }
           console.log('coucou');
           $('.jumbotron').append('<h2>Mot clé ' + response[0].x[0] + '</h2>');
           createCloud(response);
       }
    });
}

function createCloud(data){
    data = result;
    var wordTab = [];
    var wordFrequency = [];

    data.forEach(function(element){
        element.y.forEach(function(word){
            var position = wordTab.indexOf(word);
            if(position > -1){
                wordFrequency[position]++;
            } else {
                wordTab.push(word);
                wordFrequency.push(1);
            }
        });
    });

    $('.jumbotron').append('<div id="cloud"></div>');

    for(var i = 0; i < wordTab.length; ++i){
        $('.jumbotron #cloud').append(
            $('<a />')
                .attr('href', '#')
                .attr('rel', wordFrequency[i])
                .text(" " + wordTab[i] + " ")
        );
    }

    $.fn.tagcloud.defaults = {
        size: {start: 22, end: 40, unit: 'pt'},
        color: {start: '#cde', end: '#f52'}
    };

    $(function () {
        $('.jumbotron #cloud a').tagcloud();
    });
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

    var form = getForm();

    $.post('/backend/scenario/new', JSON.stringify(form), function(data){
        $.get('/backend/scenario/' + data.id + '/run');
    })
}