## Exercicio C
### Class SetOfNumbers
Tinha uma coverage de 100% exceto nos metodos autogerados `hashcode` e `equals`, e no metodo `subtract` ainda nao implementado

### Class SetOfNaturals
Continha uma coverage de 100%

### class Dip
Apos correr o jacoco, há falta de cobertura de testes nas branches else da criação de um Dip aleatorio
```java
public static Dip generateRandomDip() {
    Dip randomDip = new Dip();
    for (int i = 0; i < REQUIRED_NUMBERS_COUNT_FOR_BET; ) {
        int candidate = generator.nextInt(49) + 1;
        if (!randomDip.getNumbersColl().contains(candidate)) {
            randomDip.getNumbersColl().add(candidate);
            i++;
        } /* Nao testa o caso else */
    }
    for (int i = 0; i < REQUIRED_STARS_COUNT_FOR_BET; ) {
        int candidate = generator.nextInt(11) + 1;
        if (!randomDip.getStarsColl().contains(candidate)) {
            randomDip.getStarsColl().add(candidate);
            i++;
        } /* Nao testa o caso else */
    }
    return randomDip;
}
```
Para efetuar um teste relativo a essas branches, teriamos de alguma maneira (como por exemplo atraves de uma seed) forçar a sair dois numeros iguais para verificar se é gerado tudo corretamente, para nao ser um teste baseado na sorte, assim a cobertura destas branches será complicada e possivelmente deixado na compreensao do codigo aquando da analise de codigo

### Class Euromillions
A unica falta de cobertura corresponde ao metodo `getDrawResults` e `generateRandomDraw`. A primeira retorna apenas o objeto que corresponde ao resultado do `generateRandomDraw`, o segundo cria um Dip novo guardado na class Euromillions através do metodo `generateRandomDip` da class Dip, analisada anteriormente

### Class CuponEuromillions
Tem uma coverage de 100%, exceto relativamente ao metodo `format`.


## Exercicio d
- O metodo `subtract` ainda nao foi implementado nem coberto por testes
- Nao existe presente algo que defina o tamanho maximo do set, ou seja o resultado na tentativa de adicionar mais do que N elementos nao é atualmente testada (No entanto será necessario 2^31 elementos para o fazer, sendo que é usado um ArrayList como collection, caso nao seja definido um tamanho maximo no SetOfNaturals)

## Exercicio e
Após implementar esse metodo
```java
public void subtract (SetOfNumbers subset) {
    for (Integer number : subset) {
        collection.remove(number);
    }
}
```
E implementar testes simples
```java
@Test
@DisplayName("Test Intersection")
public void testSubtraction() {
    setB.subtract(SetOfNumbers.fromArray(new int[] {}));
    assertTrue(
            setB.equals(
                    SetOfNumbers.fromArray(new int[] { 10, 20, 30, 40, 50, 60 })),
            "subtract: empty subtraction not correctly working");

    setB.subtract(SetOfNumbers.fromArray(new int[] { 10, 20, 30 }));
    assertTrue(
            setB.equals(SetOfNumbers.fromArray(new int[] { 40, 50, 60 })),
            "subtract: elements not correctly subtracted from  set");
}
```
Já assume uma coverage de 100%

- No metodo `subtract` valeria a pena testar o metodo com outro set que continha tambem numeros nao presentes no set original, para detetar qualquer falha


## Exercicio f e g
As as classes ui.DemoMain, euromillions.CuponEuromillions, euromillions.EuromillionsDraw, sets.SetOfNumber falham todas este novo requisito.<br>
Após excluir a ui.DemoMain dos testes, adicionar teste ao metodo `format` do CuponEuromillions. Ja apenas as duas ultimas falham. <br>
No entanto, SetOfNumbers falha devido ao metodo `equals`, e `EuromillionsDraw` falha por causa dos metodos `getDrawResults` e `generateRandomDraw`, que à partida o segundo é dificil de ser testado e o getDrawResults é automaticamente gerado, retornando apenas um objeto `Dip` interno à classe
