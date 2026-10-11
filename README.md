# Baozi Store - API REST

API REST de uma loja de baozi (pãozinho chinês recheado), feita para a disciplina **Desenvolvimento Web Back-End** da **Uninter**.

A API permite cadastrar **clientes**, **produtos** e **pedidos**. Cada pedido liga um cliente a um produto e guarda a quantidade comprada. Todas as respostas são em JSON e o projeto segue o padrão MVC do Spring.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
  - Spring Web (API REST / MVC)
  - Spring Data JPA (persistência com Hibernate)
- Banco de dados H2 (em arquivo, na pasta `./data`)
- Maven Wrapper (`mvnw.cmd`), que baixa o Maven sozinho

## Estrutura de pacotes

```
src/main/java/br/com/uninter/baozistore
├── BaoziStoreApplication.java   -> classe principal (inicia a aplicação)
├── model/                       -> entidades JPA (tabelas do banco)
│   ├── Cliente.java
│   ├── Produto.java
│   └── Pedido.java
├── repository/                  -> acesso ao banco (JpaRepository)
│   ├── ClienteRepository.java
│   ├── ProdutoRepository.java
│   └── PedidoRepository.java
├── controller/                  -> endpoints REST (@RestController)
│   ├── ClienteController.java
│   ├── ProdutoController.java
│   └── PedidoController.java
└── dto/
    └── PedidoRequest.java       -> corpo do POST/PUT de pedido (ids + quantidade)
```

## Entidades e campos

Relacionamentos: **um Cliente faz N Pedidos** e **um Produto é vendido em N Pedidos** (`@ManyToOne` em `Pedido`).

### Cliente (tabela `cliente`)

| Campo          | Tipo      | Coluna          | Regras                                   |
|----------------|-----------|-----------------|------------------------------------------|
| `id`           | Long      | `id`            | Chave primária, auto incremento          |
| `nome`         | String    | `nome`          | Obrigatório, até 100 caracteres          |
| `clienteDesde` | LocalDate | `cliente_desde` | Obrigatório (se não vier no POST, usa a data de hoje) |

### Produto (tabela `produto`)

| Campo     | Tipo       | Coluna    | Regras                            |
|-----------|------------|-----------|-----------------------------------|
| `id`      | Long       | `id`      | Chave primária, auto incremento   |
| `nome`    | String     | `nome`    | Obrigatório, até 100 caracteres   |
| `preco`   | BigDecimal | `preco`   | Obrigatório, `numeric(10,2)`      |
| `estoque` | Boolean    | `estoque` | Obrigatório (true = disponível)   |

### Pedido (tabela `pedido`)

| Campo        | Tipo    | Coluna       | Regras                                  |
|--------------|---------|--------------|-----------------------------------------|
| `id`         | Long    | `id`         | Chave primária, auto incremento         |
| `cliente`    | Cliente | `cliente_id` | Chave estrangeira para `cliente`, obrigatória |
| `produto`    | Produto | `produto_id` | Chave estrangeira para `produto`, obrigatória |
| `quantidade` | Integer | `quantidade` | Obrigatória, maior que zero             |

## Endpoints

Base: `http://localhost:8080`

| Método | Rota              | Descrição                    | Respostas                    |
|--------|-------------------|------------------------------|------------------------------|
| POST   | `/clientes`       | Cria um cliente              | 201                          |
| GET    | `/clientes`       | Lista todos os clientes      | 200                          |
| GET    | `/clientes/{id}`  | Busca um cliente pelo id     | 200, 404                     |
| PUT    | `/clientes/{id}`  | Atualiza um cliente          | 200, 404                     |
| DELETE | `/clientes/{id}`  | Remove um cliente            | 204, 404, 409 (tem pedidos)  |
| POST   | `/produtos`       | Cria um produto              | 201                          |
| GET    | `/produtos`       | Lista todos os produtos      | 200                          |
| GET    | `/produtos/{id}`  | Busca um produto pelo id     | 200, 404                     |
| PUT    | `/produtos/{id}`  | Atualiza um produto          | 200, 404                     |
| DELETE | `/produtos/{id}`  | Remove um produto            | 204, 404, 409 (tem pedidos)  |
| POST   | `/pedidos`        | Cria um pedido               | 201, 400                     |
| GET    | `/pedidos`        | Lista todos os pedidos       | 200                          |
| GET    | `/pedidos/{id}`   | Busca um pedido pelo id      | 200, 404                     |
| PUT    | `/pedidos/{id}`   | Atualiza um pedido           | 200, 400, 404                |
| DELETE | `/pedidos/{id}`   | Remove um pedido             | 204, 404                     |

Os erros 400 e 409 retornam um JSON no formato `{"erro": "mensagem"}`.

## Como rodar

Pré-requisito: **Java 17** instalado. O Maven não precisa ser instalado, porque o wrapper baixa tudo na primeira execução.

No PowerShell, dentro da pasta do projeto:

```powershell
.\run.bat
```

A API sobe em `http://localhost:8080`. Para parar, use `Ctrl + C`.

Para gerar o `.jar` sem rodar:

```powershell
.\mvnw.cmd clean package -DskipTests
```

## Console do H2

Com a aplicação rodando, acesse **http://localhost:8080/h2-console** e preencha:

| Campo     | Valor                             |
|-----------|-----------------------------------|
| JDBC URL  | `jdbc:h2:file:./data/baozi_store` |
| User Name | `sa`                              |
| Password  | *(em branco)*                     |

Os dados ficam salvos na pasta `data/` e continuam lá depois que a aplicação é reiniciada. Para zerar o banco, pare a aplicação e apague essa pasta.

## Exemplos de JSON

### Criar cliente: `POST /clientes`

```json
{
  "nome": "ErickRU_AQUI",
  "clienteDesde": "2026-10-11"
}
```

Resposta (201):

```json
{
  "id" : 1,
  "nome" : "ErickRU_AQUI",
  "clienteDesde" : "2026-10-11"
}
```

### Criar produto: `POST /produtos`

```json
{
  "nome": "Baozi Tradicional de Porco",
  "preco": 8.50,
  "estoque": true
}
```

### Criar pedido: `POST /pedidos`

```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 6
}
```

Resposta (201), com o cliente e o produto completos:

```json
{
  "id" : 1,
  "cliente" : {
    "id" : 1,
    "nome" : "ErickRU_AQUI",
    "clienteDesde" : "2026-10-11"
  },
  "produto" : {
    "id" : 1,
    "nome" : "Baozi Tradicional de Porco",
    "preco" : 8.50,
    "estoque" : true
  },
  "quantidade" : 6
}
```

### Exemplos de erro

Pedido com cliente inexistente (400):

```json
{
  "erro" : "Cliente não encontrado: 999"
}
```

Excluir um cliente que ainda tem pedidos (409):

```json
{
  "erro" : "Cliente possui pedidos vinculados e não pode ser excluído"
}
```

## Postman

O arquivo `baozi-store.postman_collection.json` (na raiz) tem todas as requisições, já na ordem certa para testar. Para usar, abra o Postman e vá em **Import**. A variável `{{baseUrl}}` já vem com o valor `http://localhost:8080`.
