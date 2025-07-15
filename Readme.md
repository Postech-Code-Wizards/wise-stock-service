# 📦 Wise Stock Service

Microserviço responsável pelo controle de estoque de produtos no ecossistema WISE. Permite consultar, repor e baixar a quantidade em estoque de forma segura, com validações para evitar estoques negativos.

---

## 🔍 Visão Geral

O **Wise Stock Service** tem como principais responsabilidades:

- Consultar o estoque atual de produtos
- Realizar baixas no estoque (ex: após uma venda)
- Repor itens no estoque (ex: após uma compra ou devolução)
- Garantir que o estoque nunca fique negativo
- Integrar-se com outros microsserviços, como o de pedidos ou produtos

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- PostgreSQL
- Docker & Docker Compose
- JUnit & Mockito

---

## ⚙️ Funcionalidades

| Funcionalidade         | Descrição                                                              |
|------------------------|------------------------------------------------------------------------|
| 📦 Consulta de estoque | Retorna a quantidade disponível de um produto                          |
| ➖ Baixa de estoque    | Subtrai uma quantidade do estoque, com validação contra saldo negativo |
| ➕ Reposição de estoque| Soma uma quantidade ao estoque atual                                   |
| 🔗 Integração          | Pode ser acionado via eventos (mensageria) ou API REST                 |

---

## 📂 Estrutura do Projeto

```
wise-stock-service/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── br/com/wise/stock_service/
│ │ │ ├── application/ 
│ │ │ │ ├── service/ # Camada de serviço
│ │ │ │ └── usecase/ # Casos de uso
│ │ │ ├── domain/ # Entidades de negócios
│ │ │ ├── converter/ # Conversores
│ │ │ ├── gateway/
│ │ │ │ └── database/ # Persistência (JPA)
│ │ │ ├── infrastructure/
│ │ │ │ ├── rest/ # Controladores REST
│ │ │ │ └── configuration/ # Configurações Spring Boot e RabbitMq
│ │ └── resources/
│ │ └── application.properties # Configurações da aplicação
│ └── test/
│ └── ... # Testes unitários
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## ▶️ Como Executar

### 1. Clone o repositório:

```
git clone https://github.com/Postech-Code-Wizards/wise-stock-service.git

cd wise-stock-service
```

### 2. Gere o .jar:

```
mvn clean package -DskipTests
```

### 3. Subindo os containers:

```
docker-compose up --build -d
```

---

## 🧪 Testes

```
mvn test
```

Ou usando Docker:

```
docker exec -it wise-stock-service mvn test
```

---

## 📚 Endpoints Principais

Abaixo estão os principais endpoints expostos pela API REST do Wise Stock Service:

| Método | Endpoint                        | Descrição                             |
|--------|---------------------------------|---------------------------------------|
| GET    | `/api/v1/estoque/verifica/{id}` | Consulta o estoque de um produto      |
| PUT    | `/api/v1/estoque/baixa/{id}`    | Realiza baixa de quantidade do estoque|
| PUT    | `/api/v1/estoque/repor/{id}`    | Realiza reposição de itens no estoque |

---

## 🤝 Contribuindo

1. Fork este repositório

2. Crie uma branch: git checkout -b feat/minha-feature

3. Commit suas alterações: git commit -m 'feat: nova funcionalidade'

4. Push para o fork: git push origin feat/minha-feature

5. Abra um Pull Request explicando a mudança


