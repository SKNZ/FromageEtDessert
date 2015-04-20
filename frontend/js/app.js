/**
 * Created by thomasmunoz on 18/04/15.
 */
$(document).ready(function(){
    var file;

    getScenarios();
    setInterval(function(){
        getScenarios();
    }, 1000);

    $('#entry').on('change', function(){
        $('#csvfile').remove();
        $('#twitterkeyword').remove();
        $('#twitternumber').remove();

        if($(this).val() == 'csv'){
            if($('#csvfile').length == 0){
                $(this).parent().append(
                    $('<input />')
                        .attr('type', 'file')
                        .attr('id', 'csvfile')
                );
            }
        } else if ($(this).val() == 'twitter' || $(this).val() == 'twitterStream'){
            if($('#twitterkeyword').length == 0 && $('#twitternumber').length == 0){
                $('#entryform').append(
                    $('<input>')
                        .attr('type', 'text')
                        .attr('placeholder', 'Élement de recherche')
                        .attr('name', 'twitterkeyword')
                        .attr('id', 'twitterkeyword'),
                    $('<input>')
                        .attr('type', 'number')
                        .attr('placeholder', "Nombre de tweets")
                        .attr('name', 'twitternumber')
                        .attr('id', 'twitternumber')
                );
            }
        }
    });

    $('.jumbotron').on('change', '#csvfile', function(e){
        file = e.target.files[0];
    });

    $('#sendbutton').on('click', function(){
        var value = $('#entry').val();
        var minconf = $('#minconf').val();

        if(value == 'blackbox'){
            location.href = 'http://unicorn.ovh';
        } else if (value == 'twitter' || value == 'twitterStream'){
            getTwitter((value == 'twitterStream'));
        } else if(value == 'csv'){
            var form = getForm();
            upload(file, form);
        }
    });


    $('table').on('click','a.link', function(e){
        e.preventDefault();

        $.get('/backend/' + $(this).attr('href'), function(data){
            display(data)
        });
    });
});

function getForm(){
    var form = {
        "scenario" : {
        }
    };

    var entry = $('#entry').val();

    if(entry == 'twitter' || entry == 'twitterStream'){
        entry = entry + " " + $('#twitterkeyword').val() + ' ' + (($('#twitternumber').val() === undefined) ? '1000' : $('#twitternumber').val());
    }

    form.scenario.entry = entry;
    form.scenario.process = "apriori-nedseb-c " + $('#minfreq').val() + " " + $('#minconf').val() + " " + $('#minlift').val();

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
        $('.jumbotron #scenariosTable tbody').empty();
        scenarios.scenarios.forEach(function(scenar){
            $('.jumbotron #scenariosTable tbody').append(
                $('<tr />')
                    .append(
                    $('<td />')
                        .text(scenar.entry),
                    $('<td />')
                        .text(scenar.process),
                    $('<td />')
                        .html((scenar.doing) ? scenar.state : '<a href="/scenario/' + scenar.id + '" class="link">Terminé</a> ' +
                        '<a href=/backend/' + scenar.id + '/input.csv>Télécharger csv</a>')
                )
            );
        });
    });
}

function display(response){
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
           if($('#cloud').length != 0){
               $('.jumbotron #cloud').remove();
               $('.jumbotron h2').remove();
           }
           createTab(response);

       } else if($('#outtype').val() == 'tagcloud'){
           if($('.jumbotron table').length != 0){
               $('.jumbotron table').remove();
           }
           //$('.jumbotron').append('<h2>Mot clé ' + response.results[0].x[0] + '</h2>');
           createCloud(response.results);
       }
    });
}

function createTab(data){
    data = data.results;
        $('.jumbotron').append(
            $('<b />')
                .text('Cliquez sur l\'intitulé de la colonne pour la trier'),
            $('<br>'),
            $('<table />')
                .addClass('table table-striped')
                .attr('id', 'resultTable')
                .append(
                $('<thead />')
                    .append(
                    $('<tr />')
                        .append(
                        $('<th />')
                            .text('X'),
                        $('<th />')
                            .text('Y'),
                        $('<th />')
                            .text('Conf'),
                        $('<th />')
                            .text('Lift')
                    )
                ),
                $('<tbody />')
            )
        );
    data.forEach(function(elem){
        $('.jumbotron #resultTable tbody').append(
            $('<tr />').append(
                $('<td />')
                    .text(elem.x[0]),
                $('<td />')
                    .text(elem.y.join(' ')),
                $('<td />')
                    .text(elem.conf),
                $('<td />')
                    .text(elem.lift)
            )
        )
    });

    $('#resultTable').dataTable();
}

function createCloud(data){
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
        color: {start: '#0080FF', end: '#FF0000'}
    };

    $(function () {
        $('.jumbotron #cloud a').tagcloud();
    });
}

function getTwitter(stream){
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
