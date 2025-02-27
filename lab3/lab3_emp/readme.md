## exercise 1

### a

assertThat foi encadeado por:

- extracting: A cada elemento da lista ou a um elemento, chamar o metodo Employee::metodo, que funciona estilo map()

#### Coleçoes

- hasSize: Verificar tamanho da lista
- contains{ExactlyInAnyOrder | Only | }: para fazer a verificaçao

#### Comparaçoes

- isNotNull
- isNull
- isEqualTo

### b

- @DataJpaTest
  Usado para testar repositorio criando uma base de dados de teste

- @MockBean
  Usado para criar beans, é parecido ao Mock mas para spring, ja que o spring precisa de anotaçºoes proprios para ser reconhecido como componente deste

- @WebMvcTest(EmployeeRestController.class)
  Usado para fazer testes de controladores usando um `MockMvc` para simular pedidos API

- @SpringBootTest(webEnvironment = (WebEnvironment.MOCK | WebEnvironment.RANDOM_PORT), classes = EmployeeMngrApplication.class)
  Usado para fazer testes de integração da aplicação por completo, podendo ter um mock simulador de pedidos api ou etnao um real

- @AutoConfigureMockMvc

### c

No teste B

### d

O ficheiro serve como ficheiro de variaveis de ambiente às quais o spring irá buscar para inicializar certos campos. É usado para passar, por exemplo, chaves api, endereços de base de dados ou outros componentes do ecossistema

### f
