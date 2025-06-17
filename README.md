# Utilização de Tools com SpringAi 
Tools são insumos que podemos fornecer para a IA afim de termos uma resposta mais precisa. Seria munir de mais informações a IA. É como dar um "superpoder" extra para um modelo de IA. O modelo sozinho não consegue acessar informações em tempo real, dados atualizados nem executar ações externas. Então Tools são funções que a IA pode chamar para buscar informações atualizadas ou realizar ações práticas.

Essa consulta extra pode ser chamar uma API, consultar um banco de dados ou algum outro sistema externo.

Após a IA utilizar a TOOL a informação obtida é organizada e apresentada de forma clara e natural, como se a IA tivesse feito utod sozinha.

Abaixo segue uma imagem que demonstra o funcionamento de Tool (imagem da documentação do SpringAi)
![image](https://github.com/user-attachments/assets/ff69a702-397c-4e91-9673-78253100c9a5)

https://docs.spring.io/spring-ai/reference/api/tools.html

Esse projeto visa fornecer informações de investimentos em tempo real com base na minha carteira de investimento e informações em tempo real destes investimentos através de uma API.

Neste projeto foi utilizado o Tool para fornecer as seguintes informações para a IA:
- Fornecer a consulta de minha carteira de investimentos a IA (tais informações estão em base)
- Fornecer para a IA a API da Twelvedata para consultar em tempo real as informações das minhas carteiras de investimento  (https://twelvedata.com/)
