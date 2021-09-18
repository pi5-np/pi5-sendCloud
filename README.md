# pi5-sendCloud
Microsserviço de envio de arquivos para o Backblaze - Newton Paiva

### Microsserviço de Envio de Arquivo da CLOUD utilizando Kotlin + Spring Boot + Backblaze (CLOUD)

Method: POST
-- URL local para envio de arquivos da CLOUD : localhost:8080/api/send
Obs.: Caso o Spring Boot esteja rodando em outra porta, substitua o 8080 pela porta escolhida

##### Exemplo de input para envio de arquivo para a CLOUD
{
    "username":"usuario",
    "message":"Message TEST -- 01",
    "name":"Arquivo de TEST"
}
-- username é o nome do usuário logado
-- message é o texto que foi analisado pelo microsserviço do OCR
-- name é o nome no qual o usuário deseja colocar para salvar seu arquivo

##### O microsserviço irá acessar Backblaze e consultar se o BUCKET já existe (lembrando que o nome do BUCKET é o username que é unico), caso o BUCKET exista ele irá transformar o texto em um arquivo .txt salvo na pasta tmp e irá fazer o upload do arquivo para a CLOUD, caso o BUCKET não exista ele irá criar um BUCKET com o nome sendo o username que é único e depois irá realizar o processo de envio do arquivo .txt.
 
### O microsseviço está usando a CLOUD Backblaze (https://www.backblaze.com/).
