**Sistema de Gestão de Biblioteca (Library Management System)**
Este é um sistema de backend robusto desenvolvido em Java para gerenciar o fluxo completo de uma biblioteca, desde o cadastro de acervo até o controle dinâmico de filas de reserva e monitoramento de atrasos.

Funcionalidades Principais
**- Gestão de Acervo e Usuários**
CRUD Completo: Gerenciamento de Livros, Usuários, Empréstimos e Reservas.

- Busca Inteligente: Filtros por título, autor (usando LIKE para buscas parciais) e categorias via Enums.

- Segmentação: Diferenciação de usuários por tipo (Ex: Aluno, Professor).

**Controle de Empréstimos e Reservas**
- Fila FIFO (First-In, First-Out): Sistema de reservas que organiza usuários por ordem de chegada através de timestamps.

- Notificação Automática: Ao devolver um livro, o sistema verifica automaticamente se há alguém na fila e altera o status do livro para RESERVADO.

- Monitoramento de Atrasos: Relatório dinâmico que calcula dias de atraso comparando a data atual com a data prevista de devolução.

**Consultas Avançadas (HQL)**
- Histórico completo de empréstimos por usuário.

- Visualização da fila de espera de um livro específico.

- Filtros multicritério para administração do acervo.

**Tecnologias Utilizadas**
- Java (JDK 17+): Linguagem core do projeto.

- Hibernate: Framework ORM para mapeamento de entidades e persistência.

- DBeaver: Banco de dados relacional para armazenamento seguro.

- HQL (Hibernate Query Language): Para consultas otimizadas e personalizadas.

- Maven: Gestão de dependências e build.

**Estrutura do Projeto (Padrão Service-DAO)**
O projeto segue uma arquitetura em camadas para facilitar a manutenção e escalabilidade:

- Entities: Classes POJO mapeadas com anotações JPA/Hibernate.

- DAO (Data Access Object): Responsável por todas as operações de banco de dados e consultas HQL.

- Services: Contém as regras de negócio (validações de datas, cálculos de atraso, lógica de reserva).

- App/Main: Interface CLI (Command Line Interface) para interação com o usuário.

**Como Executar o Projeto**
- Clone o repositório:
git clone https://github.com/seu-usuario/nome-do-repositorio.git

- Configure o Banco de Dados:

  - Crie um banco de dados MySQL chamado biblioteca_db.

  - Atualize o arquivo src/main/resources/hibernate.cfg.xml com seu usuário e senha.

- Build e Run:

Importe como projeto Maven na sua IDE (Eclipse/IntelliJ).

Execute a classe BibliotecaApp.java.

**Funcionalidades: **
<img width="475" height="389" alt="image" src="https://github.com/user-attachments/assets/ada3ecdc-21c9-4b3c-b546-b1465c2f9bd3" />
