# SMTransQuímico

SMTransQuímico é um aplicativo projetado para auxiliar na gestão e monitoramento de produtos químicos, especialmente em situações de logística e transporte. O aplicativo permite o cadastro, consulta e gerenciamento de produtos químicos regulamentados por diferentes entidades como ANVISA, Exército e Polícia, além de oferecer funcionalidades de comunicação e monitoramento de usuários.

## Funcionalidades

### Autenticação

- **Tela de Login**: Integrada ao Firebase para autenticação segura. Os usuários podem acessar suas contas usando e-mail ou uma conta Google, garantindo uma autenticação rápida e confiável.
- **Tela de Cadastro**: Permite que os usuários criem uma conta fornecendo nome, telefone, e-mail e senha. As informações são salvas de forma segura no Firebase, garantindo proteção e privacidade dos dados.

### Gerenciamento de Produtos

- **Cadastro de Produtos Químicos**: Adicione e gerencie informações detalhadas sobre produtos químicos, com opções para consultas e atualizações rápidas.
- **Cadastro de Produtos ANVISA**: Registre e consulte produtos regulamentados pela ANVISA, garantindo conformidade e controle eficiente.
- **Cadastro de Produtos Químicos do Exército**: Insira e gerencie dados sobre produtos químicos utilizados pelo exército, com funcionalidades para consultas e atualizações específicas.
- **Cadastro de Produtos Químicos da Polícia**: Registre e controle informações sobre produtos químicos utilizados pela polícia, permitindo consultas e gerenciamento detalhado.
- **Tela de Listagem de Produtos ANVISA**: Exibe todos os produtos cadastrados com opções de busca e filtro para facilitar a localização. Permite visualizar detalhes, editar ou excluir produtos.
- **Tela de Listagem de Produtos do Exército**: Mostra todos os produtos químicos cadastrados para o exército, com busca e filtro, permitindo visualização, edição e exclusão.
- **Tela de Listagem de Produtos da Polícia**: Exibe todos os produtos químicos cadastrados para a polícia, com busca e filtro, e permite visualização, edição e exclusão.

### Comunicação

- **Chat entre Usuários**: Usuários logados no aplicativo podem se comunicar diretamente via chat, permitindo troca de mensagens em tempo real e facilitando a interação.
- **Chatbot**: Facilita a localização de produtos regulamentados pela ANVISA, utilizando o número de registro ou a descrição do produto. O chatbot fornece uma busca rápida e precisa.

### Monitoramento

- **Consulta de Usuários Logados**: Visualize e gerencie os usuários que estão atualmente logados no sistema, permitindo controle eficaz da atividade dos usuários.

### Identificação de Produtos

- **Identificação de Produtos Duplicados**: Se um produto cadastrado na lista da ANVISA tiver o mesmo nome que um produto na lista do exército ou da polícia, será exibido com uma indicação adicional. Produtos relacionados ao exército serão marcados com "EX" e produtos relacionados à polícia com "PF", para facilitar a identificação e evitar confusões.

## Como Usar

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/smtransquimico.git
