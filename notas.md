## Arquitetura 
[ ] Refatorar para 3 MicroServiços
- Admin
- Order
- Customer

[ ] Cada micro serviço ter o seu banco de dados

[ ] Usar pelo menos 1 banco SQL e 1 banco NoSQL

[ ] Serviços devem se comunicar entre si (HTTP direto)

## Teste e qualidade
[ ] Cada MicroServiço deve ter testes unitários

[ ] Pelo menos um fluxo de testes deve usar BDD

[ ] Cobertura de testes de 80% 

[ ] Validar build + qualidade via SonarQube (ou similar) no PR com mínimo 70% de coverage.

## Repositórios e CI/CD
[ ] Repositório separados por microserviço

[ ] Branch main/master/develop protegida

[ ] PR obrigatório para main/master/develop

[ ] No merge, o deploy de todos os microserviços deve acontecer (cada repo com CI/CD funcionando)

## Documentação
[ ] Ajustar README de cada projeto