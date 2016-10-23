
Camel é essencialmente uma routing-engine
Camel segue boas práticas através do Padrões de Integração
Devemos criar um rota através do RouteBuilder
A Camel DSL é utilizada para configurar a rota de alto nível
Os métodos from(..) e to(..) definem os enpoints
Através da Camel Expression Language podemos acessar a mensagem na rota



Apache Camel, como framework de integração, ajuda a diminuir a complexidade e o impacto dessas integrações. Com um framework de integração, 
seguimos boas práticas que foram identificadas e descritas nos padrões de integração.
Em vez de escrever código de integração na mão, usamos componentes para isso, que podemos facilmente configurar com o Camel. 
Essas configurações são feitas na rota (routing engine). Ou seja, o Camel não implementa os padrões como SOAP e WSDL e 
apenas configura o componente que trabalha com isso.
O desenvolvedor principal do Camel, Claus Ibsen, descreveu o Camel da seguinte maneira:
Apache Camel é um framework Java de código aberto que tenta deixar a integração mais simples e acessível para todos os desenvolvedores. 

Ele faz isso através de:

Implementações concretas dos padrões de integração (EIP)
Conectividade com uma grande variedade de protocolos e APIs
Uso de uma Domain Specific Languages (DSLs) para amarrar os EIPs e protocolos
E não podemos esquecer que, segundo os autores do framework, Camel significa: Concise Application Message Exchange Language. 
Essa linguagem é a Camel DSL!



Para gerar a id, o Camel usa o nome da máquina na rede concatenado com um seed.
Em casos raros pode ser preciso personalizar a geração dessa id. Isso pode ser feito a partir do CamelContext, passando um gerador propriamente 
implementado:

context.setUuidGenerator(new MeuGeradorPersonalizado());



