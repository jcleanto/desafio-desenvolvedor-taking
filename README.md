# Projeto para administrar alunos, professores e cursos com o frontend em Angular 19 e backend usando o framework Quarkus (Java) 

Desafio de desenvolvedor para processo seletivo da empresa Taking.

Nessa versão estão contemplados os CRUDs de Cursos, Semestres, Disciplinas e Montagem da Grade Curricular (entidade CursoSemestreDisciplina).

No backend foi utilizado o padrão MVC separado por contextos: controller, service, model e repository.

No frontend foi utilizada a biblioteca @angular/material para criar uma interface responsiva e agradável ao usuário (UX).

Obs.: Ainda não foi adicionado o docker e docker-compose, bem como o Keycloak (autenticação).

## Requisitos

- Maven 3.8.1+
- JDK 11+

## Iniciando a aplicação

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

Acesse o Quarkus Swagger UI no endereço http://localhost:8080/q/swagger-ui e o Angular front end no endereço http://localhost:4200
