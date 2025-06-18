Este projeto demonstra a implementação dos princípios **SOLID** e **Clean Architecture** em uma aplicação Spring Boot que consome a API do TMDB.

A arquitetura é organizada em camadas distintas com responsabilidades claras:

* **Domain**: Contém as regras de negócio e entidades centrais.

* **Application**: Implementa casos de uso da aplicação.

* **Infrastructure**:  Fornece implementações concretas para interfaces definidas no domínio.

* **Presentation:** Gerencia a interface com o usuário (API REST).

Com esta arquitetura, o sistema é flexível, testável e fácil de manter, permitindo modificações e extensões sem comprometer a integridade do sistema como um todo.


------
Aplicação dos Princípios SOLID

**S - Princípio da Responsabilidade Única (Single Responsibility Principle)**

Cada classe tem apenas uma responsabilidade. Por exemplo, o **MovieService** trata apenas da lógica de negócios relacionada a filmes, enquanto o **MovieRepository** gerencia apenas o acesso aos dados.
O **MovieMapper** é responsável exclusivamente pela conversão entre diferentes representações de objetos.

**O - Princípio Aberto/Fechado (Open/Closed Principle)**

O design permite extensões sem modificar o código existente. Por exemplo, para adicionar novos recursos, como uma lista de filmes para assistir, não precisamos alterar as classes existentes.
A arquitetura de adapters permite que novas fontes de dados ou serviços externos sejam adicionados sem modificar a lógica central.

**L - Princípio da Substituição de Liskov (Liskov Substitution Principle)**

Os componentes são projetados de forma que implementações concretas possam ser substituídas sem afetar o comportamento do sistema.
Por exemplo, podemos substituir a implementação do **MovieRepository** para usar outro banco de dados ou outra API de filmes sem impactar o resto do sistema.

**I - Princípio da Segregação de Interface (Interface Segregation Principle)**

As interfaces são específicas para cada cliente, em vez de monolíticas. Por exemplo, o **MovieRepository** define apenas os métodos necessários para operações relacionadas a filmes.
O cliente OpenFeign (TmdbClient) declara apenas os endpoints específicos que precisa usar.

**D - Princípio da Inversão de Dependência (Dependency Inversion Principle)**

As camadas de alto nível (como services) não dependem de camadas de baixo nível (como repositórios), mas ambas dependem de abstrações.
A interface MovieRepository é definida no domínio (alto nível) e implementada na infraestrutura (baixo nível).
A injeção de dependências é usada em todo o projeto para fornecer as implementações concretas.