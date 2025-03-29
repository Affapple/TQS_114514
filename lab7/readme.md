```ts
type Cenario {
    vus: numero, // Representa o numero de utilizadores a mandar requests ao mesmo tempo,
    iterations: numero, // numero de pacotes a enviar, dividido pelos vus
    duration: "stringEscaladeTempo", //inves de iteraçoes, envias pacotes ao longo de Xsegundos,
    target: numero, // começa com o numero atual de vus e durante a duraçao vai ate target vus,
    rate: numero, // numero de pacotese por segundo
}
options = {
    stages: Cenario[]
    OU
    Cenario,
}
```

# Exercicios

## Exercicio 7.1:

![Imagem_10vus_10iterations](./K6_10vues_10iters.png)
Foram feitos 30 pacotes http_reqs aos quais 0 falharam
avg=176.7ms min=11.75ms med=166.57ms max=453.29ms

## Exercicio 7.2:

```

  █ TOTAL RESULTS

    HTTP
    http_req_duration.......................................................: avg=136.71ms min=4.23ms med=112.29ms max=73
7.39ms p(90)=265.01ms p(95)=328.62ms
      { expected_response:true }............................................: avg=136.71ms min=4.23ms med=112.29ms max=73
7.39ms p(90)=265.01ms p(95)=328.62ms
    http_req_failed.........................................................: 0.00%  0 out of 2227
    http_reqs...............................................................: 2227   110.860143/s

    EXECUTION
    iteration_duration......................................................: avg=136.93ms min=4.39ms med=112.53ms max=73
7.57ms p(90)=265.23ms p(95)=328.8ms
    iterations..............................................................: 2227   110.860143/s
    vus.....................................................................: 1      min=1         max=20
    vus_max.................................................................: 20     min=20        max=20
```

a)
1 - avg=136.71ms min=4.23ms med=112.29ms max=73
2 - Foram feitos 2227 requests
3 - Nao falhou nenhum request

f) Neste cenario houve um aumento de falhas por segundo, assim como aumentou o tempo de resposta etc. O servidor ficou sobrecarregado, fazendo com que os thresholds fossem ultrapassados, falhando entao nestes.

g) O que falhou mais foi o check de status === 200

## Exercicio 7.3

c) As metricas que mais afetam são First Contentful Pain, ou seja, o tempo entre a pagina abrir e qualquer conteudo ser renderizado e Largest Contentful Paint, que é o tmepo de renderização do conteudo com o maior tamanho da pagina. O que afeta mais este tempo é o tamanho das "dependencias", e das dependencias _nao usadas_, do site, ou seja, scripts javascript, o Lighthouse recomenda a comprimir estes ficheiros disponibilizados para reduzir o tempo de envio pela rede.

Para fazer o site mais acessivel, segundo as sugestoes do website, alteraria o contraste das cores do texto, pois links sao pintadas de azul claro que dificulta a leitura snedo o fundo da pagina branca. E segundo o site, um link nao tem descrição adequada do seu destino.

d e e) O resultado deu exatamente igual, mas houve uma diferença, deu para escolher modo desktop ou mobile, no modo desktop, a performance já deu quase otima, enquanto que no mobile deu 82%, o resultado foi pior porque ele teve em consideração questoes como a quantidade de dados moveis os clientes poderão gastar e sendo que o dispositivos moveis sao mais pequenos, estes nao necessitam de imagens com tanta resolução como dispositivos com ecrãs maiores

f) Porque a acessibilidade é uma questão muito importante a aser analisada, dado que pessoas com diversas dificuldades diferentes poderão utilizar os websites, como pessoas daltonicas, cegas, etc. e, na criação de interfaces, estas devem ser tambem tomadas em consideração. Quanto a performance, é impportante tambem dado que dispositivos mais fracos e/ou com velocidades de rede inferiores são muito comuns, entao faz todo o sentido focar-se nesse aspeto, o que tambem reduzirá os gastos energéticos.

Best pratices ajudam a mitigar ataques que poderao acontecer aos utilizadores do nosso website
SEO search engine optimizationns permitem aos motor de busca melhor perceber o nosso website a quem recomendar o website

g) Os principais problemas sao ficheiros nao comprimidos nem minified, LCP com grandes demoras, javascripts nao utilizados, tamanhos das imagens, codigo antigo,
Os impactos sao de maiores tempos de carregamento, maiores gastos de rede, codigo nao utilizado é rede desperdiçada, codigo legacy poderá introduzir riscos de segurança. E isto poderá afetar a acessibilidade e dificultar o uso da aplicação
