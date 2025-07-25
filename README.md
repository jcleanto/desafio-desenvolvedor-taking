# Projeto para administrar alunos, professores e cursos com o frontend em Angular 19 e backend usando o framework Quarkus (Java) 

Desafio de desenvolvedor para processo seletivo da empresa Taking.

Nessa versão estão contemplados os CRUDs de Cursos, Semestres, Disciplinas e Montagem da Grade Curricular (entidade CursoSemestreDisciplina).

No backend foi utilizado o padrão MVC separado por contextos: controller, service, model e repository.

No frontend foi utilizada a biblioteca ```@angular/material``` para criar uma interface responsiva e agradável ao usuário (UX).

## Suposições

Nessa versão existem dois tipos de usuários: administrador (role: admin) e aluno (role: aluno).

Foram criados 2 usuários no Keycloak: login: administrador senha: 123 | login: aluno senha: 123

O administrador possui acesso completo aos Cruds e também acesso a Árvore Curricular (que é a tela de visualização das Grades Curriculares com filtro por Curso).

O aluno possui acesso apenas a Árvore Curricular de qualquer curso.

As telas de CRUD para Users do Keycloak foram disponibilizadas.

Para atender requisitos de UX:
  - os Cruds foram todos feitos utilizando janelas modais, para criação e atualização de entidades
  - as ações de deletar possuem um diálogo de confirmação
  - foram tratadas as mensagens de erro nas requisições ao backend, mostrando mensagens amigáveis ao usuário

Obs.: A API Rest está com segurança agora, e o frontend também está mandando corretamente o token de segurança, publicado 15/07/2025 as 19:08
Obs.: As telas de CRUD para Users do Keycloak foram disponibilizadas, publicado 17/07/2025

## Requisitos

- Maven 3.8.1+
- JDK 17+
- Angular 19+

## Iniciando a aplicação localmente

Build do projeto Quarkus (backend-app):
```bash
$ cd backend-app/
$ ./mvnw quarkus:dev
```

Build do projeto Angular (frontend-app):
```
$ cd frontend-app/
$ npm install
$ ng serve
```

Obs.: No painel de administração do Keycloak (rodando na url: http://localhost:8188) será necessário fazer a criação de um realm chamado: ```desafio-desenvolvedor-taking```, usando o arquivo de importação na pasta ```./backend-app/src/main/resources/desafio-desenvolvedor-taking-realm.json```, para acessar com os usuários: ```login: administrador senha: 123``` | ```login: aluno senha: 123```, já com seus roles associados (admin e aluno).

Acesse o Quarkus Swagger UI no endereço http://localhost:8080/q/swagger-ui e o Angular front end no endereço http://localhost:4200

## Iniciando a aplicação com Docker

Build do projeto Quarkus (backend-app):
```bash
$ cd backend-app/
$ ./mvnw clean package
```

Build do projeto Angular (frontend-app):
```
$ cd frontend-app/
$ npm install
$ ng build
```

Iniciando o Docker:
```
$ cd ..
$ docker-compose -f ./docker-compose.yml up --build
```

Acesse o Quarkus Swagger UI no endereço http://localhost:8080/q/swagger-ui e o Angular front end no endereço http://localhost

