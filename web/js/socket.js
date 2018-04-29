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
            let protocol = arr[4];
            let _nome = arr[3];
            let _message = arr[2];


            if(_nome != "Sala") {
                $("#chat").append(`<li> <b>${_nome}:</b> ${_message}</li>`);
            }else{
                $("#chat").append(`<li><i>${_message}</i></li>`);
            }

        }else{
            alert("Aconteceu um erro!");
        }


    }

};

var pedirMusica = function () {
    console.log("aqui");

};

$("#enter").keyup(function (e){
        if(e.which == 13){
            let message = $("#enter").val();
            socket.send(`send_message:${token}:${message}:${nome}`);
            $("#enter").val("");
        }
});

$(".open-popup").magnificPopup({
    type: "inline",
    midClick: true,
    closeBtnInside: true
});