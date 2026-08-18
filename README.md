# 🚀 Sistema de Gestão de Usuários - Residência de Software

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)

Um sistema *Full-Stack* robusto e moderno para gerenciamento de contas, contando com autenticação via token JWT, proteção de rotas, controle de acessos (Admin / User) e uma interface responsiva construída puramente com tecnologias nativas da web.

---

## ✨ Funcionalidades

- **🔐 Autenticação Segura:** Login e Registro com criptografia de senha (`BCrypt`) e geração de token JWT.
- **🛡️ Controle de Perfil (RBAC):** 
  - **ADMIN:** Tem acesso total ao CRUD (listar todos, criar, editar e excluir qualquer usuário).
  - **USER:** Tem acesso restrito apenas à visualização e edição de seus próprios dados.
- **📝 Gestão de Usuários (CRUD):** Criação, leitura, atualização e exclusão de contas.
- **🎨 UI/UX Moderna:** Interface limpa no estilo *SaaS*, responsiva e sem dependência de bibliotecas externas (Vanilla JS/CSS).

---

## 🛠️ Tecnologias Utilizadas

O projeto foi dividido conceitualmente em Backend (API Rest) e Frontend (Cliente).

### Backend
- **Linguagem:** Java 17+
- **Framework:** Spring Boot 3
- **Segurança:** Spring Security com JWT (JSON Web Token)
- **Banco de Dados:** PostgreSQL
- **Migrações:** Flyway (Scripts de criação e alteração de tabelas)
- **Documentação:** Swagger / OpenAPI 3

### Frontend
- **Linguagem Base:** HTML5 Semântico
- **Estilização:** CSS3 puro (Variáveis nativas, Flexbox, CSS Grid)
- **Comportamento/Lógica:** JavaScript Vanilla (ES6+) consumindo a API nativamente através do `fetch`.

---

## 📂 Estrutura da Aplicação

O repositório é organizado separando a API Spring Boot e os arquivos estáticos do Frontend:

```text
📦 projeto-residencia
 ┣ 📂 backend/                     # API em Spring Boot[cite: 1]
 ┃ ┣ 📂 src/main/java/com/crud_/residencia/
 ┃ ┃ ┣ 📂 controller/              # Endpoints (AuthenticationController, UsuarioController)[cite: 1]
 ┃ ┃ ┣ 📂 domain/                  # Entidades JPA (Usuario)[cite: 1]
 ┃ ┃ ┣ 📂 dtos/                    # Objetos de Transferência (RegisterDTO, LoginResponseDTO, etc.)[cite: 1]
 ┃ ┃ ┣ 📂 enums/                   # Enums (UsuarioRole)[cite: 1]
 ┃ ┃ ┣ 📂 repositories/            # Interfaces Spring Data (UsuarioRepository)[cite: 1]
 ┃ ┃ ┣ 📂 security/                # Configurações de CORS, Filtros e JWT[cite: 1]
 ┃ ┃ ┗ 📂 services/                # Regras de Negócio (TokenService, UsuarioService)[cite: 1]
 ┃ ┣ 📂 src/main/resources/
 ┃ ┃ ┣ 📂 db/migration/            # Scripts Flyway (V1, V2, V3, V4...)[cite: 1]
 ┃ ┃ ┗ 📜 application.properties   # Configurações de banco, porta e JWT
