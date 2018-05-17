# Projeto de redes 1: Party.dj

Party.dj é uma aplicação baseada no plug.dj, a qual consiste em compartilhar e descobrir vídeos entre amigos.  
Utilizando a API do YouTube, é possível criar uma sala, convidar seus amigos passando-os o token desta sala e usufruir de uma experiência de compartilhamento de vídeos, em especial músicas.

O projeto foi estruturado com base no paradigma de orientação a objetos, mapeando o sistema em classes: Server, Room, Video, Protocol, User. 

A classe Server é a responsável por gerenciar toda parte da lógica, desde a primeira conexão até o encerramento, tomando como auxilio a classe Protocol, onde processa as mensagens que chegam e passam ao servidor. A partir da mensagem, o servidor escolhe as ações a serem tomadas, como por exemplo, criar uma sala.  

Para tentar garantir a mesma experiência para todos, a classe Room possui uma Thread que monitora o vídeo em execução e sincroniza aos usuários que chegam na sala (mesmo se ele já estiver começado). 

## Como rodar

Este é um projeto Maven. O maven cuidará de importar as dependência necessárias e configurar o ambiente para o Java 8. 
As dependências são Spark (para servir a aplicação) e WebSocket (para o websocket).  
Deve ser executada a classe App.java, onde iniciará um servidor na porta 4000 para a web (http://localhost:4000) e um servidor na porta 8000 (http://localhost:8000) para o socket.  

O projeto foi desenvolvido na IDE IDEA Ultimate.  

Obs.: caso após executar e encerrar e depois executar novamente poderá haver um erro (as vezes o garbage collector não foi executado ainda e a porta ainda não está livre).  

## Funcionalidades

1. Criar Sala. 
2. Entrar na Sala. 
3. Enviar Mensagem. 
4. Adicionar vídeo a fila da sala. 
5. Sincronizar os vídeos sendo exibidos entre os participantes daquela sala. 
6. Ver os vídeos na fila.      

## Principais dificuldades encontradas

No início do desenvolvimento, estava pensando em utilizar sockets do Java (raw socket) e combiná-los à web. Mas esbarrei em uma restrição imposta pelos browsers. Há uma RFC que propõe permitir a comunicação, mas esta está muito nova e as implementações que testei não estavam funcionando.  
Também é possível se comunicar utilizando uma espécie de ponte: websockets que se comunica a um servidor como node.js e internamente se comunica a uma aplicação java que utiliza sockets, mas esta abordagem parecia ser mais complicada do que utilizar websockets diretamente, uma vez que existe implementação para Java de WebSockets.  
Sendo assim, optei por utilizar WebSockets e manter minha ideia inicial de combinar à web.

## Protocolo

Foi definido um protocolo baseado em mensagens com o seguinte formato:  

1. Mensagens enviadas pelo servidor  

`<ação> : <erro> : <parametros>`  

Onde será recebida pelo cliente e receberá o tratamento específico, o parâmetro erro é um booleano (0 ou 1), indicando se houve erro ou não.  

2. Mensagens enviadas pelo cliente  

`<ação> : <parametros> ` 

Onde será recebida pelo cliente e receberá o tratamento específico.

Em ambos casos, o separador será sempre `:` para os parâmetros.  

Nesta primeira implementação, está vedada a utilização de mensagens ou nome que contenham o caracter `:` em seu conteúdo, sendo assim, é um caracter reservado ao protocolo.  

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
  
  `list_video:<erro>:<json em formato de texto ou mensagem de erro>:<token>`
  
 * **GET_VIDEO:** envia o vídeo a ser exibido nos clientes
  
  `get_video:<erro>:<videoId>:<videoThumb>:<videoNome>`
 
 ## O que poderia ter sido feito a mais
 
Poderia ter melhorado a experiência do usuário e ter adicionado mais mensagens no protocolo, principalmente no tocante à administração da sala (poder passar vídeos, gostei ou não gostei, etc). Poderia ter sido adicionado tratamento ao `:` quando o mesmo fosse conteúdo da mensagem passada ao servidor.


## Protocolo usado na camada de transporte

Como foi utilizado uma implementação padrão do websocket, é utilizad o TCP na camada de transporte.  
