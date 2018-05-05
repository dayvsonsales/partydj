var socket = new WebSocket("ws://localhost:8000/");;
var token;
var nome;

var criarSala = function () {
    nome = $("#nome").val();

    if(!nome){
        alert("Necessário informar o nome!");
        return;
    }

    socket.send(`create_room:${nome}`);

};

var entrarSala = function () {
    nome = $("#nome").val();
    token = $("#token-sala").val();

    if(!nome){
        alert("Necessário informar o nome!");
        return;
    }

    if(!token){
        alert("Necessário informar o token!");
        return;
    }

    socket.send(`enter_room:${token}:${nome}`);
};


socket.onopen = function (ev) {
    console.log(ev);
};
socket.onerror = function (ev) {
    alert("Erro ao se conectar ao servidor de Socket! Por favor, inicie e reinicie a aplicação web!");
};

socket.onmessage = function (ev) {
    console.log(ev);
    let message = ev.data;
    let arr = message.split(":");

    let command = arr[0];

    if(command == "create_room"){
        if(arr[1] == "false"){
            token = arr[3];
            $("#secao-inicial").prop("style", "display: none");
            $("#secao-sala").prop("style", "display: ");
            $("#nome-dono").text($("#nome").val());
            $("#numero-token-sala").text(token);
        }else{
            alert("Falha ao criar uma sala!");
        }
    }

    if(command == "enter_room"){
        if(arr[1] == "false"){
            $("#secao-inicial").prop("style", "display: none");
            $("#secao-sala").prop("style", "display: ");
            $("#nome-dono").text(arr[4]);
            $("#numero-token-sala").text(arr[3]);
        }else{
            alert("Sala inexistente!");
        }
    }

    if(command == "receive_message"){

        if(arr[1] == "false"){
            $("#users-count").empty();
            $("#queue-count").empty();

            let _queue_count = arr[6];
            let _users_count = arr[5];
            let protocol = arr[4];
            let _nome = arr[3];
            let _message = arr[2];

            if(_nome != "Sala") {
                $("#chat").append(`<li> <b>${_nome}:</b> ${_message}</li>`);
            }else{
                $("#chat").append(`<li><i>${_message}</i></li>`);
            }

            $("#users-count").append(_users_count);
            $("#queue-count").append(`Há_${queue_count} vídeo(s) na fila!`);
        }else{
            alert("Aconteceu um erro!");
        }
    }

    if(command == "get_video"){
        $("#name-video").empty();
        $("#video").empty();
        if(arr[1] == "true"){
            $("#name-video").val("");
            $("#video").empty();
            $("#video").append("Ainda não temos vídeo para exibir :(");
        }else{
            let videoId = arr[2];
            let thumbNail = arr[3];
            let name = arr[4];
            let time = arr[5];

            $("#name-video").empty();
            $("#name-video").append(`<h3>${name}</h3>`);
            $("#video").val("");

            if(arr[5]){
                $("#video").append(`<iframe width="560" height="315" src="https://www.youtube.com/embed/${videoId}?controls=0&autoplay=1&start=${arr[5]}&disablekb=1&showinfo=0&fs=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>`);
            }else{
                $("#video").append(`<iframe width="560" height="315" src="https://www.youtube.com/embed/${videoId}?controls=0&autoplay=1&disablekb=1&showinfo=0&fs=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>`);
            }
        }
    }

};

var pedirMusica = function (title, thumbnailUrl, videoId) {
    $.magnificPopup.close();

    console.log(videoId);
    console.log(thumbnailUrl);
    console.log(title);

    $.ajax({
        url: "https://www.googleapis.com/youtube/v3/videos",
        type: "GET",
        data: {
            key : 'AIzaSyC0dipLBJBsxKil4F6g-jBXhQPzcQ6EtTQ',
            part: "contentDetails",
            id: videoId
        }
    }).done(function (r){
        console.log(r);
        var duration = r.items[0].contentDetails.duration;
        socket.send(`add_video:${token}:${nome}:${videoId}:${thumbnailUrl}:${duration}:${title}`);
    });

    $("#list-video").empty();
    $("#video-search").val("");

};

$("#enter").keyup(function (e){
        if(e.which == 13){
            let message = $("#enter").val();
            socket.send(`send_message:${token}:${message}:${nome}`);
            $("#enter").val("");
        }
});

$("#video-search").keyup(function (e){
        if(e.which == 13){
            $("#list-video").empty();
            let q = $("#video-search").val();
            console.log(q);
            $.ajax({
                url: 'https://www.googleapis.com/youtube/v3/search',
                type: 'GET',
                data: {
                    'key' : 'AIzaSyC0dipLBJBsxKil4F6g-jBXhQPzcQ6EtTQ',
                    'q' : q,
                    'part' : 'snippet',
                    'type': "video",
                    'maxResults' : 10
                }
            }).done(function (r) {
                r.items.forEach((p, k) => {
                    let v = p.snippet;
                    let videoId = p.id.videoId;
                    var url = v.thumbnails.default.url;
                    url = url.replace(/https:/, "");
                    $("#list-video").append(`<li class="list-group-item"><div class="row"><div class="col-md-4"><img src="${url}"/></div><div class="col-md-4">${v.title}</div><div class="col-md-4"><button class="btn btn-success" onclick="pedirMusica('${v.title}', '${url}', '${videoId}')">+</button></div></div></li>`);
                });
            });
        }
});

$(".open-popup").magnificPopup({
    type: "inline",
    midClick: true,
    closeBtnInside: true,
    callbacks: {
        open: function(){
            $("#list-video").empty();
        }
    }
});
