# Projeto de redes 1: Party.dj

Party.dj é uma aplicação baseada no plug.dj, a qual consiste em compartilhar e descobrir músicas entre amigos.  
Utilizando a API do YouTube, é possível criar uma sala, convidar seus amigos passando-os o token desta sala e usufruir de uma experiência de compartilhamento de vídeos, em especial músicas.  

## Funcionalidades

1. Criar Sala. 
2. Entrar na Sala. 
3. Enviar Mensagem. 
4. Adicionar vídeo a fila da sala. 
5. Sincronizar os vídeos sendo exibidos entre os participantes daquela sala. 

## Principais dificuldades encontradas

No início do desenvolvimento, estava pensando em utilizar sockets do Java (raw socket) e combiná-los à web. Mas esbarrei em uma restrição imposta pelos browsers. Há uma RFC que propõe permitir a comunicação, mas esta está muito nova e as implementações que testei não estavam funcionando.  
Também é possível se comunicar utilizando uma espécie de ponte: websockets que se comunica a um servidor como node.js e internamente se comunica a uma aplicação java que utiliza sockets, mas esta abordagem parecia ser mais complicada do que utilizar websockets diretamente, uma vez que existe implementação para Java de WebSockets.  
Sendo assim, optei por utilizar WebSockets e manter minha ideia inicial de combinar à web.

## Protocolo

Foi definido um protocolo baseado em mensagens com o seguinte formato:  

1. Mensagens enviadas pelo servidor  

`<ação> : <erro> : <parametros>`  

Onde será recebida pelo cliente e receberá o tratamento específico, o parâmetro erro é um booleano, indicando se houve erro ou não.  

2. Mensagens enviadas pelo cliente  

`<ação> : <parametros> ` 

Onde será recebida pelo cliente e receberá o tratamento específico.

## Ações

    ENTER_ROOM,  -> Entrar em uma sala
    CREATE_ROOM,  -> Criar uma sala
    SEND_MESSAGE,  -> Enviar uma mensagem
    RECEIVE_MESSAGE,  -> Receber uma mensagem
    ADD_VIDEO,  -> Adicionar vídeo a fila
    LIST_VIDEOS,  -> Listar os vídeos
    GET_VIDEO,  -> Receber o vídeo a ser exibido
    UNKNOW -> Mensagem não reconhecida
   
Onde, 

1. Para o cliente. 

  * **ENTER_ROOM**: deve ser passado o nome do cliente e o token da sala
  
  `enter_room:<nome>:<token>`
  
  * **CREATE_ROOM**: deve ser passado o nome do cliente, o servidor enviará um retorno com o token gerado e redicionará o cliente para a sala.
  
  `create_room:<nome>`
  
  * **SEND_MESSAGE**: será passado a mensagem, o nome do cliente, o token da sala
  
  `send_message:<token>:<mensagem>:<nome>`
  
  * **RECEIVE_MESSAGE**: esta ação é exclusiva para envio por parte do servidor, onde o cliente irá tratá-la.
  
  * **ADD_VIDEO**: deve ser passado o token da sala, o nome do cliente, o id do vídeo, o thumbnail dele, a duração e seu título.
  
  `add_video:<token>:<nome>:<videoId>:<videoThumb>:<videoDuracao>:<videoNome>`
  
  * **LIST_VIDEOS**: deve ser passado o token da sala, será retornado a lista dos 5 primeiros vídeos na fila (se houver).  
  
  `list_video:<token>`
  
  * **GET_VIDEO**: aqui será tratado o vídeo que irá ser exibido. O servidor cuida do envio desta mensagem.  
  
2. Para o servidor.  

 * **ENTER_ROOM:** quando recebido esta mensagem do cliente, o servidor verifica se o token é válido e qual sala o pertence, então adiciona o usuário a sala correspondente, se houver, e verifica se há vídeo sendo exibido, para manter uma sincronia e usuário não ficar esperando pelo próximo vídeo a ser exibido.  
  É retornado ao cliente,
  
  `enter_room:<erro>:<mensagem>:<token>`
  
  que o redireciona para a sala.
  
 * **CREATE_ROOM:** o servidor enviará um retorno com o token gerado e redicionará o cliente para a sala. Internamente, uma sala é criada, uma nova thread é iniciada, para organizar a fila de vídeos a serem exibidos.  
  
  `create_room:<erro>:<mensagem>:<token>`
  
 * **SEND_MESSAGE:** o servidor recebe este parâmetro e faz um broadcast para todos os usuários na sala, a mensagem a ser enviada para o cliente é uma receive_message.  
  
 * **RECEIVE_MESSAGE:** esta ação é exclusiva para envio por parte do servidor, onde o cliente irá tratá-la.
  
  `receive_message:<erro>:<mensagem>:<quem enviou>:<tipo>:<tamanho de usuarios na sala>:<contagem de videos na fila>`
  
 * **ADD_VIDEO:** o servidor recebe a mensagem do cliente e envia uma confirmação. Uma receive_message também é enviada, para comunicar a todos a inserção do vídeo. Internamente, adiciona o vídeo a fila.  
  
  `add_video:<erro>:<mensagem>:<token>`
  
 * **LIST_VIDEOS:** o servidor retorna um json com as informações dos vídeos que estão na fila 
  
  `list_video:<erro>:<json ou mensagem de erro>:<token>`
  
 * **GET_VIDEO:** envia o vídeo a ser exibido nos clientes
  
  `get_video:<erro>:<videoId>:<videoThumb>:<videoNome>`
  
